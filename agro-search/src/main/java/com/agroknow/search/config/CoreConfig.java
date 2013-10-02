package com.agroknow.search.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan({ "com.agroknow.search.services", "com.agroknow.search.config.json" })
@EnableAsync
@EnableAspectJAutoProxy
public class CoreConfig {

}
