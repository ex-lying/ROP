package org.streamnative.pulsar.handlers.rocketmq.inner.proxy;

import org.apache.bookkeeper.common.util.OrderedExecutor;
import org.apache.bookkeeper.zookeeper.BoundExponentialBackoffRetryPolicy;
import org.apache.bookkeeper.zookeeper.ZooKeeperClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.apache.bookkeeper.util.SafeRunnable.safeRun;

/**
 * @author Lying
 * @Date: 2024/11/12 15:49
 */
public class ZookeeperBkClientFactoryImpl implements ZooKeeperClientFactory {
    private static final Logger log = LoggerFactory.getLogger(ZookeeperBkClientFactoryImpl.class);

    private final OrderedExecutor executor;

    public ZookeeperBkClientFactoryImpl(OrderedExecutor executor) {
        this.executor = executor;
    }

    @Override
    public CompletableFuture<ZooKeeper> create(String serverList, SessionType sessionType, int zkSessionTimeoutMillis) {
        CompletableFuture<ZooKeeper> future = new CompletableFuture<>();

        serverList = formateServerList(serverList);

        try {
            ZooKeeper zk = ZooKeeperClient.newBuilder().connectString(serverList)
                    .sessionTimeoutMs(zkSessionTimeoutMillis)
                    .connectRetryPolicy(new BoundExponentialBackoffRetryPolicy(zkSessionTimeoutMillis,
                            zkSessionTimeoutMillis, 0))
                    .build();

            if (zk.getState() == ZooKeeper.States.CONNECTEDREADONLY && sessionType != SessionType.AllowReadOnly) {
                zk.close();
                future.completeExceptionally(new IllegalStateException("Cannot use a read-only session"));
            }

            log.info("ZooKeeper session established: {}", zk);
            future.complete(zk);
        } catch (IOException | KeeperException | InterruptedException exception) {
            log.error("Failed to establish ZooKeeper session: {}", exception.getMessage());
            future.completeExceptionally(exception);
        }

        /*executor.execute(safeRun(() -> {
            try {
                ZooKeeper zk = ZooKeeperClient.newBuilder().connectString(serverList)
                        .sessionTimeoutMs(zkSessionTimeoutMillis)
                        .connectRetryPolicy(new BoundExponentialBackoffRetryPolicy(zkSessionTimeoutMillis,
                                zkSessionTimeoutMillis, 0))
                        .build();

                if (zk.getState() == ZooKeeper.States.CONNECTEDREADONLY && sessionType != SessionType.AllowReadOnly) {
                    zk.close();
                    future.completeExceptionally(new IllegalStateException("Cannot use a read-only session"));
                }

                log.info("ZooKeeper session established: {}", zk);
                future.complete(zk);
            } catch (IOException | KeeperException | InterruptedException exception) {
                log.error("Failed to establish ZooKeeper session: {}", exception.getMessage());
                future.completeExceptionally(exception);
            }
        }, throwable -> {
            future.completeExceptionally(throwable);
        }));*/

        return future;
    }

    private String formateServerList(String serverList) {
        if (serverList.contains("zk:")) {
            serverList = serverList.replace("zk:", "");
        }

        return serverList;
    }
}
