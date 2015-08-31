package com.baidu.oped.apm.test.weather.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;

/**
 * Created by mason on 8/31/15.
 */
@Entity
public class City extends AbstractPersistable<Long> {
    private Long weaid;
    private String citynm;
    private String cityno;
    private String cityid;

    public Long getWeaid() {
        return weaid;
    }

    public void setWeaid(Long weaid) {
        this.weaid = weaid;
    }

    public String getCitynm() {
        return citynm;
    }

    public void setCitynm(String citynm) {
        this.citynm = citynm;
    }

    public String getCityno() {
        return cityno;
    }

    public void setCityno(String cityno) {
        this.cityno = cityno;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }
}
