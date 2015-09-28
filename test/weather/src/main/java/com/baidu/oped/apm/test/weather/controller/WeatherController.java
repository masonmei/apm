package com.baidu.oped.apm.test.weather.controller;

import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.test.weather.domain.City;
import com.baidu.oped.apm.test.weather.domain.Weather;
import com.baidu.oped.apm.test.weather.repository.CityRepository;
import com.baidu.oped.apm.test.weather.repository.WeatherRepository;

/**
 * Created by mason on 8/31/15.
 */
@RestController
public class WeatherController {
    private static final String AUTH_AND_FORMAT = "&appkey=15198&sign=3c009bbf405331a22f377757771431a7&format=json";
    private static final String WEATHER_URL = "http://api.k780.com:88/?app=weather.today&weaid=%s" + AUTH_AND_FORMAT;
    private static final String CITY_URL = "http://api.k780.com:88/?app=weather.city" + AUTH_AND_FORMAT;
    private static final String BAIDU_URL = "http://www.baidu.com/";

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @RequestMapping("city/update")
    public int update(@RequestParam("cityName") String cityName) {
        return cityRepository.updateCityBeijing(cityName);
    }

    @RequestMapping("city/sync")
    public boolean syncCity() {
        cityRepository.deleteAll();
//        randomSleep();
        String urlString = format(CITY_URL);
        String content = getContent(BAIDU_URL);
        System.out.println(content);
        content = getContent(urlString);
        JSONObject jsonObject = new JSONObject(content);
        Object success = jsonObject.get("success");
        final JSONObject result = (JSONObject) jsonObject.get("result");
        if (success.equals("1")) {
            result.keySet().forEach(key -> {
                JSONObject value = (JSONObject) result.get(key.toString());
                City city = new City();
                city.setWeaid(value.getLong("weaid"));
                city.setCitynm(value.getString("citynm"));
                String cityno = value.getString("cityno");
                if (cityno.startsWith("bei")) {
                    city.setCityno(cityno);
                    city.setCityid(value.getString("cityid"));
                    cityRepository.save(city);
                }
            });
        }
        return true;
    }

    @RequestMapping("weather/sync")
    public boolean syncWeather(@RequestParam("cityName") String cityName) {
        City city = cityRepository.findOneByCityno(cityName);
        if (city == null) {
            return false;
        }

        randomSleep();
        String urlString = format(WEATHER_URL, city.getWeaid());
        String content = getContent(urlString);
        if (content.equals("")) {
            return false;
        }
        JSONObject jsonObject = new JSONObject(content);
        Object success = jsonObject.get("success");
        JSONObject result = (JSONObject) jsonObject.get("result");
        if (success.equals("1")) {

            Weather weather = new Weather();
            weather.setWeaid(result.getLong("weaid"));
            weather.setCitynm(result.getString("citynm"));
            weather.setCityno(result.getString("cityno"));
            weather.setCityid(result.getString("cityid"));
            weather.setDays(result.getString("days"));
            weather.setWeek(result.getString("week"));
            weather.setTemperature(result.getString("temperature"));
            weather.setTemperature_curr(result.getString("temperature_curr"));
            weather.setHumidity(result.getString("humidity"));
            weather.setWeather(result.getString("weather"));
            weather.setWeather_icon(result.getString("weather_icon"));
            weather.setWeather_icon1(result.getString("weather_icon1"));
            weather.setWind(result.getString("wind"));
            weather.setWinp(result.getString("winp"));
            weather.setTemp_high(result.getString("temp_high"));
            weather.setTemp_low(result.getString("temp_low"));
            weather.setTemp_curr(result.getString("temp_curr"));
            weather.setHumi_high(result.getString("humi_high"));
            weather.setHumi_low(result.getString("humi_low"));
            weather.setWeatid1(result.getString("weatid1"));
            weather.setWeatid1(result.getString("weatid"));
            weather.setWindid(result.getString("windid"));
            weather.setWinpid(result.getString("winpid"));
            weatherRepository.save(weather);
        }
        return true;
    }

    private String getContent(String urlString) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(urlString);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() != 200) {
                return "";
            }

            Reader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
            BufferedReader reader = new BufferedReader(inputStreamReader);

            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line).append("\r\n");
                line = reader.readLine();
            }

            return builder.toString();
        } catch (IOException exception) {
            return "";
        }
    }

    private void randomSleep() {
        Random random = new Random();
        long randLong = random.nextLong();
        while (randLong > 3000 || randLong < 300) {
            randLong = random.nextLong();
        }
        try {
            Thread.sleep(randLong);
        } catch (InterruptedException e) {
        }
    }

}
