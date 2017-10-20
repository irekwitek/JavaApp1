/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.programs;

import com.mk.classes.ApplicationProperties;
import com.mk.classes.BusinessProcessControl;
import com.mk.classes.IDGenerator;
import com.mk.classes.RegisteredStudent;
import com.mk.classes.Registration;
import com.mk.dao.DBConnectionData;
import com.mk.dao.DBConnectionManager;
import com.mk.dao.DBManager;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author giw
 */
public class BulkRegistrationEmailConfirmation {

    public static void main(String[] arg) {
        DBConnectionData cd;
        DBConnectionManager cm = null;
        DBManager dbMgr;
        ResultSet rs = null;
        try {
            cd = new DBConnectionData(DBManager.MYSQL, DBManager.DB_NAME[DBManager.MYSQL]);
            cd.setDriverName("com.mysql.jdbc.Driver");
            cd.setUserName("root");
            cd.setPassword("eureka15");
            cd.setUrlString("jdbc:mysql://10.1.10.30:3306/KangarooDB");
            cd.setConnectionMode(DBConnectionData.MODE_STANDARD_CONNECTION);
            cm = new DBConnectionManager(cd);

            DBConnectionData[] cdArr = new DBConnectionData[DBManager.DB_NAME.length];
            cdArr[DBManager.MYSQL] = cd;
            cdArr[DBManager.MSSQL] = cd;
            cdArr[DBManager.ORACLE] = cd;
            dbMgr = new DBManager();
            dbMgr.setConnections(cdArr);

            BusinessProcessControl bpc = new BusinessProcessControl(dbMgr);

            RegisteredStudent student;

            String year = ApplicationProperties.getCurrentRegistrationYear(dbMgr);
            rs = cm.getResultsSQL("SELECT * FROM KangarooDB.TRegistration where payment_method = 4 and status = 2 and location_id = 85");
            int cnt = 0;
            while (rs.next()) {
                try {
                    int studentId = rs.getInt("STUDENT_ID");
                    student = bpc.getStudentAndRegistration(studentId, year);
                    if ( student.getParentGuardianEmail().equalsIgnoreCase("info@kumoofredmond.com")) {
                        cnt += 1;
                        student.setParentGuardianEmail(student.getStudentEmail());
                        System.out.print("Processing student ID :: " + studentId + " Name:" + student.getFirstName() + ", " + student.getLastName());
                        System.out.println(" E-mail :: " + student.getStudentEmail() + "::" + student.getParentGuardianEmail());
                        bpc.sendEmailRegistrationConfirmation(student,1);
                    }
                } catch (SQLException se) {
                    System.out.println("Exception: " + se);
                }
            }
            System.out.println("Finished; Total students = " + cnt);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }

    }
}
