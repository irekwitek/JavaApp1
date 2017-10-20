/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.programs;

import com.mk.classes.BusinessProcessControl;
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
public class CalculateResults
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
            cd.setUrlString("jdbc:mysql://10.1.10.30:3306/KangarooDB");
            //cd.setUrlString("jdbc:mysql://192.168.1.50:3306/KangarooDB");
            cd.setConnectionMode(DBConnectionData.MODE_STANDARD_CONNECTION);
            cm = new DBConnectionManager(cd);

            DBConnectionData[] cdArr = new DBConnectionData[DBManager.DB_NAME.length];
            cdArr[DBManager.MYSQL] = cd;
            cdArr[DBManager.MSSQL] = cd;
            cdArr[DBManager.ORACLE] = cd;
            dbMgr = new DBManager();
            dbMgr.setConnections(cdArr);

            BusinessProcessControl bpc = new BusinessProcessControl(dbMgr);


            String year = "2017";
            
            // delete all previous calculations 
            System.out.println("Deleting all previous calculations...");
            String delPrvCalc = "update TResults set state_place = 0, nation_place = 0, state_pctile = 0, nation_pctile = 0, state = '' where year = '" + year + "'";
            cm.executeStmtSQL(delPrvCalc);
            
            for (int levelCnt = 1; levelCnt < 13; levelCnt++)
            {
                System.out.println("Calculating " + year + " Level " + levelCnt + "\nWhole Nation");
                Hashtable htN = bpc.getNationResultsStats(year, levelCnt);
                bpc.updateNationLevelResults(year, levelCnt, htN);
                
                String getAllStates = "select distinct (case (@myvar:= b.residency_state) when '' then c.state else @myvar end ) as STATE from TResults m, TStudents a, TRegistration b, TLocations c where m.year = '" + year + "' and b.status = 2 and m.year = b.year and m.level = " + levelCnt + " and m.level = b.level and m.student_identification_cd = a.student_identification_cd and a.student_id = b.student_id and b.location_id = c.location_id ";
                rs = cm.getResultsSQL(getAllStates);
                try
                {
                    while (rs.next())
                    {
                        String state = rs.getString("STATE");
                        try {
                        System.out.println("Calculating state: " + state);
                        Hashtable htS = bpc.getStateResultsStats(year, levelCnt, state);
                        System.out.println("Updating state: " + state);
                        bpc.updateStateLevelResults(year, state, levelCnt, htS);
                        }
                        catch (Exception e )
                        {
                            System.out.println("Problem during state " + state + " calc :" + e);
                        }
                    }
                } catch (Exception se)
                {
                    System.out.println("General Exception: " + se);
                }
            }
        } catch (Exception e)
        {
            System.out.println("Exception: " + e);
        } finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
    }
}
