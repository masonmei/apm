package com.baidu.oped.apm.test.weather.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.test.weather.domain.WeatherDefinition;
import com.baidu.oped.apm.test.weather.repository.WeatherDefinitionRepository;
import com.cdyne.ws.weatherws.ArrayOfWeatherDescription;
import com.cdyne.ws.weatherws.Weather;
import com.cdyne.ws.weatherws.WeatherDescription;
import com.cdyne.ws.weatherws.WeatherSoap;

/**
 * Created by mason on 8/31/15.
 */
@RestController
public class WeatherDefinitionController {
    @Autowired
    private WeatherDefinitionRepository repository;

    @RequestMapping("sync")
    public boolean syncWeather() {
        Weather weather = new Weather();
        WeatherSoap weatherSoap12 = weather.getWeatherSoap12();
        ArrayOfWeatherDescription weatherInformation = weatherSoap12.getWeatherInformation();
        for (WeatherDescription wd : weatherInformation.getWeatherDescription()) {
            // short weatherID = wd.getWeatherID();
            String description = wd.getDescription();
            String pictureURL = wd.getPictureURL();

            try {

                WeatherDefinition definition = new WeatherDefinition();
                definition.setDescription(description);

                URL url = new URL(pictureURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    continue;
                }
                BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = bin.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }

                //

                byte[] weatherImage = bos.toByteArray();
                definition.setWeatherImage(weatherImage);

                repository.save(definition);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
