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
public class LocationBulkRegistration {

    public static void main(String[] arg) {
        DBConnectionData cd;
        DBConnectionManager cm = null;
        DBManager dbMgr;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {

            cd = new DBConnectionData(DBManager.MYSQL, DBManager.DB_NAME[DBManager.MYSQL]);
            cd.setDriverName("com.mysql.jdbc.Driver");
            cd.setUserName("root");
            cd.setPassword("eureka15");
            //cd.setUrlString("jdbc:mysql://192.168.1.55:3306/KangarooDB");
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

            String stID;
            String stFN;
            String stLN;
            String stMN;
            String stGender;
            String stLevel;
            int stLevelCode;
            String stTShirt;
            String stEMail;
            String pEMail;
            String contactPhone;
            int location;
            int locationSessionId;
            int userID;
            String transactionId;
            String comment = "";
            String status = "";
            int recId = 0;
            double amount;
            int studentId;
            RegisteredStudent student;

            String year = ApplicationProperties.getCurrentRegistrationYear(dbMgr);
            //DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            stm = cm.getConnection().prepareStatement("insert into MaintDB.BulkRegLoadLog (REC_ID,STATUS,COMMENT) values (?,?,?)");
            rs = cm.getResultsSQL("select * from MaintDB.BulkRegLoad where STATUS = 'R'");
            int cnt = 0;
            while (rs.next()) {
                try {
                    cnt += 1;
                    recId = rs.getInt("REC_ID");
                    location = rs.getInt("L_ID");
                    locationSessionId = rs.getInt("LS_ID");
                    stID = rs.getString("S_ID").trim();
                    stFN = rs.getString("S_FN").trim();
                    stLN = rs.getString("S_LN").trim();
                    stMN = rs.getString("S_MN").trim();
                    stGender = rs.getString("S_G");
                    stLevel = rs.getString("S_L").trim();
                    stLevelCode = Integer.parseInt(stLevel);
                    stTShirt = rs.getString("S_TS").trim();
                    stEMail = rs.getString("S_EM").trim();
                    pEMail = rs.getString("P_EM").trim();
                    contactPhone = rs.getString("R_C1").trim();
                    userID = rs.getInt("P_ID");
                    amount = rs.getDouble("AMT");
                    transactionId = rs.getString("TRANSACTION_ID");
                    System.out.println("Processing student ID :: " + stID + " Name:" + stFN + ", " + stLN);
                    if (!stID.trim().equals("")) {
                        student = bpc.getStudentByCodeID(stID);
                        student.setFirstName(stFN.toUpperCase());
                        student.setLastName(stLN.toUpperCase());
                        student.setMiddleName(stMN.toUpperCase());
                        if (stGender != null && !stGender.equals("")) {
                            student.setGender(stGender);
                            student.setGenderCode(bpc.getCommonData().getGenderCode(stGender));
                        }
                        if (stEMail != null && !stEMail.equals("")) {
                            student.setStudentEmail(stEMail);
                        }
                        if (pEMail != null && !pEMail.equals("")) {
                            student.setParentGuardianEmail(pEMail);
                        }
                        bpc.updateStudent(student);
                    } else {
                        student = new RegisteredStudent();
                        student.setFirstName(stFN.toUpperCase());
                        student.setLastName(stLN.toUpperCase());
                        student.setMiddleName(stMN.toUpperCase());
                        if (stGender != null && !stGender.equals("")) {
                            student.setGender(stGender);
                            student.setGenderCode(bpc.getCommonData().getGenderCode(stGender));
                        } else {
                            student.setGenderCode(0);
                        }
                        student.setEthnicGroupName("");
                        student.setParentGuardianEmail(pEMail);
                        student.setStudentEmail(stEMail);
                        student.setStudentIdentificationCode(IDGenerator.getStudentIdentificationCode(dbMgr));
                        student.setStudentID(bpc.createStudent(student));
                        if (student.getStudentID() != 0) {
                            ArrayList<RegisteredStudent> arr = new ArrayList<RegisteredStudent>();
                            arr.add(student);
                            bpc.addStudentsToUser(userID, arr);
                        }
                    }
                    if (student.getStudentID() != 0) {
                        student.setTshirtSize(stTShirt);
                        student.setTshirtSizeCode(bpc.getCommonData().getTshirtCode(student.getTshirtSize()));
                        student.setContactPhone1(contactPhone);
                        student.setLevel(stLevel);
                        student.setLevelCode(stLevelCode);
                        student.setLocationID(location);
                        student.setSessionID(locationSessionId);
                        student.setAdminNote("Registration created by the bulk load process");
                        student.setStatus(2);
                        student.setTransactionID(transactionId);
                        student.setPaymentAmount(amount);
                        student.setYear(year);
                        student.setPaymentID("");
                        student.setPaymentMethod(4);
                        bpc.createRegistrationBulk(student);
                        status = "C";
                        comment = "";
                    } else {
                        status = "E";
                        comment = "Error creating student record.";
                    }
                } catch (SQLException se) {
                    status = "E";
                    comment = se.toString();
                }
                stm.setInt(1, recId);
                stm.setString(2, status);
                stm.setString(3, comment);
                stm.execute();
            }
            System.out.println("Finished; Total students = " + cnt);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
        }

    }
}
