/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import com.mk.dao.DBConnectionManager;
import com.mk.dao.DBManager;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author irek
 */
public class BusinessProcessControl implements Serializable {

    private DBManager dbMgr;
    private CommonData commonData;

    public BusinessProcessControl(DBManager dbMgr) {
        this.dbMgr = dbMgr;
        this.commonData = new CommonData(dbMgr);
    }

    // get set methods
    public DBManager getDbMgr() {
        return dbMgr;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    // business process methods
    public int getCenterSessionTakenSeats(int centerID, int sessionID) {
        int ret = 0;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select count(*) as TAKEN_SEATS from TRegistration where LOCATION_ID = " + centerID + " and SESSION_ID = " + sessionID + " and YEAR = '" + ApplicationProperties.getCurrentRegistrationYear(dbMgr) + "' and STATUS in (2,3)");
        try {
            if (rs.next()) {
                ret = rs.getInt("TAKEN_SEATS");
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getCenterSessionTakenSeats()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public int getCenterAvailableSeats(int centerID, int sessionID) {
        int ret = 0;
        LocationSession ls = this.getLocationSession(centerID, sessionID);
        if (ls != null) {
            if (ls.getSeatCapacity() == 0) {
                ret = 999;
            } else {
                ret = ls.getSeatCapacity() - this.getCenterSessionTakenSeats(centerID, sessionID);
            }
        }
        return ret;
    }

    public String getCenterAvailableSeatsAsText(int centerID, int sessionID) {
        String ret = "";
        LocationSession ls = this.getLocationSession(centerID, sessionID);
        if (ls != null) {
            if (ls.getSeatCapacity() == 0) {
                ret = "UNLIMITED";
            } else {
                ret = "" + (ls.getSeatCapacity() - this.getCenterSessionTakenSeats(centerID, sessionID));
            }
        }
        return ret;
    }

    public ArrayList<Location> getManagerCenters( Leader mgr, boolean primaryOnly ) {
        ArrayList<Location> ret = new ArrayList<Location>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        String centerList = "SELECT * FROM TLocations_Leaders a, TLocations b " +
            " where a.leader_id = " + mgr.getLeaderID() +
            " and a.location_id = b.location_id " +
            " and a.primary_ind " + (primaryOnly ? " = 'Y' " : " in ('Y','N') ")  +
            " and b.active = 'Y'";

        try {
            rs = cm.getResultsSQL(centerList);
            while (rs.next()) 
            {
                Location l = new Location();
                l.setLocationAddress1(rs.getString("ADDRESS1"));
                l.setLocationAddress2(rs.getString("ADDRESS2"));
                l.setLocationCity(rs.getString("CITY"));
                l.setLocationCounty(rs.getString("COUNTY"));
                l.setLocationCode(rs.getString("LOCATION_CODE"));
                l.setLocationID(rs.getInt("LOCATION_ID"));
                l.setLocationZipcode(rs.getString("ZIPCODE"));
                l.setComment(rs.getString("COMMENT"));
                l.setWebsite(rs.getString("WEBSITE"));
                l.setLocationName(rs.getString("LOCATION_NAME"));
                l.setLocationState(rs.getString("STATE"));
                l.setLatitude(rs.getDouble("LATITUDE"));
                l.setLongitude(rs.getDouble("LONGITUDE"));
                l.setLocationConfirmationStatus(rs.getString("CONFIRMATION_STATUS"));
                l.setActive(rs.getString("ACTIVE"));
                l.setLocationAccessStatus(rs.getInt("LOC_ACCESS_STATUS"));
                l.setLocationAccessMessage(rs.getString("LOC_ACCESS_MESSAGE"));
                l.setLocationRegistrationCode(rs.getString("LOC_REGISTRATION_CODE"));
                String primaryInd = rs.getString("PRIMARY_IND");
                l.setManagerPrimary( primaryInd.equals("Y") ? true : false );
                l.setLocationSessions(this.getLocationSessions(l.getLocationID()));
                ret.add(l);
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getManagerCenters()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public ArrayList<Location> getAdminStateCenters(String state ) {
        ArrayList<Location> ret = new ArrayList<Location>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        String centerList = "SELECT a.*,c.FIRST_NAME,c.LAST_NAME,c.EMAIL,c.ADDRESS1 as MGR_ADDRESS1,c.ADDRESS1 as MGR_ADDRESS2,c.CITY as MGR_CITY,c.STATE as MGR_STATE,c.ZIPCODE as MGR_ZIPCODE,c.EMAIL,c.PHONE,c.PHONE_CELL,d.USER_ID,CONCAT(d.FIRST_NAME,' ',d.LAST_NAME) as DIRECTOR_NAME "
                + " FROM TLocations a left join TUsers d on a.DIRECTOR_USER_ID = d.USER_ID, TLocations_Leaders b, TLeaders c "
                + " where a.STATE = '" + state + "' and a.ACTIVE = 'Y' and a.LOCATION_ID = b.LOCATION_ID and b.PRIMARY_IND = 'Y' and b.LEADER_ID = c.LEADER_ID";

        try {
            rs = cm.getResultsSQL(centerList);
            while (rs.next()) 
            {
                Location l = new Location();
                Leader ld = new Leader();
                l.setLocationAddress1(rs.getString("ADDRESS1"));
                l.setLocationAddress2(rs.getString("ADDRESS2"));
                l.setLocationCity(rs.getString("CITY"));
                l.setLocationCounty(rs.getString("COUNTY"));
                l.setLocationCode(rs.getString("LOCATION_CODE"));
                l.setLocationID(rs.getInt("LOCATION_ID"));
                l.setLocationZipcode(rs.getString("ZIPCODE"));
                l.setLocationName(rs.getString("LOCATION_NAME"));
                l.setLocationState(rs.getString("STATE"));
                l.setLocationConfirmationStatus(rs.getString("CONFIRMATION_STATUS"));
                l.setLocationRegistrationCode(rs.getString("LOC_REGISTRATION_CODE"));
                l.setDirectorUserId(rs.getInt("DIRECTOR_USER_ID"));
                l.setDirectorName(rs.getString("DIRECTOR_NAME"));
                if ( l.getDirectorName() == null )
                {
                    l.setDirectorName("*** NOT ASSIGNED ***");
                }
                l.setActive(rs.getString("ACTIVE"));
                l.setLocationSessions(this.getLocationSessions(l.getLocationID()));
                ld.setFirstName(rs.getString("FIRST_NAME"));
                ld.setLastName(rs.getString("LAST_NAME"));
                ld.setEmail(rs.getString("EMAIL"));
                ld.setPhoneCell(rs.getString("PHONE_CELL"));
                ld.setPhone(rs.getString("PHONE"));
                ld.setAddress1(rs.getString("MGR_ADDRESS1"));
                ld.setAddress2(rs.getString("MGR_ADDRESS2"));
                ld.setCity(rs.getString("MGR_CITY"));
                ld.setState(rs.getString("MGR_STATE"));
                ld.setZipcode(rs.getString("MGR_ZIPCODE"));
                l.setLeader(ld);
                ret.add(l);
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getAdminStateCenters()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public ArrayList<Location> getAdminCenters(String codePattern, String namePattern ) {
        ArrayList<Location> ret = new ArrayList<Location>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        String centerList = "SELECT * FROM TLocations where LOCATION_CODE like '" + codePattern + "%' and LOCATION_NAME like '%" + namePattern + "%'";

        try {
            rs = cm.getResultsSQL(centerList);
            while (rs.next()) 
            {
                Location l = new Location();
                l.setLocationAddress1(rs.getString("ADDRESS1"));
                l.setLocationAddress2(rs.getString("ADDRESS2"));
                l.setLocationCity(rs.getString("CITY"));
                l.setLocationCounty(rs.getString("COUNTY"));
                l.setLocationCode(rs.getString("LOCATION_CODE"));
                l.setLocationID(rs.getInt("LOCATION_ID"));
                l.setLocationZipcode(rs.getString("ZIPCODE"));
                l.setComment(rs.getString("COMMENT"));
                l.setWebsite(rs.getString("WEBSITE"));
                l.setLocationName(rs.getString("LOCATION_NAME"));
                l.setLocationState(rs.getString("STATE"));
                l.setLatitude(rs.getDouble("LATITUDE"));
                l.setLongitude(rs.getDouble("LONGITUDE"));
                l.setLocationConfirmationStatus(rs.getString("CONFIRMATION_STATUS"));
                l.setLocationAccessStatus(rs.getInt("LOC_ACCESS_STATUS"));
                l.setLocationAccessMessage(rs.getString("LOC_ACCESS_MESSAGE"));
                l.setLocationRegistrationCode(rs.getString("LOC_REGISTRATION_CODE"));
                l.setActive(rs.getString("ACTIVE"));
                l.setLocationSessions(this.getLocationSessions(l.getLocationID()));
                ret.add(l);
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getAdminCenters()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public int searchCentersCount(String codePattern, String namePattern ) {
        int ret = 0;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        String centerList = "SELECT count(*) as COUNT FROM TLocations where LOCATION_CODE like '" + codePattern + "%' and LOCATION_NAME like '%" + namePattern + "%'";

        try {
            rs = cm.getResultsSQL(centerList);
            while (rs.next()) 
            {
                ret = rs.getInt("COUNT");
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [searchCentersCount()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public ArrayList<User> getAdminUsers(String firstPattern, String lastPattern, String logonNamePattern, String emailPattern, String userStudentIdPattern) {
        ArrayList<User> ret = new ArrayList<User>();
        if (userStudentIdPattern != null && !userStudentIdPattern.equals("")) 
        {
            User u = this.getUserByStudentId(userStudentIdPattern);
            if (u != null) 
            {
                ret.add(u);
            }
        } 
        else 
        {
            DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            ResultSet rs = null;
            String userList = "SELECT * FROM TUsers where FIRST_NAME like '" + firstPattern + "%' and LAST_NAME like '" + lastPattern + "%' and EMAIL like '" + emailPattern + "%' and LOGON_NAME like '" + logonNamePattern + "%'";

            try {
                rs = cm.getResultsSQL(userList);
                while (rs.next()) {
                    User u = new User();
                    u.setUserID(rs.getInt("USER_ID"));
                    u.setLogonName(rs.getString("LOGON_NAME"));
                    u.setUserFirstName(rs.getString("FIRST_NAME"));
                    u.setLogonPassword(rs.getString("LOGON_PASSWORD"));
                    u.setUserLastName(rs.getString("LAST_NAME"));
                    u.setAuthAnswer(rs.getString("AUTH_ANSWER"));
                    u.setAuthQuestion(rs.getString("AUTH_QUESTION"));
                    u.setAuthCodeTxt(rs.getString("AUTH_CODE"));
                    u.setUserAddress1(rs.getString("ADDRESS1"));
                    u.setUserAddress2(rs.getString("ADDRESS2"));
                    u.setUserCity(rs.getString("CITY"));
                    u.setUserEmail(rs.getString("EMAIL"));
                    u.setUserGender(rs.getString("GENDER"));
                    u.setUserPhone(rs.getString("PHONE"));
                    u.setUserPhoneCell(rs.getString("PHONE_CELL"));
                    u.setUserState(rs.getString("STATE"));
                    u.setUserZipcode(rs.getString("ZIPCODE"));
                    u.setRoleCode(rs.getInt("ROLE"));
                    u.setOtherID(rs.getInt("OTHER_ID"));
                    u.setAdminRole(rs.getInt("ADMIN_ROLE"));
                    u.setRoleName(commonData.getUserRoleName(u.getRoleCode()));
                    if (u.getAdminRole() == 1) {
                        u.setRoleName(u.getRoleName() + ", ADMIN");
                    }
                    ret.add(u);
                }
            } catch (SQLException se) {
                System.out.println("Error in BPC [getAdminUsers()]: " + se);
            } finally {
                DBConnectionManager.dropConnObject(rs);
                cm.freeConnection();
                cm = null;
            }
        }
        return ret;
    }

    public int searchUsersCount(String firstPattern, String lastPattern, String logonNamePattern, String emailPattern) {
        int ret = 0;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        String userList = "SELECT count(*) as COUNT FROM TUsers where FIRST_NAME like '" + firstPattern + "%' and LAST_NAME like '" + lastPattern + "%' and EMAIL like '" + emailPattern + "%' and LOGON_NAME like '" + logonNamePattern + "%'";

        try {
            rs = cm.getResultsSQL(userList);
            while (rs.next()) 
            {
                ret = rs.getInt("COUNT");
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [searchUsersCount()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public ArrayList<RegisteredStudent> getAdminStudentsSearch(String firstPattern, String lastPattern, String logonNamePattern, String emailPattern, String centerCodePattern, String studentId) {
        ArrayList<RegisteredStudent> ret = new ArrayList<RegisteredStudent>();
        String year = ApplicationProperties.getCurrentRegistrationYear(this.dbMgr);
        if (studentId != null && !studentId.equals("")) 
        {
            RegisteredStudent regSt = this.getStudentAndRegistrationForAdmin(studentId, year);
            if ( regSt != null) 
            {
                ret.add( regSt );
            }
        } 
        else 
        {
            DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            ResultSet rs = null;
            String studentList = "SELECT a.FIRST_NAME, a.LAST_NAME, a.STUDENT_IDENTIFICATION_CD, b.STATUS "
                    + " FROM TStudents a left join TRegistration b on a.student_id = b.student_id and b.YEAR = '" + year + "' "
                    + " left join TUsers_Students c on a.student_id = c.student_id "
                    + " left join TUsers d on c.user_id = d.user_id "
                    + " left join TLocations e on b.location_id = e.location_id WHERE 1 = 1 ";
            String first = ( firstPattern != null && !firstPattern.equals("") ) ? " and a.FIRST_NAME like '%" + firstPattern + "%' " : "";
            String last = ( lastPattern != null && !lastPattern.equals("") ) ? " and a.LAST_NAME like '%" + lastPattern + "%' " : "";
            String logonName = ( logonNamePattern != null && !logonNamePattern.equals("") ) ? " and d.LOGON_NAME like '%" + logonNamePattern + "%' " : "";
            String email = ( emailPattern != null && !emailPattern.equals("") ) ? " and d.EMAIL like '%" + emailPattern + "%' " : "";
            String centerCode = ( centerCodePattern != null && !centerCodePattern.equals("") ) ? " and e.LOCATION_CODE like '%" + centerCodePattern + "%' " : "";
            studentList += first + last + logonName + email + centerCode;
            try {
                rs = cm.getResultsSQL( studentList );
                while (rs.next()) {
                    RegisteredStudent regSt = this.getStudentByCodeID(studentId);
                    regSt.setStudentIdentificationCode(rs.getString("STUDENT_IDENTIFICATION_CD"));
                    regSt.setFirstName(rs.getString("FIRST_NAME"));
                    regSt.setLastName(rs.getString("LAST_NAME"));
                    regSt.setStatus(rs.getInt("STATUS"));
                    ret.add( regSt );
                }
            } catch (SQLException se) {
                System.out.println("Error in BPC [getAdminStudentsSearch()]: " + se);
            } finally {
                DBConnectionManager.dropConnObject(rs);
                cm.freeConnection();
                cm = null;
            }
        }
        return ret;
    }

    public int searchStudentsCount(String firstPattern, String lastPattern, String logonNamePattern, String emailPattern, String centerCodePattern ) {
        int ret = 0;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
            String year = ApplicationProperties.getCurrentRegistrationYear(this.dbMgr);
            String studentList = "SELECT count(8) as COUNT "
                    + " FROM TStudents a left join TRegistration b on a.student_id = b.student_id and b.YEAR = '" + year + "' "
                    + " left join TUsers_Students c on a.student_id = c.student_id "
                    + " left join TUsers d on c.user_id = d.user_id "
                    + " left join TLocations e on b.location_id = e.location_id WHERE 1 = 1 ";
            String first = ( firstPattern != null && !firstPattern.equals("") ) ? "and a.FIRST_NAME like '%" + firstPattern + "%'" : "";
            String last = ( lastPattern != null && !lastPattern.equals("") ) ? "and a.LAST_NAME like '%" + lastPattern + "%'" : "";
            String logonName = ( logonNamePattern != null && !logonNamePattern.equals("") ) ? "and d.LOGON_NAME like '%" + logonNamePattern + "%'" : "";
            String email = ( emailPattern != null && !emailPattern.equals("") ) ? "and d.EMAIL like '%" + emailPattern + "%'" : "";
            String centerCode = ( centerCodePattern != null && !centerCodePattern.equals("") ) ? " and e.LOCATION_CODE like '%" + centerCodePattern + "%' " : "";
            studentList += first + last + logonName + email + centerCode;

        try {
            rs = cm.getResultsSQL(studentList);
            while (rs.next()) 
            {
                ret = rs.getInt("COUNT");
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [searchStudentsCount()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public ArrayList<Location> getNewCenterRequests() 
    {
        ArrayList<Location> ret = new ArrayList<Location>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        String centerList = "SELECT * FROM TCenterStaging where CONVERTED = 'N'";

        try {
            rs = cm.getResultsSQL(centerList);
            while (rs.next()) 
            {
                Location l = new Location();
                l.setLocationAddress1(rs.getString("ADDRESS1"));
                l.setLocationAddress2(rs.getString("ADDRESS2"));
                l.setLocationCity(rs.getString("CITY"));
                l.setLocationCounty(rs.getString("COUNTY"));
                l.setLocationCode(rs.getString("LOCATION_CODE"));
                l.setLocationID(rs.getInt("LOCATION_ID"));
                l.setLocationZipcode(rs.getString("ZIPCODE"));
                l.setComment(rs.getString("COMMENT"));
                l.setWebsite(rs.getString("WEBSITE"));
                l.setLocationName(rs.getString("LOCATION_NAME"));
                l.setLocationState(rs.getString("STATE"));
                l.setLatitude(rs.getDouble("LATITUDE"));
                l.setLongitude(rs.getDouble("LONGITUDE"));
                l.setConverted(rs.getString("CONVERTED"));
                int createdByUser = rs.getInt("CREATED_BY");
                l.setCreatedByUser(createdByUser);
                l.setLocationSessions(this.getNewLocationSessions(l.getLocationID()));
                ret.add(l);
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getNewCenterRequests()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

   
    public Location getLocationByID(int locID )
    {
        Location ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TLocations where LOCATION_ID = " + locID);
        ResultSet rs1 = cm.getResultsSQL("select * from TLocationSession where LOCATION_ID = " + locID);
        try
        {
            if (rs.next())
            {
                ret = new Location();
                ret.setLocationAddress1(rs.getString("ADDRESS1"));
                ret.setLocationAddress2(rs.getString("ADDRESS2"));
                ret.setLocationCity(rs.getString("CITY"));
                ret.setLocationCounty(rs.getString("COUNTY"));
                ret.setLocationCode(rs.getString("LOCATION_CODE"));
                ret.setLocationID(rs.getInt("LOCATION_ID"));
                ret.setLocationZipcode(rs.getString("ZIPCODE"));
                ret.setComment(rs.getString("COMMENT"));
                ret.setWebsite(rs.getString("WEBSITE"));
                ret.setLocationName(rs.getString("LOCATION_NAME"));
                ret.setLocationState(rs.getString("STATE"));
                ret.setLatitude(rs.getDouble("LATITUDE"));
                ret.setLongitude(rs.getDouble("LONGITUDE"));
                ret.setLocationConfirmationStatus(rs.getString("CONFIRMATION_STATUS"));
                ret.setLocationAccessStatus(rs.getInt("LOC_ACCESS_STATUS"));
                ret.setLocationAccessMessage(rs.getString("LOC_ACCESS_MESSAGE"));
                ret.setLocationRegistrationCode(rs.getString("LOC_REGISTRATION_CODE"));
                ret.setActive(rs.getString("ACTIVE"));
                ret.setLeader(this.getLeaderByLocation(ret.getLocationID()));
            }
            if ( ret != null )
            {
                ArrayList<LocationSession> sessions = new ArrayList<LocationSession>();
                while (rs1.next())
                {
                    LocationSession ls = new LocationSession();
                    ls.setSessionID(rs1.getInt("SESSION_ID"));
                    ls.setSessionName(rs1.getString("SESSION_NAME"));
                    ls.setLevels(rs1.getString("LEVELS"));
                    ls.setSeatCapacity(rs1.getInt("SEAT_CAPACITY"));
                    ls.setCompetitionTime(rs1.getTime("COMPETITION_TIME"));
                    ls.setSessionStatus(rs1.getInt("SESSION_STATUS"));
                    sessions.add(ls);
                }
                ret.setLocationSessions(sessions);
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BPC [getLocationByID()]: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            DBConnectionManager.dropConnObject(rs1);
            cm.freeConnection();
            cm = null;
        }

        return ret;
    }

    public Location getLocationByCenterRegCode(String code)
    {
        Location ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps = null;
        try
        {
            ps = cm.getConnection().prepareStatement("select * from TLocations where CONFIRMATION_STATUS = 'Y' and ACTIVE = 'Y' and LOC_REGISTRATION_CODE = ?");
            ps.setString(1, code);
            rs = ps.executeQuery();
            if (rs.next())
            {
                ret = new Location();
                ret.setLocationAddress1(rs.getString("ADDRESS1"));
                ret.setLocationAddress2(rs.getString("ADDRESS2"));
                ret.setLocationCity(rs.getString("CITY"));
                ret.setLocationCounty(rs.getString("COUNTY"));
                ret.setLocationCode(rs.getString("LOCATION_CODE"));
                ret.setLocationID(rs.getInt("LOCATION_ID"));
                ret.setLocationZipcode(rs.getString("ZIPCODE"));
                ret.setComment(rs.getString("COMMENT"));
                ret.setWebsite(rs.getString("WEBSITE"));
                ret.setLocationName(rs.getString("LOCATION_NAME"));
                ret.setLocationState(rs.getString("STATE"));
                ret.setLatitude(rs.getDouble("LATITUDE"));
                ret.setLongitude(rs.getDouble("LONGITUDE"));
                ret.setLocationConfirmationStatus(rs.getString("CONFIRMATION_STATUS"));
                ret.setLocationAccessStatus(rs.getInt("LOC_ACCESS_STATUS"));
                ret.setLocationAccessMessage(rs.getString("LOC_ACCESS_MESSAGE"));
                ret.setLocationRegistrationCode(rs.getString("LOC_REGISTRATION_CODE"));
                ret.setLeader(this.getLeaderByLocation(ret.getLocationID()));
                ArrayList<LocationSession> sessions = new ArrayList<LocationSession>();
                rs1 = cm.getResultsSQL("select * from TLocationSession where LOCATION_ID = " + ret.getLocationID());
                while ( rs1.next())
                {
                    LocationSession ls = new LocationSession();
                    ls.setSessionID(rs1.getInt("SESSION_ID"));
                    ls.setSessionName(rs1.getString("SESSION_NAME"));
                    ls.setLevels(rs1.getString("LEVELS"));
                    ls.setSeatCapacity(rs1.getInt("SEAT_CAPACITY"));
                    ls.setCompetitionTime(rs1.getTime("COMPETITION_TIME"));
                    ls.setSessionStatus(rs1.getInt("SESSION_STATUS"));
                    sessions.add(ls);
                }
                ret.setLocationSessions(sessions);
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BPC [getLocationByCenterRegCode()]: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            DBConnectionManager.dropConnObject(rs1);
            DBConnectionManager.dropConnObject(ps);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    
    public LocationSession getLocationSession(int locID, int sesID) {
        LocationSession ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TLocationSession where LOCATION_ID = " + locID + " and SESSION_ID = " + sesID);
        try {
            if (rs.next()) {
                ret = new LocationSession();
                ret.setLocationID(locID);
                ret.setSessionID(rs.getInt("SESSION_ID"));
                ret.setSessionName(rs.getString("SESSION_NAME"));
                ret.setLevels(rs.getString("LEVELS"));
                ret.setSeatCapacity(rs.getInt("SEAT_CAPACITY"));
                ret.setCompetitionTime(rs.getTime("COMPETITION_TIME"));
                ret.setSessionStatus(rs.getInt("SESSION_STATUS"));
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getLocationSession()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }

        return ret;
    }

    public ArrayList<LocationSession> getLocationSessions(int locID )
    {
        ArrayList<LocationSession> ret = new ArrayList<LocationSession>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs1 = cm.getResultsSQL("select * from TLocationSession where LOCATION_ID = " + locID);
        try
        {
                while (rs1.next())
                {
                    LocationSession ls = new LocationSession();
                    ls.setLocationID(locID);
                    ls.setSessionID(rs1.getInt("SESSION_ID"));
                    ls.setSessionName(rs1.getString("SESSION_NAME"));
                    ls.setLevels(rs1.getString("LEVELS"));
                    ls.setSeatCapacity(rs1.getInt("SEAT_CAPACITY"));
                    ls.setCompetitionTime(rs1.getTime("COMPETITION_TIME"));
                    ls.setSessionStatus(rs1.getInt("SESSION_STATUS"));
                    ret.add(ls);
                }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BPC [getLocationSessions()]: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs1);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public ArrayList<Leader> getLocationManagers(int locID) {
        ArrayList<Leader> ret = new ArrayList<Leader>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TLocations_Leaders a, TLeaders b where a.leader_id = b.leader_id and a.location_id = " + locID);
        try {
            while (rs.next()) {
                Leader l = new Leader();
                l.setAddress1(rs.getString("ADDRESS1"));
                l.setAddress2(rs.getString("ADDRESS2"));
                l.setCity(rs.getString("CITY"));
                l.setEmail(rs.getString("EMAIL"));
                l.setFirstName(rs.getString("FIRST_NAME"));
                l.setLastName(rs.getString("LAST_NAME"));
                l.setLeaderID(rs.getInt("LEADER_ID"));
                l.setPhone(rs.getString("PHONE"));
                l.setPhoneCell(rs.getString("PHONE_CELL"));
                l.setState(rs.getString("STATE"));
                l.setZipcode(rs.getString("ZIPCODE"));
                l.setLeaderLogonName(rs.getString("LOGON_NAME"));
                l.setLeaderLogonPassword(rs.getString("LOGON_PASSWORD"));
                l.setAgreementStatus(rs.getString("AGREEMENT_STATUS"));
                String primaryInd = rs.getString("PRIMARY_IND");
                l.setPrimaryManager(primaryInd.equalsIgnoreCase("Y"));
                ret.add(l);
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getLocationManagers()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    
    public ArrayList<LocationSession> getNewLocationSessions(int locID )
    {
        ArrayList<LocationSession> ret = new ArrayList<LocationSession>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs1 = cm.getResultsSQL("select * from TCenterSessionStaging where LOCATION_ID = " + locID);
        try
        {
                while (rs1.next())
                {
                    LocationSession ls = new LocationSession();
                    ls.setLocationID(locID);
                    ls.setSessionID(rs1.getInt("SESSION_ID"));
                    ls.setSessionName(rs1.getString("SESSION_NAME"));
                    ls.setLevels(rs1.getString("LEVELS"));
                    ls.setSeatCapacity(rs1.getInt("SEAT_CAPACITY"));
                    ls.setCompetitionTime(rs1.getTime("COMPETITION_TIME"));
                    ls.setSessionStatus(rs1.getInt("SESSION_STATUS"));
                    ret.add(ls);
                }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BPC [getNewLocationSessions()]: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs1);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean addLocationSession(int centerId, LocationSession ls) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String sessionInsert = "INSERT INTO TLocationSession (LOCATION_ID,SESSION_ID,SESSION_NAME,COMPETITION_TIME,LEVELS,SESSION_STATUS,SEAT_CAPACITY ) "
                + " VALUES (?,?,?,?,?,?,?)";
        PreparedStatement stm = null;
        try {
            stm = cm.getConnection().prepareStatement(sessionInsert);
            stm.setInt(1, centerId);
            stm.setInt(2, ls.getSessionID());
            stm.setString(3, ls.getSessionName());
            java.sql.Time time = new Time(ls.getCompetitionTime().getHours(), ls.getCompetitionTime().getMinutes(), ls.getCompetitionTime().getSeconds());
            stm.setTime(4, time);
            stm.setString(5, ls.getLevels());
            stm.setInt(6, ls.getSessionStatus());
            stm.setInt(7, ls.getSeatCapacity());
            stm.execute();
        } catch (SQLException se) {
            System.out.println("Error in BPC [addLocationSession()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean updateLocationSession(int centerId, LocationSession ls) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String sessionUpdate = "UPDATE TLocationSession set COMPETITION_TIME = ?,LEVELS = ?,SESSION_STATUS = ?,SEAT_CAPACITY = ? WHERE LOCATION_ID = ? AND SESSION_ID = ? ";
        PreparedStatement stm = null;
        try {

            stm = cm.getConnection().prepareStatement(sessionUpdate);
            stm.setInt(5, centerId);
            stm.setInt(6, ls.getSessionID());
            java.sql.Time time = new Time(ls.getCompetitionTime().getHours(), ls.getCompetitionTime().getMinutes(), ls.getCompetitionTime().getSeconds());
            stm.setTime(1, time);
            stm.setString(2, ls.getLevels());
            stm.setInt(3, ls.getSessionStatus());
            stm.setInt(4, ls.getSeatCapacity());
            stm.execute();
        } catch (SQLException se) {
            System.out.println("Error in BPC [updateLocationSession()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean deleteLocationSession(int centerId, LocationSession ls) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String sessionUpdate = "DELETE from TLocationSession WHERE LOCATION_ID = ? AND SESSION_ID = ? ";
        PreparedStatement stm = null;
        try {
            stm = cm.getConnection().prepareStatement(sessionUpdate);
            stm.setInt(1, centerId);
            stm.setInt(2, ls.getSessionID());
            stm.execute();
        } catch (SQLException se) {
            System.out.println("Error in BPC [deleteLocationSession()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean deleteManagerFromCenter(int centerId, Leader leader) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String centerUpdate = "DELETE from TLocations_Leaders WHERE LOCATION_ID = ? AND LEADER_ID = ? ";
        PreparedStatement stm = null;
        try {
            stm = cm.getConnection().prepareStatement(centerUpdate);
            stm.setInt(1, centerId);
            stm.setInt(2, leader.getLeaderID());
            int cnt = stm.executeUpdate();
            if ( cnt != 1 )
            {
                ret = false;
                System.out.println("Error in BPC [deleteManagerFromCenter()]: DELETE COUNT PROBLEM.");
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [deleteManagerFromCenter()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    
    public Leader getLeaderByLocation(int locID)
    {
        Leader ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TLocations_Leaders where LOCATION_ID = " + locID + " and PRIMARY_IND = 'Y'");
        try
        {
            if (rs.next())
            {
                int leaderID = rs.getInt("LEADER_ID");
                ret = this.getLeaderByID(leaderID);
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BusinessProcessControl [getLeaderByLocation()]: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public Leader getLeaderByID(int leaderID)
    {
        Leader ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TLeaders where LEADER_ID = " + leaderID);
        try
        {
            if (rs.next())
            {
                ret = new Leader();
                ret.setAddress1(rs.getString("ADDRESS1"));
                ret.setAddress2(rs.getString("ADDRESS2"));
                ret.setCity(rs.getString("CITY"));
                ret.setEmail(rs.getString("EMAIL"));
                ret.setFirstName(rs.getString("FIRST_NAME"));
                ret.setLastName(rs.getString("LAST_NAME"));
                ret.setLeaderID(rs.getInt("LEADER_ID"));
                ret.setPhone(rs.getString("PHONE"));
                ret.setPhoneCell(rs.getString("PHONE_CELL"));
                ret.setTshirtSize(rs.getString("TSHIRT_SIZE"));
                ret.setTshirtSizeCode(this.commonData.getTshirtCode(ret.getTshirtSize()));
                ret.setState(rs.getString("STATE"));
                ret.setZipcode(rs.getString("ZIPCODE"));
                ret.setLeaderLogonName(rs.getString("LOGON_NAME"));
                ret.setLeaderLogonPassword(rs.getString("LOGON_PASSWORD"));
                ret.setAgreementStatus(rs.getString("AGREEMENT_STATUS"));
                ret.setPrimaryManager(this.isManagerPrimary(leaderID));
                ret.setManagerRole(ret.isPrimaryManager() ? "PRIMARY" : "SUPPORTING" );
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BusinessProcessControl [getLeaderByID()]: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public Leader getLeaderByLogon( String logonName, String password )
    {
        Leader ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TLeaders where LOGON_NAME = '" + logonName + "' and LOGON_PASSWORD = '" + password + "'");
        try
        {
            if (rs.next())
            {
                ret = new Leader();
                ret.setAddress1(rs.getString("ADDRESS1"));
                ret.setAddress2(rs.getString("ADDRESS2"));
                ret.setCity(rs.getString("CITY"));
                ret.setEmail(rs.getString("EMAIL"));
                ret.setFirstName(rs.getString("FIRST_NAME"));
                ret.setLastName(rs.getString("LAST_NAME"));
                ret.setLeaderID(rs.getInt("LEADER_ID"));
                ret.setPhone(rs.getString("PHONE"));
                ret.setTshirtSize(rs.getString("TSHIRT_SIZE"));
                ret.setTshirtSizeCode(this.commonData.getTshirtCode(ret.getTshirtSize()));
                ret.setPhoneCell(rs.getString("PHONE_CELL"));
                ret.setState(rs.getString("STATE"));
                ret.setZipcode(rs.getString("ZIPCODE"));
                ret.setLeaderLogonName(rs.getString("LOGON_NAME"));
                ret.setLeaderLogonPassword(rs.getString("LOGON_PASSWORD"));
                ret.setAgreementStatus(rs.getString("AGREEMENT_STATUS"));
                ret.setPrimaryManager(this.isManagerPrimary(ret.getLeaderID()));
                ret.setManagerRole(ret.isPrimaryManager() ? "PRIMARY" : "SUPPORTING" );
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BusinessProcessControl [getLeaderByLogon()]: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }

        return ret;
    }

    public Leader getLeaderByLogon( String logonName )
    {
        Leader ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TLeaders where LOGON_NAME = '" + logonName + "'");
        try
        {
            if (rs.next())
            {
                ret = new Leader();
                ret.setAddress1(rs.getString("ADDRESS1"));
                ret.setAddress2(rs.getString("ADDRESS2"));
                ret.setCity(rs.getString("CITY"));
                ret.setEmail(rs.getString("EMAIL"));
                ret.setFirstName(rs.getString("FIRST_NAME"));
                ret.setLastName(rs.getString("LAST_NAME"));
                ret.setLeaderID(rs.getInt("LEADER_ID"));
                ret.setPhone(rs.getString("PHONE"));
                ret.setTshirtSize(rs.getString("TSHIRT_SIZE"));
                ret.setTshirtSizeCode(this.commonData.getTshirtCode(ret.getTshirtSize()));
                ret.setPhoneCell(rs.getString("PHONE_CELL"));
                ret.setState(rs.getString("STATE"));
                ret.setZipcode(rs.getString("ZIPCODE"));
                ret.setLeaderLogonName(rs.getString("LOGON_NAME"));
                ret.setLeaderLogonPassword(rs.getString("LOGON_PASSWORD"));
                ret.setAgreementStatus(rs.getString("AGREEMENT_STATUS"));
                ret.setPrimaryManager(this.isManagerPrimary(ret.getLeaderID()));
                ret.setManagerRole(ret.isPrimaryManager() ? "PRIMARY" : "SUPPORTING" );
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BusinessProcessControl [getLeaderByLogon()]: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }

        return ret;
    }

    public boolean isManagerPrimary( int leaderId )
    {
        boolean ret = false;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("SELECT * FROM TLocations_Leaders a, TLocations b " +
            " where a.leader_id = " + leaderId +
            " and a.location_id = b.location_id " +
            " and a.primary_ind = 'Y' " +
            " and b.active = 'Y'");
        try
        {
            if (rs.next())
            {
                ret = true;
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BusinessProcessControl [isManagerPrimary()]: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    
    public ArrayList<RegisteredStudent> getMyStudents(User user) {
        ArrayList<RegisteredStudent> ret = new ArrayList<RegisteredStudent>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;

        try {
            rs = cm.getResultsSQL("select * from TUsers_Students where USER_ID = " + user.getUserID());
            while (rs.next()) {
                int stID = rs.getInt("STUDENT_ID");
                RegisteredStudent st = this.getStudentAndRegistration(stID, ApplicationProperties.getCurrentRegistrationYear(dbMgr));
                if (st != null) {
                    // solution to use user e-mail as parent/guardian e-mail
                    st.setParentGuardianEmail(user.getUserEmail());
                    ret.add(st);
                }
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getMyStudents()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    
    public boolean isUserLogonNameAvailable(String logonName) {
        boolean ret = true;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        PreparedStatement stm = null;
        String sqlString = "select * from TUsers where LOGON_NAME = ?";

        try {
            stm = cm.getConnection().prepareStatement(sqlString);
            stm.setString(1, logonName);
            rs = stm.executeQuery();
            if (rs.next()) {
                ret = false;
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [isUserLogonNameAvailable()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public int createNewUser(User user) {
        int ret = 0;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));

        String insertSql = "INSERT INTO TUsers (FIRST_NAME,LAST_NAME,"
                + "GENDER,ADDRESS1,CITY,STATE,ZIPCODE,LOGON_NAME,LOGON_PASSWORD,"
                + "AUTH_QUESTION,AUTH_ANSWER,AUTH_CODE,PHONE,PHONE_CELL,EMAIL,COMMENT,ROLE,USER_STATUS) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String userIdRetrieval = "SELECT LAST_INSERT_ID() AS USER_ID";
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = cm.getConnection().prepareStatement(insertSql);
            stm.setString(1, user.getUserFirstName().toUpperCase());
            stm.setString(2, user.getUserLastName().toUpperCase());
            stm.setString(3, user.getUserGender());
            stm.setString(4, user.getUserAddress1());
            stm.setString(5, user.getUserCity());
            stm.setString(6, user.getUserState());
            stm.setString(7, user.getUserZipcode());
            stm.setString(8, user.getLogonName());
            stm.setString(9, user.getLogonPassword());
            stm.setString(10, user.getAuthQuestion());
            stm.setString(11, user.getAuthAnswer());
            stm.setString(12, user.getAuthCodeTxt());
            stm.setString(13, user.getUserPhone());
            stm.setString(14, user.getUserPhoneCell());
            stm.setString(15, user.getUserEmail());
            stm.setString(16, "");
            stm.setInt(17, 0);
            stm.setInt(18, 0);
            int result = stm.executeUpdate();
            if (result != 1) {
                System.out.println("Error in BPC [createNewUser()]: User cannot be created");
            }
            rs = cm.getResultsSQL(userIdRetrieval);
            if (rs.next()) {
                ret = rs.getInt("USER_ID");
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [createNewUser()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public int createNewManager(User user) {
        int ret = 0;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));

        String insertSql = "INSERT INTO TLeaders (FIRST_NAME,LAST_NAME,PHONE,PHONE_CELL,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,EMAIL,LOGON_NAME,LOGON_PASSWORD,GENDER ) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String mgrIdRetrieval = "SELECT LAST_INSERT_ID() AS LEADER_ID";
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = cm.getConnection().prepareStatement(insertSql);
            stm.setString(1, user.getUserFirstName().toUpperCase());
            stm.setString(2, user.getUserLastName().toUpperCase());
            stm.setString(3, user.getUserPhone());
            stm.setString(4, user.getUserPhoneCell());
            stm.setString(5, user.getUserAddress1());
            stm.setString(6, user.getUserAddress2());
            stm.setString(7, user.getUserCity());
            stm.setString(8, user.getUserState());
            stm.setString(9, user.getUserZipcode());
            stm.setString(10, user.getUserEmail());
            stm.setString(11, user.getLogonName());
            stm.setString(12, user.getLogonPassword());
            String gender = user.getUserGender();
            int code = 0;
            if ( gender != null && !gender.trim().equals(""))
            {
                code = gender.equalsIgnoreCase("M") ? 1 : 2;
            }
            stm.setInt(13, code );
            int result = stm.executeUpdate();
            if (result != 1) {
                System.out.println("Error in BPC [createNewManager()]: Manager cannot be created");
            }
            rs = cm.getResultsSQL(mgrIdRetrieval);
            if (rs.next()) {
                ret = rs.getInt("LEADER_ID");
            }
            if ( ret != 0 )
            {
                cm.executeStmtSQL("update TUsers set ROLE = 2, OTHER_ID = " + ret + " where USER_ID = " + user.getUserID());
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [createNewManager()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public User getUserById(int userId) {
        User ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TUsers where USER_ID = " + userId);
        try {
            if (rs.next()) {
                ret = new User();
                ret.setUserID(rs.getInt("USER_ID"));
                ret.setLogonName(rs.getString("LOGON_NAME"));
                ret.setUserFirstName(rs.getString("FIRST_NAME"));
                ret.setLogonPassword(rs.getString("LOGON_PASSWORD"));
                ret.setUserLastName(rs.getString("LAST_NAME"));
                ret.setAuthAnswer(rs.getString("AUTH_ANSWER"));
                ret.setAuthQuestion(rs.getString("AUTH_QUESTION"));
                ret.setAuthCodeTxt(rs.getString("AUTH_CODE"));
                ret.setUserAddress1(rs.getString("ADDRESS1"));
                ret.setUserAddress2(rs.getString("ADDRESS2"));
                ret.setUserCity(rs.getString("CITY"));
                ret.setUserEmail(rs.getString("EMAIL"));
                ret.setUserGender(rs.getString("GENDER"));
                ret.setUserPhone(rs.getString("PHONE"));
                ret.setUserPhoneCell(rs.getString("PHONE_CELL"));
                ret.setUserState(rs.getString("STATE"));
                ret.setUserZipcode(rs.getString("ZIPCODE"));
                ret.setRoleCode(rs.getInt("ROLE"));
                ret.setOtherID(rs.getInt("OTHER_ID"));
                ret.setAdminRole(rs.getInt("ADMIN_ROLE"));
                ret.setRoleName(commonData.getUserRoleName(ret.getRoleCode()));
                if (ret.getAdminRole() == 1) {
                    ret.setRoleName(ret.getRoleName() + ", ADMIN");
                }
            }
        } catch (SQLException se) {
            System.out.println("Error in BusinessProcessControl [getUserById()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }

        return ret;
    }

    public User getUserByStudentId(String studentIDCode) {
        User ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select a.* from TUsers a, TUsers_Students b, TStudents c where c.STUDENT_IDENTIFICATION_CD = '" + studentIDCode + "' and c.STUDENT_ID = b.STUDENT_ID and b.USER_ID = a.USER_ID");
        try {
            if (rs.next()) {
                ret = new User();
                ret.setUserID(rs.getInt("USER_ID"));
                ret.setLogonName(rs.getString("LOGON_NAME"));
                ret.setUserFirstName(rs.getString("FIRST_NAME"));
                ret.setLogonPassword(rs.getString("LOGON_PASSWORD"));
                ret.setUserLastName(rs.getString("LAST_NAME"));
                ret.setAuthAnswer(rs.getString("AUTH_ANSWER"));
                ret.setAuthQuestion(rs.getString("AUTH_QUESTION"));
                ret.setAuthCodeTxt(rs.getString("AUTH_CODE"));
                ret.setUserAddress1(rs.getString("ADDRESS1"));
                ret.setUserAddress2(rs.getString("ADDRESS2"));
                ret.setUserCity(rs.getString("CITY"));
                ret.setUserEmail(rs.getString("EMAIL"));
                ret.setUserGender(rs.getString("GENDER"));
                ret.setUserPhone(rs.getString("PHONE"));
                ret.setUserPhoneCell(rs.getString("PHONE_CELL"));
                ret.setUserState(rs.getString("STATE"));
                ret.setUserZipcode(rs.getString("ZIPCODE"));
                ret.setRoleCode(rs.getInt("ROLE"));
                ret.setOtherID(rs.getInt("OTHER_ID"));
                ret.setAdminRole(rs.getInt("ADMIN_ROLE"));
                ret.setRoleName(commonData.getUserRoleName(ret.getRoleCode()));
                if (ret.getAdminRole() == 1) {
                    ret.setRoleName(ret.getRoleName() + ", ADMIN");
                }
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getUserByStudentId()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }

        return ret;
    }
    
    
    public User getUserByManagerId(int mgrId) {
        User ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TUsers where OTHER_ID = " + mgrId);
        try {
            if (rs.next()) {
                ret = new User();
                ret.setUserID(rs.getInt("USER_ID"));
                ret.setLogonName(rs.getString("LOGON_NAME"));
                ret.setUserFirstName(rs.getString("FIRST_NAME"));
                ret.setLogonPassword(rs.getString("LOGON_PASSWORD"));
                ret.setUserLastName(rs.getString("LAST_NAME"));
                ret.setAuthAnswer(rs.getString("AUTH_ANSWER"));
                ret.setAuthQuestion(rs.getString("AUTH_QUESTION"));
                ret.setAuthCodeTxt(rs.getString("AUTH_CODE"));
                ret.setUserAddress1(rs.getString("ADDRESS1"));
                ret.setUserAddress2(rs.getString("ADDRESS2"));
                ret.setUserCity(rs.getString("CITY"));
                ret.setUserEmail(rs.getString("EMAIL"));
                ret.setUserGender(rs.getString("GENDER"));
                ret.setUserPhone(rs.getString("PHONE"));
                ret.setUserPhoneCell(rs.getString("PHONE_CELL"));
                ret.setUserState(rs.getString("STATE"));
                ret.setUserZipcode(rs.getString("ZIPCODE"));
                ret.setRoleCode(rs.getInt("ROLE"));
                ret.setOtherID(rs.getInt("OTHER_ID"));
                ret.setAdminRole(rs.getInt("ADMIN_ROLE"));
                ret.setRoleName(commonData.getUserRoleName(ret.getRoleCode()));
                if (ret.getAdminRole() == 1) {
                    ret.setRoleName(ret.getRoleName() + ", ADMIN");
                }
            }
        } catch (SQLException se) {
            System.out.println("Error in BusinessProcessControl [getUserByManagerId()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }

        return ret;
    }

    public User getUserByLogon(String logon, String password) {
        User ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String sqlString = "Select * from TUsers where LOGON_NAME = ? and LOGON_PASSWORD = ?";
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = cm.getConnection().prepareStatement(sqlString);
            stm.setString(1, logon);
            stm.setString(2, password);
            rs = stm.executeQuery();
            if (rs.next()) {
                String passwordFromTable = rs.getString("LOGON_PASSWORD");
                if (passwordFromTable.equals(password)) {
                    ret = new User();
                    ret.setUserID(rs.getInt("USER_ID"));
                    ret.setLogonName(rs.getString("LOGON_NAME"));
                    ret.setUserFirstName(rs.getString("FIRST_NAME"));
                    ret.setLogonPassword(rs.getString("LOGON_PASSWORD"));
                    ret.setUserLastName(rs.getString("LAST_NAME"));
                    ret.setAuthAnswer(rs.getString("AUTH_ANSWER"));
                    ret.setAuthQuestion(rs.getString("AUTH_QUESTION"));
                    ret.setAuthCodeTxt(rs.getString("AUTH_CODE"));
                    ret.setUserAddress1(rs.getString("ADDRESS1"));
                    ret.setUserAddress2(rs.getString("ADDRESS2"));
                    ret.setUserCity(rs.getString("CITY"));
                    ret.setUserEmail(rs.getString("EMAIL"));
                    ret.setUserGender(rs.getString("GENDER"));
                    ret.setUserPhone(rs.getString("PHONE"));
                    ret.setUserPhoneCell(rs.getString("PHONE_CELL"));
                    ret.setUserState(rs.getString("STATE"));
                    ret.setUserZipcode(rs.getString("ZIPCODE"));
                    ret.setRoleCode(rs.getInt("ROLE"));
                    ret.setOtherID(rs.getInt("OTHER_ID"));
                    ret.setAdminRole(rs.getInt("ADMIN_ROLE"));
                    ret.setRoleName(commonData.getUserRoleName(ret.getRoleCode()));
                    if ( ret.getAdminRole() == 1 )
                    {
                        ret.setRoleName(ret.getRoleName() + ", ADMIN");
                    }
                }
            }
        } catch (SQLException se) {
            System.out.println("Error in BusinessProcessControl [getUserByLogon(logon,password)]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public User getUserByLogon(String logon) {
        User ret = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String sqlString = "Select * from TUsers where LOGON_NAME = ?";
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = cm.getConnection().prepareStatement(sqlString);
            stm.setString(1, logon);
            rs = stm.executeQuery();
            if (rs.next()) {
                ret = new User();
                ret.setUserID(rs.getInt("USER_ID"));
                ret.setLogonName(rs.getString("LOGON_NAME"));
                ret.setUserFirstName(rs.getString("FIRST_NAME"));
                ret.setLogonPassword(rs.getString("LOGON_PASSWORD"));
                ret.setUserLastName(rs.getString("LAST_NAME"));
                ret.setAuthAnswer(rs.getString("AUTH_ANSWER"));
                ret.setAuthQuestion(rs.getString("AUTH_QUESTION"));
                ret.setAuthCodeTxt(rs.getString("AUTH_CODE"));
                ret.setUserAddress1(rs.getString("ADDRESS1"));
                ret.setUserAddress2(rs.getString("ADDRESS2"));
                ret.setUserCity(rs.getString("CITY"));
                ret.setUserEmail(rs.getString("EMAIL"));
                ret.setUserGender(rs.getString("GENDER"));
                ret.setUserPhone(rs.getString("PHONE"));
                ret.setUserPhoneCell(rs.getString("PHONE_CELL"));
                ret.setUserState(rs.getString("STATE"));
                ret.setUserZipcode(rs.getString("ZIPCODE"));
                ret.setRoleCode(rs.getInt("ROLE"));
                ret.setOtherID(rs.getInt("OTHER_ID"));
                ret.setAdminRole(rs.getInt("ADMIN_ROLE"));
                ret.setRoleName(commonData.getUserRoleName(ret.getRoleCode()));
                if (ret.getAdminRole() == 1) {
                    ret.setRoleName(ret.getRoleName() + ", ADMIN");
                }
            }
        } catch (SQLException se) {
            System.out.println("Error in BusinessProcessControl [getUserByLogon(logon)]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public UserEmailConfirmation getUserEmailConfirmation(int userId, String email) {
        UserEmailConfirmation userEmailConfirmation = new UserEmailConfirmation();
        userEmailConfirmation.setUserId(userId);
        userEmailConfirmation.setEmail(email);
        userEmailConfirmation.setEmailStatus("N");
        userEmailConfirmation.setConfirmationDate("");
        userEmailConfirmation.setAuthCode("");

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;

        try {
            rs = cm.getResultsSQL("select * from TUserEmailConfirmation where USER_ID = " + userId + " and EMAIL = '" + email + "'");
            if (rs.next()) {
                userEmailConfirmation = new UserEmailConfirmation();
                userEmailConfirmation.setUserId(rs.getInt("USER_ID"));
                userEmailConfirmation.setEmail(rs.getString("EMAIL"));
                userEmailConfirmation.setEmailStatus(rs.getString("EMAIL_STATUS"));
                userEmailConfirmation.setConfirmationDate(rs.getString("CONFIRMATION_DATE"));
                userEmailConfirmation.setAuthCode(rs.getString("AUTH_CODE"));
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getUserEmailConfirmation()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }

        return userEmailConfirmation;
    }

    public void setUserEmailConfirmation(UserEmailConfirmation userEmailConfirmation) {

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        PreparedStatement stm = null;
        String updateRec = "update TUserEmailConfirmation set EMAIL_STATUS = ?, CONFIRMATION_DATE = now(), AUTH_CODE = ? where USER_ID = ? and EMAIL = ?";
        String insertRec = "insert into TUserEmailConfirmation (USER_ID,EMAIL,EMAIL_STATUS,CONFIRMATION_DATE,AUTH_CODE) values (?,?,?,now(),?)";

        try {
            rs = cm.getResultsSQL("select * from TUserEmailConfirmation where USER_ID = " + userEmailConfirmation.getUserId() + " and EMAIL = '" + userEmailConfirmation.getEmail() + "'");
            if (rs.next()) {
                stm = cm.getConnection().prepareStatement(updateRec);
                stm.setString(1, userEmailConfirmation.getEmailStatus());
                stm.setString(2, userEmailConfirmation.getAuthCode());
                stm.setInt(3, userEmailConfirmation.getUserId());
                stm.setString(4, userEmailConfirmation.getEmail());
            } else {
                stm = cm.getConnection().prepareStatement(insertRec);
                stm.setInt(1, userEmailConfirmation.getUserId());
                stm.setString(2, userEmailConfirmation.getEmail());
                stm.setString(3, userEmailConfirmation.getEmailStatus());
                stm.setString(4, userEmailConfirmation.getAuthCode());
            }
            stm.execute();
        } catch (SQLException se) {
            System.out.println("Error in BPC [setUserEmailConfirmation()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
    }

    public void updateUser(User user) {
        DBConnectionManager cm = null;
        PreparedStatement stm = null;
        cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String updateSql = "UPDATE TUsers set FIRST_NAME = ?,LAST_NAME = ?,LOGON_PASSWORD = ?,AUTH_QUESTION = ?,AUTH_ANSWER = ?,AUTH_CODE = ?,PHONE_CELL = ?,EMAIL = ? where USER_ID = ?";
        try {
            stm = cm.getConnection().prepareStatement(updateSql);
            stm.setString(1, user.getUserFirstName().toUpperCase());
            stm.setString(2, user.getUserLastName().toUpperCase());
            stm.setString(3, user.getLogonPassword());
            stm.setString(4, user.getAuthQuestion());
            stm.setString(5, user.getAuthAnswer());
            stm.setString(6, user.getAuthCodeTxt());
            stm.setString(7, user.getUserPhoneCell());
            stm.setString(8, user.getUserEmail());
            stm.setInt(9, user.getUserID());

            int result = stm.executeUpdate();
            if (result != 1) {
                System.out.println("Error in BPC [updateUser()]: No such USER_ID");
            }
        } catch (Exception se) {
            System.out.println("Error in BPC [updateUser()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
    }

    public void updateManagerSimple(Leader mgr) {
        DBConnectionManager cm = null;
        PreparedStatement stm = null;
        cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String updateSql = "UPDATE TLeaders set EMAIL = ? where LEADER_ID = ?";
        try {
            stm = cm.getConnection().prepareStatement(updateSql);
            stm.setString(1, mgr.getEmail());
            stm.setInt(2, mgr.getLeaderID());

            int result = stm.executeUpdate();
            if (result != 1) {
                System.out.println("Error in BPC [updateManagerSimple()]: No such LEADER_ID");
            }
        } catch (Exception se) {
            System.out.println("Error in BPC [updateManagerSimple()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
    }

    public ArrayList<User> findMatchedUser(UserPasswordRetrieval upr) {
        ArrayList<User> ret = new ArrayList<User>();

        String queryPart = upr.getQueryToMatchUser();
        if (queryPart != null) {
            DBConnectionManager cm = null;
            PreparedStatement stm = null;
            ResultSet rs = null;

            cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            String updateSql = "select * from TUsers " + queryPart;
            try {
                stm = cm.getConnection().prepareStatement(updateSql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("USER_ID"));
                    user.setLogonName(rs.getString("LOGON_NAME"));
                    user.setUserFirstName(rs.getString("FIRST_NAME"));
                    user.setLogonPassword(rs.getString("LOGON_PASSWORD"));
                    user.setUserLastName(rs.getString("LAST_NAME"));
                    user.setAuthAnswer(rs.getString("AUTH_ANSWER"));
                    user.setAuthQuestion(rs.getString("AUTH_QUESTION"));
                    user.setAuthCodeTxt(rs.getString("AUTH_CODE"));
                    user.setUserAddress1(rs.getString("ADDRESS1"));
                    user.setUserAddress2(rs.getString("ADDRESS2"));
                    user.setUserCity(rs.getString("CITY"));
                    user.setUserEmail(rs.getString("EMAIL"));
                    user.setUserGender(rs.getString("GENDER"));
                    user.setUserPhone(rs.getString("PHONE"));
                    user.setUserPhoneCell(rs.getString("PHONE_CELL"));
                    user.setUserState(rs.getString("STATE"));
                    user.setUserZipcode(rs.getString("ZIPCODE"));
                    user.setRoleCode(rs.getInt("ROLE"));
                    user.setOtherID(rs.getInt("OTHER_ID"));
                    user.setRoleName(commonData.getUserRoleName(user.getRoleCode()));
                    ret.add(user);
                }
            } catch (Exception se) {
                System.out.println("Error in BPC [findMatchedUser()]: " + se);
            } finally {
                DBConnectionManager.dropConnObject(stm);
                DBConnectionManager.dropConnObject(rs);
                cm.freeConnection();
                cm = null;
            }
        }
        return ret;
    }

    public boolean updateUserProfile(User user) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String updateRec = "update TUsers set FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, AUTH_QUESTION = ?, AUTH_ANSWER = ?, AUTH_CODE = ?, PHONE_CELL = ?, ADDRESS1 = ?, CITY = ?, STATE = ?, ZIPCODE = ? where USER_ID = ?";

        try {
            stm = cm.getConnection().prepareStatement(updateRec);
            stm.setString(1, user.getUserFirstName().toUpperCase());
            stm.setString(2, user.getUserLastName().toUpperCase());
            stm.setString(3, user.getUserEmail());
            stm.setString(4, user.getAuthQuestion());
            stm.setString(5, user.getAuthAnswer());
            stm.setString(6, user.getAuthCodeTxt());
            stm.setString(7, user.getUserPhoneCell());
            stm.setString(8, user.getUserAddress1());
            stm.setString(9, user.getUserCity());
            stm.setString(10, user.getUserState());
            stm.setString(11, user.getUserZipcode());
            stm.setInt(12, user.getUserID());
            stm.execute();
        } catch (SQLException se) {
            System.out.println("Error in BPC [updateUserProfile()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean updateManagerProfile(Leader mgr) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String updateRec = "update TLeaders set FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, PHONE_CELL = ?, ADDRESS1 = ?, CITY = ?, STATE = ?, ZIPCODE = ?, TSHIRT_SIZE = ? where LEADER_ID = ?";

        try {
            stm = cm.getConnection().prepareStatement(updateRec);
            stm.setString(1, mgr.getFirstName().toUpperCase());
            stm.setString(2, mgr.getLastName().toUpperCase());
            stm.setString(3, mgr.getEmail());
            stm.setString(4, mgr.getPhoneCell());
            stm.setString(5, mgr.getAddress1());
            stm.setString(6, mgr.getCity());
            stm.setString(7, mgr.getState());
            stm.setString(8, mgr.getZipcode());
            stm.setString(9, this.commonData.getTshirtCodeName(mgr.getTshirtSizeCode()));
            stm.setInt(10, mgr.getLeaderID());
            stm.execute();
        } catch (SQLException se) {
            System.out.println("Error in BPC [updateManagerProfile()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean updateManagerProfileFull(Leader mgr) 
        {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String updateRec = "update TLeaders set FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, PHONE_CELL = ?, ADDRESS1 = ?, CITY = ?, STATE = ?, ZIPCODE = ?, TSHIRT_SIZE = ?, LOGON_NAME = ?, LOGON_PASSWORD = ? where LEADER_ID = ?";

        try {
            stm = cm.getConnection().prepareStatement(updateRec);
            stm.setString(1, mgr.getFirstName().toUpperCase());
            stm.setString(2, mgr.getLastName().toUpperCase());
            stm.setString(3, mgr.getEmail());
            stm.setString(4, mgr.getPhoneCell());
            stm.setString(5, mgr.getAddress1());
            stm.setString(6, mgr.getCity());
            stm.setString(7, mgr.getState());
            stm.setString(8, mgr.getZipcode());
            stm.setString(9, this.commonData.getTshirtCodeName(mgr.getTshirtSizeCode()));
            stm.setString(10, mgr.getLeaderLogonName());
            stm.setString(11, mgr.getLeaderLogonPassword());
            stm.setInt(12, mgr.getLeaderID());
            stm.execute();
        } catch (SQLException se) {
            System.out.println("Error in BPC [updateManagerProfileFull()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean updateUserPassword(User user) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String updateRec = "update TUsers set LOGON_PASSWORD = ? where USER_ID = ?";

        try {
            stm = cm.getConnection().prepareStatement(updateRec);
            stm.setString(1, user.getLogonPassword());
            stm.setInt(2, user.getUserID());
            stm.execute();
        } catch (SQLException se) {
            System.out.println("Error in BPC [updateUserPassword()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean updateManagerPassword(Leader mgr) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String updateRec = "update TLeaders set LOGON_PASSWORD = ? where LEADER_ID = ?";

        try {
            stm = cm.getConnection().prepareStatement(updateRec);
            stm.setString(1, mgr.getLeaderLogonPassword());
            stm.setInt(2, mgr.getLeaderID());
            stm.execute();
        } catch (SQLException se) {
            System.out.println("Error in BPC [updateManagerPassword()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public double getLocationRegistrationFee(int locID) {
        double defaultFee = ApplicationProperties.getDefaultRegistrationFee(dbMgr);
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TLocationRegistrationFee where LOCATION_ID = " + locID + " and YEAR = '" + ApplicationProperties.getCurrentRegistrationYear(dbMgr) + "' and NOW() > DATE_FROM and NOW() < DATE_TO");
        double fee = defaultFee;
        try {
            if (rs.next()) {
                fee = rs.getDouble("AMOUNT");
            }
        } catch (SQLException e) {
            fee = defaultFee;
            System.out.println("Error getting a registration fee for this location: " + locID + ".\n" + e);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return fee;
    }

    public RegisteredStudent getStudentByID(int id) {
        RegisteredStudent st = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TStudents where STUDENT_ID = " + id);
        try {
            if (rs.next()) {
                st = new RegisteredStudent();
                st.setStudentID(rs.getInt("STUDENT_ID"));
                st.setStudentIdentificationCode(rs.getString("STUDENT_IDENTIFICATION_CD"));
                st.setAddress1(rs.getString("ADDRESS1"));
                st.setAddress2(rs.getString("ADDRESS2"));
                st.setCity(rs.getString("CITY"));
                st.setEthnicGroupCode(rs.getInt("ETHNIC_GROUP"));
                st.setEthnicGroupName(commonData.getEthnicGroupName(st.getEthnicGroupCode()));
                st.setFirstName(rs.getString("FIRST_NAME"));
                st.setGenderCode(rs.getInt("GENDER"));
                st.setGender(commonData.getGenderName(st.getGenderCode()));
                st.setLastName(rs.getString("LAST_NAME"));
                st.setMiddleName(rs.getString("MIDDLE_NAME"));
                st.setParentGuardianEmail(rs.getString("PARENT_GUARDIAN_EMAIL"));
                st.setSchoolName(rs.getString("SCHOOL_NAME"));
                st.setState(rs.getString("STATE"));
                st.setStudentEmail(rs.getString("STUDENT_EMAIL"));
                st.setTeacherEmail(rs.getString("TEACHER_EMAIL"));
                st.setTeacherFirstName(rs.getString("TEACHER_FIRST_NAME"));
                st.setTeacherLastName(rs.getString("TEACHER_LAST_NAME"));
                st.setZipcode(rs.getString("ZIPCODE"));
                st.setCountryOfOrigin(rs.getString("COUNTRY_OF_ORIGIN"));
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [geStudentByIDNumber()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return st;
    }

    public RegisteredStudent getStudentByCodeID(String id) {
        RegisteredStudent st = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TStudents where STUDENT_IDENTIFICATION_CD = '" + id + "'");
        try {
            if (rs.next()) {
                st = new RegisteredStudent();
                st.setStudentID(rs.getInt("STUDENT_ID"));
                st.setStudentIdentificationCode(rs.getString("STUDENT_IDENTIFICATION_CD"));
                st.setAddress1(rs.getString("ADDRESS1"));
                st.setAddress2(rs.getString("ADDRESS2"));
                st.setCity(rs.getString("CITY"));
                st.setEthnicGroupCode(rs.getInt("ETHNIC_GROUP"));
                st.setEthnicGroupName(commonData.getEthnicGroupName(st.getEthnicGroupCode()));
                st.setFirstName(rs.getString("FIRST_NAME"));
                st.setGenderCode(rs.getInt("GENDER"));
                st.setGender(commonData.getGenderName(st.getGenderCode()));
                st.setLastName(rs.getString("LAST_NAME"));
                st.setMiddleName(rs.getString("MIDDLE_NAME"));
                st.setParentGuardianEmail(rs.getString("PARENT_GUARDIAN_EMAIL"));
                st.setSchoolName(rs.getString("SCHOOL_NAME"));
                st.setState(rs.getString("STATE"));
                st.setStudentEmail(rs.getString("STUDENT_EMAIL"));
                st.setTeacherEmail(rs.getString("TEACHER_EMAIL"));
                st.setTeacherFirstName(rs.getString("TEACHER_FIRST_NAME"));
                st.setTeacherLastName(rs.getString("TEACHER_LAST_NAME"));
                st.setZipcode(rs.getString("ZIPCODE"));
                st.setCountryOfOrigin(rs.getString("COUNTRY_OF_ORIGIN"));
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getStudentByCodeID()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return st;
    }

    public RegisteredStudent getRegistration(int stID, String year) {
        RegisteredStudent reg = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TRegistration where STUDENT_ID = " + stID + " and YEAR = '" + year + "'");
        try {
            if (rs.next()) {
                reg = new RegisteredStudent();
                reg.setRegistrationID(rs.getInt("REGISTRATION_ID"));
                reg.setStudentID(rs.getInt("STUDENT_ID"));
                reg.setYear(rs.getString("YEAR"));
                reg.setLocationID(rs.getInt("LOCATION_ID"));
                reg.setSessionID(rs.getInt("SESSION_ID"));
                reg.setStatus(rs.getInt("STATUS"));
                reg.setLevel(rs.getString("LEVEL"));
                reg.setContactPhone1(rs.getString("CONTACT_PHONE1"));
                reg.setContactPhone2(rs.getString("CONTACT_PHONE2"));
                reg.setPaymentID(rs.getString("PP_PAYMENT_ID"));
                reg.setTransactionID(rs.getString("PP_TRANSACTION_ID"));
                reg.setPaymentAmount(rs.getDouble("PAYMENT_AMOUNT"));
                reg.setTshirtSize(rs.getString("TSHIRT_SIZE"));
                reg.setLastUpdated("" + rs.getTimestamp("LAST_UPDATED"));
                reg.setAdCode(rs.getInt("AD_CODE"));
                reg.setAdNote(rs.getString("AD_NOTE"));
                reg.setStateOfResidency(rs.getString("RESIDENCY_STATE"));
                reg.setDiscountCodeId(rs.getInt("DISCOUNT_CODE_ID"));
                reg.setDiscountTypeId(rs.getInt("DISCOUNT_TYPE_ID"));
                reg.setDiscountTypeName(rs.getString("DISCOUNT_TYPE_NAME"));
                if (reg.getDiscountTypeName() == null) {
                    reg.setDiscountTypeName("");
                }
                try {
                    reg.setDateTime("" + rs.getTimestamp("REG_DATE"));
                } catch (SQLException se) {
                    reg.setDateTime("");
                }
                String memo = rs.getString("MEMO");
                if (memo == null) {
                    memo = "";
                }
                reg.setParentNote(memo);
                String adminMemo = rs.getString("ADMIN_MEMO");
                if (adminMemo == null) {
                    adminMemo = "";
                }
                reg.setAdminNote(adminMemo);
                reg.setUpdatedBy(rs.getInt("UPDATED_BY"));
            }
        } catch (SQLException se) {
            System.out.println("Error in BusinessProcessControl [getRegistration()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return reg;
    }

    // try to combine this and the version for admin in one
    public RegisteredStudent getStudentAndRegistration(int stID, String year) {
        RegisteredStudent reg = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select * from TRegistration where STUDENT_ID = " + stID + " and YEAR = '" + year + "'");
        reg = new RegisteredStudent();
        try {
            if (rs.next()) {
                reg.setRegistrationID(rs.getInt("REGISTRATION_ID"));
                reg.setStudentID(rs.getInt("STUDENT_ID"));
                reg.setYear(rs.getString("YEAR"));
                reg.setLocationID(rs.getInt("LOCATION_ID"));
                reg.setSessionID(rs.getInt("SESSION_ID"));
                reg.setStatus(rs.getInt("STATUS"));
                reg.setLevel(rs.getString("LEVEL"));
                if ( reg.getLevel() != null && !reg.getLevel().trim().equals(""))
                {
                    reg.setLevelCode(Integer.parseInt(reg.getLevel()));
                }
                reg.setContactPhone1(rs.getString("CONTACT_PHONE1"));
                reg.setContactPhone2(rs.getString("CONTACT_PHONE2"));
                reg.setPaymentID(rs.getString("PP_PAYMENT_ID"));
                reg.setPaymentAmount(rs.getDouble("PAYMENT_AMOUNT"));
                reg.setTransactionID(rs.getString("PP_TRANSACTION_ID"));
                reg.setTshirtSize(rs.getString("TSHIRT_SIZE"));
                reg.setTshirtSizeCode(this.commonData.getTshirtCode(reg.getTshirtSize()));
                try {
                    reg.setLastUpdated("" + rs.getTimestamp("LAST_UPDATED"));
                } catch (SQLException se) {
                    reg.setLastUpdated("");
                }
                reg.setAdCode(rs.getInt("AD_CODE"));
                reg.setAdNote(rs.getString("AD_NOTE"));
                reg.setStateOfResidency(rs.getString("RESIDENCY_STATE"));
                reg.setDiscountCodeId(rs.getInt("DISCOUNT_CODE_ID"));
                reg.setDiscountTypeId(rs.getInt("DISCOUNT_TYPE_ID"));
                reg.setDiscountTypeName(rs.getString("DISCOUNT_TYPE_NAME"));
                if (reg.getDiscountTypeName() == null) {
                    reg.setDiscountTypeName("");
                }
                try {
                    reg.setDateTime("" + rs.getTimestamp("REG_DATE"));
                } catch (SQLException se) {
                    reg.setDateTime("");
                }
                String memo = rs.getString("MEMO");
                if (memo == null) {
                    memo = "";
                }
                reg.setParentNote(memo);
                String adminMemo = rs.getString("ADMIN_MEMO");
                if (adminMemo == null) {
                    adminMemo = "";
                }
                reg.setAdminNote(adminMemo);
                reg.setUpdatedBy(rs.getInt("UPDATED_BY"));
                // get location and session info
                Location l = this.getLocationByID(reg.getLocationID());
                if ( l != null )
                {
                    reg.setLocationCode(l.getLocationCode());
                    LocationSession ls = l.getSession(reg.getSessionID());
                    if ( ls != null )
                    {
                        reg.setSessionName(ls.getSessionInfo());
                    }
                }
                
            }
            rs = cm.getResultsSQL("select * from TStudents where STUDENT_ID = " + stID);
            if (rs.next()) {
                reg.setStudentID(rs.getInt("STUDENT_ID"));
                reg.setStudentIdentificationCode(rs.getString("STUDENT_IDENTIFICATION_CD"));
                reg.setAddress1(rs.getString("ADDRESS1"));
                reg.setAddress2(rs.getString("ADDRESS2"));
                reg.setCity(rs.getString("CITY"));
                reg.setEthnicGroupCode(rs.getInt("ETHNIC_GROUP"));
                reg.setEthnicGroupName(commonData.getEthnicGroupName(reg.getEthnicGroupCode()));
                reg.setFirstName(rs.getString("FIRST_NAME"));
                reg.setGenderCode(rs.getInt("GENDER"));
                reg.setGender(commonData.getGenderName(reg.getGenderCode()));
                reg.setLastName(rs.getString("LAST_NAME"));
                reg.setMiddleName(rs.getString("MIDDLE_NAME"));
                reg.setParentGuardianEmail(rs.getString("PARENT_GUARDIAN_EMAIL"));
                reg.setSchoolName(rs.getString("SCHOOL_NAME"));
                reg.setState(rs.getString("STATE"));
                reg.setStudentEmail(rs.getString("STUDENT_EMAIL"));
                reg.setTeacherEmail(rs.getString("TEACHER_EMAIL"));
                reg.setTeacherFirstName(rs.getString("TEACHER_FIRST_NAME"));
                reg.setTeacherLastName(rs.getString("TEACHER_LAST_NAME"));
                reg.setZipcode(rs.getString("ZIPCODE"));
                reg.setCountryOfOrigin(rs.getString("COUNTRY_OF_ORIGIN"));
            }

        } catch (SQLException se) {
            System.out.println("Error in BusinessProcessControl [getStudentAndRegistration()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return reg;
    }

    public void updateStudentAndRegistration(RegisteredStudent reg, String year) {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String updateSQLSt = "update TStudents set FIRST_NAME = ?, LAST_NAME = ?, MIDDLE_NAME = ?, STUDENT_EMAIL = ?, PARENT_GUARDIAN_EMAIL = ?,"
                + "GENDER = ?, ETHNIC_GROUP = ?, TEACHER_FIRST_NAME = ?, TEACHER_LAST_NAME = ?, TEACHER_EMAIL = ?,"
                + "STUDENT_IDENTIFICATION_CD = ?, ADDRESS1 = ?, ADDRESS2 = ?, CITY = ?, STATE = ?, ZIPCODE = ?, SCHOOL_NAME = ?,"
                + "COUNTRY_OF_ORIGIN = ? where STUDENT_ID = ?";
        String updateSQLReg = "update TRegistration set LOCATION_ID = ?, SESSION_ID = ?, STATUS = ?, LEVEL = ?, CONTACT_PHONE1 = ?, CONTACT_PHONE2 = ?,"
                + " TSHIRT_SIZE = ?, STUDENT_IDENTIFICATION_CD = ?, MEMO = ?,"
                + " AD_CODE = ?, AD_NOTE = ?, LAST_UPDATED = NOW(), UPDATED_BY = ?, DISCOUNT_CODE_ID = ?, DISCOUNT_TYPE_ID = ?, DISCOUNT_TYPE_NAME = ?, RESIDENCY_STATE = ?, ADMIN_MEMO = ? "
                + " where STUDENT_ID = ? and YEAR = ?";

        try {
            // student update 
            stm = cm.getConnection().prepareStatement(updateSQLSt);
            stm.setString(1, reg.getFirstName().toUpperCase());
            stm.setString(2, reg.getLastName().toUpperCase());
            stm.setString(3, ((reg.getMiddleName() != null && !reg.getMiddleName().trim().equals("")) ? reg.getMiddleName().substring(0, 1).toUpperCase() : ""));
            stm.setString(4, reg.getStudentEmail());
            stm.setString(5, reg.getParentGuardianEmail());
            stm.setInt(6, reg.getGenderCode());
            stm.setInt(7, reg.getEthnicGroupCode());
            stm.setString(8, reg.getTeacherFirstName());
            stm.setString(9, reg.getTeacherLastName());
            stm.setString(10, reg.getTeacherEmail());
            stm.setString(11, reg.getStudentIdentificationCode());
            stm.setString(12, reg.getAddress1());
            stm.setString(13, reg.getAddress2());
            stm.setString(14, reg.getCity());
            stm.setString(15, reg.getState());
            stm.setString(16, reg.getZipcode());
            stm.setString(17, reg.getSchoolName());
            stm.setString(18, reg.getCountryOfOrigin());
            stm.setInt(19, reg.getStudentID());
            stm.execute();
            // registration update
            stm = cm.getConnection().prepareStatement(updateSQLReg);
            stm.setInt(1, reg.getLocationID());
            stm.setInt(2, reg.getSessionID());
            stm.setInt(3, reg.getStatus());
            stm.setString(4, "" + reg.getLevelCode());
            stm.setString(5, reg.getContactPhone1());
            stm.setString(6, reg.getContactPhone2());
            stm.setString(7, this.commonData.getTshirtCodeName(reg.getTshirtSizeCode()));
            stm.setString(8, reg.getStudentIdentificationCode());
            stm.setString(9, reg.getParentNote());
            stm.setInt(10, reg.getAdCode());
            stm.setString(11, reg.getAdNote());
            stm.setInt(12, reg.getUpdatedBy());
            stm.setInt(13, reg.getDiscountCodeId());
            stm.setInt(14, reg.getDiscountTypeId());
            stm.setString(15, reg.getDiscountTypeName());
            stm.setString(16, reg.getStateOfResidency());
            stm.setString(17, reg.getAdminNote());
            stm.setInt(18, reg.getStudentID());
            stm.setString(19, year);
            stm.execute();
        } catch (SQLException se) {
            System.out.println("Error in BPC [updateStudentAndRegistration()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
    }

    public int createRegistration(RegisteredStudent reg) {
        
        int ret = -1;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        ResultSet rs = null;
        String insertReg = "INSERT INTO TRegistration (STUDENT_ID,YEAR,REG_DATE,LOCATION_ID,SESSION_ID,STATUS,LEVEL,"
                + " CONTACT_PHONE1,CONTACT_PHONE2,TSHIRT_SIZE,STUDENT_IDENTIFICATION_CD,MEMO,AD_CODE,AD_NOTE,LAST_UPDATED,UPDATED_BY,DISCOUNT_CODE_ID,DISCOUNT_TYPE_ID,DISCOUNT_TYPE_NAME,RESIDENCY_STATE,ADMIN_MEMO) "
                + " VALUES (?,?,NOW(),?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?)";
        String regIdRetrieval = "SELECT LAST_INSERT_ID() AS REGISTRATION_ID";

        try {
            stm = cm.getConnection().prepareStatement(insertReg);
            stm.setInt(1, reg.getStudentID());
            stm.setString(2, reg.getYear());
            stm.setInt(3, reg.getLocationID());
            stm.setInt(4, reg.getSessionID());
            stm.setInt(5, reg.getStatus());
            stm.setString(6, "" + reg.getLevelCode());
            stm.setString(7, reg.getContactPhone1());
            stm.setString(8, reg.getContactPhone2());
            stm.setString(9, this.commonData.getTshirtCodeName(reg.getTshirtSizeCode()));
            stm.setString(10, reg.getStudentIdentificationCode());
            stm.setString(11, reg.getParentNote());
            stm.setInt(12, reg.getAdCode());
            stm.setString(13, reg.getAdNote());
            stm.setInt(14, reg.getUpdatedBy());
            stm.setInt(15, reg.getDiscountCodeId());
            stm.setInt(16, reg.getDiscountTypeId());
            stm.setString(17, reg.getDiscountTypeName());
            stm.setString(18, reg.getStateOfResidency());
            stm.setString(19, reg.getAdminNote());

            stm.execute();
            
            rs = cm.getResultsSQL(regIdRetrieval);
            if (rs.next()) {
                ret = rs.getInt("REGISTRATION_ID");
                reg.setRegistrationID(ret);
            }

        } catch (SQLException se) {
            System.out.println("Error in BPC [createRegistration()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public int createRegistrationBulk(RegisteredStudent reg) {
        
        int ret = -1;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        ResultSet rs = null;
        String insertReg = "INSERT INTO TRegistration (STUDENT_ID,YEAR,REG_DATE,LOCATION_ID,SESSION_ID,STATUS,LEVEL,"
                + " CONTACT_PHONE1,CONTACT_PHONE2,TSHIRT_SIZE,STUDENT_IDENTIFICATION_CD,MEMO,AD_CODE,AD_NOTE,LAST_UPDATED,UPDATED_BY,DISCOUNT_CODE_ID,DISCOUNT_TYPE_ID,DISCOUNT_TYPE_NAME,RESIDENCY_STATE,ADMIN_MEMO,PP_PAYMENT_ID,PP_TRANSACTION_ID,PAYMENT_AMOUNT,PAYMENT_METHOD,PAYMENT_DATE) "
                + " VALUES (?,?,NOW(),?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?,NOW())";
        String regIdRetrieval = "SELECT LAST_INSERT_ID() AS REGISTRATION_ID";

        try {
            stm = cm.getConnection().prepareStatement(insertReg);
            stm.setInt(1, reg.getStudentID());
            stm.setString(2, reg.getYear());
            stm.setInt(3, reg.getLocationID());
            stm.setInt(4, reg.getSessionID());
            stm.setInt(5, reg.getStatus());
            stm.setString(6, "" + reg.getLevelCode());
            stm.setString(7, reg.getContactPhone1());
            stm.setString(8, reg.getContactPhone2());
            stm.setString(9, this.commonData.getTshirtCodeName(reg.getTshirtSizeCode()));
            stm.setString(10, reg.getStudentIdentificationCode());
            stm.setString(11, reg.getParentNote());
            stm.setInt(12, reg.getAdCode());
            stm.setString(13, reg.getAdNote());
            stm.setInt(14, reg.getUpdatedBy());
            stm.setInt(15, reg.getDiscountCodeId());
            stm.setInt(16, reg.getDiscountTypeId());
            stm.setString(17, reg.getDiscountTypeName());
            stm.setString(18, reg.getStateOfResidency());
            stm.setString(19, reg.getAdminNote());
            stm.setString(20, reg.getPaymentID());
            stm.setString(21, reg.getTransactionID());
            stm.setDouble(22, reg.getPaymentAmount());
            stm.setInt(23, reg.getPaymentMethod());

            stm.execute();
            
            rs = cm.getResultsSQL(regIdRetrieval);
            if (rs.next()) {
                ret = rs.getInt("REGISTRATION_ID");
                reg.setRegistrationID(ret);
            }

        } catch (SQLException se) {
            System.out.println("Error in BPC [createRegistrationBulk()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean createRegistrationPayment(ArrayList<RegisteredStudent> regList, MKPaymentInfo pInfo) {

        boolean ret = true;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        PreparedStatement stm2 = null;
        String insertPmt = "INSERT INTO TPayments (PP_PAYMENT_ID,PP_TRANSACTION_ID,PAYMENT_DATE,PAYMENT_METHOD,MADE_BY_NAME,MADE_BY_USER,AMOUNT,COMMENT,YEAR ) VALUES (?,?,NOW(),?,?,?,?,?,?)";
        String updateReg = "UPDATE TRegistration set YEAR = ?, LAST_UPDATED = NOW(), PP_PAYMENT_ID = ?, PP_TRANSACTION_ID = ?, PAYMENT_DATE = NOW(), PAYMENT_METHOD = ?, PAYMENT_AMOUNT = ? "
                + " WHERE REGISTRATION_ID = ?";

        try {
            stm = cm.getConnection().prepareStatement(insertPmt);
            stm.setString(1, pInfo.getPaypalPaymentId());
            stm.setString(2, pInfo.getPaypalTransactionId());
            stm.setInt(3, this.getCommonData().getPaymentMethodCode("CC"));
            stm.setString(4, pInfo.getMadeByUserName());
            stm.setInt(5, pInfo.getMadeByUserId());
            stm.setDouble(6, pInfo.getAmount());
            stm.setString(7, pInfo.getComment());
            stm.setString(8, ApplicationProperties.getCurrentRegistrationYear(dbMgr));
            int result = stm.executeUpdate();
            if ( result != 1 )
            {
                this.sendCriticalErrorToAdmin("The payment record insert problem. Transaction ID::" + pInfo.getPaypalTransactionId());
                ret = false;
            }
            stm2 = cm.getConnection().prepareStatement(updateReg);
            stm2.setString(1, ApplicationProperties.getCurrentRegistrationYear(dbMgr));
            stm2.setString(2, pInfo.getPaypalPaymentId());
            stm2.setString(3, pInfo.getPaypalTransactionId());
            stm2.setInt(4, this.getCommonData().getPaymentMethodCode("CC"));
            for (RegisteredStudent rs : regList) {
                stm2.setDouble(5, rs.getPaymentAmount());
                stm2.setInt(6, rs.getRegistrationID());
                try {
                    int result2 = stm2.executeUpdate();
                    if ( result2 != 1 )
                    {
                         this.sendCriticalErrorToAdmin("The update registration problem.\nStudentID::" + rs.getStudentIdentificationCode() + "\nStudent First::" + rs.getFirstName() + "\nStudent Last::" + rs.getLastName() + "\nPP TransactionID::" + pInfo.getPaypalTransactionId() );
                         ret = false;
                    }
                    else
                    {
                        this.sendEmailRegistrationConfirmation(rs,0);
                    }
                    if ( rs.getDiscountCodeId() != 0 )
                    {
                        rs.setOwnerId(pInfo.getMadeByUserId());
                        this.createDiscountCodeUsage(rs);
                    }
                } catch (SQLException se) {
                    // send email to Irek; this is a problem
                    this.sendCriticalErrorToAdmin("The update registration problem. StudentID::" + rs.getStudentIdentificationCode() + " Student First::" + rs.getFirstName() + " Student Last::" + rs.getLastName() + "\n\n" + se.toString());
                    ret = false;
                    System.out.println("Error in BPC [createRegistrationPayment() individual registration step]: " + se);
                }
            }
        } catch (SQLException se) {
            ret = false;
            this.sendCriticalErrorToAdmin("The payment record insert problem. Transaction ID::" + pInfo.getPaypalTransactionId() + "\n\n" + se.toString());
            System.out.println("Error in BPC [createRegistrationPayment()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(stm2);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public MKRefund unregisterAndRefund( RegisteredStudent regSt, MKRefund mkr ) {

        int response = ApplicationProperties.PAYMENT_RESPONSE_CODE_SUCCESS;
        if ( mkr.getAmount() != 0.0 )
        {    
            response = mkr.processPaypalRefund();
            mkr.setComment("Transaction was processed by PP API");
        }
        else
        {
            mkr.setComment("The original PP Transaction amount was zero. No PP Refund was needed.");
        }
        if ( response != ApplicationProperties.PAYMENT_RESPONSE_CODE_SUCCESS)
        {
            return mkr;
        }
        regSt.setStatus(4);
        regSt.setLocationID(0);
        regSt.setSessionID(0);
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        PreparedStatement stm2 = null;
        String insertRefund = "INSERT INTO TRefunds (REGISTRATION_ID,PP_PAYMENT_ID,PP_TRANSACTION_ID,PARENT_TRANSACTION_ID,REFUND_DATE,MADE_BY_NAME,MADE_BY_USER,AMOUNT,COMMENT ) VALUES (?,?,?,?,NOW(),?,?,?,?)";
        String updateReg = "UPDATE TRegistration set LAST_UPDATED = NOW(), LOCATION_ID = 0, SESSION_ID = 0, PP_PAYMENT_ID = ?, PP_TRANSACTION_ID = ?, PAYMENT_AMOUNT = ?, PAYMENT_DATE = NOW(), STATUS = ?, ADMIN_MEMO = ?, DISCOUNT_CODE_ID = 0, DISCOUNT_TYPE_ID = 0, DISCOUNT_TYPE_NAME = '' "
                + " WHERE REGISTRATION_ID = ?";

        try {
            stm = cm.getConnection().prepareStatement(insertRefund);
            stm.setInt(1, regSt.getRegistrationID());
            stm.setString(2, mkr.getPaypalPaymentId());
            stm.setString(3, mkr.getPaypalTransactionId());
            stm.setString(4, mkr.getParentPaypalTransactionId());
            stm.setString(5, mkr.getMadeByUserName());
            stm.setInt(6, mkr.getMadeByUserId());
            stm.setDouble(7, mkr.getAmount());
            stm.setString(8, mkr.getComment());
            boolean result = stm.execute();
            stm2 = cm.getConnection().prepareStatement(updateReg);
            stm2.setString(1, "" );
            stm2.setString(2, "" );
            stm2.setDouble(3, 0.00);
            stm2.setInt(4, regSt.getStatus());
            stm2.setString(5, regSt.getAdminNote() + "\n\nStudent was unregistered.\nThe amount refunded was $" +  mkr.getAmount() + "\nRefund comment: " + mkr.getComment() );
            stm2.setInt(6, regSt.getRegistrationID());
            stm2.execute();
        } catch (SQLException se) {
            mkr.setResponseCode(ApplicationProperties.REFUND_RESPONSE_CODE_GENERAL_ERROR);
            mkr.setResponseMessage("Error updating student data. Refund was successful.");
            System.out.println("Error in BPC [unregisterAndRefund()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(stm2);
            cm.freeConnection();
            cm = null;
        }
        return mkr;
    }

    
    public void sendEmailRegistrationConfirmation(RegisteredStudent regSt, int sentFrom )
    {
        
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        String sqlString = "select * from TLocations a left join TLocationSession b on a.LOCATION_ID = b.LOCATION_ID " +
                    " left join TLocations_Leaders c on a.LOCATION_ID = c.LOCATION_ID and c.PRIMARY_IND = 'Y' " +
                    " left join TLeaders d on c.LEADER_ID = d.LEADER_ID " +
                    " where a.LOCATION_ID = " + regSt.getLocationID() +
                    " and b.SESSION_ID = " + regSt.getSessionID();

        String leaderEmail = "";
        String leaderName = "";
        String locationCity = "";
        String locationState = "";
        String locationName = "";
        String sessionName = "";
        String locationNameFull = "";
        String locationCompetitionTime = "";
        String currentYear = ApplicationProperties.getCurrentRegistrationYear(dbMgr);
        String sentFromMessageBody = "";
        String sentFromSubject = "";
        String ccEmail = "";
        
        rs = cm.getResultsSQL(sqlString);
        try
        {
            if (rs.next())
            {
                locationName = rs.getString("LOCATION_NAME");
                locationCity = rs.getString("CITY");
                locationState = rs.getString("STATE");
                locationNameFull = locationName + " - " + rs.getString("ADDRESS1") + ", " + locationCity + ", " + locationState;
                java.sql.Time cTime = rs.getTime("COMPETITION_TIME");
                DateFormat formatter = new SimpleDateFormat("hh:mm a");
                locationCompetitionTime = formatter.format(cTime);
                //locationCompetitionTime = rs.getString("COMPETITION_TIME");
                sessionName = rs.getString("SESSION_NAME");
                if (locationCompetitionTime == null)
                {
                    locationCompetitionTime = "TBD";
                }
                leaderEmail = rs.getString("EMAIL");
                leaderName = rs.getString("FIRST_NAME") + " " + rs.getString("LAST_NAME");
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BPC [sendEmailRegistrationConfirmation() Cannot get registration confirmation info!]: " + se);
        }
        finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }

        switch ( sentFrom )
        {
            case 0: // regular email from the registration process
                sentFromMessageBody = "";
                sentFromSubject = "";
                ccEmail = leaderEmail + ",";
                break;
            case 1: // resending by the admin request
                sentFromMessageBody = "<p>This e-mail was generated by the MK team request.</p><br>";
                sentFromSubject = "MK Admin request - ";
                break;
            case 2: // resending by the user request
                sentFromMessageBody = "<p>This e-mail was generated by the user request.</p><br>";
                sentFromSubject = "User request - ";
                break;
            case 3: // resending by updating registration center
                sentFromMessageBody = "<p>This e-mail was generated after updating MK Center.</p><br>";
                sentFromSubject = "User update - ";
                ccEmail = leaderEmail + ",";
                break;
        }
        
        
        EmailAdapter ea = new EmailAdapter();
        ea.setEmailTo(regSt.getParentGuardianEmail());
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        
        ea.setEmailCC(ccEmail + ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailSubject( sentFromSubject + "MK" + currentYear + " : " + regSt.getFirstName().toUpperCase() + " " + regSt.getLastName().toUpperCase()
                    + " Level " + regSt.getLevelCode() + " at " + locationName  + " in " + locationCity + ", " + locationState );

        String bodyText = "<html>" + sentFromMessageBody + "<p>Do not reply to this message as this mailbox is not monitored. Please contact us at info@mathkangaroo.org.</p><br><p>Print this e-mail and bring it to the Math Kangaroo Competition place on the Math Kangaroo Day.<br><br>Please verify that information below is correct (your student's full name, grade level and the center location). Use 'Update Student Registration' link to correct it in case of any invalid data. Notify us ASAP, if correction is needed and cannot be made online, with your message to info@mathkangaroo.org with the subject: 'Correction requested'.</p>"
                + "<br><p style=\"font-family:Georgia;font-size:11pt\" align=\"center\"><strong>Congratulations!</strong></p><br>"
                + regSt.getFirstName().toUpperCase() + " " + regSt.getMiddleName().toUpperCase() + " " + regSt.getLastName().toUpperCase()
                + " has been registered for Math Kangaroo " + currentYear + " level "
                + regSt.getLevelCode() + " in " + locationNameFull + ", session: " + sessionName
                + ".<br><br>The competition time is " + ApplicationProperties.getCompetitionDatePart1(dbMgr) + ", at " + locationCompetitionTime + " [" + ApplicationProperties.getCompetitionDatePart2(dbMgr) + "]"
                + ".<br><br>The Registration ID is " + regSt.getStudentIdentificationCode() + ". Students need to know this ID when checking results and claiming awards. It is also used for registration verification, updates, and for next year's enrollment."
                + "<br><br>The Math Kangaroo manager for your center is "
                + leaderName + " and can be contacted at <a href=\"mailto:" + leaderEmail + "\">" + leaderEmail + "</a>.<br>"
                + "<br><br>Please visit <a href=\"https://www.mathkangaroo.org\">www.mathkangaroo.org</a> as changes and announcements may be made frequently."
                + "<br><br>After the competition the question booklet is taken home. The correct answers are posted on our web page a month and one day after the day of the competition. SCANTRON's hardware and software are used for determining the results."
                + "  The families of the winners will be contacted by the beginning of May. Information about the winners will be posted on our website sometime in May. The results for each participant will be available via our web page at the same time when the winners are contacted."
                + "<br><br>During the Math Kangaroo events, photos are taken and will be displayed on our web page or related social media. <strong>If you do not wish us to photograph your child, please contact the Math Kangaroo manager at your center.</strong>"
                + "<br><br>See the \"Practice Material\" tab on the main web page for additional resources.<br>"
                + "The competition's Answer Card can be viewed on the website at <a href=\"https://www.mathkangaroo.org/mk/answer_card_info.html\">www.mathkangaroo.org/mk/answer_card_info.html.</a>"
                + "<br><br>Take advantage of Video Solutions in our Media Library to get familiar with the type of the competition questions. Access to the material from several years is free for all registered students until the end of August.  Complete solutions of the math problems in the next competition will be available by next summer. You need to have a Math Kangaroo account (through the website) in order to access the solutions free of charge."
                + "<br><br>Feel free to contact your Math Kangaroo Manager " + leaderName + " at <a href=\"mailto:" + leaderEmail + "\">" + leaderEmail + "</a>"
                + " regarding questions about the local competition logistics. With all other questions contact the Math Kangaroo in USA Leadership Team at <a href=\"mailto:info@mathkangaroo.org\">info@mathkangaroo.org</a>" 
                + "<br><br><br><strong>Math Kangaroo in USA disclaimer:</strong><br><br>"
                + "By entering the Competition, each participant and his or her parent(s) or legal guardian(s), understands, accepts, and adheres to the following terms regarding Math Kangaroo in USA's legal position and policy on organizing, governing, administering, and overseeing this competition:"
                + "<br>1. Math Kangaroo in USA, NFP, holds or assumes no responsibility for missing, lost, incomplete, illegible, or unclear Scantron cards, or any other failures associated with answer recording. This includes marking multiple answers for one question, incompletely erasing or smudging answer choices on the Scantron card, or not answering or choosing a selection on the Scantron card (leaving it blank). It is the participant's responsibility to complete the Scantron card in compliance with the instructions given on the website, on the Scantron card and at the time of the competition."
                + "<br>2. Upon entry into the competition, each participant releases Math Kangaroo in USA, NFP, its administration, leadership, and facilitators from any and all liability relating to or arising from the competition and/or acceptance or use/misuse of the content, study materials, or exams, including liability for personal injury, bodily, emotional, or mental injury, new-onset stress or anxiety, or adverse outcome of any kind."
                + "<br>3. Any disagreements or disputes arising out of, or connected with, this competition and its adjunct activities that cannot be settled with Math Kangaroo in USA, NFP, directly will be resolved independently and individually, without resort to any form of class action, before a court of jurisdiction. In any such dispute(s), participant(s) and his or her parent(s), legal guardian(s) or Math Kangaroo managers shall not be entitled to any punitive, indirect, incidental nor consequential damages, including any and all attorneys' fees, travel or lodging expenses, or any damages whatsoever."
                + "<br><br><br></html>";

        ea.setEmailBodyText(bodyText);
        ea.sendEmailHtml();
    }
   
    public void sendUnregisterConfirmation(RegisteredStudent regSt, MKRefund mkr )
    {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        String sqlString = "select * from TLocations a left join TLocationSession b on a.LOCATION_ID = b.LOCATION_ID " +
                    " left join TLocations_Leaders c on a.LOCATION_ID = c.LOCATION_ID and c.PRIMARY_IND = 'Y' " +
                    " left join TLeaders d on c.LEADER_ID = d.LEADER_ID " +
                    " where a.LOCATION_ID = " + regSt.getLocationID() +
                    " and b.SESSION_ID = " + regSt.getSessionID();

        String leaderEmail = "";
        String leaderName = "";
        String locationName = "";
        String sessionName = "";
        String locationNameFull = "";
        String locationCompetitionTime = "";
        String currentYear = ApplicationProperties.getCurrentRegistrationYear(dbMgr);

        rs = cm.getResultsSQL(sqlString);
        try
        {
            if (rs.next())
            {
                locationName = rs.getString("LOCATION_NAME");
                locationNameFull = locationName + " - " + rs.getString("ADDRESS1") + ", " + rs.getString("CITY") + ", " + rs.getString("STATE");
                locationCompetitionTime = rs.getString("COMPETITION_TIME");
                sessionName = rs.getString("SESSION_NAME");
                if (locationCompetitionTime == null)
                {
                    locationCompetitionTime = "TBD";
                }
                leaderEmail = rs.getString("EMAIL");
                leaderName = rs.getString("FIRST_NAME") + " " + rs.getString("LAST_NAME");
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BPC [sendUnregisterConfirmation() Cannot get registration confirmation info!]: " + se);
        }
        finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        
        EmailAdapter ea = new EmailAdapter();
        String bodyText = "";
        
        // first email to parent with the refund info
        
        // if the original amount was zero the below email is not needed; no refund was processed
        if ( mkr.getAmount() != 0 )
        {
            ea.setEmailTo(regSt.getParentGuardianEmail());
            ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
            ea.setEmailCC(leaderEmail + "," + ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
            ea.setEmailSubject("MK" + currentYear + " : Registration refund confirmation");

            bodyText = "<html>Do not reply to this message as this mailbox is not monitored. Please contact us at info@mathkangaroo.org.<br><br>" + regSt.getFirstName().toUpperCase() + " " + regSt.getLastName().toUpperCase() + " has been unregistered"
                    + " from the " + currentYear + " Math Kangaroo competition at " + locationNameFull + ", session: " + sessionName
                    + "<br><br>The credit card used to pay the registration fee has been credited $" + mkr.getAmount() + "."
                    + "<br>The reference transaction number issued by our processor is " + mkr.getPaypalTransactionId() + "."
                    + "<br><br>Please notify the Math Kangaroo team in case of any problems with your refund at <a href=\"mailto:info@mathkangaroo.org\">info@mathkangaroo.org</a></html>";
            ea.setEmailBodyText(bodyText);
            ea.sendEmailHtml();
        }
        
        // email to parent, center manager, and info
        ea.setEmailTo(regSt.getParentGuardianEmail());
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailCC(leaderEmail + "," + ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailSubject("MK" + currentYear + " : Unregistered " + regSt.getFirstName().toUpperCase() + " " + regSt.getLastName().toUpperCase()
                    + " Level " + regSt.getLevelCode() + " at " + locationName);
        bodyText = "<html>Student " + regSt.getFirstName().toUpperCase() + " " + regSt.getLastName().toUpperCase()
                + " has been unregistered from the " + currentYear + " Math Kangaroo competition at " + locationNameFull + ", session: " + sessionName
                + "<br><br>Please notify the Math Kangaroo team if you believe there is a mistake pertaining your account."
                + "<br><br>Feel free to contact your Math Kangaroo Manager " + leaderName + " at <a href=\"mailto:" + leaderEmail + "\">" + leaderEmail + "</a>"
                + " regarding questions about the local competition logistics. With all other questions contact the Math Kangaroo in USA Leadership Team at <a href=\"mailto:info@mathkangaroo.org\">info@mathkangaroo.org</a></html>";
        ea.setEmailBodyText(bodyText);
        ea.sendEmailHtml();
    }

    public void sendRegistrationUpdateOldCenterConfirmation(RegisteredStudent regSt )
    {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        String sqlString = "select * from TLocations a, TLocationSession b, TLocations_Leaders c, TLeaders d " +
            " where a.LOCATION_ID = " + regSt.getLocationID() +
            " and a.LOCATION_ID = b.LOCATION_ID" +
            " and b.SESSION_ID = " + regSt.getSessionID() +
            " and a.LOCATION_ID = c.LOCATION_ID" +
            " and c.PRIMARY_IND = 'Y'" +
            " and c.LEADER_ID = d.LEADER_ID";

        String leaderEmail = "";
        String leaderName = "";
        String locationName = "";
        String sessionName = "";
        String locationNameFull = "";
        String locationCompetitionTime = "";
        String currentYear = ApplicationProperties.getCurrentRegistrationYear(dbMgr);

        rs = cm.getResultsSQL(sqlString);
        try
        {
            if (rs.next())
            {
                locationName = rs.getString("LOCATION_NAME");
                locationNameFull = locationName + " - " + rs.getString("ADDRESS1") + ", " + rs.getString("CITY") + ", " + rs.getString("STATE");
                locationCompetitionTime = rs.getString("COMPETITION_TIME");
                sessionName = rs.getString("SESSION_NAME");
                if (locationCompetitionTime == null)
                {
                    locationCompetitionTime = "TBD";
                }
                leaderEmail = rs.getString("EMAIL");
                leaderName = rs.getString("FIRST_NAME") + " " + rs.getString("LAST_NAME");
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error in BPC [sendRegistrationUpdateOldCenterConfirmation() Cannot get registration confirmation info!]: " + se);
        }
        finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        
        EmailAdapter ea = new EmailAdapter();
        
        // email to parent, center manager, and info
        ea.setEmailTo(regSt.getParentGuardianEmail());
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailCC(leaderEmail + "," + ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailSubject("MK" + currentYear + " : Center changed " + regSt.getFirstName().toUpperCase() + " " + regSt.getLastName().toUpperCase()
                    + " Level " + regSt.getLevelCode());
        String bodyText = "<html>Do not reply to this message as this mailbox is not monitored. Please contact us at info@mathkangaroo.org.<br><br>Registration center for " + regSt.getFirstName().toUpperCase() + " " + regSt.getLastName().toUpperCase()
                + " has been changed and the student is no longer registered at " + locationNameFull + ", session: " + sessionName
                + "<br><br>Please notify the Math Kangaroo team at <a href=\"mailto:info@mathkangaroo.org\">info@mathkangaroo.org</a> if you believe there is a mistake pertaining your account.</html>";
        ea.setEmailBodyText(bodyText);
        ea.sendEmailHtml();
    }

    public void sendEmailNewCenterRequest(Location center)
    {
        
        EmailAdapter ea = new EmailAdapter();
        ea.setEmailTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO + "," + ApplicationProperties.MATH_KANGAROO_EMAIL_PRESIDENT + "," + ApplicationProperties.MATH_KANGAROO_EMAIL_CIO);
        ea.setEmailCC(center.getLeader().getEmail());
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailSubject("MK Center Registration request for : " + center.getLocationName()  + " in " + center.getLocationCity() + ", " + center.getLocationState() );

        String sessionsInfo = "";
        for ( LocationSession ls : center.getLocationSessions() )
        {
            sessionsInfo += "<br>" + ls.getSessionInfo() + "  Status: " + this.commonData.getCenterStatusName(ls.getSessionStatus()) 
                    + "  Seat capacity: " + (ls.getSeatCapacity() == 0 ? "Unlimited" : "" + ls.getSeatCapacity() + " seat(s)")
                    + "  Levels offered: " + ls.getLevels();
        }
        
        String bodyText = "<html>Do not reply to this message as this mailbox is not monitored. Please contact us at info@mathkangaroo.org.<br><br>A new request to register Math Kangaroo Center has been submitted.<br><br><strong>Center Information</strong>"
                + "<br>Name: " + center.getLocationName() + "<br>Address: " + center.getLocationAddress1() + " " + center.getLocationAddress2()
                + "<br>City: " + center.getLocationCity() + " State: " + center.getLocationState() + " Zip code: " + center.getLocationZipcode()
                + "<br><br><strong>Sessions</strong>"
                + sessionsInfo
                + "<br><br> Request was sent by " + center.getLeader().getFirstName() + " " + center.getLeader().getLastName() + " E-mail: " + center.getLeader().getEmail();

        ea.setEmailBodyText(bodyText);
        ea.sendEmailHtml();
    }

    
    
    
    public int createStudent( RegisteredStudent reg ) {

        int ret = 0;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));

        String insertSt = "INSERT INTO TStudents (FIRST_NAME,LAST_NAME,MIDDLE_NAME,STUDENT_EMAIL,PARENT_GUARDIAN_EMAIL,GENDER,ETHNIC_GROUP,"
                + " TEACHER_FIRST_NAME,TEACHER_LAST_NAME,TEACHER_EMAIL,STUDENT_IDENTIFICATION_CD,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,SCHOOL_NAME,"
                + " COUNTRY_OF_ORIGIN,COMMENT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String userIdRetrieval = "SELECT LAST_INSERT_ID() AS STUDENT_ID";
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = cm.getConnection().prepareStatement(insertSt);
            stm.setString(1, reg.getFirstName().toUpperCase());
            stm.setString(2, reg.getLastName().toUpperCase());
            stm.setString(3, ((reg.getMiddleName() != null && !reg.getMiddleName().trim().equals("")) ? reg.getMiddleName().substring(0, 1).toUpperCase() : ""));
            stm.setString(4, reg.getStudentEmail());
            stm.setString(5, reg.getParentGuardianEmail());
            stm.setInt(6, reg.getGenderCode());
            stm.setInt(7, reg.getEthnicGroupCode());
            stm.setString(8, reg.getTeacherFirstName().toUpperCase());
            stm.setString(9, reg.getTeacherLastName().toUpperCase());
            stm.setString(10, reg.getTeacherEmail());
            stm.setString(11, reg.getStudentIdentificationCode());
            stm.setString(12, reg.getAddress1());
            stm.setString(13, reg.getAddress2());
            stm.setString(14, reg.getCity());
            stm.setString(15, reg.getState());
            stm.setString(16, reg.getZipcode());
            stm.setString(17, reg.getSchoolName());
            stm.setString(18, reg.getCountryOfOrigin());
            stm.setString(19, reg.getComment());
            stm.execute();
            rs = cm.getResultsSQL(userIdRetrieval);
            if (rs.next()) {
                ret = rs.getInt("STUDENT_ID");
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [createStudent()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public void updateRegistration(RegisteredStudent reg) {

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String updateReg = "UPDATE TRegistration set LOCATION_ID = ?, SESSION_ID = ?, STATUS = ?, LEVEL = ?,"
                + " CONTACT_PHONE1 = ?, CONTACT_PHONE2 = ?, TSHIRT_SIZE = ?, STUDENT_IDENTIFICATION_CD = ?, MEMO = ?, AD_CODE = ?, AD_NOTE = ?,"
                + " LAST_UPDATED = NOW(), UPDATED_BY = ?, DISCOUNT_CODE_ID = ?, DISCOUNT_TYPE_ID = ?, DISCOUNT_TYPE_NAME = ?, RESIDENCY_STATE = ? WHERE STUDENT_ID = ? AND YEAR = ?";
        try {
            stm = cm.getConnection().prepareStatement(updateReg);
            stm.setInt(1, reg.getLocationID());
            stm.setInt(2, reg.getSessionID());
            stm.setInt(3, reg.getStatus());
            stm.setString(4, "" + reg.getLevelCode());
            stm.setString(5, reg.getContactPhone1());
            stm.setString(6, reg.getContactPhone2());
            stm.setString(7, this.commonData.getTshirtCodeName(reg.getTshirtSizeCode()));
            stm.setString(8, reg.getStudentIdentificationCode());
            stm.setString(9, reg.getParentNote());
            stm.setInt(10, reg.getAdCode());
            stm.setString(11, reg.getAdNote());
            stm.setInt(12, reg.getUpdatedBy());
            stm.setInt(13, reg.getDiscountCodeId());
            stm.setInt(14, reg.getDiscountTypeId());
            stm.setString(15, reg.getDiscountTypeName());
            stm.setString(16, reg.getStateOfResidency());
            
            stm.setInt(17, reg.getStudentID());
            stm.setString(18, reg.getYear());

            stm.executeUpdate();
        } catch (SQLException se) {
            System.out.println("Error in BPC [updateRegistration()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
    }
    
    public int updateStudent( RegisteredStudent reg ) {

        int ret = 0;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));

        String updateSt = "UPDATE TStudents set FIRST_NAME = ?, LAST_NAME = ?, MIDDLE_NAME = ?, STUDENT_EMAIL = ?, PARENT_GUARDIAN_EMAIL = ?, GENDER = ?, ETHNIC_GROUP = ?,"
                + " TEACHER_FIRST_NAME = ?, TEACHER_LAST_NAME = ?, TEACHER_EMAIL = ?, STUDENT_IDENTIFICATION_CD = ?, ADDRESS1 = ?, ADDRESS2 = ?, CITY = ?, STATE = ?, "
                + "ZIPCODE = ?, SCHOOL_NAME = ?, COUNTRY_OF_ORIGIN = ? WHERE STUDENT_ID = ?";
        PreparedStatement stm = null;
        try {
            stm = cm.getConnection().prepareStatement(updateSt);
            stm.setString(1, reg.getFirstName().toUpperCase());
            stm.setString(2, reg.getLastName().toUpperCase());
            stm.setString(3, ((reg.getMiddleName() != null && !reg.getMiddleName().trim().equals("")) ? reg.getMiddleName().substring(0, 1).toUpperCase() : ""));
            stm.setString(4, reg.getStudentEmail());
            stm.setString(5, reg.getParentGuardianEmail());
            stm.setInt(6, reg.getGenderCode());
            stm.setInt(7, reg.getEthnicGroupCode());
            stm.setString(8, reg.getTeacherFirstName().toUpperCase());
            stm.setString(9, reg.getTeacherLastName().toUpperCase());
            stm.setString(10, reg.getTeacherEmail());
            stm.setString(11, reg.getStudentIdentificationCode());
            stm.setString(12, reg.getAddress1());
            stm.setString(13, reg.getAddress2());
            stm.setString(14, reg.getCity());
            stm.setString(15, reg.getState());
            stm.setString(16, reg.getZipcode());
            stm.setString(17, reg.getSchoolName());
            stm.setString(18, reg.getCountryOfOrigin());
            stm.setInt(19, reg.getStudentID());
            stm.executeUpdate();
        } catch (SQLException se) {
            System.out.println("Error in BPC [updateStudent()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public boolean copyRegisteredStudent( RegisteredStudent stFrom, RegisteredStudent stTo) {
        boolean ret = false;
        if ( stFrom != null && stTo != null) {
            stTo.setAdCode(stFrom.getAdCode());
            stTo.setAdNote(stFrom.getAdNote());
            stTo.setAddress1(stFrom.getAddress1());
            stTo.setAddress2(stFrom.getAddress2());
            stTo.setCity(stFrom.getCity());
            stTo.setContactPhone1(stFrom.getContactPhone1());
            stTo.setContactPhone2(stFrom.getContactPhone2());
            stTo.setCountryOfOrigin(stFrom.getCountryOfOrigin());
            stTo.setDateTime(stFrom.getDateTime());
            stTo.setDiscountCodeId(stFrom.getDiscountCodeId());
            stTo.setDiscountTypeId(stFrom.getDiscountTypeId());
            stTo.setDiscountTypeName(stFrom.getDiscountTypeName());
            stTo.setEthnicGroupCode(stFrom.getEthnicGroupCode());
            stTo.setEthnicGroupName(stFrom.getEthnicGroupName());
            stTo.setFirstName(stFrom.getFirstName());
            stTo.setGender(stFrom.getGender());
            stTo.setGenderCode(stFrom.getGenderCode());
            stTo.setLastName(stFrom.getLastName());
            stTo.setLastUpdated(stFrom.getLastUpdated());
            stTo.setLateRegistrationDays(stFrom.getLateRegistrationDays());
            stTo.setLevel(stFrom.getLevel());
            stTo.setLevelCode(stFrom.getLevelCode());
            stTo.setLocationCode(stFrom.getLocationCode());
            stTo.setLocationID(stFrom.getLocationID());
            stTo.setLocationNameCityState(stFrom.getLocationNameCityState());
            stTo.setMemo(stFrom.getMemo());
            stTo.setMiddleName(stFrom.getMiddleName());
            stTo.setParentGuardianEmail(stFrom.getParentGuardianEmail());
            stTo.setParentNote(stFrom.getParentNote());
            stTo.setPaymentAmount(stFrom.getPaymentAmount());
            stTo.setPaymentID(stFrom.getPaymentID());
            stTo.setRegistrationFee(stFrom.getRegistrationFee());
            stTo.setRegistrationFlag(stFrom.getRegistrationFlag());
            stTo.setRegistrationID(stFrom.getRegistrationID());
            stTo.setSchoolName(stFrom.getSchoolName());
            stTo.setSessionID(stFrom.getSessionID());
            stTo.setSessionName(stFrom.getSessionName());
            stTo.setState(stFrom.getState());
            stTo.setStateName(stFrom.getStateName());
            stTo.setStatus(stFrom.getStatus());
            stTo.setStudentEmail(stFrom.getStudentEmail());
            stTo.setStudentID(stFrom.getStudentID());
            stTo.setStudentIdentificationCode(stFrom.getStudentIdentificationCode());
            stTo.setTeacherEmail(stFrom.getTeacherEmail());
            stTo.setTeacherFirstName(stFrom.getTeacherFirstName());
            stTo.setTeacherLastName(stFrom.getTeacherLastName());
            stTo.setTshirtSize(stFrom.getTshirtSize());
            stTo.setTshirtSizeCode(stFrom.getTshirtSizeCode());
            stTo.setUpdatedBy(stFrom.getUpdatedBy());
            stTo.setYear(stFrom.getYear());
            stTo.setZipcode(stFrom.getZipcode());
            stTo.setStateOfResidency(stFrom.getStateOfResidency());
            stTo.setAdminNote(stFrom.getAdminNote());
            stTo.setManagerNote(stFrom.getManagerNote());
            ret = true;
        }
        return ret;
    }

        public boolean copyUser( User userFrom, User userTo) {
        boolean ret = false;
        if ( userFrom != null && userTo != null) {
            userTo.setAuthAnswer(userFrom.getAuthAnswer());
            userTo.setAuthCodeTxt(userFrom.getAuthCodeTxt());
            userTo.setAuthQuestion(userFrom.getAuthQuestion());
            userTo.setLogonName(userFrom.getLogonName());
            userTo.setLogonPassword(userFrom.getLogonPassword());
            userTo.setLogonPasswordConfirm(userFrom.getLogonPasswordConfirm());
            userTo.setOtherID(userFrom.getOtherID());
            userTo.setOtherIDLogonName(userFrom.getOtherIDLogonName());
            userTo.setOtherIDLogonPassword(userFrom.getOtherIDLogonPassword());
            userTo.setRoleCode(userFrom.getRoleCode());
            userTo.setRoleName(userFrom.getRoleName());
            userTo.setUserAddress1(userFrom.getUserAddress1());
            userTo.setUserAddress2(userFrom.getUserAddress2());
            userTo.setUserCity(userFrom.getUserCity());
            userTo.setUserEmail(userFrom.getUserEmail());
            userTo.setUserFirstName(userFrom.getUserFirstName());
            userTo.setUserGender(userFrom.getUserGender());
            userTo.setUserID(userFrom.getUserID());
            userTo.setUserLastName(userFrom.getUserLastName());
            userTo.setUserPhone(userFrom.getUserPhone());
            userTo.setUserPhoneCell(userFrom.getUserPhoneCell());
            userTo.setUserState(userFrom.getUserState());
            userTo.setUserZipcode(userFrom.getUserZipcode());

            ret = true;
        }
        return ret;
    }
        public boolean copyManager( Leader from, Leader to) {
        boolean ret = false;
        if ( from != null && to != null) {
            to.setLeaderLogonName(from.getLeaderLogonName());
            to.setLeaderLogonPassword(from.getLeaderLogonPassword());
            to.setLeaderLogonPasswordConfirm(to.getLeaderLogonPasswordConfirm());
            to.setRole(from.getRole());
            to.setAddress1(from.getAddress1());
            to.setAddress2(from.getAddress2());
            to.setCity(from.getCity());
            to.setEmail(from.getEmail());
            to.setFirstName(from.getFirstName());
            to.setGender(from.getGender());
            to.setLeaderID(from.getLeaderID());
            to.setLastName(from.getLastName());
            to.setPhone(from.getPhone());
            to.setPhoneCell(from.getPhoneCell());
            to.setState(from.getState());
            to.setZipcode(from.getZipcode());
            to.setTshirtSize(from.getTshirtSize());
            to.setTshirtSizeCode(from.getTshirtSizeCode());
            ret = true;
        }
        return ret;
    }

    public void addStudentsToUser(int userID, ArrayList<RegisteredStudent> students) 
    {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rset = null;
        for (RegisteredStudent rs : students) 
        {
            String myStudentStr = "select * from TUsers_Students where STUDENT_ID = " + rs.getStudentID();
            rset = cm.getResultsSQL(myStudentStr);
            try {
                if (!rset.next()) {
                    String insertStr = "INSERT INTO TUsers_Students (USER_ID,STUDENT_ID,RELATIONSHIP_CD,COMMENT) "
                            + "VALUES (" + userID + "," + rs.getStudentID() + ",0,'')";
                    cm.executeStmtSQL(insertStr);
                }
                else
                {
                    String updateStr = "UPDATE TUsers_Students set USER_ID = " + userID + " WHERE STUDENT_ID = " + rs.getStudentID();
                    cm.executeStmtSQL(updateStr);
                }
            } catch (SQLException se) 
            {
                System.out.println("Error in BPC [addStudentsToUser()]: " + se);
            }
        }
        DBConnectionManager.dropConnObject(rset);
        cm.freeConnection();
        cm = null;
    }

    public Hashtable addLocationToList( Hashtable ht, Location loc)
    {
        Hashtable cities = null;
        boolean addFlag = false;

        if (ht.containsKey(loc.getLocationState()))
        {
            cities = (Hashtable)ht.get(loc.getLocationState());
            if (cities.containsKey(loc.getLocationCity()))
            {
                Vector v = (Vector)cities.get(loc.getLocationCity());
                addFlag = true;
                for ( int i = 0; i < v.size(); i++)
                {
                    Location temp = (Location)v.get(i);
                    if (loc.getLocationID() == temp.getLocationID())
                    {
                        temp.setLocationSessions(loc.getLocationSessions());
                        addFlag = false;
                        break;
                    }
                }
                if (addFlag)
                {
                    v.add(loc);
                }
            }
            else
            {
                Vector v = new Vector();
                v.add(loc);
                cities.put(loc.getLocationCity(), v);
            }
        }
        else
        {
            Vector v = new Vector();
            v.add(loc);
            cities = new Hashtable();
            cities.put(loc.getLocationCity(), v);
            ht.put(loc.getLocationState(), cities);
        }
        return ht;
    }
    
    public boolean saveNewCenterRequest( Location center )
    {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String centerInsert = "INSERT INTO TCenterStaging ( LOCATION_CODE,LOCATION_NAME,CITY,STATE,ZIPCODE,ADDRESS1,ADDRESS2,COMMENT,WEBSITE,LOCATION_STATUS,CREATED_BY) " +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        String centerIdRetrieval = "SELECT LAST_INSERT_ID() AS LOCATION_ID";
        String sessionInsert = "INSERT INTO TCenterSessionStaging (LOCATION_ID,SESSION_ID,SESSION_NAME,COMPETITION_TIME,LEVELS,SESSION_STATUS,SEAT_CAPACITY ) " +
                " VALUES (?,?,?,?,?,?,?)";
        PreparedStatement stm = null;
        PreparedStatement stm2 = null;
        ResultSet rs = null;
        try {
            stm = cm.getConnection().prepareStatement(centerInsert);
            stm.setString(1, "TBD");
            stm.setString(2, center.getLocationName());
            stm.setString(3, center.getLocationCity());
            stm.setString(4, center.getLocationState());
            stm.setString(5, center.getLocationZipcode());
            stm.setString(6, center.getLocationAddress1());
            stm.setString(7, center.getLocationAddress2());
            stm.setString(8, center.getComment());
            stm.setString(9, center.getWebsite());
            stm.setInt(10, 10);
            stm.setInt(11,center.getLeader().getLeaderID());

            stm.execute();
            rs = cm.getResultsSQL(centerIdRetrieval);
            if (rs.next()) {
                center.setLocationID(rs.getInt("LOCATION_ID"));
            }
            stm2 = cm.getConnection().prepareStatement(sessionInsert);
            int sessionId = 1;
            for ( LocationSession ls : center.getLocationSessions() )
            {
                stm2.setInt(1, center.getLocationID());
                stm2.setInt(2, sessionId);
                stm2.setString(3,ls.getSessionName());
                java.sql.Time time = new Time(ls.getCompetitionTime().getHours(),ls.getCompetitionTime().getMinutes(),ls.getCompetitionTime().getSeconds());
                stm2.setTime(4, time);
                stm2.setString(5, ls.getLevels());
                stm2.setInt(6, ls.getSessionStatus());
                stm2.setInt(7, ls.getSeatCapacity());
                stm2.execute();
                sessionId++;
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [saveNewCenterRequest()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(stm2);
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        this.sendEmailNewCenterRequest(center);
        return ret;
    }

    public int createNewCenter( Location center )
    {
        int ret = 0;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String centerInsert = "INSERT INTO TLocations ( LOCATION_CODE,LOCATION_NAME,CITY,STATE,ZIPCODE,ADDRESS1,ADDRESS2,COMMENT,REGION_USA,REGION_STATE,DIRECTOR_USER_ID,ESTABLISHED,LOC_REGISTRATION_CODE) " +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String centerIdRetrieval = "SELECT LAST_INSERT_ID() AS LOCATION_ID";
        String sessionInsert = "INSERT INTO TLocationSession (LOCATION_ID,SESSION_ID,SESSION_NAME,COMPETITION_TIME,LEVELS,SESSION_STATUS,SEAT_CAPACITY ) " +
                " VALUES (?,?,?,?,?,?,?)";
        PreparedStatement stm = null;
        PreparedStatement stm2 = null;
        ResultSet rs = null;
        try {
            stm = cm.getConnection().prepareStatement(centerInsert);
            stm.setString(1, center.getLocationCode().toUpperCase());
            stm.setString(2, center.getLocationName());
            stm.setString(3, center.getLocationCity().toUpperCase());
            stm.setString(4, center.getLocationState().toUpperCase());
            stm.setString(5, center.getLocationZipcode());
            stm.setString(6, center.getLocationAddress1());
            stm.setString(7, center.getLocationAddress2());
            stm.setString(8, center.getComment());
            stm.setString(9, center.getLocationCountryRegion());
            stm.setString(10, center.getLocationStateRegion());
            stm.setInt(11, center.getDirectorUserId());
            stm.setString(12, center.getEstablished());
            stm.setString(13, center.getLocationRegistrationCode());

            stm.execute();
            rs = cm.getResultsSQL(centerIdRetrieval);
            if (rs.next()) {
                ret = rs.getInt("LOCATION_ID");
            }
            stm2 = cm.getConnection().prepareStatement(sessionInsert);
            int sessionId = 1;
            for ( LocationSession ls : center.getLocationSessions() )
            {
                stm2.setInt(1, ret);
                stm2.setInt(2, sessionId);
                stm2.setString(3,ls.getSessionName());
                java.sql.Time time = new Time(ls.getCompetitionTime().getHours(),ls.getCompetitionTime().getMinutes(),ls.getCompetitionTime().getSeconds());
                stm2.setTime(4, time);
                stm2.setString(5, ls.getLevels());
                stm2.setInt(6, ls.getSessionStatus());
                stm2.setInt(7, ls.getSeatCapacity());
                stm2.execute();
                sessionId++;
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [createNewCenter()]: " + se);
            ret = 0 - ret;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(stm2);
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean addManagerToCenter( int centerId, int managerId, String primaryInd ) {
        
        boolean ret = true;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String insertSql = "INSERT INTO TLocations_Leaders (LOCATION_ID,LEADER_ID,PRIMARY_IND) VALUES (?,?,?)";

        try {
            stm = cm.getConnection().prepareStatement(insertSql);
            stm.setInt(1, centerId);
            stm.setInt(2, managerId);
            stm.setString(3, primaryInd);
            stm.execute();
        } catch (SQLException se) {
            ret = false;
            System.out.println("Error in BPC [addManagerToCenter()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public boolean updateNewCenterRequestStatus( Location center, String code ) {
        
        boolean ret = true;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String insertSql = "UPDATE TCenterStaging set CONVERTED = ?, COMMENT = ? where LOCATION_ID = ?";

        try {
            stm = cm.getConnection().prepareStatement(insertSql);
            stm.setString(1, code);
            stm.setString(2, center.getComment());
            stm.setInt(3, center.getLocationID());
            stm.executeUpdate();
        } catch (SQLException se) {
            ret = false;
            System.out.println("Error in BPC [updateNewCenterRequest()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public boolean deleteStudentAndRegistration( RegisteredStudent regStudent)
    {
        boolean ret = true;
        
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        
        String insertSql = "INSERT INTO TStudents_d (select STUDENT_ID,FIRST_NAME,LAST_NAME,MIDDLE_NAME,STUDENT_EMAIL,PARENT_GUARDIAN_EMAIL,COMMENT,GENDER,ETHNIC_GROUP,TEACHER_FIRST_NAME,TEACHER_LAST_NAME,TEACHER_EMAIL,STUDENT_IDENTIFICATION_CD,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,SCHOOL_NAME,COUNTRY_OF_ORIGIN,STUDENT_AGE, ?,NOW(),? from TStudents where student_id = ?)";
        PreparedStatement stm = null;
        try
        {
            stm = cm.getConnection().prepareStatement(insertSql);
            stm.setInt(1, regStudent.getOwnerId());
            stm.setInt(2, regStudent.getOwnerId());
            stm.setInt(3, regStudent.getStudentID());
            int result = stm.executeUpdate();
            if (result != 1)
            {
                 ret = false;
                 System.out.println("Error while inserting a deleted student to _d table in BPC[deleteStudentAndRegistration]: ");
            }
         }
         catch (SQLException se)
         {
             ret = false;
             System.out.println("Error while inserting a deleted student to _d table in BPC[deleteStudentAndRegistration]: "+se);
         }
        
        insertSql = "INSERT INTO TRegistration_d (select * from TRegistration where student_id = ?)";
        stm = null;
        try
        {
            stm = cm.getConnection().prepareStatement(insertSql);
            stm.setInt(1, regStudent.getStudentID());
            int result = stm.executeUpdate();
         }
         catch (SQLException se)
         {
             ret = false;
             System.out.println("Error while inserting deleted student registrations to _d table in BPC[deleteStudentAndRegistration]: "+se);
         }
        
        insertSql = "INSERT INTO TResults_d (select * from TResults where student_identification_cd = ?)";
        stm = null;
        try
        {
            stm = cm.getConnection().prepareStatement(insertSql);
            stm.setString(1, regStudent.getStudentIdentificationCode());
            int result = stm.executeUpdate();
         }
         catch (SQLException se)
         {
             ret = false;
             System.out.println("Error while inserting deleted student results to _d table in BPC[deleteStudentAndRegistration]: "+se);
         }

        String delSql = "delete from TResults where STUDENT_IDENTIFICATION_CD = '" + regStudent.getStudentIdentificationCode() + "'";
        boolean result = cm.executeStmtSQL(delSql);
        if (result)
        {
            ret = false;
            System.out.println("Error while deleting results records in BPC[deleteStudentAndRegistration]");
        }

        delSql = "delete from TRegistration where STUDENT_ID = " + regStudent.getStudentID();
        result = cm.executeStmtSQL(delSql);
        if (result)
        {
            ret = false;
            System.out.println("Error while deleting registration records in BPC[deleteStudentAndRegistration]");
        }

        delSql = "delete from TUsers_Students where STUDENT_ID = " + regStudent.getStudentID();
        result = cm.executeStmtSQL(delSql);
        if (result)
        {
            ret = false;
            System.out.println("Error while deleting a User_Student record in BPC[deleteStudentAndRegistration]");
        }

        delSql = "delete from TStudents where STUDENT_ID = " + regStudent.getStudentID();
        result = cm.executeStmtSQL(delSql);
        if (result)
        {
            ret = false;
            System.out.println("Error while deleting a student record in BPC[deleteStudentAndRegistration]");
        }
        
        DBConnectionManager.dropConnObject(stm);
        cm.freeConnection();
        cm = null;
        
        return ret;
    }

    private boolean updateManagerAcceptance( int mgrId ) 
    {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm2 = null;
        boolean error = false;

        try {
            String updateSql = "UPDATE TLeaders set AGREEMENT_STATUS = 'Y' where LEADER_ID = " + mgrId;

            stm2 = cm.getConnection().prepareStatement(updateSql);
            int result = stm2.executeUpdate();
            if (result != 1) {
                error = true;
            }
        } catch (SQLException se) {
            error = true;
        } finally {
            DBConnectionManager.dropConnObject(stm2);
            cm.freeConnection();
            cm = null;
        }
        return !error;
    }

    public synchronized void sendAgreementAcceptance( Leader mgr ) 
    {
        this.updateManagerAcceptance(mgr.getLeaderID());
        EmailAdapter ea = new EmailAdapter();
        String currentYear = ApplicationProperties.getCurrentRegistrationYear(this.dbMgr);
        
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailTo( ApplicationProperties.MATH_KANGAROO_EMAIL_PRESIDENT + "," + ApplicationProperties.MATH_KANGAROO_EMAIL_INFO+ "," + mgr.getEmail());
        ea.setEmailSubject("Manager " + mgr.getFirstName() + " " + mgr.getLastName() + currentYear + " - Agreement acceptance for manager center");
        String bodyText = "Do not reply to this message as this mailbox is not monitored. Please contact us at info@mathkangaroo.org.\n\nManager " + mgr.getFirstName() + " " + mgr.getLastName() + " { " + mgr.getLeaderID() + " } has accepted the agreement between Math Kangaroo in USA, NFP and Math Kangaroo (MK) Center Primary Manager for MK " + currentYear
                    + ".\nSee below for agreement details.\n\n"
                    + "This agreement applies to all centers that you might be listed as a primary manager at."
                    + "\n\nGenerated by the Manager Center Administration website."
                    + "\n\nAs a Math Kangaroo Manager, I agree:";
            for ( String s : ApplicationProperties.getManagerAgreementItems(this.dbMgr) )
            {
                bodyText += "\n" + s;
            }
            bodyText += "\n\nAdditional important information to consider:\n";
            int i = 1;
            for ( String s : ApplicationProperties.getManagerAgreementAdditionalItems(this.dbMgr))
            {
                bodyText += "\n" + i + ". " + s;
                i++;
            }
            ea.setEmailBodyText(bodyText);
            ea.sendEmailPlain();
    }

    public boolean updateCenterStatus( Location center ) 
    {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm2 = null;
        boolean success = true;

        try {
            String updateSql = "UPDATE TLocations set ACTIVE = ? where LOCATION_ID = " + center.getLocationID();

            stm2 = cm.getConnection().prepareStatement(updateSql);
            stm2.setString(1, center.getActive());
            int result = stm2.executeUpdate();
            if (result != 1) {
                success = false;
                System.out.println("Error in BPC [updateCenterStatus()] NO UPDATE");
            }
        } catch (SQLException se) {
            success = false;
            System.out.println("Error in BPC [updateCenterStatus()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm2);
            cm.freeConnection();
            cm = null;
        }
        return success;
    }

    private boolean updateCenterConfirmationStatus( int centerId ) 
    {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm2 = null;
        boolean error = false;

        try {
            String updateSql = "UPDATE TLocations set CONFIRMATION_STATUS = 'Y' where LOCATION_ID = " + centerId;

            stm2 = cm.getConnection().prepareStatement(updateSql);
            int result = stm2.executeUpdate();
            if (result != 1) {
                error = true;
                System.out.println("Error in BPC [updateCenterConfirmationStatus()]: NO UPDATE");
            }
        } catch (SQLException se) {
            error = true;
            System.out.println("Error in BPC [updateCenterConfirmationStatus()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm2);
            cm.freeConnection();
            cm = null;
        }
        return !error;
    }

    public synchronized void sendCenterConfirmation( Leader mgr, Location l ) 
    {
        this.updateCenterConfirmationStatus( l.getLocationID() );
        EmailAdapter ea = new EmailAdapter();
        String confirmationYear = ApplicationProperties.getCenterConfirmationYear(dbMgr);
        
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailTo( ApplicationProperties.MATH_KANGAROO_EMAIL_PRESIDENT + "," + ApplicationProperties.MATH_KANGAROO_EMAIL_INFO + "," + mgr.getEmail());
        ea.setEmailSubject( confirmationYear + " Center confirmation for: " + l.getLocationName() + " in " + l.getLocationCity() + ", " + l.getLocationState() );
        String bodyText = "Do not reply to this message as this mailbox is not monitored. Please contact us at info@mathkangaroo.org.\n\nManager " + mgr.getFirstName() + " " + mgr.getLastName() + " { " + mgr.getLeaderID() + " } has confirmed MK Center " + l.getLocationCode()
                    + " { " + l.getLocationName() + " } in " + l.getLocationCity() + ", " + l.getLocationState() + " for the " + confirmationYear + " registration year."
                    + "\n\nThis email was generated by the Manager Center Administration website.";
        ea.setEmailBodyText(bodyText);
        ea.sendEmailPlain();
    }

    public synchronized void sendCriticalErrorToAdmin( String message ) 
    {
        EmailAdapter ea = new EmailAdapter();
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailTo( ApplicationProperties.MATH_KANGAROO_EMAIL_ADMIN);
        ea.setEmailSubject( "CRITICAL REGISTRATION ERROR - Registration process" );
        String bodyText = "Please invastigate the critical error during the registration. Details below.\n\n" + message;
        ea.setEmailBodyText(bodyText);
        ea.sendEmailPlain();
    }

    public ArrayList<RegisteredStudent> getLocationSessionStudents( LocationSession ls, String year) {
        ArrayList<RegisteredStudent> ret = new ArrayList<RegisteredStudent>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String qStr = "SELECT * FROM TRegistration a left join TStudents b on a.student_id = b.student_id "
            + " left join TUsers_Students c on b.student_id = c.student_id "
            + " left join TUsers d on c.user_id = d.user_id "
            + " where a.year = '" + ApplicationProperties.getCurrentRegistrationYear(dbMgr) 
            + "' and a.LOCATION_ID = " + ls.getLocationID() + " and a.SESSION_ID = " + ls.getSessionID() + " and a.STATUS = 2 order by LEVEL";
        ResultSet rs = cm.getResultsSQL(qStr);
        try {
            while (rs.next()) {
                RegisteredStudent regSt = new RegisteredStudent();
                regSt.setRegistrationID(rs.getInt("REGISTRATION_ID"));
                regSt.setLocationID(rs.getInt("LOCATION_ID"));
                regSt.setSessionID(rs.getInt("SESSION_ID"));
                regSt.setLevel(rs.getString("LEVEL"));
                regSt.setTshirtSize(rs.getString("TSHIRT_SIZE"));
                regSt.setContactPhone1(rs.getString("CONTACT_PHONE1"));
                regSt.setContactPhone2(rs.getString("CONTACT_PHONE2"));
                regSt.setParentNote(rs.getString("MEMO"));
                regSt.setStatus(rs.getInt("STATUS"));
                regSt.setDateTime("" + rs.getTimestamp("REG_DATE"));
                regSt.setManagerNote(rs.getString("MGR_NOTE"));
                regSt.setStudentID(rs.getInt("STUDENT_ID"));
                regSt.setFirstName(rs.getString("FIRST_NAME"));
                regSt.setLastName(rs.getString("LAST_NAME"));
                regSt.setStudentIdentificationCode(rs.getString("STUDENT_IDENTIFICATION_CD"));
                regSt.setStudentEmail(rs.getString("STUDENT_EMAIL"));
                regSt.setParentGuardianEmail(rs.getString("EMAIL"));
                String isChecked = rs.getString("CHECKED_IN");
                regSt.setChecked(isChecked.equalsIgnoreCase("Y") ? true : false );
                String photoRestriction = rs.getString("PHOTO_RESTRICTION");
                regSt.setPhotoRestricted(photoRestriction.equalsIgnoreCase("Y") ? true : false );
                ret.add(regSt);
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getLocationSessionStudents()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean addUserDonation( MKPaymentInfo payment ) 
    {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String insertSql = "INSERT INTO TUserDonation (USER_ID,AMOUNT,COMMENT) VALUES (?,?,?)";

        try {
            stm = cm.getConnection().prepareStatement(insertSql);
            stm.setInt(1, payment.getMadeByUserId());
            stm.setInt(2, payment.getDonationAmount() != 0 ? payment.getDonationAmount() : payment.getOtherDonationAmount());
            stm.setString(3, "Donation made during registration");
            stm.execute();
        } catch (SQLException se) {
            ret = false;
            System.out.println("Error in BPC [addUserDonation()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean createDiscountCode( DiscountCode discountCode ) 
    {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String insertRec = "insert into TDiscountCodes (USER_ID,ISSUED_BY_ID,ISSUED_BY_NAME,TYPE,EXPIRE_DATE,PURPOSE_TYPE,COMMENT) values (?,?,?,?,?,?,?)";

        try {
            stm = cm.getConnection().prepareStatement(insertRec);
            for ( int i = 0; i < discountCode.getNumberOfCodes(); i++)
            {
                stm.setInt(1, discountCode.getUserIdFor());
                stm.setInt(2, discountCode.getIssuedBy());
                stm.setString(3, discountCode.getIssuedByName());
                stm.setInt(4, discountCode.getType());
                stm.setDate(5, new java.sql.Date(discountCode.getValidUntil().getTime()));
                stm.setInt(6, discountCode.getPurposeType());
                stm.setString(7, discountCode.getComment());
                int cnt = stm.executeUpdate();
                if ( cnt != 1 )
                {
                    ret = false;
                }
            }
        } catch (SQLException se) {
            ret = false;
            System.out.println("Error in BPC [createDiscountCode()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public boolean createDiscountCodeUsage(RegisteredStudent regStudent) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String insertRec = "insert into TDiscountCodesUsage (CODE_ID,REGISTRATION_ID,USED_BY_ID,USED_FOR_NAME) values (?,?,?,?)";
        String updateRec = "update TDiscountCodes set STATUS = ? where CODE_ID = ?";

        try {
            stm = cm.getConnection().prepareStatement(insertRec);
            stm.setInt(1, regStudent.getDiscountCodeId());
            stm.setInt(2, regStudent.getRegistrationID());
            stm.setInt(3, regStudent.getOwnerId());
            stm.setString(4, regStudent.getFirstName() + " " + regStudent.getLastName());
            int cnt = stm.executeUpdate();
            if (cnt != 1) {
                ret = false;
            } else {
                stm = cm.getConnection().prepareStatement(updateRec);
                stm.setString(1, "C");
                stm.setInt(2, regStudent.getDiscountCodeId());
                cnt = stm.executeUpdate();
                if (cnt != 1) {
                    ret = false;
                }
            }
        } catch (SQLException se) {
            ret = false;
            System.out.println("Error in BPC [createDiscountCodeUsage()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public ArrayList<DiscountCode> getUserDiscountCodes( int userId ) {
        ArrayList<DiscountCode> ret = new ArrayList<DiscountCode>();
            DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            ResultSet rs = null;
            String codeList = "SELECT * FROM TDiscountCodes a left join TDiscountCodesUsage b on a.CODE_ID = b.CODE_ID where a.USER_ID = " + userId;

            try {
                rs = cm.getResultsSQL(codeList);
                while (rs.next()) {
                    DiscountCode dc = new DiscountCode();
                    dc.setCodeId(rs.getInt("CODE_ID"));
                    dc.setIssuedByName(rs.getString("ISSUED_BY_NAME"));
                    dc.setIssuedBy(rs.getInt("ISSUED_BY_ID"));
                    dc.setIssuedDate(rs.getDate("ISSUED_DATE"));
                    dc.setStatus(rs.getString("STATUS"));
                    dc.setType(rs.getInt("TYPE"));
                    dc.setTypeName(this.commonData.getDiscountCodeName(dc.getType()));
                    dc.setValidUntil(rs.getDate("EXPIRE_DATE"));
                    dc.setUsedOnDate(rs.getDate("USED_DATE"));
                    dc.setPurposeType(rs.getInt("PURPOSE_TYPE"));
                    dc.setComment(rs.getString("COMMENT"));
                    ret.add(dc);
                }
            } catch (SQLException se) {
                System.out.println("Error in BPC [getUserDiscountCodes()]: " + se);
            } finally {
                DBConnectionManager.dropConnObject(rs);
                cm.freeConnection();
                cm = null;
            }
        return ret;
    }

    public void sendDiscountCodeInfoToUser( DiscountCode dc, User user)
    {
        
        EmailAdapter ea = new EmailAdapter();
        ea.setEmailTo(user.getUserEmail());
        ea.setEmailCC(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailSubject("MK Registration discount code(s) notification" );

        
        String bodyText = "Do not reply to this message as this mailbox is not monitored. Please contact us at info@mathkangaroo.org.\n\nThe MK team has generated " + dc.getNumberOfCodes() + " " + this.commonData.getDiscountCodeName(dc.getType()) + 
                " discount code(s).\nThey can be used when you register students using your MK account associated with logon name: " + user.getLogonName() +
                ".\nOn the registration page, please select your student(s), proceed to the payment page, and click on to the 'Add discount code' icon next to the student first name. " +
                "\nAfter you select a discount code, your payment amount will be adjusted appropriately.\nRegards,\n" + dc.getIssuedByName();

        ea.setEmailBodyText(bodyText);
        ea.sendEmailPlain();
    }
    
    public boolean createManagerRequest( ManagerRequest mr ) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String insertRec = "insert into TLocationManagerRequest (LOCATION_ID,REQUESTOR_LEADER_ID,REQUESTOR_USER_ID,USER_ID_FOR,COMMENT,STATUS) values (?,?,?,?,?,?)";

        try {
            stm = cm.getConnection().prepareStatement(insertRec);
            stm.setInt(1, mr.getCenterId());
            stm.setInt(2, mr.getRequestorManager().getLeaderID());
            stm.setInt(3, mr.getRequestorUser().getUserID());
            stm.setInt(4, mr.getUser().getUserID());
            stm.setString(5, mr.getComment());
            stm.setString(6, mr.getStatus());
            int cnt = stm.executeUpdate();
            if (cnt != 1) {
                ret = false;
                System.out.println("Error in BPC [createManagerRequest()]: INSERT RECORD failed.");
            }
        } catch (SQLException se) {
            ret = false;
            System.out.println("Error in BPC [createManagerRequest()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public ArrayList<ManagerRequest> getLocationManagerRequests( int centerId ) {
        ArrayList<ManagerRequest> ret = new ArrayList<ManagerRequest>();
            DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            ResultSet rs = null;
            String codeList = "SELECT * FROM TLocationManagerRequest where LOCATION_ID = " + centerId + " and STATUS = 'O'";

            try {
                rs = cm.getResultsSQL(codeList);
                while (rs.next()) {
                    ManagerRequest mr = new ManagerRequest();
                    mr.setRequestId(rs.getInt("REQUEST_ID"));
                    int leaderId = rs.getInt("REQUESTOR_LEADER_ID");
                    int leaderUserId = rs.getInt("REQUESTOR_USER_ID");
                    int userId = rs.getInt("USER_ID_FOR");
                    Leader l = this.getLeaderByID(leaderId);
                    mr.setRequestorManager(l);
                    User lu = this.getUserById(leaderUserId);
                    mr.setRequestorUser(lu);
                    User u = this.getUserById(userId);
                    mr.setUser(u);
                    mr.setRequestDate(rs.getString("DATE_REQUESTED"));
                    mr.setComment(rs.getString("COMMENT"));
                    mr.setStatus(rs.getString("STATUS"));
                    ret.add(mr);
                }
            } catch (SQLException se) {
                System.out.println("Error in BPC [getLocationManagerRequests()]: " + se);
            } finally {
                DBConnectionManager.dropConnObject(rs);
                cm.freeConnection();
                cm = null;
            }
        return ret;
    }
    
    public boolean setLocationPrimaryManager( Location loc, Leader ld ) 
    {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        boolean error = false;

        try {
            String updateSql = "UPDATE TLocations_Leaders set PRIMARY_IND = 'N' where LOCATION_ID = " + loc.getLocationID();
            stm = cm.getConnection().prepareStatement(updateSql);
            stm.executeUpdate();
            updateSql = "UPDATE TLocations_Leaders set PRIMARY_IND = 'Y' where LOCATION_ID = ? and LEADER_ID = ?";
            stm = cm.getConnection().prepareStatement(updateSql);
            stm.setInt(1, loc.getLocationID());
            stm.setInt(2, ld.getLeaderID());
            int result = stm.executeUpdate();
            if (result != 1) {
                error = true;
            }
        } catch (SQLException se) {
            error = true;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return error;
    }
    
    public boolean updateManagerRequest( ManagerRequest mr, String comment ) 
    {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        boolean error = false;

        try {
            String updateSql = "UPDATE TLocationManagerRequest set STATUS = ?, COMMENT = ? where REQUEST_ID = " + mr.getRequestId();
            stm = cm.getConnection().prepareStatement(updateSql);
            stm.setString(1, mr.getStatus());
            stm.setString(2, mr.getComment() + comment );
            int result = stm.executeUpdate();
            if (result != 1) {
                error = true;
            }
        } catch (SQLException se) {
            error = true;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return !error;
    }

    public ArrayList<CenterManagerRequest> getAdminManagerRequests() {
        ArrayList<CenterManagerRequest> ret = new ArrayList<CenterManagerRequest>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        String codeList = "SELECT DISTINCT(LOCATION_ID) FROM TLocationManagerRequest where STATUS = 'O'";

        try {
            rs = cm.getResultsSQL(codeList);
            while (rs.next()) {
                CenterManagerRequest cmr = new CenterManagerRequest();
                int centerId = rs.getInt("LOCATION_ID");
                Location l = this.getLocationByID(centerId);
                cmr.setCenter(l);
                cmr.setManagerRequests(this.getLocationManagerRequests(centerId));
                ret.add(cmr);
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getAdminManagerRequests()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public RegisteredStudent getStudentAndRegistrationForAdmin(String stID, String year) {
        RegisteredStudent reg = null;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String studentDetail = "SELECT * "
                    + " FROM TStudents a left join TRegistration b on a.student_id = b.student_id and b.YEAR = '" + year + "' "
                    + " WHERE a.STUDENT_IDENTIFICATION_CD = '" + stID + "'";
        ResultSet rs = cm.getResultsSQL( studentDetail );
        try {
            if (rs.next()) {
                reg = new RegisteredStudent();
                reg.setStudentID(rs.getInt("STUDENT_ID"));
                reg.setStudentIdentificationCode(rs.getString("STUDENT_IDENTIFICATION_CD"));
                reg.setAddress1(rs.getString("ADDRESS1"));
                reg.setAddress2(rs.getString("ADDRESS2"));
                reg.setCity(rs.getString("CITY"));
                reg.setEthnicGroupCode(rs.getInt("ETHNIC_GROUP"));
                reg.setEthnicGroupName(commonData.getEthnicGroupName(reg.getEthnicGroupCode()));
                reg.setFirstName(rs.getString("FIRST_NAME"));
                reg.setGenderCode(rs.getInt("GENDER"));
                reg.setGender(commonData.getGenderName(reg.getGenderCode()));
                reg.setLastName(rs.getString("LAST_NAME"));
                reg.setMiddleName(rs.getString("MIDDLE_NAME"));
                reg.setParentGuardianEmail(rs.getString("PARENT_GUARDIAN_EMAIL"));
                reg.setSchoolName(rs.getString("SCHOOL_NAME"));
                reg.setState(rs.getString("STATE"));
                reg.setStudentEmail(rs.getString("STUDENT_EMAIL"));
                reg.setTeacherEmail(rs.getString("TEACHER_EMAIL"));
                reg.setTeacherFirstName(rs.getString("TEACHER_FIRST_NAME"));
                reg.setTeacherLastName(rs.getString("TEACHER_LAST_NAME"));
                reg.setZipcode(rs.getString("ZIPCODE"));
                reg.setCountryOfOrigin(rs.getString("COUNTRY_OF_ORIGIN"));
                
                // registration part
                reg.setRegistrationID(rs.getInt("REGISTRATION_ID"));
                reg.setYear(rs.getString("YEAR"));
                reg.setLocationID(rs.getInt("LOCATION_ID"));
                reg.setSessionID(rs.getInt("SESSION_ID"));
                reg.setStatus(rs.getInt("STATUS"));
                reg.setStatusName(this.commonData.getRegistrationStatusCodeName(reg.getStatus()));
                reg.setLevel(rs.getString("LEVEL"));
                if ( reg.getLevel() != null && !reg.getLevel().trim().equals(""))
                {
                    reg.setLevelCode(Integer.parseInt(reg.getLevel()));
                }
                reg.setContactPhone1(rs.getString("CONTACT_PHONE1"));
                reg.setContactPhone2(rs.getString("CONTACT_PHONE2"));
                reg.setPaymentID(rs.getString("PP_PAYMENT_ID"));
                reg.setPaymentAmount(rs.getDouble("PAYMENT_AMOUNT"));
                reg.setTransactionID(rs.getString("PP_TRANSACTION_ID"));
                reg.setPaymentMethod(rs.getInt("PAYMENT_METHOD"));
                reg.setTshirtSize(rs.getString("TSHIRT_SIZE"));
                reg.setTshirtSizeCode(this.commonData.getTshirtCode(reg.getTshirtSize()));
                try {
                    reg.setLastUpdated("" + rs.getTimestamp("LAST_UPDATED"));
                } catch (SQLException se) {
                    reg.setLastUpdated("");
                }
                try {
                    reg.setPaymentDate("" + rs.getTimestamp("PAYMENT_DATE"));
                } catch (SQLException se) {
                    reg.setPaymentDate("");
                }
                reg.setAdCode(rs.getInt("AD_CODE"));
                reg.setAdNote(rs.getString("AD_NOTE"));
                reg.setStateOfResidency(rs.getString("RESIDENCY_STATE"));
                reg.setDiscountCodeId(rs.getInt("DISCOUNT_CODE_ID"));
                reg.setDiscountTypeId(rs.getInt("DISCOUNT_TYPE_ID"));
                reg.setDiscountTypeName(rs.getString("DISCOUNT_TYPE_NAME"));
                if (reg.getDiscountTypeName() == null) {
                    reg.setDiscountTypeName("");
                }
                try {
                    reg.setDateTime("" + rs.getTimestamp("REG_DATE"));
                } catch (SQLException se) {
                    reg.setDateTime("");
                }
                String memo = rs.getString("MEMO");
                if (memo == null) {
                    memo = "";
                }
                reg.setParentNote(memo);
                String adminMemo = rs.getString("ADMIN_MEMO");
                if (adminMemo == null) {
                    adminMemo = "";
                }
                reg.setAdminNote(adminMemo);
                String mgrMemo = rs.getString("MGR_NOTE");
                if (mgrMemo == null) {
                    mgrMemo = "";
                }
                reg.setManagerNote(mgrMemo);
                reg.setUpdatedBy(rs.getInt("UPDATED_BY"));
                // get location and session info
                Location l = this.getLocationByID(reg.getLocationID());
                if ( l != null )
                {
                    reg.setLocationCode(l.getLocationCode());
                    LocationSession ls = l.getSession(reg.getSessionID());
                    if ( ls != null )
                    {
                        reg.setSessionName(ls.getSessionInfo());
                    }
                }
            }
        } catch (SQLException se) {
            System.out.println("Error in BusinessProcessControl [getStudentAndRegistrationForAdmin()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return reg;
    }

    public ArrayList<Location> getPublicCenters() {
        ArrayList<Location> ret = new ArrayList<Location>();
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;

        String sqlQuery = "select s.LOCATION_ID, s.LOCATION_NAME, s.ADDRESS1, s.ADDRESS2, s.CITY, s.STATE, " +
            "GROUP_CONCAT(s.SESSION_ID order by s.SESSION_ID) as SESSION_IDS, " +
            "GROUP_CONCAT(s.SESSION_NAME order by s.SESSION_ID) as SESSION_NAMES, " +
            "GROUP_CONCAT(s.SEAT_CAPACITY order by s.SESSION_ID) as SESSION_CAPS, " +
            "GROUP_CONCAT(s.REG_CNT order by s.SESSION_ID) as SESSION_REGS, " +
            "GROUP_CONCAT(DATE_FORMAT(s.COMPETITION_TIME,'%l:%i %p') order by s.SESSION_ID) as SESSION_TIMES " +
            "from ( " +
            "    select a.*,COALESCE(COUNT(b.REGISTRATION_ID), 0) as REG_CNT, c.LOCATION_CODE, c.LOCATION_NAME, c.ADDRESS1, c.ADDRESS2, c.CITY, c.STATE " +
            "	from TLocationSession a left join TRegistration b on a.location_id = b.location_id and a.session_id = b.session_id " +
            "    left join TLocations c on a.location_id = c.location_id " +
            "	where a.SESSION_STATUS = 0 " +
            "	and c.CONFIRMATION_STATUS = 'Y' and c.ACTIVE = 'Y' " +
            "	group by a.location_id,a.session_id " +
            "    order by a.location_id, a.session_id " +
            "    ) s " +
            "	group by s.location_id " +
            "	order by s.STATE,s.CITY,s.LOCATION_CODE ";

        try {
            rs = cm.getResultsSQL(sqlQuery);
            while (rs.next()) {
                Location loc = new Location();
                loc.setLocationID(rs.getInt("LOCATION_ID"));
                loc.setLocationCity(rs.getString("CITY").trim());
                loc.setLocationAddress1(rs.getString("ADDRESS1").trim());
                loc.setLocationAddress2(rs.getString("ADDRESS2").trim());
                loc.setLocationState(rs.getString("STATE").trim());
                loc.setLocationName(rs.getString("LOCATION_NAME").trim());
                String sessionIDs = rs.getString("SESSION_IDS");
                String sessionNames = rs.getString("SESSION_NAMES");
                String sessionRegs = rs.getString("SESSION_REGS");
                String sessionCaps = rs.getString("SESSION_CAPS");
                String sessionTimes = rs.getString("SESSION_TIMES");
                ArrayList<LocationSession> sessions = new ArrayList<LocationSession>();
                // unpack sessions
                StringTokenizer st1 = new StringTokenizer(sessionIDs,",");
                StringTokenizer st2 = new StringTokenizer(sessionNames,",");
                StringTokenizer st3 = new StringTokenizer(sessionRegs,",");
                StringTokenizer st4 = new StringTokenizer(sessionCaps,",");
                StringTokenizer st5 = new StringTokenizer(sessionTimes,",");
                while ( st2.hasMoreTokens())
                {
                    LocationSession ls = new LocationSession();
                    String name = st2.nextToken();
                    int regCnt = Integer.parseInt(st3.nextToken());
                    int capCnt = Integer.parseInt(st4.nextToken());
                    String time = st5.nextToken();
                    ls.setSessionName(name + " Time:" + time);
                    ls.setAvailableSeatCount(capCnt == 0 ? 999 : capCnt - regCnt );
                    sessions.add(ls);
                }
                loc.setLocationSessions(sessions);
                ret.add(loc);
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getPublicCenters()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean createActionLog( ActionLog actionLog ) 
    {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String insertRec = "insert into TActionLog (ACTION_ID,TABLE_NAME,USER_ID,COMMENT) values (?,?,?,?)";

        try {
            stm = cm.getConnection().prepareStatement(insertRec);
            stm.setInt(1, actionLog.getActionId());
            stm.setString(2, actionLog.getTableName());
            stm.setInt(3, actionLog.getUserId());
            stm.setString(4, actionLog.getComment());
            int cnt = stm.executeUpdate();
            if ( cnt != 1 )
            {
                ret = false;
            }
        } catch (SQLException se) {
            ret = false;
            System.out.println("Error in BPC [createActionLog()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public int adminCreateRegistration(RegisteredStudent reg) {
        
        int ret = -1;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        ResultSet rs = null;
        String insertReg = "INSERT INTO TRegistration (STUDENT_ID,YEAR,REG_DATE,LOCATION_ID,SESSION_ID,STATUS,LEVEL,"
                + " CONTACT_PHONE1,TSHIRT_SIZE,STUDENT_IDENTIFICATION_CD,LAST_UPDATED,UPDATED_BY,ADMIN_MEMO,PP_PAYMENT_ID,PP_TRANSACTION_ID,PAYMENT_AMOUNT,PAYMENT_DATE,PAYMENT_METHOD) "
                + " VALUES (?,?,NOW(),?,?,?,?,?,?,?,NOW(),?,?,?,?,?,NOW(),?)";
        String regIdRetrieval = "SELECT LAST_INSERT_ID() AS REGISTRATION_ID";

        try {
            stm = cm.getConnection().prepareStatement(insertReg);
            stm.setInt(1, reg.getStudentID());
            stm.setString(2, reg.getYear());
            stm.setInt(3, reg.getLocationID());
            stm.setInt(4, reg.getSessionID());
            stm.setInt(5, reg.getStatus());
            stm.setString(6, "" + reg.getLevelCode());
            stm.setString(7, reg.getContactPhone1());
            stm.setString(8, this.commonData.getTshirtCodeName(reg.getTshirtSizeCode()));
            stm.setString(9, reg.getStudentIdentificationCode());
            stm.setInt(10, reg.getUpdatedBy());
            stm.setString(11, reg.getAdminNote());
            stm.setString(12, reg.getPaymentID());
            stm.setString(13, reg.getTransactionID());
            stm.setDouble(14, reg.getPaymentAmount());
            stm.setInt(15, reg.getPaymentMethod());

            stm.execute();
            
            rs = cm.getResultsSQL(regIdRetrieval);
            if (rs.next()) {
                ret = rs.getInt("REGISTRATION_ID");
                reg.setRegistrationID(ret);
            }

        } catch (SQLException se) {
            System.out.println("Error in BPC [adminCreateRegistration()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public boolean adminUpdateRegistration(RegisteredStudent reg) {
        boolean ret = true;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String updateReg = "UPDATE TRegistration set LOCATION_ID = ?, SESSION_ID = ?, STATUS = ?, LEVEL = ?,"
                + " CONTACT_PHONE1 = ?, TSHIRT_SIZE = ?, LAST_UPDATED = NOW(), UPDATED_BY = ?, ADMIN_MEMO = ?,"
                + "PP_PAYMENT_ID = ?,PP_TRANSACTION_ID = ?,PAYMENT_AMOUNT = ?,PAYMENT_DATE = NOW(),PAYMENT_METHOD = ? WHERE REGISTRATION_ID = ?";
        try {
            stm = cm.getConnection().prepareStatement(updateReg);
            stm.setInt(1, reg.getLocationID());
            stm.setInt(2, reg.getSessionID());
            stm.setInt(3, reg.getStatus());
            stm.setString(4, "" + reg.getLevelCode());
            stm.setString(5, reg.getContactPhone1());
            stm.setString(6, this.commonData.getTshirtCodeName(reg.getTshirtSizeCode()));
            stm.setInt(7, reg.getUpdatedBy());
            stm.setString(8, reg.getAdminNote());
            stm.setString(9, reg.getPaymentID());
            stm.setString(10, reg.getTransactionID());
            stm.setDouble(11, reg.getPaymentAmount());
            stm.setInt(12, reg.getPaymentMethod());
            
            stm.setInt(13, reg.getRegistrationID());

            stm.executeUpdate();
        } catch (SQLException se) {
            System.out.println("Error in BPC [adminUpdateRegistration()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean adminUpdateStudent( RegisteredStudent reg ) {
        boolean ret = true;
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));

        String updateSt = "UPDATE TStudents set FIRST_NAME = ?, LAST_NAME = ?, MIDDLE_NAME = ?, STUDENT_EMAIL = ?, PARENT_GUARDIAN_EMAIL = ?, GENDER = ?, ETHNIC_GROUP = ?,"
                + " TEACHER_FIRST_NAME = ?, TEACHER_LAST_NAME = ?, TEACHER_EMAIL = ?, STUDENT_IDENTIFICATION_CD = ?, ADDRESS1 = ?, ADDRESS2 = ?, CITY = ?, STATE = ?, "
                + "ZIPCODE = ?, SCHOOL_NAME = ?, COUNTRY_OF_ORIGIN = ?, COMMENT = ? WHERE STUDENT_ID = ?";
        PreparedStatement stm = null;
        try {
            stm = cm.getConnection().prepareStatement(updateSt);
            stm.setString(1, reg.getFirstName().toUpperCase());
            stm.setString(2, reg.getLastName().toUpperCase());
            stm.setString(3, ((reg.getMiddleName() != null && !reg.getMiddleName().trim().equals("")) ? reg.getMiddleName().substring(0, 1).toUpperCase() : ""));
            stm.setString(4, reg.getStudentEmail());
            stm.setString(5, reg.getParentGuardianEmail());
            stm.setInt(6, reg.getGenderCode());
            stm.setInt(7, reg.getEthnicGroupCode());
            stm.setString(8, reg.getTeacherFirstName().toUpperCase());
            stm.setString(9, reg.getTeacherLastName().toUpperCase());
            stm.setString(10, reg.getTeacherEmail());
            stm.setString(11, reg.getStudentIdentificationCode());
            stm.setString(12, reg.getAddress1());
            stm.setString(13, reg.getAddress2());
            stm.setString(14, reg.getCity());
            stm.setString(15, reg.getState());
            stm.setString(16, reg.getZipcode());
            stm.setString(17, reg.getSchoolName());
            stm.setString(18, reg.getCountryOfOrigin());
            stm.setString(19, reg.getComment());
            stm.setInt(20, reg.getStudentID());
            stm.executeUpdate();
        } catch (SQLException se) {
            System.out.println("Error in BPC [adminUpdateStudent()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public MKPaymentInfo getMKPayment(String transactionId) {
        MKPaymentInfo payment = new MKPaymentInfo();
        if (transactionId != null && !transactionId.trim().equals("")) {
            DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            ResultSet rs = cm.getResultsSQL("select * from TPayments where PP_TRANSACTION_ID = '" + transactionId + "'");
            try {
                if (rs.next()) {
                    payment.setAmount(rs.getDouble("AMOUNT"));
                    payment.setComment(rs.getString("COMMENT"));
                    payment.setPaymentMethod(rs.getInt("PAYMENT_METHOD"));
                    payment.setPaypalDate(rs.getTimestamp("PAYMENT_DATE"));
                    payment.setMadeByUserId(rs.getInt("MADE_BY_USER"));
                    payment.setMadeByUserName(rs.getString("MADE_BY_NAME"));
                }
            } catch (SQLException se) {
                System.out.println("Error in BPC [geStudentByIDNumber()]: " + se);
            } finally {
                DBConnectionManager.dropConnObject(rs);
                cm.freeConnection();
                cm = null;
            }
        }
        return payment;
    }

    
    public ArrayList<LocationSimple> getAdminCentersSimple() {
        ArrayList<LocationSimple> ret = new ArrayList<LocationSimple>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        String sqlQuery = "";
        String sqlQuery2 = "";

        sqlQuery = "select a.*, GROUP_CONCAT(b.SESSION_ID order by b.SESSION_ID) as sessions "
                + " from TLocations a, TLocationSession b "
                + " where a.LOCATION_ID = b.LOCATION_ID "
                + " and a.CONFIRMATION_STATUS = 'Y' and a.ACTIVE = 'Y' "
                + " group by a.location_id "
                + " order by a.LOCATION_CODE";
        sqlQuery2 = "select * from TLocationSession where LOCATION_ID = ? and FIND_IN_SET(SESSION_ID, ?) > 0 order by SESSION_ID";

        try {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            stm = cm.getConnection().prepareStatement(sqlQuery2);
            rs = cm.getResultsSQL(sqlQuery);
            while (rs.next()) {
                LocationSimple loc = new LocationSimple();
                loc.setLocationCity(rs.getString("CITY").trim());
                loc.setLocationCode(rs.getString("LOCATION_CODE").trim());
                loc.setLocationID(rs.getInt("LOCATION_ID"));
                loc.setLocationState(rs.getString("STATE").trim());
                loc.setLocationName(rs.getString("LOCATION_NAME").trim());
                String sessionList = rs.getString("sessions");
                ArrayList<LocationSession> sessions = new ArrayList<LocationSession>();
                stm.setInt(1, loc.getLocationID());
                stm.setString(2, sessionList);
                ResultSet rs2 = stm.executeQuery();
                while (rs2.next()) {
                    LocationSession ls = new LocationSession();
                    ls.setSessionID(rs2.getInt("SESSION_ID"));
                    ls.setSessionName(rs2.getString("SESSION_NAME"));
                    ls.setLevels(rs2.getString("LEVELS"));
                    ls.setSeatCapacity(rs2.getInt("SEAT_CAPACITY"));
                    ls.setCompetitionTime(rs2.getTime("COMPETITION_TIME"));
                    ls.setSessionStatus(rs2.getInt("SESSION_STATUS"));
                    sessions.add(ls);
                }
                loc.setLocationSessions(sessions);
                ret.add(loc);
            }
        } catch (SQLException se) {
            System.out.println("getAdminCentersSimple runtime error: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    
    public boolean adminCreatePayment(MKPaymentInfo pInfo) {

        boolean ret = true;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;
        String insertPmt = "INSERT INTO TPayments (PP_PAYMENT_ID,PP_TRANSACTION_ID,PAYMENT_DATE,PAYMENT_METHOD,MADE_BY_NAME,MADE_BY_USER,AMOUNT,COMMENT,YEAR ) VALUES (?,?,NOW(),?,?,?,?,?,?)";
 
        try {
            stm = cm.getConnection().prepareStatement(insertPmt);
            stm.setString(1, pInfo.getPaypalPaymentId());
            stm.setString(2, pInfo.getPaypalTransactionId());
            stm.setInt(3, pInfo.getPaymentMethod());
            stm.setString(4, pInfo.getMadeByUserName());
            stm.setInt(5, pInfo.getMadeByUserId());
            stm.setDouble(6, pInfo.getAmount());
            stm.setString(7, pInfo.getComment());
            stm.setString(8, ApplicationProperties.getCurrentRegistrationYear(dbMgr));
            int result = stm.executeUpdate();
            if ( result != 1 )
            {
                this.sendCriticalErrorToAdmin("The payment record insert problem. Transaction ID::" + pInfo.getPaypalTransactionId());
                ret = false;
            }
        } catch (SQLException se) {
            ret = false;
            this.sendCriticalErrorToAdmin("The payment record insert problem. Transaction ID::" + pInfo.getPaypalTransactionId() + "\n\n" + se.toString());
            System.out.println("Error in BPC [createRegistrationPayment()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public ArrayList<StudentResult> getResultsForStudent( RegisteredStudent regSt ) 
    {
            ArrayList<StudentResult> ret = new ArrayList<StudentResult>();
            DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            ResultSet rs = null;
            String rStr = "SELECT a.LEVEL,a.YEAR,a.SCORE,a.PERCENTAGE,a.STATE_PLACE,a.NATION_PLACE,a.STATE_PCTILE,a.NATION_PCTILE,a.STATE as CALCULATION_STATE,"
                    + " c.CITY,c.LOCATION_NAME,c.STATE as CENTER_STATE "
                    + " FROM TResults a "
                    + "	left join TRegistration b on a.STUDENT_IDENTIFICATION_CD = b.STUDENT_IDENTIFICATION_CD and a.YEAR = b.YEAR "
                    + " left join TLocations c on b.LOCATION_ID = c.LOCATION_ID "
                    + " where a.student_identification_cd = '" + regSt.getStudentIdentificationCode() 
                    + "' and a.YEAR in (" + ApplicationProperties.getApprovedResultsYears(dbMgr)
                    + ") order by a.year desc";

            try {
                rs = cm.getResultsSQL(rStr);
                while (rs.next()) {
                    StudentResult stResult = new StudentResult();
                    stResult.setLevel(rs.getString("LEVEL"));
                    stResult.setYear(rs.getString("YEAR"));
                    stResult.setScore(rs.getDouble("SCORE"));
                    stResult.setPercentage(rs.getDouble("PERCENTAGE"));
                    stResult.setStatePlace(rs.getInt("STATE_PLACE"));
                    stResult.setNationPlace(rs.getInt("NATION_PLACE"));
                    stResult.setStatePercentile(rs.getDouble("STATE_PCTILE"));
                    stResult.setNationPercentile(rs.getDouble("NATION_PCTILE"));
                    stResult.setLocationCity(rs.getString("CITY"));
                    stResult.setLocationName(rs.getString("LOCATION_NAME"));
                    stResult.setLocationState(rs.getString("CENTER_STATE"));
                    stResult.setCalculationState(rs.getString("CALCULATION_STATE"));
                    ret.add(stResult);
                }
            } catch (SQLException se) {
                System.out.println("Error in BPC [getResultsForStudent()]: " + se);
            } finally {
                DBConnectionManager.dropConnObject(rs);
                cm.freeConnection();
                cm = null;
            }
        return ret;
    }

    public ArrayList<StudentResult> getAdminResultsForStudent( RegisteredStudent regSt ) 
    {
            ArrayList<StudentResult> ret = new ArrayList<StudentResult>();
            DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            ResultSet rs = null;
            String rStr = "SELECT a.LEVEL,a.YEAR,a.SCORE,a.PERCENTAGE,a.STATE_PLACE,a.NATION_PLACE,a.STATE_PCTILE,a.NATION_PCTILE,a.STATE as CALCULATION_STATE,"
                    + " c.CITY,c.LOCATION_NAME,c.STATE as CENTER_STATE "
                    + " FROM TResults a "
                    + "	left join TRegistration b on a.STUDENT_IDENTIFICATION_CD = b.STUDENT_IDENTIFICATION_CD and a.YEAR = b.YEAR "
                    + " left join TLocations c on b.LOCATION_ID = c.LOCATION_ID "
                    + " where a.student_identification_cd = '" + regSt.getStudentIdentificationCode() 
                    + "' order by a.year desc";

            try {
                rs = cm.getResultsSQL(rStr);
                while (rs.next()) {
                    StudentResult stResult = new StudentResult();
                    stResult.setLevel(rs.getString("LEVEL"));
                    stResult.setYear(rs.getString("YEAR"));
                    stResult.setScore(rs.getDouble("SCORE"));
                    stResult.setPercentage(rs.getDouble("PERCENTAGE"));
                    stResult.setStatePlace(rs.getInt("STATE_PLACE"));
                    stResult.setNationPlace(rs.getInt("NATION_PLACE"));
                    stResult.setStatePercentile(rs.getDouble("STATE_PCTILE"));
                    stResult.setNationPercentile(rs.getDouble("NATION_PCTILE"));
                    stResult.setLocationCity(rs.getString("CITY"));
                    stResult.setLocationName(rs.getString("LOCATION_NAME"));
                    stResult.setLocationState(rs.getString("CENTER_STATE"));
                    stResult.setCalculationState(rs.getString("CALCULATION_STATE"));
                    ret.add(stResult);
                }
            } catch (SQLException se) {
                System.out.println("Error in BPC [getResultsForStudent()]: " + se);
            } finally {
                DBConnectionManager.dropConnObject(rs);
                cm.freeConnection();
                cm = null;
            }
        return ret;
    }

    public int getCheckedinCount(LocationSession ls) {
        int ret = 0;
        DBConnectionManager cm = null;
        cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = cm.getResultsSQL("select count(*) as COUNT from TRegistration where LOCATION_ID = " + ls.getLocationID() + " and SESSION_ID = " + ls.getSessionID() + " and CHECKED_IN = 'Y' and YEAR = '" + ApplicationProperties.getCurrentRegistrationYear(dbMgr) + "'");
        try {
            if (rs.next()) {
                ret = rs.getInt("COUNT");
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [getCheckedinCount()]: " + se);
        } finally {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public boolean updateCheckedInStudent(RegisteredStudent regSt, User user ) 
    {
        boolean ret = true;
        DBConnectionManager cm = null;
        cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm2 = null;

        try {
            String updateSql = "UPDATE TRegistration set CHECKIN_LEADER_ID = ?,CHECKIN_TIME = NOW(), MGR_NOTE = ?, PHOTO_RESTRICTION = ?, CHECKED_IN = ? WHERE REGISTRATION_ID = ?";
            stm2 = cm.getConnection().prepareStatement(updateSql);
            stm2.setInt(1, user.getUserID());
            stm2.setString(2, regSt.getManagerNote());
            stm2.setString(3, (regSt.isPhotoRestricted() ? "Y":"N" ));
            stm2.setString(4, ( regSt.isChecked() ? "Y" : "N" ));
            stm2.setInt(5, regSt.getRegistrationID());
            int result = stm2.executeUpdate();
            if (result != 1) {
                System.out.println("Error in BPC [updateCheckedInStudent()]");
                ret = false;
            }
        } catch (SQLException se) {
            System.out.println("Error in BPC [updateCheckedInStudent()]: " + se);
            ret = false;
        } finally {
            DBConnectionManager.dropConnObject(stm2);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    
    /* Methods for calculating results
     * 
     */
     public Hashtable getStateResultsStats(String year, int level, String state)
    {
        Hashtable hs = new Hashtable();
        int scorePos = 0;
        int pctPos = 0;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String qry = "select SCORE from TResults m, TStudents a, TRegistration b, TLocations c where m.year = '" + year + "' and m.year = b.year and m.level = " + level + " and m.level = b.level and b.status = 2 and m.student_identification_cd = a.student_identification_cd and a.student_id = b.student_id and b.location_id = c.location_id and (case (@myvar:= b.residency_state) when '' then c.state else @myvar end ) = '" + state + "' order by m.score desc";
        ResultSet rs = cm.getResultsSQL(qry);
        ResultStatElement last = null;
        try 
        {
            while (rs.next()) 
            {
                pctPos++;
                double score = rs.getDouble("SCORE");
                String str = "" + Math.round(score);
                if (!hs.containsKey(str)) 
                {
                    scorePos++;
                    ResultStatElement rse = new ResultStatElement(scorePos);
                    hs.put(str, rse);
                    if (last != null)
                    {
                        last.setPctPos(pctPos);
                    }
                    last = rse;
                }
            }
            if (last != null)
            {
                last.setPctPos(pctPos + 1);
            }
            Enumeration enu = hs.keys();
            while (enu.hasMoreElements()) 
            {
                String key = (String) enu.nextElement();
                ResultStatElement rsElem = (ResultStatElement) hs.get(key);
                rsElem.setPctTile( (double)( pctPos -  rsElem.getPctPos() + 1 ) / (double) pctPos * 100.0);
            }
        } 
        catch (SQLException se) {
            System.out.println("BPC [getStateResultsStats()] error: " + se);
        } 
        catch (Exception e) {
            System.out.println("BPC [getStateResultsStats()] error: " + e);
        } 
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return hs;
    }
    
    public Hashtable getNationResultsStats(String year, int level)
    {
        Hashtable hs = new Hashtable();
        int scorePos = 0;
        int pctPos = 0;

        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String qry = "select SCORE from TResults m where m.year = '" + year + "' and m.level = " + level + " order by m.score desc";
        ResultSet rs = cm.getResultsSQL(qry);
        ResultStatElement last = null;
        try 
        {
            while (rs.next()) 
            {
                pctPos++;
                double score = rs.getDouble("SCORE");
                String str = "" + Math.round(score);
                if (!hs.containsKey(str)) 
                {
                    scorePos++;
                    ResultStatElement rse = new ResultStatElement(scorePos);
                    hs.put(str, rse);
                    if (last != null)
                    {
                        last.setPctPos(pctPos);
                    }
                    last = rse;
                }
            }
            if (last != null)
            {
                last.setPctPos(pctPos + 1);
            }
            Enumeration enu = hs.keys();
            while (enu.hasMoreElements()) 
            {
                String key = (String) enu.nextElement();
                ResultStatElement rsElem = (ResultStatElement) hs.get(key);
                rsElem.setPctTile( (double)( pctPos -  rsElem.getPctPos() + 1 ) / (double) pctPos * 100.0);
            }
        } catch (SQLException se) {
            System.out.println("BPC [getNationResultsStats()] error: " + se);
        } 
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return hs;
    }

    
    public void updateNationLevelResults( String year, int level, Hashtable htN )
    {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String updateSql = "update TResults set NATION_PLACE = ?, NATION_PCTILE = ? "
                + " where YEAR = '" + year + "' and LEVEL = " + level + " and SCORE = ? ";
        
        PreparedStatement stm = null;
        try
        {
            stm = cm.getConnection().prepareStatement(updateSql);
            Enumeration enu = htN.keys();
            while (enu.hasMoreElements()) {
                String key = (String) enu.nextElement();
                ResultStatElement rse = (ResultStatElement) htN.get(key);
                stm.setInt(1, rse.getScorePos());
                stm.setDouble(2, rse.getPctTile());
                stm.setDouble(3, new Double(key).doubleValue());
                stm.executeUpdate();
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error while updating nation stats for TResults in BPC[updateNationLevelResults()]");
        }
        finally
        {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
       }
    }


    public void updateStateLevelResults( String year, String state, int level, Hashtable htN )
    {
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        String updateSql = "update TResults set STATE_PLACE = ?, STATE_PCTILE = ?, STATE = ? "
                + " where YEAR = '" + year + "' and LEVEL = " + level 
                + " and SCORE = ? and STUDENT_IDENTIFICATION_CD in ( select a.STUDENT_IDENTIFICATION_CD from TStudents a, TRegistration b, TLocations c where b.year = '" + year + "' and b.level = " + level + " and b.status = 2 and a.student_id = b.student_id and b.location_id = c.location_id and (case (@myvar:= b.residency_state) when '' then c.state else @myvar end ) = '" + state + "' )";
        PreparedStatement stm = null;
        try
        {
            stm = cm.getConnection().prepareStatement(updateSql);
            Enumeration enu = htN.keys();
            while (enu.hasMoreElements()) {
                try {
                String key = (String) enu.nextElement();
                ResultStatElement rse = (ResultStatElement) htN.get(key);
                stm.setInt(1, rse.getScorePos());
                stm.setDouble(2, rse.getPctTile());
                stm.setString(3, state );
                stm.setDouble(4, new Double(key).doubleValue());
                stm.executeUpdate();
                }
                catch (Exception e)
                {
                    System.out.println("Exception in state enumeration: " + e );
                }
            }
        }
        catch (SQLException se)
        {
            System.out.println("Error while updating stats for TResults in SessionBean[updateStateLevelResults]");
        }
        catch (Exception se)
        {
            System.out.println("Error while updating stats for TResults in SessionBean[updateStateLevelResults]" + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
    }
   
}
