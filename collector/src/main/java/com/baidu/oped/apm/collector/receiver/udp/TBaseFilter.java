package com.baidu.oped.apm.collector.receiver.udp;

import org.apache.thrift.TBase;

/**
 * Created by mason on 11/12/15.
 */
public interface TBaseFilter<T> {
    boolean CONTINUE = true;
    boolean BREAK = false;

    boolean filter(TBase<?, ?> tBase, T remoteHostAddress);

    TBaseFilter CONTINUE_FILTER = (tBase, remoteHostAddress) -> CONTINUE;
}
