package org.streamnative.pulsar.handlers.rocketmq.inner.namesvr;

/**
 * 用于补充升级rocket版本后，MixAll中删除的常亮
 */
public class RopMixAll {
    public static final String TRANS_CHECK_MAX_TIME_TOPIC = "TRANS_CHECK_MAX_TIME_TOPIC";
    public static final String AUTO_CREATE_TOPIC_KEY_TOPIC = "TBW102";
    public static final String BENCHMARK_TOPIC = "BenchmarkTest";
    public static final String OFFSET_MOVED_EVENT = "OFFSET_MOVED_EVENT";
    public static final String RMQ_SYS_TRANS_HALF_TOPIC = "RMQ_SYS_TRANS_HALF_TOPIC";
    public static final String RMQ_SYS_TRANS_OP_HALF_TOPIC = "RMQ_SYS_TRANS_OP_HALF_TOPIC";
}
