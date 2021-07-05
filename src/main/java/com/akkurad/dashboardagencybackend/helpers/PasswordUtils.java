package com.akkurad.dashboardagencybackend.helpers;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class PasswordUtils {

    @Bean
    public RandomStringUtils randomStringUtils() {
        return new RandomStringUtils();
    }
}
