/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.Leader;
import com.mk.classes.Location;
import com.mk.classes.LocationSession;
import com.mk.classes.User;
import com.mk.dao.DBManager;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author irek
 */
@ManagedBean(name="centerRegistration")
@ViewScoped
public class CenterRegistration implements Serializable 
{

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;

    private UserLogin userLogin;
    
    // user data
    private User user;
    
    
    // bean specific properties
    private Location newCenter = new Location();
    private LocationSession session1 = new LocationSession();
    private LocationSession session2 = new LocationSession();
    private LocationSession session3 = new LocationSession();
    private LocationSession session4 = new LocationSession();
    private LocationSession session5 = new LocationSession();
    private ArrayList<String> levels1;
    private ArrayList<String> levels2;
    private ArrayList<String> levels3;
    private ArrayList<String> levels4;
    private ArrayList<String> levels5;
    private String[] selectedLevels1;
    private String[] selectedLevels2;
    private String[] selectedLevels3;
    private String[] selectedLevels4;
    private String[] selectedLevels5;
    
    private boolean readyToCreateCenter = false;
            
    
    
    
    
    
    public CenterRegistration() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        user = this.userLogin.getUser();
        levels1 = new ArrayList<String>();
        levels2 = new ArrayList<String>();
        levels3 = new ArrayList<String>();
        levels4 = new ArrayList<String>();
        levels5 = new ArrayList<String>();
        for ( int i = 1; i < 13; i++ )
        {
            levels1.add("" + i);
            levels2.add("" + i);
            levels3.add("" + i);
            levels4.add("" + i);
            levels5.add("" + i);
        }
                
    }

    public LocationSession getSession4() {
        return session4;
    }

    public void setSession4(LocationSession session4) {
        this.session4 = session4;
    }

    public LocationSession getSession5() {
        return session5;
    }

    public void setSession5(LocationSession session5) {
        this.session5 = session5;
    }

    public ArrayList<String> getLevels4() {
        return levels4;
    }

    public void setLevels4(ArrayList<String> levels4) {
        this.levels4 = levels4;
    }

    public ArrayList<String> getLevels5() {
        return levels5;
    }

    public void setLevels5(ArrayList<String> levels5) {
        this.levels5 = levels5;
    }

    public String[] getSelectedLevels4() {
        return selectedLevels4;
    }

    public void setSelectedLevels4(String[] selectedLevels4) {
        this.selectedLevels4 = selectedLevels4;
    }

    public String[] getSelectedLevels5() {
        return selectedLevels5;
    }

    public void setSelectedLevels5(String[] selectedLevels5) {
        this.selectedLevels5 = selectedLevels5;
    }

    public boolean isReadyToCreateCenter() {
        return readyToCreateCenter;
    }

    public void setReadyToCreateCenter(boolean readyToCreateCenter) {
        this.readyToCreateCenter = readyToCreateCenter;
    }

    public ArrayList<String> getLevels1() {
        return levels1;
    }

    public void setLevels1(ArrayList<String> levels1) {
        this.levels1 = levels1;
    }

    public ArrayList<String> getLevels2() {
        return levels2;
    }

    public void setLevels2(ArrayList<String> levels2) {
        this.levels2 = levels2;
    }

    public ArrayList<String> getLevels3() {
        return levels3;
    }

    public void setLevels3(ArrayList<String> levels3) {
        this.levels3 = levels3;
    }

    public String[] getSelectedLevels1() {
        return selectedLevels1;
    }

    public void setSelectedLevels1(String[] selectedLevels1) {
        this.selectedLevels1 = selectedLevels1;
    }

    public String[] getSelectedLevels2() {
        return selectedLevels2;
    }

    public void setSelectedLevels2(String[] selectedLevels2) {
        this.selectedLevels2 = selectedLevels2;
    }

    public String[] getSelectedLevels3() {
        return selectedLevels3;
    }

    public void setSelectedLevels3(String[] selectedLevels3) {
        this.selectedLevels3 = selectedLevels3;
    }


    public LocationSession getSession1() {
        return session1;
    }

    public void setSession1(LocationSession session1) {
        this.session1 = session1;
    }

    public LocationSession getSession2() {
        return session2;
    }

    public void setSession2(LocationSession session2) {
        this.session2 = session2;
    }

    public LocationSession getSession3() {
        return session3;
    }

    public void setSession3(LocationSession session3) {
        this.session3 = session3;
    }

    public Location getNewCenter() {
        return newCenter;
    }

    public void setNewCenter(Location newCenter) {
        this.newCenter = newCenter;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }


    // UI supporting methods

    public String getLevelList(int session) {
        String ret = "";
        String[] l = null;
        if (session == 1) {
            l = this.selectedLevels1;
        } else if (session == 2) {
            l = this.selectedLevels2;
        } else if (session == 3) {
            l = this.selectedLevels3;
        } else if (session == 4) {
            l = this.selectedLevels4;
        } else {
            l = this.selectedLevels5;
        }
        int cnt = 0;
        if (l != null) {
            for (String level : l) {
                ret += ret.equals("") ? level : "," + level;
                cnt++;
            }
        }
        return ret;
    }

    public void verifyNewCenter() {
        
        if (!this.isCenterInfoComplete()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide all required data.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        else
        {
            ArrayList<LocationSession> sessions = new ArrayList<LocationSession>();
            if ( this.newCenter.getNumberOfSessions() > 0 )
            {
                sessions.add(this.session1);
            }
            if ( this.newCenter.getNumberOfSessions() > 1 )
            {
                sessions.add(this.session2);
            }
            if ( this.newCenter.getNumberOfSessions() > 2 )
            {
                sessions.add(this.session3);
            }
            if ( this.newCenter.getNumberOfSessions() > 3 )
            {
                sessions.add(this.session4);
            }
            if ( this.newCenter.getNumberOfSessions() > 4 )
            {
                sessions.add(this.session5);
            }
            this.newCenter.setLocationSessions(sessions);
            this.readyToCreateCenter = true;
        }
    }

    
    public void saveNewCenter()
    {
        Leader l = new Leader();
        l.setLeaderID(this.user.getUserID());
        l.setEmail(this.user.getUserEmail());
        l.setFirstName(this.getUser().getUserFirstName());
        l.setLastName(this.getUser().getUserLastName());
        this.newCenter.setLeader(l);
        this.bc.saveNewCenterRequest(this.newCenter);
        this.readyToCreateCenter = false;  
        this.userLogin.setRedirectPage("");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {        
            externalContext.redirect("/mk/default.html");
        } catch (IOException ex) {
        }
    }
    
    private boolean isCenterInfoComplete() {
        boolean ret = true;
        this.session1.setLevels(this.getLevelList(1));
        this.session1.setSessionName("SESSION 1");
        this.session1.setCompetitionTime(new Time(this.session1.getCompetitionDate().getTime()));
        this.session2.setLevels(this.getLevelList(2));
        this.session2.setSessionName("SESSION 2");
        this.session2.setCompetitionTime(new Time(this.session2.getCompetitionDate().getTime()));
        this.session3.setLevels(this.getLevelList(3));
        this.session3.setSessionName("SESSION 3");
        this.session3.setCompetitionTime(new Time(this.session3.getCompetitionDate().getTime()));
        this.session4.setLevels(this.getLevelList(4));
        this.session4.setSessionName("SESSION 4");
        this.session4.setCompetitionTime(new Time(this.session4.getCompetitionDate().getTime()));
        this.session5.setLevels(this.getLevelList(5));
        this.session5.setSessionName("SESSION 5");
        this.session5.setCompetitionTime(new Time(this.session5.getCompetitionDate().getTime()));
        if ( this.newCenter.getLocationAddress1().trim().equals("")
                || this.newCenter.getLocationCity().trim().equals("")
                || this.newCenter.getLocationState().trim().equals("")
                || this.newCenter.getLocationName().trim().equals("")
                || this.newCenter.getLocationZipcode().trim().equals("")
                || this.newCenter.getNumberOfSessions() == 0
                || (this.newCenter.getNumberOfSessions() > 0 && !isSessionComplete(this.session1))
                || (this.newCenter.getNumberOfSessions() > 1 && !isSessionComplete(this.session2))
                || (this.newCenter.getNumberOfSessions() > 2 && !isSessionComplete(this.session3))
                || (this.newCenter.getNumberOfSessions() > 3 && !isSessionComplete(this.session4))
                || (this.newCenter.getNumberOfSessions() > 4 && !isSessionComplete(this.session5))
                )
        {
            ret = false;
        }
        return ret;
    }
    
    private boolean isSessionComplete( LocationSession ls )
    {
        boolean ret = true;
        if ( ls.getLevels().trim().equals(""))
        {
            ret = false;
        }
        return ret;
    }

}
