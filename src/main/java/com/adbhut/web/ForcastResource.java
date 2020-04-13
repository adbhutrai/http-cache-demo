package com.adbhut.web;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adbhut.web.model.Forecast;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ForcastResource {
    @GetMapping("/forecast")
    public ResponseEntity<Forecast> getTodaysForecast() {
        log.info("get forcast method");
        Forecast weatherForecast = new Forecast();
        weatherForecast.setLocation("Dun Laoghaire");
        weatherForecast.setTemperature("25.5 C");
        weatherForecast.setTime(expiresDateInString());
        ZonedDateTime expiresDate = ZonedDateTime.now()
                .with(LocalTime.MAX);
        String expires = expiresDate.format(DateTimeFormatter.RFC_1123_DATE_TIME);
        return ResponseEntity.ok()
                .header(HttpHeaders.EXPIRES, expires)
                .body(weatherForecast);
    }

    private String expiresDateInString() {
        ZonedDateTime expiresDate = ZonedDateTime.now()
                .with(LocalTime.MAX);
        return expiresDate.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
