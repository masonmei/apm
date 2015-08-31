package com.baidu.oped.apm.test.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baidu.oped.apm.test.weather.domain.WeatherDefinition;

/**
 * Created by mason on 8/31/15.
 */
public interface WeatherDefinitionRepository extends JpaRepository<WeatherDefinition, Long> {
}
