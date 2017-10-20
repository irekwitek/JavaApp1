package com.mk.classes;

import com.mk.dao.DBConnectionManager;
import com.mk.dao.DBManager;
import java.sql.*;

public class IDGenerator
{

    public static synchronized String getStudentIdentificationCode(DBManager dbMgr)
    {
        DBConnectionManager cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
        String code = "";
        boolean isFound = false;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            st = cm.getConnection().createStatement();
            while (!isFound)
            {
                code = Utility.generateRandomNumberAsString(6);
                rs = st.executeQuery("select STUDENT_IDENTIFICATION_CD from TStudents where STUDENT_IDENTIFICATION_CD = '" + code + "'");
                if (!rs.next())
                {
                    isFound = true;
                }
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error getting a unique student identification code in IDGenerator: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(st);
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return code;
    }


}
