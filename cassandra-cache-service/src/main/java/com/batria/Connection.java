
package com.batria;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;


public class Connection
{
	public static Cluster cluster;
	public static Session session;
	private String serverIp = "127.0.0.1";
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
			cluster = Cluster.builder().addContactPoint(serverIp).withPort(9042).build();
			session = cluster.connect();
			System.out.println("getSession method is called");

		}
		return session;
	}

}
