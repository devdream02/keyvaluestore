package com.devdream02.keyvaluestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.devdream02.keyvaluestore")
public class KeyValueStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.devdream02.keyvaluestore.KeyValueStoreApplication.class, args);
    }
}