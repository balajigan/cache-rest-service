package com.batria;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
/*
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
*/
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@SpringBootApplication
public class MainClass 
{
    private static Logger logger = LogManager.getLogger(MainClass.class);

    public static void main(String[] args)
    {
	   logger.info("Testing the logger"); 
	// Log4j configuration   
/* 
  	PatternLayout layout = new PatternLayout();
      	String conversionPattern = "%-7p %d [%t] %c %x - %m%n";
	layout.setConversionPattern(conversionPattern);

	ConsoleAppender consoleAppender = new ConsoleAppender();
	consoleAppender.setLayout(layout);
        consoleAppender.activateOptions();

	FileAppender fileAppender = new FileAppender();
	fileAppender.setFile("client.log");
	fileAppender.setLayout(layout);
	fileAppender.activateOptions();

	Logger rootLogger = Logger.getRootLogger();
	rootLogger.setLevel(Level.INFO);
	rootLogger.addAppender(fileAppender);
	logger.info("@@@@@@@@@@@@@@@@@  Application Started @@@@@@@@@@@@@@@@@@@");
*/
//    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(MainClass.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

}
