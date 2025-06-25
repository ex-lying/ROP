package org.streamnative.pulsar.handlers.rocketmq.inner.proxy;

import org.apache.zookeeper.data.Stat;

/**
 * @author Lying
 * @Date: 2024/11/12 13:37
 */
public interface ZooKeeperCacheListener <T>{
    public void onUpdate(String path, T data, Stat stat);
}
