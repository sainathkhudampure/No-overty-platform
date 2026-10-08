package com.nopoverty.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NoPovertyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoPovertyApplication.class, args);
        System.out.println("====================================================================");
        System.out.println("🇮🇳 NO POVERTY Welfare Platform is Running Successfully!");
        System.out.println("Web Portal: http://localhost:8085/");
        System.out.println("API Base:   http://localhost:8085/api");
        System.out.println("H2 Console: http://localhost:8085/h2-console");
        System.out.println("UN SDG 1:   End Poverty in all its forms everywhere");
        System.out.println("====================================================================");
    }
}

