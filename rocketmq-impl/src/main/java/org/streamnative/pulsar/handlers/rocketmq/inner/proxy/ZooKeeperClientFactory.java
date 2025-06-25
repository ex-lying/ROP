package org.streamnative.pulsar.handlers.rocketmq.inner.proxy;

import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CompletableFuture;

/**
 * @author Lying
 * @Date: 2024/11/12 13:40
 */
public interface ZooKeeperClientFactory {
    enum SessionType {
        /**
         * Create a normal ZK session that requires a valid quorum
         */
        ReadWrite,

        /**
         * Create a ZK session that allow the client to stay connected (in read only mode) to a ZK server that has lost
         * the quorum
         */
        AllowReadOnly,
    }

    /**
     * Return a future yielding a connected ZooKeeper client
     *
     * @param serverList
     * @param sessionType
     * @param zkSessionTimeoutMillis
     * @return
     */
    CompletableFuture<ZooKeeper> create(String serverList, SessionType sessionType, int zkSessionTimeoutMillis);
}
