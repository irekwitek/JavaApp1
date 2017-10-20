/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.dao.DBConnectionData;
import com.mk.dao.DBManager;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;


/**
 *
 * @author irek
 */
@ManagedBean
@ApplicationScoped
public class AppBean implements Serializable 
{


    private DBManager dbMgr;
    private final BusinessProcessControl bc;
    private final CommonData commonData;
    
    /**
     * Creates a new instance of ResultsBean
     */
    public AppBean() 
    {
        // set all necessary database connections
        DBConnectionData[] cdArr = new DBConnectionData[DBManager.DB_NAME.length];
        cdArr[DBManager.MYSQL] = new DBConnectionData(DBManager.MYSQL,
                DBManager.DB_NAME[DBManager.MYSQL], "jdbc/mysqlDB2");
        cdArr[DBManager.MSSQL] = new DBConnectionData(DBManager.MSSQL,
                DBManager.DB_NAME[DBManager.MSSQL]);
        cdArr[DBManager.ORACLE] = new DBConnectionData(DBManager.ORACLE,
                DBManager.DB_NAME[DBManager.ORACLE]);
        dbMgr = new DBManager();
        dbMgr.setConnections(cdArr);
        // initiate business process control
        bc = new BusinessProcessControl(dbMgr);
        commonData = bc.getCommonData();
    }

}
