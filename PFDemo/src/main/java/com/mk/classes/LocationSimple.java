
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author giw
 */
public class LocationSimple implements Serializable
{

    private int locationID;
    private String locationName = "";
    private String locationCity = "";
    private String locationState = "";
    private String locationCode;

    private ArrayList<LocationSession> locationSessions;

    public String getLocationCode()
    {
        return locationCode;
    }

    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
    }

    public String getLocationState()
    {
        return locationState;
    }

    public void setLocationState(String state)
    {
        this.locationState = state;
    }

    public String getLocationCity()
    {
        return locationCity;
    }

    public void setLocationCity(String city)
    {
        this.locationCity = city;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    public int getLocationID()
    {
        return locationID;
    }

    public void setLocationID(int locationID)
    {
        this.locationID = locationID;
    }

    public ArrayList<LocationSession> getLocationSessions() {
        return locationSessions;
    }

    public void setLocationSessions(ArrayList<LocationSession> locationSessions) {
        this.locationSessions = locationSessions;
    }
    
    public LocationSession getSession( int sessionID )
    {
        LocationSession ret = null;
        for ( LocationSession ls : this.getLocationSessions())
        {
            if ( ls.getSessionID() == sessionID )
            {
                ret = ls;
                break;
            }
        }
        return ret;
    }
    
    public String toString() {
        return this.locationCode + " {" + this.locationID + "} [" + this.locationName + "]}";
    }

    public String getLocationNameAndAddress()
    {
        return getLocationName() + " City: " + getLocationCity() + " State:" + getLocationState();
    }
    
    public int getLocationCapacity()
    {
        int ret = 0;
        if ( this.getLocationSessions() != null )
        {
            for ( LocationSession ls : this.getLocationSessions() )
            {
                ret += ls.getSeatCapacity();
            }
        }
        return ret;
    }
    
    public String getLocationCapacityAsString()
    {
        String ret = "Not available.";
        if ( this.getLocationSessions() != null )
        {
            int cnt = 0;
            String prefix = "";
            for ( LocationSession ls : this.getLocationSessions() )
            {
                if (ls.getSeatCapacity() == 0 )
                {
                    prefix = "Unlimited";
                    break;
                }
                cnt += ls.getSeatCapacity();
                prefix = "" + cnt;
            }
            ret = prefix + " seats in " + this.getLocationSessions().size() + " session(s)";
        }
        return ret;
    }
}
