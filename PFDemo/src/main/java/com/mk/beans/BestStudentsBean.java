/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.ListCode;
import com.mk.classes.StudentResult;
import com.mk.dao.DBConnectionData;
import com.mk.dao.DBConnectionManager;
import com.mk.dao.DBManager;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author irek
 */
@ManagedBean
@ApplicationScoped
public class BestStudentsBean implements Serializable {


    private ArrayList<StudentResult>[] nationStudents;
    private ArrayList<StudentResult>[] statesStudents;
    private ArrayList<ListCode> rewardList = new ArrayList<ListCode>();

    private DBManager dbMgr;
    
    /**
     * Creates a new instance of ResultsBean
     */
    public BestStudentsBean() 
    {
       ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
       String dataSource = ctx.getInitParameter("DataSourceName");
       if ( dataSource == null || dataSource.trim().equals(""))
       {
           dataSource = "jdbc/mysqlDB2";
       }
        // set all necessary database connections
        DBConnectionData[] cdArr = new DBConnectionData[DBManager.DB_NAME.length];
        cdArr[DBManager.MYSQL] = new DBConnectionData(DBManager.MYSQL,
                DBManager.DB_NAME[DBManager.MYSQL], dataSource );
        cdArr[DBManager.MSSQL] = new DBConnectionData(DBManager.MSSQL,
                DBManager.DB_NAME[DBManager.MSSQL]);
        cdArr[DBManager.ORACLE] = new DBConnectionData(DBManager.ORACLE,
                DBManager.DB_NAME[DBManager.ORACLE]);
        dbMgr = new DBManager();
        dbMgr.setConnections(cdArr);
        this.nationStudents = new ArrayList[13];
        this.statesStudents = new ArrayList[13];
        for ( int i = 0; i < 13; i++)
        {
            this.nationStudents[i] = new ArrayList<StudentResult>();
            this.statesStudents[i] = new ArrayList<StudentResult>();
        }
        this.initBean();
    }



    public ArrayList<ListCode> getRewardList() {
        return rewardList;
    }

    public void setRewardList(ArrayList<ListCode> rewardList) {
        this.rewardList = rewardList;
    }

    public ArrayList<StudentResult>[] getNationStudents() {
        return nationStudents;
    }

    public void setNationStudents(ArrayList<StudentResult>[] nationStudents) {
        this.nationStudents = nationStudents;
    }

    public ArrayList<StudentResult>[] getStatesStudents() {
        return statesStudents;
    }

    public void setStatesStudents(ArrayList<StudentResult>[] statesStudents) {
        this.statesStudents = statesStudents;
    }


    
    
    public void initBean() {
        
        //String year = ApplicationProperties.getCurrentRegistrationYear(dbMgr);
        String year = "2017";

        String nationQuery = "SELECT T1.level, T2.first_name, T2.last_name, T1.score, T1.nation_place, T4.LOCATION_NAME, T4.city, T4.state, T1.hidden " +
                " FROM TResults T1 left join TStudents T2 on T1.student_identification_cd = T2.student_identification_cd " +
                " left join TRegistration T3 on T2.student_id = T3.student_id and T1.year = T3.year " +
                " left join TLocations T4 on T3.location_id = T4.location_id " +
                " where T1.year = '" + year + "' and T1.Nation_place < 21 " +
                " order by T1.level * 1,T1.Nation_place,T2.last_name, T2.first_name";
        String statesQuery = "SELECT T1.level, T2.first_name, T2.last_name, T1.state_place, T4.LOCATION_NAME, T4.city, T4.state, T1.hidden " +
                " FROM TResults T1 left join TStudents T2 on T1.student_identification_cd = T2.student_identification_cd " +
                " left join TRegistration T3 on T2.student_id = T3.student_id and T1.year = T3.year " +
                " left join TLocations T4 on T3.location_id = T4.location_id " +
                " where T1.year = '" + year + "' and T1.state_place < 4" +
                " order by T4.state, T1.level * 1, T1.state_place,T2.last_name, T2.first_name";

        String awardsQuery = "select award, count(*) as COUNT from TResults where year = '" + year + "' and award <> '' group by 1 order by 1";
        
        DBConnectionManager cm = new DBConnectionManager(this.dbMgr.getDBConnectionData(DBManager.MYSQL));
        ResultSet rs = null;
        try {
            rs = cm.getResultsSQL(nationQuery);
            while (rs.next()) {
                StudentResult stResult = new StudentResult();
                stResult.setFirstName(rs.getString("FIRST_NAME"));
                stResult.setLastName(rs.getString("LAST_NAME"));
                stResult.setLevel(rs.getString("LEVEL"));
                stResult.setLevelCode(Integer.parseInt(stResult.getLevel()));
                stResult.setLocationName(rs.getString("LOCATION_NAME"));
                stResult.setLocationCity(rs.getString("CITY"));
                stResult.setLocationState(rs.getString("STATE"));
                stResult.setScore(rs.getDouble("SCORE"));
                stResult.setNationPlace(rs.getInt("NATION_PLACE"));
                String hidden = rs.getString("HIDDEN");
                if ( hidden.equalsIgnoreCase("Y"))
                {
                    stResult.setFirstName("***HIDDEN***");
                    stResult.setLastName("***HIDDEN***");
                }
                this.nationStudents[stResult.getLevelCode()].add(stResult);
            }
            rs = cm.getResultsSQL(statesQuery);
            while (rs.next()) {
                StudentResult stResult = new StudentResult();
                stResult.setFirstName(rs.getString("FIRST_NAME"));
                stResult.setLastName(rs.getString("LAST_NAME"));
                stResult.setLevel(rs.getString("LEVEL"));
                stResult.setLevelCode(Integer.parseInt(stResult.getLevel()));
                stResult.setLocationName(rs.getString("LOCATION_NAME"));
                stResult.setLocationCity(rs.getString("CITY"));
                stResult.setLocationState(rs.getString("STATE"));
                stResult.setStatePlace(rs.getInt("STATE_PLACE"));
                String hidden = rs.getString("HIDDEN");
                if ( hidden.equalsIgnoreCase("Y"))
                {
                    stResult.setFirstName("***HIDDEN***");
                    stResult.setLastName("***HIDDEN***");
                }
                this.statesStudents[stResult.getLevelCode()].add(stResult);
            }
            rs = cm.getResultsSQL(awardsQuery);
            while (rs.next()) {
                ListCode award = new ListCode();
                award.setCodeAlpha(rs.getString("AWARD"));
                award.setCode(rs.getInt("COUNT"));
                this.rewardList.add(award);
            }
        } catch (SQLException sqlException) 
        {
            System.out.println("Error in ResultsBean [init()]: " + sqlException);
        } finally 
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
            if (cm != null)
            {
                cm.freeConnection();
            }
            cm = null;
        }

    }
}
