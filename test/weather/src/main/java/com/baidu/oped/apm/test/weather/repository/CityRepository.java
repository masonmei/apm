package com.baidu.oped.apm.test.weather.repository;

import com.baidu.oped.apm.test.weather.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mason on 8/31/15.
 */
public interface CityRepository extends JpaRepository<City, Long> {

    City findOneByCityno(String cityno);
}
