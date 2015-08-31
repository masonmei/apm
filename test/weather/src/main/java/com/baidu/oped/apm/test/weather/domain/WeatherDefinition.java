package com.baidu.oped.apm.test.weather.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class WeatherDefinition implements Serializable {
    private static final long serialVersionUID = -1186732718456669682L;

    private Integer weatherId;
    private String description;
    private byte[] weatherImage;

    /** default constructor */
    public WeatherDefinition() {
    }

    /** full constructor */
    public WeatherDefinition(String description, byte[] weatherImage) {
        super();
        this.description = description;
        this.weatherImage = weatherImage;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Integer weatherId) {
        this.weatherId = weatherId;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "weather_image", length = 65535)
    public byte[] getWeatherImage() {
        return weatherImage;
    }

    public void setWeatherImage(byte[] weatherImage) {
        this.weatherImage = weatherImage;
    }

}
