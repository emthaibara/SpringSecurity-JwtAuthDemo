package com.securityserviceprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SecurityServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceProviderApplication.class, args);
    }

}
