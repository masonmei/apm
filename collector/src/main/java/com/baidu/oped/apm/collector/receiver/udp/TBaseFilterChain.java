package com.baidu.oped.apm.collector.receiver.udp;

import org.apache.thrift.TBase;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mason on 11/12/15.
 */
public class TBaseFilterChain<T extends SocketAddress> implements TBaseFilter<T> {
    private final List<TBaseFilter<T>> filterChain;

    public TBaseFilterChain(List<TBaseFilter<T>> tBaseFilter) {
        if (tBaseFilter == null) {
            throw new NullPointerException("tBaseFilter must not be null");
        }
        this.filterChain = new ArrayList<>(tBaseFilter);
    }

    @Override
    public boolean filter(TBase<?, ?> tBase, T remoteHostAddress) {
        for (TBaseFilter tBaseFilter : filterChain) {
            if (!tBaseFilter.filter(tBase, remoteHostAddress)) {
                return BREAK;
            }
        }
        return TBaseFilter.CONTINUE;
    }
}
