/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.programs;

import com.mk.classes.Utility;
import com.mk.dao.DBConnectionData;
import com.mk.dao.DBConnectionManager;
import com.mk.dao.DBManager;
import java.sql.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author giw
 */
public class KangurRunTaskCheckEmailSyntax
{

    public static void main(String[] arg)
    {
        DBConnectionData cd = null;
        DBConnectionManager cm = null;
        DBManager dbMgr;
        ResultSet rs = null;

        try
        {
            
            cd = new DBConnectionData(DBManager.MYSQL, DBManager.DB_NAME[DBManager.MYSQL]);
            cd.setDriverName("com.mysql.jdbc.Driver");
            cd.setUserName("root");
            cd.setPassword("eureka15");
            //cd.setUrlString("jdbc:mysql://192.168.1.55:3306/KangurDB");
            cd.setUrlString("jdbc:mysql://10.1.10.30:3306/KangarooDB");
            cd.setConnectionMode(DBConnectionData.MODE_STANDARD_CONNECTION);
            cm = new DBConnectionManager(cd);

            DBConnectionData[] cdArr = new DBConnectionData[DBManager.DB_NAME.length];
            cdArr[DBManager.MYSQL] = cd;
            cdArr[DBManager.MSSQL] = cd;
            cdArr[DBManager.ORACLE] = cd;
            dbMgr = new DBManager();
            dbMgr.setConnections(cdArr);

            //DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TLocations_Leaders a, TLeaders b, TLocations c where a.leader_id = b.leader_id and a.location_id = c.location_id and a.primary_ind = 'Y' and c.active = 'Y'");
            
            System.out.println("Begin scanning emails...");
            try
                {
                    while (rs.next())
                    {
                        String email = rs.getString("EMAIL");
                        int leaderId = rs.getInt("LEADER_ID");
                        if ( email != null && !email.trim().equals("") && !Utility.isValidEmailAddress(email))
                        {
                            System.out.println("Manager:" + leaderId + " Email: " + email);
                        }
                    }
                } catch (SQLException se)
                {
                    System.out.println(se);
                }

        } catch (Exception e)
        {
            System.out.println("Exception: " + e);
        } finally
        {
            if (cm != null)
            cm.freeConnection();
        }
            System.out.println("End scanning emails.");


    }
}
