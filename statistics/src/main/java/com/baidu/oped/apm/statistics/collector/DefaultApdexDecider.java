package com.baidu.oped.apm.statistics.collector;

import static com.baidu.oped.apm.common.utils.ApdexUtils.Level.FRUSTRATED;
import static com.baidu.oped.apm.common.utils.ApdexUtils.Level.SATISFIED;
import static com.baidu.oped.apm.common.utils.ApdexUtils.Level.TOLERATED;
import static com.baidu.oped.apm.common.utils.ApdexUtils.Level.getLevel;

import org.springframework.stereotype.Component;

/**
 * Created by mason on 9/1/15.
 */
public class DefaultApdexDecider implements ApdexDecider {
    private final long apdexTimeInMills;

    public DefaultApdexDecider(long apdexTimeInMills) {
        this.apdexTimeInMills = apdexTimeInMills;
    }

    @Override
    public boolean isSatisfied(int elapsedInMills) {
        return getLevel(apdexTimeInMills, elapsedInMills) == SATISFIED;
    }

    @Override
    public boolean isTolerated(int elapsedInMills) {
        return getLevel(apdexTimeInMills, elapsedInMills) == TOLERATED;
    }

    @Override
    public boolean isFrustrated(int elapsedInMills) {
        return getLevel(apdexTimeInMills, elapsedInMills) == FRUSTRATED;
    }
}
