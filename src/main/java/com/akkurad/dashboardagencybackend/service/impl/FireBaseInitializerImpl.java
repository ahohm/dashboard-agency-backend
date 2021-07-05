package com.akkurad.dashboardagencybackend.service.impl;

import com.akkurad.dashboardagencybackend.service.IFireBaseInitializer;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.remoteconfig.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@Configuration
public class FireBaseInitializerImpl implements IFireBaseInitializer {


    @Value("${firebase.key}")
    private String cridentialPath;
    @Value("${firebase.database}")
    private String databaseUrl;


    @PostConstruct
    @Bean
    public FirebaseApp  initDb() throws IOException, ExecutionException, InterruptedException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(this.cridentialPath).getInputStream());
        FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .setDatabaseUrl(this.databaseUrl)
                    .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseRemoteConfig getFirebase() throws IOException, ExecutionException, InterruptedException {
        return FirebaseRemoteConfig.getInstance(initDb());
    }


}
