package com.baidu.oped.apm.mvc.vo;

import java.time.LocalDateTime;

import org.springframework.util.Assert;

import com.baidu.oped.apm.utils.TimeUtils;

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

    @Override
    public String toString() {
        return String.format("%s|%s", TimeUtils.writeToString(from), TimeUtils.writeToString(to));
    }
}
