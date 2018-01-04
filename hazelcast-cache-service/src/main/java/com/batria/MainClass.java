
/* Starting point for the application
 *  *
 *   */


package com.batria;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

@SpringBootApplication
public class MainClass 
{
    private static Logger logger = LogManager.getLogger(MainClass.class);

    public static void main(String[] args)
    {
	logger.info("Hazelcast APT MainClass started ");

	// Configure the IP and port, before starting this.
        String serverIp = "10.128.0.4:5701";

	// Create the client, before start accepting the requests.
        Connection conn = new Connection(serverIp);
	HazelcastInstance client = conn.getClient();

        ApplicationContext ctx = SpringApplication.run(MainClass.class, args);

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

}
