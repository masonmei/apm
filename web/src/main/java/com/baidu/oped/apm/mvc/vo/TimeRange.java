package com.baidu.oped.apm.mvc.vo;

import java.time.LocalDateTime;

import org.springframework.util.Assert;

/**
 * Created by mason on 8/27/15.
 */
public class TimeRange {
    private LocalDateTime from;
    private LocalDateTime to;

    public TimeRange(LocalDateTime from, LocalDateTime to) {
        Assert.notNull(from);
        Assert.notNull(to);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
