package com.mk.dao;


import java.io.Serializable;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;



/**
* Provides useful methods to open a database connection and 
* to query a database. 
* The connection setting are specified in a ConnectionData object. 
* 
* @author IW9396
*/
public class DBConnectionManager implements Serializable
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private DBConnectionData connectionData;
	private DataSource ds;
	

	public DBConnectionManager( DBConnectionData cd ) 
	{
		connectionData = cd;
		getConnection();
	}


	public DBConnectionData getConnectionData() 
	{
		return connectionData;
	}


	public void setConnectionData( DBConnectionData i ) 
	{
		connectionData = i;
	}


	public Connection getConnection() 
	{
		if ( connection == null ) 
		{
//System.out.println("GETTING");
			switch (connectionData.getConnectionMode() )
			{

				case DBConnectionData.MODE_DATA_SOURCE:
						try
						{
							Context initCtx = new InitialContext();
							Context envCtx = (Context) initCtx.lookup("java:comp/env");

							// Look up data source
							ds = (DataSource)
							  envCtx.lookup(connectionData.getDataSourceName());

						}
						catch ( javax.naming.NamingException ne )
						{
							System.out.println( "ConnectionManager error getting DataSource: " + ne );
						}
						try
						{
				  			connection = ds.getConnection();
						}
						catch ( SQLException sqle )
						{
							System.out.println( "ConnectionManager error getting connection from DataSource: " + sqle );
						}
					break;
					
				case DBConnectionData.MODE_STANDARD_CONNECTION:
					try
				   	{
						Class.forName( connectionData.getDriverName() ).newInstance();
				   	}
				   	catch ( InstantiationException ie )
				   	{
						System.out.println( "ConnectionManager [getConnection()] error registering database driver: " + ie );
				   	}
					catch ( ClassNotFoundException cnfe )
					{
						System.out.println( "ConnectionManager [getConnetion()] ClassNotFound error: " + cnfe );
					}
					catch ( IllegalAccessException iae )
					{
						System.out.println( "ConnectionManager [getConnection()] IllegalAccess error: " + iae );
					}
				   	try
				   	{
						connection = DriverManager.getConnection( connectionData.getUrlString(), connectionData.getUserName(), connectionData.getPassword() );
				   	}
					catch ( SQLException ex ) 
					{
						System.out.println( "ConnectionManager [getConnection()] error: " + ex );
					}
					break;

			}

		}
		return connection;
	}



	public void setConnection(Connection i) 
	{
		if ( connection != null )
		{
			try 
			{
				connection.close();
			}
			catch ( SQLException ex ) 
			{
				connection = null;
				System.out.println( "SQL Exception in ConnectionManager [freeConnection()]: " + ex );
			}
		}
		connection = i;
	}


	public void freeConnection()
	{
//System.out.println("RELEASING");
		if ( connection != null )
		{
			try 
			{
				connection.close();
				connection = null;
			}
			catch ( SQLException ex ) 
			{
				connection = null;
				System.out.println( "SQL Exception in ConnectionManager [freeConnection()]: " + ex );
			}
		}
	}


	/**
	* Takes a SQL statement in the form of a string, runs a query and returns a java.sql.ResultSet
	*/   
	public ResultSet getResultsSQL( String sql ) 
	{
		if ( connection == null )
                {
			connection = getConnection();
                }
		ResultSet rs = null; 	
		Statement stmt = null;
                try 
		{
			stmt = connection.createStatement();
			rs = stmt.executeQuery( sql );
		}
		catch ( SQLException ex ) 
		{
			System.out.println( "SQL Exception in ConnectionManager [getResultSQL()]: " + ex );
		}
                finally
                {
                }
		return rs;
	}	



	/**
	* Takes a SQL statement in the form of a string, runs a query and returns a ResultSet
	* This method is to execute INSERT or DELETE statetments.
	*/   
	public boolean executeStmtSQL( String sql ) 
	{
            boolean closeFlag = false;
		if ( connection == null )
                {
			connection = getConnection();
                        closeFlag = true;
                }
		boolean ret = true;
                Statement stmt = null;
		try 
		{
			stmt = connection.createStatement();
			ret = stmt.execute( sql );
		}
		catch ( SQLException ex ) 
		{
			System.out.println( "SQL Exception in ConnectionManager [executeStmtSQL()]: " + ex );
		}
                finally
                {
                    if (stmt!=null)
                    {
                        try
                        {
                            stmt.close();
                            stmt = null;
                        }
                        catch(SQLException se) 
                        {;}
                    }
                    if (closeFlag)
                        freeConnection();
                }
		return ret;
	}	

        //static useful methods
            public static void dropConnObject( Object o )
    {
        if ( o instanceof PreparedStatement )
        {
            try
            {
                ((PreparedStatement)o).close();
            }
            catch (SQLException sqle)
            {
                System.out.println("Error in BPC [dropConnObject()]: " + sqle );
            }
            finally
            {
                o = null;
            }
        }
        else if ( o instanceof ResultSet )
        {
            try
            {
                ((ResultSet)o).close();
            }
            catch (SQLException sqle)
            {
                System.out.println("Error in BPC [dropConnObject()]: " + sqle );
            }
            finally
            {
                o = null;
            }
        }
    }

        
}
