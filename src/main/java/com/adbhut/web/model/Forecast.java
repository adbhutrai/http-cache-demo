package com.adbhut.web.model;

import lombok.Data;

@Data
public class Forecast {
    private String temperature;

    private String location;

    private String time;
}
