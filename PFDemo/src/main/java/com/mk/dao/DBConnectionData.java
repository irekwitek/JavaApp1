package com.mk.dao;


import java.io.Serializable;
import java.lang.String;

/**
* Convenient class to hold database connection settings such as driver name, url, userid, password.
*  
* @author IW9396
*/
public class DBConnectionData implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MODE_DATA_SOURCE = 0;
	public static final int MODE_STANDARD_CONNECTION = 1;
	private int connectionMode;
	private int dbCode;
	private String dbName;
	private String password;
	private String userName;
	private String urlString;
	private String driverName;
	private String dataSourceName;



	public DBConnectionData() 
	{
	}


	public DBConnectionData( int dbcode, String dbname ) 
	{
		dbCode = dbcode;
		dbName = dbname;
	}


	public DBConnectionData( int dbcode, String dbname, String drivername, String url, String user, String passwrd ) 
	{
		dbCode = dbcode;
		dbName = dbname;
		driverName = drivername;
		urlString = url;
		userName = user;
		password = passwrd;
		connectionMode = MODE_STANDARD_CONNECTION;
	}
	
	
	public DBConnectionData( int dbcode, String dbname, String dsn )
	{
		dbCode = dbcode;
		dbName = dbname;
		dataSourceName = dsn;
		connectionMode = MODE_DATA_SOURCE;
	}



	public String getDbName() {
		return dbName;
	}


	public void setDbName(String i) {
		dbName = i;
	}


	public String getUrlString() {
		return urlString;
	}


	public void setUrlString(String i) {
		urlString = i;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String i) {
		userName = i;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String i) {
		password = i;
	}


	public int getDbCode() {
		return dbCode;
	}


	public void setDbCode(int i) {
		dbCode = i;
	}


	public String getDriverName() {
		return driverName;
	}


	public void setDriverName(String i) {
		driverName = i;
	}
	
	
	/**
	 * Returns the dataSourceName.
	 * @return String
	 */
	public String getDataSourceName() 
	{
		return dataSourceName;
	}

	/**
	 * Sets the dataSourceName.
	 * @param dsn The dataSourceName to set
	 */
	public void setDataSourceName( String dsn ) 
	{
		dataSourceName = dsn;
	}

	/**
	 * Returns the mode.
	 * @return int
	 */
	public int getConnectionMode() 
	{
		return connectionMode;
	}

	/**
	 * Sets the mode.
	 * @param mode The connection mode to set
	 */
	public void setConnectionMode( int mode ) 
	{
		connectionMode = mode;
	}

}
