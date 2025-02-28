package com.epam.campus.gymcrm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.epam.campus.gymcrm")
@PropertySource("classpath:application.properties")
public class Config {

}
