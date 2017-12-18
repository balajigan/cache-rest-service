package com.batria;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@SpringBootApplication
public class MainClass 
{
    private static Logger logger = LogManager.getLogger(MainClass.class);

    public static void main(String[] args)
    {
	logger.info("Hazelcast APT MainClass started "); 
        ApplicationContext ctx = SpringApplication.run(MainClass.class, args);

        //System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

}
