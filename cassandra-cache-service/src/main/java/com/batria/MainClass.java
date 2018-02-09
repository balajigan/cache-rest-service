
/* Starting point for the application
 *
 */

package com.batria;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.datastax.driver.core.Session;
import com.batria.Connection;

@SpringBootApplication
public class MainClass 
{
    private static Logger logger = LogManager.getLogger(MainClass.class);

    public static void main(String[] args)
    {
	logger.info("Main started");
        // Change the serverIp based on the actuls	
        String serverIp = "10.128.0.2";

	// Create the connection pool.
	Connection conn = new Connection(serverIp);
	Session session = conn.getSession();

        ApplicationContext ctx = SpringApplication.run(MainClass.class, args);

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

}
