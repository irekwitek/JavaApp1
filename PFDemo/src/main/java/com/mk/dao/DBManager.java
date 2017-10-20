package com.mk.dao;


import java.lang.String;
import java.io.Serializable;
import java.util.*;

/**
* The main controller for all database connection of any application.
* An object of this class can be put in the application server session where
* can be reused by any part of the application.
* 
* @author IW9396
*/
public class DBManager implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ORACLE = 0;
	public static final int MYSQL = 1;
	public static final int MSSQL = 2;
	public static final String DB_NAME[] = { "ORACLE", "MYSQL", "MSSQL 2000" };

	private DBConnectionData[] connections;



	public DBManager() 
	{
	}

	public DBManager( Vector v ) 
	{
		this();
		setConnections( v );
	}

	
	public DBConnectionData[] getConnections() 
	{
		return connections;
	}

	public void setConnections(DBConnectionData[] i) 
	{
		connections = i;
	}

	public void setConnections( Vector v ) 
	{
		connections = new DBConnectionData[DB_NAME.length];
		for ( int i = 0; i < v.size(); i++ )
		{
			DBConnectionData cd = (DBConnectionData)v.get( i );
			connections[cd.getDbCode()] = cd;
		}
	}


	public DBConnectionData getDBConnectionData(String dbName) 
	{
		DBConnectionData cd = null;
		if ( connections != null )
			for ( int i = 0; i < connections.length; i++ )
				if ( connections[i].getDbName().equals( dbName ))
				{
					cd = connections[i];
					break;
				}
		return cd;
	}

	public DBConnectionData getDBConnectionData( int dbCode ) 
	{
		DBConnectionData cd = null;
		if ( connections != null )
			for ( int i = 0; i < connections.length; i++ )
				if ( connections[i].getDbCode() == dbCode )
				{
					cd = connections[i];
					break;
				}
		return cd;
	}

}
