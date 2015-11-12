package com.baidu.oped.apm.collector.receiver.udp;

import com.codahale.metrics.Timer;

/**
 * Created by mason on 11/12/15.
 */
public class TimingWrap implements Runnable {
    private final Timer timer;
    private final Runnable delegate;

    public TimingWrap(Timer timer, Runnable child) {
        if (timer == null) {
            throw new NullPointerException("timer must not be null");
        }
        if (child == null) {
            throw new NullPointerException("delegate must not be null");
        }
        this.timer = timer;
        this.delegate = child;
    }

    @Override
    public void run() {
        final Timer.Context time = timer.time();
        try {
            delegate.run();
        } finally {
            time.stop();
        }

    }
}
