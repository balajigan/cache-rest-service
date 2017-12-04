
package com.batria;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;


public class Connection
{
	public static Cluster cluster;
	public static Session session;
	public Connection()
	{

	}
	public Session getSession()
	{
		if(session == null)
		{
			cluster = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).build();
			session = cluster.connect();
			System.out.println("getSession method is called");

		}
		return session;
	}

}
