package com.mk.classes;

import java.util.*;
import java.sql.*;
import com.mk.dao.DBConnectionManager;
import com.mk.dao.DBManager;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.Serializable;
import java.net.URLEncoder;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ApplicationProperties implements Serializable
{

// Paypal related properties
    public static final String CLIENT_ID_LIVE = "Af5m1LdIknK_yO7UQ88rFS7GWFoMRqNgq1CbdrG0zHJqQwflF3nCH3EdjkfvAojOyUR3xDP8CIxcWlSM";
    public static final String CLIENT_SECRET_LIVE = "EHaDC2SxIbNE2WEaxwcpOn6BKL7eyVzqO6X8dh8lTuzL1-WMMRrGqG4wqrfTuiZWmy0VSef8E8m3rnLW";
    public static final String CLIENT_ID_SANDBOX = "ATXmMja2qFiqSPmywjCDr0JgpZJo78TuXym5zRhCvjYwinhlq9TTLjMzy1x2I2pxF2hxMzGiRzz33GOp";
    public static final String CLIENT_SECRET_SANDBOX = "EEDjMnGr0jNj02QgFgDr-9zwXIa7951FxiDibxrEgqVan9HKFRhklQ7OMb-i0dDI_c1_0wUlrKJV7Tq6";
    public static final String PAYMENT_MODE_LIVE = "live";
    public static final String PAYMENT_MODE_SANDBOX = "sandbox";
    //public static final String PAYMENT_MODE = ApplicationProperties.PAYMENT_MODE_SANDBOX;
    public static final String PAYMENT_MODE = ApplicationProperties.PAYMENT_MODE_LIVE;
    
    public static final int PAYMENT_RESPONSE_CODE_SUCCESS = 0;
    public static final int PAYMENT_RESPONSE_CODE_TOKEN_ERROR = 1;
    public static final int PAYMENT_RESPONSE_CODE_GENERAL_ERROR = 99;
    public static final int REFUND_RESPONSE_CODE_GENERAL_ERROR = 199;
    
    public static final int REFUND_PERCENTAGE = 90;
    
    public static final int REGISTRATION_RUNTIME_MODE_OPEN = 0;
    public static final int REGISTRATION_RUNTIME_MODE_MAINTENANCE = 1;
    public static final int REGISTRATION_RUNTIME_MODE_CLOSE_WITH_EDIT = 2;
    public static final int REGISTRATION_RUNTIME_MODE_CLOSE_NO_EDIT = 3;

    public static final String ILLEGAL_CHARACTERS = " ' - single quote ";
    public static final String ILLEGAL_CHARACTERS_LIST = "'";
    

    public static final int USER_ROLE_STANDARD = 0;
    public static final int USER_ROLE_ADMIN = 1;
    public static final int USER_ROLE_MANAGER = 2;
    public static final int USER_ROLE_LIMITED_ADMIN = 3;
    public static final int USER_ROLE_INSTRUCTOR = 4;

    public static final int MANAGER_ROLE_LOCATION_LEADER = 0;
    public static final int MANAGER_ROLE_LOCATION_MANAGER = 1;
    public static final int MANAGER_ROLE_STATE_REP = 2;

    public static final String MAIL_SMTP_HOST = "smtp.w14b.comcast.net";
    public static final String MAIL_FROM = "mkapp@mathkangaroo.comcastbiz.net";
    public static final String MAIL_SMTP_PORT = "25";
    public static final String MAIL_SMTP_SOCKET_FACTORY_PORT = "465";
    public static final String MAIL_USER = "mkapp@mathkangaroo.comcastbiz.net";
    public static final String MAIL_PASSWORD = "kasprowy1";

    public static final String MATH_KANGAROO_EMAIL_INFO = "info@mathkangaroo.org";
    public static final String MATH_KANGAROO_EMAIL_CHECK_SERVICE = "izabela@mathkangaroo.org";
    public static final String MATH_KANGAROO_EMAIL_PRESIDENT = "maria@mathkangaroo.org";
    public static final String MATH_KANGAROO_EMAIL_CFO = "izabela@mathkangaroo.org";
    public static final String MATH_KANGAROO_EMAIL_CIO = "joanna@mathkangaroo.org";
    public static final String MATH_KANGAROO_EMAIL_ADMIN = "irekwitek@hotmail.com";
    

    public static double getDefaultRegistrationFee(DBManager dbMgr)
    {
        DBConnectionManager cm = null;
        ResultSet rs = null;
        double fee = 0.0;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'DEFAULT_REGISTRATION_FEE'");
            if (rs.next())
            {
                fee = rs.getInt("PROPERTY_VALUE_NUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return fee;
    }


    public static String getCurrentRegistrationYear(DBManager dbMgr)
    {
        String year = "9999";
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'CURRENT_REGISTRATION_YEAR'");
            if (rs.next())
            {
                year = rs.getString("PROPERTY_VALUE_ALPHANUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return year;
    }
    
    public static int getCurrentRegistrationYearNumber(DBManager dbMgr)
    {
        int year = 9999;
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'CURRENT_REGISTRATION_YEAR'");
            if (rs.next())
            {
                year = rs.getInt("PROPERTY_VALUE_NUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return year;
    }

    
    public static String getCompetitionDatePart1(DBManager dbMgr)
    {
        String part1 = "";
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'COMPETITION_DATE_PART1'");
            if (rs.next())
            {
                part1 = rs.getString("PROPERTY_VALUE_ALPHANUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return part1;
    }

    public static String getCompetitionDatePart2(DBManager dbMgr)
    {
        String part2 = "";
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'COMPETITION_DATE_PART2'");
            if (rs.next())
            {
                part2 = rs.getString("PROPERTY_VALUE_ALPHANUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return part2;
    }
    
    
    

    public static ArrayList<ListCode> getRegistrationStatusCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TRegistration' and TABLE_COLUMN_NAME = 'STATUS' order by code");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }

    
    public static ArrayList<ListCode> getCenterStatusCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TLocations' and TABLE_COLUMN_NAME = 'LOCATION_STATUS' order by code");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }
    
    
    public static ArrayList<ListCode> getGendersCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TStudent/TUser' and TABLE_COLUMN_NAME = 'GENDER' order by code");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }

    public static ArrayList<ListCode> getPaymentMethodCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TPayment' and TABLE_COLUMN_NAME = 'PAYMENT_METHOD' order by code");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }

    public static ArrayList<ListCode> getUserRoleCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TUser' and TABLE_COLUMN_NAME = 'ROLE' order by code");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }

    public static ArrayList<ListCode> getPaymentTypeCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TPayment' and TABLE_COLUMN_NAME = 'PAYMENT_TYPE' order by code");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }
    
    
    public static ArrayList<ListCode> getParticipationLevelCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TRegistration' and TABLE_COLUMN_NAME = 'LEVEL' order by code");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }


    public static ArrayList<ListCode> getEthicGroupCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TStudent' and TABLE_COLUMN_NAME = 'ETHNIC_GROUP' order by code");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }

    public static ArrayList<ListCode> getTShirtSizeCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TRegistration' and TABLE_COLUMN_NAME = 'T-SHIRT_SIZE' order by CODE");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }


    public static Hashtable getLocationTree(DBManager dbMgr,int role, int otherID )
    {
        DBConnectionManager cm = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        Hashtable states = new Hashtable();
        Hashtable cities = null;
        

        String sqlQuery = "";
        String sqlQuery2 = "";

        switch (role)
        {
            case ApplicationProperties.USER_ROLE_STANDARD:
            case ApplicationProperties.USER_ROLE_MANAGER:
                sqlQuery = "select a.*, GROUP_CONCAT(b.SESSION_ID order by b.SESSION_ID) as sessions " +
                            " from TLocations a, TLocationSession b " +
                            " where a.LOCATION_ID = b.LOCATION_ID " +
                            " and b.SESSION_STATUS = 0 " +
                            " and a.CONFIRMATION_STATUS = 'Y' and a.ACTIVE = 'Y' " +
                            " group by a.location_id " +
                            " order by a.STATE,a.CITY,a.LOCATION_CODE";
                sqlQuery2 = "select * from TLocationSession where LOCATION_ID = ? and FIND_IN_SET(SESSION_ID, ?) > 0 order by SESSION_ID";
                break;
            case ApplicationProperties.USER_ROLE_ADMIN:
            case ApplicationProperties.USER_ROLE_LIMITED_ADMIN:
                sqlQuery = "select a.*, GROUP_CONCAT(b.SESSION_ID order by b.SESSION_ID) as sessions " +
                            " from TLocations a, TLocationSession b " +
                            " where a.LOCATION_ID = b.LOCATION_ID " +
                            " and a.CONFIRMATION_STATUS = 'Y' and a.ACTIVE = 'Y' " +
                            " group by a.location_id " +
                            " order by a.STATE,a.CITY,a.LOCATION_CODE";
                //sqlQuery2 = "select * from TLocationSession where LOCATION_ID = ? and SESSION_ID in (?) order by SESSION_ID";
                sqlQuery2 = "select * from TLocationSession where LOCATION_ID = ? and FIND_IN_SET(SESSION_ID, ?) > 0 order by SESSION_ID";
                break;
            default:
        }

        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            stm = cm.getConnection().prepareStatement(sqlQuery2);
            rs = cm.getResultsSQL(sqlQuery);
            while (rs.next())
            {
                Location loc = new Location();
                loc.setLocationAddress1(rs.getString("ADDRESS1"));
                loc.setLocationAddress2(rs.getString("ADDRESS2"));
                loc.setLocationCity(rs.getString("CITY").trim());
                loc.setLocationCode(rs.getString("LOCATION_CODE").trim());
                loc.setLocationID(rs.getInt("LOCATION_ID"));
                loc.setLocationState(rs.getString("STATE").trim());
                loc.setWebsite(rs.getString("WEBSITE"));
                loc.setComment(rs.getString("COMMENT"));
                loc.setLocationZipcode(rs.getString("ZIPCODE"));
                loc.setLocationName(rs.getString("LOCATION_NAME").trim());
                loc.setLocationConfirmationStatus(rs.getString("CONFIRMATION_STATUS"));
                loc.setLocationAccessStatus(rs.getInt("LOC_ACCESS_STATUS"));
                loc.setLocationAccessMessage(rs.getString("LOC_ACCESS_MESSAGE"));
                loc.setLatitude(rs.getDouble("LATITUDE"));
                loc.setLongitude(rs.getDouble("LONGITUDE"));
                String sessionList = rs.getString("sessions");
                ArrayList<LocationSession> sessions = new ArrayList<LocationSession>();
                stm.setInt(1,loc.getLocationID());
                stm.setString(2, sessionList);
                ResultSet rs2 = stm.executeQuery();
                while (rs2.next())
                {
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

                if (states.containsKey(loc.getLocationState()))
                {
                    cities = (Hashtable)states.get(loc.getLocationState());
                    if (cities.containsKey(loc.getLocationCity()))
                    {
                        Vector v = (Vector)cities.get(loc.getLocationCity());
                        v.add(loc);
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
                    states.put(loc.getLocationState(), cities);
                }
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
        return states;
    }
/*
    public static Hashtable getLocationTreeOld(DBManager dbMgr,int role, int otherID )
    {
        DBConnectionManager cm = null;
        ResultSet rs = null;
        Hashtable states = new Hashtable();
        Hashtable cities = null;
        

        String sqlQry = "";
        String sqlQuery2 = "select * from TLocationSession where LOCATION_ID = ? order by SESSION_ID";

        switch (role)
        {
            case ApplicationProperties.USER_ROLE_STANDARD:
                sqlQry = "select * from TLocations where LOCATION_ID <> 0 and LOCATION_STATUS not in (1) and LOCATION_STATUS < 9 and CONFIRMATION_STATUS = 'Y' order by STATE,CITY,LOCATION_CODE";
                break;
            case ApplicationProperties.USER_ROLE_MANAGER:
                sqlQry = "SELECT * FROM TLocations where (LOCATION_STATUS = 0 or (LOCATION_STATUS = 1 and LOCATION_ID in (select LOCATION_ID from TLocations_Leaders where LEADER_ID = " + otherID + "))) and CONFIRMATION_STATUS = 'Y' order by STATE,CITY,LOCATION_CODE";
                break;
            case ApplicationProperties.USER_ROLE_ADMIN:
            case ApplicationProperties.USER_ROLE_LIMITED_ADMIN:
                sqlQry = "SELECT * FROM TLocations where LOCATION_STATUS < 9 order by STATE,CITY,LOCATION_CODE";
                break;
            default:
        }

        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            PreparedStatement stm = cm.getConnection().prepareStatement(sqlQuery2);
            rs = cm.getResultsSQL(sqlQry);
            while (rs.next())
            {
                Location loc = new Location();
                loc.setLocationAddress1(rs.getString("ADDRESS1"));
                loc.setLocationAddress2(rs.getString("ADDRESS2"));
                loc.setLocationCity(rs.getString("CITY").trim());
                loc.setLocationCode(rs.getString("LOCATION_CODE").trim());
                loc.setLocationID(rs.getInt("LOCATION_ID"));
                loc.setLocationState(rs.getString("STATE").trim());
                loc.setWebsite(rs.getString("WEBSITE"));
                loc.setComment(rs.getString("COMMENT"));
                loc.setLocationZipcode(rs.getString("ZIPCODE"));
                loc.setLocationName(rs.getString("LOCATION_NAME").trim());
                loc.setLocationConfirmationStatus(rs.getString("CONFIRMATION_STATUS"));
                loc.setLocationAccessStatus(rs.getInt("LOC_ACCESS_STATUS"));
                loc.setLocationAccessMessage(rs.getString("LOC_ACCESS_MESSAGE"));
                loc.setLocationStatus(rs.getInt("LOCATION_STATUS"));
                loc.setLatitude(rs.getDouble("LATITUDE"));
                loc.setLongitude(rs.getDouble("LONGITUDE"));
                ArrayList<LocationSession> sessions = new ArrayList<LocationSession>();
                stm.setInt(1,loc.getLocationID());
                ResultSet rs2 = stm.executeQuery();
                while (rs2.next())
                {
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

                if (states.containsKey(loc.getLocationState()))
                {
                    cities = (Hashtable)states.get(loc.getLocationState());
                    if (cities.containsKey(loc.getLocationCity()))
                    {
                        Vector v = (Vector)cities.get(loc.getLocationCity());
                        v.add(loc);
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
                    states.put(loc.getLocationState(), cities);
                }
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            if (rs!=null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e )
                { ; }
                rs = null;
            }
            cm.freeConnection();
        }
        return states;
    }
 */   
    
    public static int getRegistrationRuntimeMode(DBManager dbMgr)
    {
        int mode = 0;
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'REGISTRATION_RUNTIME_MODE'");
            if (rs.next())
            {
                mode = rs.getInt("PROPERTY_VALUE_NUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return mode;
    }

    public static int getStudentCheckinMode(DBManager dbMgr)
    {
        int mode = 0;
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'CENTERS_CHECK_IN_MODE'");
            if (rs.next())
            {
                mode = rs.getInt("PROPERTY_VALUE_NUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return mode;
    }
    
    public static String generateLocationCode(DBManager dbMgr, String state, String city)
    {
        StringBuffer cityPart = new StringBuffer(city);
        if ( city.length() < 5 )
        {
            for ( int i = city.length(); i < 5; i++ )
            {
                cityPart.append("_");
            }
        }
        cityPart = new StringBuffer(Utility.replaceStringInString(cityPart.toString()," ", "_"));
        cityPart = new StringBuffer(Utility.replaceStringInString(cityPart.toString(),"\\.", "_"));
        cityPart = new StringBuffer(Utility.replaceStringInString(cityPart.toString(),",", "_"));
        cityPart = new StringBuffer(Utility.replaceStringInString(cityPart.toString(),":", "_"));
        String retBegin = state.toUpperCase() + cityPart.substring(0,5).toUpperCase();
        String retEnd = "0001";
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("SELECT location_code FROM TLocations WHERE substring(Location_Code,1,LENGTH(location_code)-4) = '" + retBegin + "' order by 1 desc");
            if (rs.next())
            {
                String str = rs.getString(1);
                String strNum = str.substring(str.length()-4,str.length());
                int num = Integer.parseInt(strNum) + 1;
                retEnd = Utility.convertIntegerToFixedString(num, 4, '0');
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return retBegin + retEnd;
    }

    public static ArrayList<ListCode> getCountryCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TCountry order by COUNTRY_NAME");
            int code = 0;
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(code++);
                lc.setCodeAlpha(rs.getString("COUNTRY_CODE"));
                lc.setDescription(rs.getString("COUNTRY_NAME"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }

    public static ArrayList<ListCode> getStateCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TState order by STATE_NAME");
            int code = 0;
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(code++);
                lc.setCodeAlpha(rs.getString("STATE_CODE"));
                lc.setDescription(rs.getString("STATE_NAME"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }

    public static ArrayList<ListCode> getTransactionGroups(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TGeneralLedger' and TABLE_COLUMN_NAME = 'GROUP' order by alpha_code");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }


    public static ArrayList<ListCode> getAdCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TRegistration' and TABLE_COLUMN_NAME = 'AD_CODE' order by CODE");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }
    
    
    public static String getCountyName(String state, String city)
    {
        String ret = "";
        
        String BASE_URI = "http://api.sba.gov/geodata/all_links_for_city_of/";
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        try
        {
            WebResource webResource = client.resource(BASE_URI).path(URLEncoder.encode(city) + "/" + state + ".json");
            String response = webResource.accept(MediaType.WILDCARD).get(String.class);
            JSONArray counties = (JSONArray)JSONValue.parse(response);
            JSONObject county = (JSONObject)counties.get(0);
            ret = (String)county.get("county_name");
        }
        catch (Exception ex)
        {
            System.out.println("ApplicationProperties [getCountyName()] error: " + ex);
        }
        return ret.toUpperCase();
    }
    
    public static ArrayList<String> getManagerAgreementItems(DBManager dbMgr)
    {
        ArrayList<String> ret = new ArrayList<String>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TLeaderAgreementItems where ITEM_ROLE = 'P' order by ITEM_ID");
            while (rs.next())
            {
                String item = "" + rs.getInt("ITEM_ID") + ". " + rs.getString("ITEM_DESCRIPTION");
                ret.add(item);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }

    public static ArrayList<String> getManagerAgreementAdditionalItems(DBManager dbMgr)
    {
        ArrayList<String> ret = new ArrayList<String>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TLeaderAgreementItems where ITEM_ROLE = 'S' order by ITEM_ID");
            while (rs.next())
            {
                String item = rs.getString("ITEM_DESCRIPTION");
                ret.add(item);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
            cm = null;
        }
        return ret;
    }
    
    public static ArrayList<ListCode> getDiscountCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TDiscountCodes' and TABLE_COLUMN_NAME = 'DISCOUNT_TYPE' order by CODE");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }

    public static ArrayList<ListCode> getDiscountPurposeCodes(DBManager dbMgr)
    {
        ArrayList<ListCode> ret = new ArrayList<ListCode>();
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {

            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TData_Definition where TABLE_NAME = 'TDiscountCodes' and TABLE_COLUMN_NAME = 'PURPOSE_TYPE' order by CODE");
            while (rs.next())
            {
                ListCode lc = new ListCode();
                lc.setCode(rs.getInt("CODE"));
                lc.setCodeAlpha(rs.getString("ALPHA_CODE"));
                lc.setDescription(rs.getString("DESCRIPTION"));
                ret.add(lc);
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }

    public static String getCenterConfirmationYear(DBManager dbMgr)
    {
        String year = "9999";
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'CENTER_CONFIRMATION_YEAR'");
            if (rs.next())
            {
                year = rs.getString("PROPERTY_VALUE_ALPHANUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return year;
    }

    public static int getCentersConfirmationMode(DBManager dbMgr)
    {
        int mode = 0;
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'CENTERS_CONFIRMATION_MODE'");
            if (rs.next())
            {
                mode = rs.getInt("PROPERTY_VALUE_NUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return mode;
    }
    
    public static int getManagersAgreementMode(DBManager dbMgr)
    {
        int mode = 0;
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'MANAGERS_AGREEMENT_MODE'");
            if (rs.next())
            {
                mode = rs.getInt("PROPERTY_VALUE_NUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return mode;
    }

    public static String getApprovedResultsYears(DBManager dbMgr)
    {
        String ret = "";
        DBConnectionManager cm = null;
        ResultSet rs = null;
        try
        {
            cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
            rs = cm.getResultsSQL("select * from TControl_Table where PROPERTY_NAME = 'RESULTS_YEARS'");
            while (rs.next())
            {
                ret = rs.getString("PROPERTY_VALUE_ALPHANUMERIC");
            }
        }
        catch (SQLException se)
        {
            System.out.println("ApplicationProperties runtime error: " + se);
        }
        finally
        {
            DBConnectionManager.dropConnObject(rs);
            cm.freeConnection();
        }
        return ret;
    }
    
}

