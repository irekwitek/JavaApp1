/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.programs;

import com.mk.dao.DBConnectionData;
import com.mk.dao.DBConnectionManager;
import com.mk.dao.DBManager;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.net.URLEncoder;
import java.sql.*;
import java.util.StringTokenizer;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author giw
 */
public class KangurRunUpdateMissingCoordinates
{

    public static void main(String[] arg)
    {
        DBConnectionData cd = null;
        DBConnectionManager cm = null;
        DBManager dbMgr;
        ResultSet rs = null;
        PreparedStatement stm = null;

        try
        {
            
            cd = new DBConnectionData(DBManager.MYSQL, DBManager.DB_NAME[DBManager.MYSQL]);
            cd.setDriverName("com.mysql.jdbc.Driver");
            cd.setUserName("root");
            cd.setPassword("eureka15");
            cd.setUrlString("jdbc:mysql://10.1.10.30:3306/KangarooDB");
            //cd.setUrlString("jdbc:mysql://192.168.1.55:3306/KangarooDB");
            cd.setConnectionMode(DBConnectionData.MODE_STANDARD_CONNECTION);
            cm = new DBConnectionManager(cd);

            DBConnectionData[] cdArr = new DBConnectionData[DBManager.DB_NAME.length];
            cdArr[DBManager.MYSQL] = cd;
            cdArr[DBManager.MSSQL] = cd;
            cdArr[DBManager.ORACLE] = cd;
            dbMgr = new DBManager();
            dbMgr.setConnections(cdArr);

            
            String BASE_URI = "http://maps.google.com/maps/api/geocode/json?address=";
            ClientConfig config = new DefaultClientConfig();
            config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client = Client.create(config);

            String updateSql = "update TLocations set LONGITUDE = ?, LATITUDE = ?, COUNTY = ? where LOCATION_ID = ?";
            stm = cm.getConnection().prepareStatement(updateSql);
            rs = cm.getResultsSQL("select * from TLocations where (latitude = 0 or latitude is null or longitude = 0 or longitude is null)");
            //rs = cm.getResultsSQL("select * from TLocations");
                try
                {
                    while (rs.next())
                    {
                        int loc_id = rs.getInt("LOCATION_ID");
                        String cityName = rs.getString("CITY");
                        String stateName = rs.getString("STATE");
                        String address1 = rs.getString("ADDRESS1");
                        String countyNameOriginal = rs.getString("COUNTY");
                        if ( countyNameOriginal == null )
                        {
                            countyNameOriginal = "";
                        }
                        String addPart = "";
                        StringTokenizer st = new StringTokenizer(address1," ");
                        int i = 2;
                        while ( st.hasMoreTokens() && i >= 0)
                        {
                            addPart += "+" + st.nextToken();
                            i--;
                        }
                        addPart += ",";  
                        st = new StringTokenizer(cityName," ");
                        i = 1;
                        while ( st.hasMoreTokens() && i >= 0)
                        {
                            addPart += "+" + st.nextToken();
                            i--;
                        }
                        addPart += ",+" + stateName;
                        System.out.println("ADDRESS = " + address1 + ", " + cityName + ", " + stateName + " String = " + addPart);
                        String response = null;
                        try
                        {
                        WebResource webResource = client.resource(BASE_URI+addPart);
                        response = webResource.accept(MediaType.WILDCARD).get(String.class);
                        
                        //System.out.println(response);
                        
                        JSONObject obj = (JSONObject)JSONValue.parse(response);
                        JSONArray results = (JSONArray)obj.get("results");
                        JSONObject location = (JSONObject)results.get(0);
                        JSONArray addrComp = (JSONArray)location.get("address_components");
                        String countyName = "";
                        if ( addrComp != null && addrComp.size() > 0)
                        {
                            for ( int j = 0; j < addrComp.size(); j++)
                            {
                                JSONObject addrCompObj = (JSONObject)addrComp.get(j);
                                JSONArray arr = (JSONArray)addrCompObj.get("types");
                                for ( int k = 0; k < arr.size();k++)
                                {
                                    String typeName = arr.get(k).toString();
                                    if ( typeName != null && typeName.equalsIgnoreCase("administrative_area_level_2"))
                                    {
                                        String str = addrCompObj.get("long_name").toString();
                                        if ( str != null )
                                        {
                                            int index = str.indexOf(" County");
                                            if (index < 0 )
                                            {
                                                index = str.length();
                                            }
                                            countyName = str.substring(0, index).toUpperCase();
                                        }
                                        break;
                                    }
                                    if ( !typeName.equals("") )
                                    {
                                        break;
                                    }
                                }
                                
                            }
                        }
                        JSONObject locGeometry = (JSONObject)location.get("geometry");
                        JSONObject locViewport = (JSONObject)locGeometry.get("viewport");
                        JSONObject locNortheast = (JSONObject)locViewport.get("northeast");
                        String lat = locNortheast.get("lat").toString();
                        String lng = locNortheast.get("lng").toString();
                        stm.setDouble(1, new Double(lng).doubleValue());
                        stm.setDouble(2, new Double(lat).doubleValue());
                        stm.setString(3, (countyName.equals("") ? countyNameOriginal : countyName ));
                        stm.setInt(4, loc_id);
                        int result = stm.executeUpdate();
                        System.out.println("COUNTY: <" + countyName + ">");
                        System.out.println("LAT:"+lat+" LNG:"+lng + " RESULT= " + result);
                        Thread.sleep(1500);
                        }
                        catch (Exception ex)
                        {
                            System.out.println(ex);
                            System.out.println(response);
                        }
                        
                    }
                   
                } catch (SQLException se)
                {
                }
                


        } catch (Exception e)
        {
            System.out.println("Exception: " + e);
        } finally
        {
            if (cm != null)
            {
                rs = null;
                stm = null;
                cm.freeConnection();
            }
        }


    }
}
