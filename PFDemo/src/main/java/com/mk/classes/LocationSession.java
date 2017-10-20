/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author irek
 */
public class LocationSession implements Serializable
{    
    private int locationID;
    private int sessionID;
    private String sessionName = "";
    private Time competitionTime;
    private Date competitionDate;
    private String levels = "";
    private int sessionStatus;
    private int seatCapacity;
    private int availableSeatCount;
    private int seatsTaken;
    private boolean editDisabled;
    private boolean changeMade;

    public LocationSession() {
        
        this.competitionDate = new Time(17,0,0);
        
       /*
       SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        try {
            this.competitionTime = formatter.parse("15:00:00");
        } catch (ParseException ex) {
            ;
        }
       */
    }

    public boolean isChangeMade() {
        return changeMade;
    }

    public void setChangeMade(boolean changeMade) {
        this.changeMade = changeMade;
    }

    public boolean isEditDisabled() {
        return editDisabled;
    }

    public void setEditDisabled(boolean editDisabled) {
        this.editDisabled = editDisabled;
    }


    public int getSeatsTaken() {
        return seatsTaken;
    }

    public void setSeatsTaken(int seatsTaken) {
        this.seatsTaken = seatsTaken;
    }


    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public Time getCompetitionTime() {
        return competitionTime;
    }

    public void setCompetitionTime(Time competitionTime) {
        this.competitionTime = competitionTime;
    }

    public Date getCompetitionDate() {
        return competitionDate;
    }

    public void setCompetitionDate(Date competitionDate) {
        this.competitionDate = competitionDate;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    public int getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(int sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
    
    public String getSessionInfo()
    {
        return this.sessionName + " Time: " + this.getCompetitionTimeFriendly(); 
    }

    public int getAvailableSeatCount() {
        return availableSeatCount;
    }

    public void setAvailableSeatCount(int availableSeatCount) {
        this.availableSeatCount = availableSeatCount;
    }
    
    public String getLocationNameAndAddressAndSession( Location center )
    {
        return center.getLocationNameAndAddress() + " " + this.getSessionInfo();
    }

    public String getCompetitionTimeFriendly() {
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        return formatter.format(competitionTime);
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

}
