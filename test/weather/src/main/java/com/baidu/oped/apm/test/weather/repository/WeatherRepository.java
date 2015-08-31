package com.baidu.oped.apm.test.weather.repository;

import com.baidu.oped.apm.test.weather.domain.City;
import com.baidu.oped.apm.test.weather.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mason on 8/31/15.
 */
public interface WeatherRepository extends JpaRepository<Weather, Long> {

}
