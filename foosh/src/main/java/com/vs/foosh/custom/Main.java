package com.vs.foosh.custom;

import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vs.foosh.api.ApplicationConfig;
import com.vs.foosh.api.model.web.SmartHomeDetails;

@Configuration
public class Main {
    @Bean
    public void setCredentials() {
        ApplicationConfig.setSmartHomeCredentials(new SmartHomeDetails("http://192.168.108.103:8080/", new HashMap<String, String>()));
        ApplicationConfig.addPredictionModel(new MyPredictionModel());
    }
    
}
