package com.baidu.oped.apm.test.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.oped.apm.test.weather.domain.City;

/**
 * Created by mason on 8/31/15.
 */
public interface CityRepository extends JpaRepository<City, Long> {

    City findOneByCityno(String cityno);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update City c set c.citynm = :cityName where c.cityno = 'beijing'")
    int updateCityBeijing(@Param(value = "cityName") String cityName);
}
