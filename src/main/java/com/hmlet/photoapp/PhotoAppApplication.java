package com.hmlet.photoapp;

import com.hmlet.photoapp.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class PhotoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoAppApplication.class, args);
    }

}
