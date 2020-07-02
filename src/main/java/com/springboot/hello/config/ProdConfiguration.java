package com.springboot.hello.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
@Profile(value = "prod")
public class ProdConfiguration {
}
