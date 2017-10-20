/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.programs;

import com.mk.classes.CommonData;
import com.mk.dao.DBConnectionData;
import com.mk.dao.DBConnectionManager;
import com.mk.dao.DBManager;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.io.FileWriter;
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
public class KangurRunDoubleRegistration
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
            //cd.setUrlString("jdbc:mysql://192.168.1.55:3306/KangurDB");
            //cd.setUrlString("jdbc:mysql://162.17.15.58:3306/KangurDB");
            cd.setConnectionMode(DBConnectionData.MODE_STANDARD_CONNECTION);
            cm = new DBConnectionManager(cd);

            DBConnectionData[] cdArr = new DBConnectionData[DBManager.DB_NAME.length];
            cdArr[DBManager.MYSQL] = cd;
            cdArr[DBManager.MSSQL] = cd;
            cdArr[DBManager.ORACLE] = cd;
            dbMgr = new DBManager();
            dbMgr.setConnections(cdArr);

            CommonData commonData = new CommonData(dbMgr);
            
            String BASE_URI = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
            String dest = "&destinations=";
            ClientConfig config = new DefaultClientConfig();
            config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client = Client.create(config);

            //DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
            //stm = cm.getConnection().prepareStatement(updateSql);
            
            String year = "2017";
            /*
            rs = cm.getResultsSQL("select a.First_name as FIRST_NAME,a.last_name as LAST_NAME,	b.level as LEVEL, GROUP_CONCAT(convert(b.status,CHAR(50)) SEPARATOR ' | ') as STATUSES,"
                    + "	GROUP_CONCAT(convert(c.location_id,CHAR(50)) SEPARATOR ' | ') as CENTERS, GROUP_CONCAT(concat(c.CITY,' ',c.state) SEPARATOR ' | ') as STATES,"
                    + "	GROUP_CONCAT(convert(concat(c.latitude,',',c.longitude),CHAR(100)) SEPARATOR '#') as COORDINATES,"
                    + "	GROUP_CONCAT(b.CONTACT_PHONE1  SEPARATOR ' | ') as CONTACT_PHONE1, GROUP_CONCAT(c.competition_time SEPARATOR ' | ') as TIMES,"
                    + "	GROUP_CONCAT(a.PARENT_GUARDIAN_EMAIL  SEPARATOR ' | ') as PARENT_EMAILS,"
                    + " GROUP_CONCAT(a.STUDENT_EMAIL  SEPARATOR ' | ') as STUDENT_EMAILS,"
                    + "	GROUP_CONCAT(convert(a.student_id,CHAR(50)) SEPARATOR ' | ') as STUDENT_IDS, GROUP_CONCAT(a.student_identification_cd SEPARATOR ' | ') as STUDENT_CDS,"
                    + " count(*) as COUNT from KangurDB.TStudents a, TRegistration b, TLocations c where a.student_id in (select student_id from KangurDB.TRegistration where year = '" + year + "' and status in (1,2,3))"
                    + " and a.student_id = b.student_id and b.year = '" + year + "' and b.status in (1,2,3) and b.location_id = c.location_id "
                    + " group by 1,2,3 having count > 1 order by 1,2");
            */
            rs = cm.getResultsSQL("select a.First_name as FIRST_NAME,a.last_name as LAST_NAME,b.level as LEVEL,"
                    + " 	GROUP_CONCAT(convert(c.location_id,CHAR(50)) SEPARATOR ' | ') as CENTERS, "
                    + "         GROUP_CONCAT(concat(c.CITY,' ',c.state) SEPARATOR ' | ') as STATES,"
                    + " 	GROUP_CONCAT(convert(r.score,CHAR(100)) SEPARATOR ' | ') as SCORES,"
                    + " 	GROUP_CONCAT(convert(concat(c.latitude,',',c.longitude),CHAR(100)) SEPARATOR '#') as COORDINATES,"
                    + " 	GROUP_CONCAT(b.CONTACT_PHONE1  SEPARATOR ' | ') as CONTACT_PHONE1, GROUP_CONCAT(d.competition_time SEPARATOR ' | ') as TIMES,"
                    + " 	GROUP_CONCAT(a.PARENT_GUARDIAN_EMAIL  SEPARATOR ' | ') as PARENT_EMAILS,"
                    + " GROUP_CONCAT(a.STUDENT_EMAIL  SEPARATOR ' | ') as STUDENT_EMAILS,"
                    + "	GROUP_CONCAT(convert(a.student_id,CHAR(50)) SEPARATOR ' | ') as STUDENT_IDS, GROUP_CONCAT(a.student_identification_cd SEPARATOR ' | ') as STUDENT_CDS,"
                    + " count(*) as COUNT from TStudents a, TRegistration b left join TLocationSession d on b.location_id = d.location_id and b.session_id = d.session_id"
                    + " left join TResults r on b.student_identification_cd = r.student_identification_cd and b.year = r.year, "
                    + " TLocations c "
                    + " where a.student_id in "
                    + "	("
                    + "    select student_id from TRegistration where year = '2017' and status in (2)"
                    + "	)"
                    + "	and a.student_id = b.student_id and b.year = '2017' and b.status in (2) and b.location_id = c.location_id and b.session_id = d.session_id"
                    + "	group by 1,2,3 having count > 1 order by 1,2");
                FileWriter writer = new FileWriter("c:\\tmp\\DoubleRegistrations.txt", false);
                writer.write("FIRST_NAME\tLAST_NAME\tLEVEL\tCENTERS\tTIMES\tDISTANCES\tRESULTS\tSTATES\tPHONES\tSTUDENT_IDS\tSTUDENT_CDS\tPARENT_EMAILS\tSTUDENT_EMAILS\tNUM_OF_CENTERS\r\n");
                
                try
                {
                    while (rs.next())
                    {
                        String line = "";
                        
                        String fname = rs.getString("FIRST_NAME");
                        String lname = rs.getString("LAST_NAME");
                        String level = rs.getString("LEVEL");
                        String states = rs.getString("STATES");
                        String scores = rs.getString("SCORES");
                        String phones = rs.getString("CONTACT_PHONE1");
                        String times = rs.getString("TIMES");
                        String pemails = rs.getString("PARENT_EMAILS");
                        String semails = rs.getString("STUDENT_EMAILS");
                        String stids = rs.getString("STUDENT_IDS");
                        String stcds = rs.getString("STUDENT_CDS");
                        String centers = rs.getString("CENTERS");
                        String coordinates = rs.getString("COORDINATES");
                        int count = rs.getInt("COUNT");

                        String url = "";
                        
                        StringTokenizer st = new StringTokenizer(coordinates,"#");
                        String dists = "";
                        String[] array = new String[count];
                        for (int i = 0; i < count; i++)
                        {
                            array[i] = st.nextToken();
                        }
                        
                        for ( int i = 0; i < count - 1; i++ )
                        {
                            for ( int j = i + 1; j < count; j++ )
                            {
                                url = BASE_URI + array[i];
                                url += dest + array[j];  
                                //System.out.println("Locations: " + locations + " Coordinates: " + coordinates);
                                String response = null;

                                WebResource webResource = client.resource(url);
                                response = webResource.accept(MediaType.WILDCARD).get(String.class);

                                //System.out.println(response);
                                String distance = "";
                                try
                                {
                                JSONObject obj = (JSONObject)JSONValue.parse(response);
                                JSONArray results = (JSONArray)obj.get("rows");
                                JSONObject res = (JSONObject)results.get(0);
                                JSONArray elems = (JSONArray)res.get("elements");
                                JSONObject rec = (JSONObject)elems.get(0);
                                distance = (String)((JSONObject)rec.get("distance")).get("text");
                                }
                                catch (java.lang.NullPointerException npe )
                                {
                                    distance = "???";
                                    System.out.println("Student: " + fname + " " + lname + " ERROR in center distance calc.");
                                }
                                dists += (dists.trim().equalsIgnoreCase("") ? "":" | ") + distance;
                                Thread.sleep(1500);
                                
                            }
                        }
                        writer.write(fname + "\t" + lname + "\t" + level + "\t" + centers + "\t" + times + "\t" + dists + "\t" + scores + "\t" + states + "\t" + phones + "\t" + stids + "\t" + stcds + "\t" + pemails + "\t" + semails + "\t" + count );
                        writer.write("\r\n");
                        System.out.println("Student: " + fname + " " + lname);
                        
                    }
                   
                } catch (SQLException se)
                {
                    System.out.println(se);
                }
                
          writer.close();

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
