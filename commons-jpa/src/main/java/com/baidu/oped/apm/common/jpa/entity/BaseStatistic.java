package com.baidu.oped.apm.common.jpa.entity;

/**
 * Created by mason on 8/31/15.
 */
public interface BaseStatistic {

    Integer getPeriod();

    void setPeriod(Integer period);

    Long getTimestamp();

    void setTimestamp(Long timestamp);

    Double getSumResponseTime();

    void setSumResponseTime(Double sumResponseTime);

    Double getMaxResponseTime();

    void setMaxResponseTime(Double maxResponseTime);

    Double getMinResponseTime();

    void setMinResponseTime(Double minResponseTime);

    Long getPv();

    void setPv(Long pv);

    Long getError();

    void setError(Long error);

    Long getSatisfied();

    void setSatisfied(Long satisfied);

    Long getTolerated();

    void setTolerated(Long tolerated);

    Long getFrustrated();

    void setFrustrated(Long frustrated);

}
