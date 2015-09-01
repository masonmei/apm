package com.baidu.oped.apm.statistics.collector;

/**
 * Created by mason on 9/1/15.
 */
public interface ApdexDecider {
    boolean isSatisfied(int elapsedInMills);

    boolean isTolerated(int elapsedInMills);

    boolean isFrustrated(int elapsedInMills);
}
