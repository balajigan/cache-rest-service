
package com.batria;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;


public class Connection
{
	private static Logger logger = LogManager.getLogger("Connection");
	public static Cluster cluster;
	public static Session session;
	private String serverIp = "10.128.0.4";
	public Connection()
	{

	}
	public Connection(String serverIp)
	{
		this.serverIp = serverIp;
	}
	public Session getSession()
	{
		if(session == null)
		{
			try{
			//cluster = Cluster.builder().addContactPoint(serverIp).withPort(9042).build();
			QueryOptions qo = new QueryOptions();
			qo.setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM);
			cluster= Cluster.builder().addContactPoint(serverIp).withPort(9042).withQueryOptions(qo).build();
			
			session = cluster.connect();
			//System.out.println("getSession method is called");
			logger.info("getSession method is called");
			}
			catch(Exception ex)
			{
				logger.error("Issues in opening connection with Cassandra");
			}
		}
		return session;
	}

}
