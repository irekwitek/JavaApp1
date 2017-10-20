
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
public class Location implements Serializable
{

    private int locationID;
    private String locationName = "";
    private String locationAddress1 = "";
    private String locationAddress2 = "";
    private String locationCity = "";
    private String locationCounty = "";
    private String locationState = "";
    private String locationZipcode = "";
    private String website = "";
    private String comment = "";
    private String locationCode;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Leader leader = null;
    private Student[] registeredStudents = null;
    private int directorUserId = 0;
    private String directorName = "";
    private String locationCountryRegion = "";
    private String locationStateRegion = "";
    private String locationConfirmationStatus;
    private int numberOfSessions;

    private int locationAccessStatus = 0;
    private String locationAccessMessage = "";
    private String locationRegistrationCode = "";

    private ArrayList<LocationSession> locationSessions;
    
    private boolean managerPrimary = false;
    private int createdByUser;
    private String established = "";
    private String converted = "";
    private String active = "";

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getConverted() {
        return converted;
    }

    public void setConverted(String converted) {
        this.converted = converted;
    }

    public String getEstablished() {
        return established;
    }

    public void setEstablished(String established) {
        this.established = established;
    }

    public int getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(int createdByUser) {
        this.createdByUser = createdByUser;
    }

    
    public boolean isManagerPrimary() {
        return managerPrimary;
    }

    public void setManagerPrimary(boolean managerPrimary) {
        this.managerPrimary = managerPrimary;
    }
    
    public String getLocationRegistrationCode() {
        return locationRegistrationCode;
    }

    public void setLocationRegistrationCode(String locationRegistrationCode) {
        this.locationRegistrationCode = locationRegistrationCode;
    }

    public int getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions(int numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }

    public String getLocationCounty() {
        return locationCounty;
    }

    public void setLocationCounty(String locationCounty) {
        this.locationCounty = locationCounty;
    }

    public int getLocationAccessStatus() {
        return locationAccessStatus;
    }

    public void setLocationAccessStatus(int locationAccessStatus) {
        this.locationAccessStatus = locationAccessStatus;
    }

    public String getLocationAccessMessage() {
        return locationAccessMessage;
    }

    public void setLocationAccessMessage(String locationAccessMessage) {
        this.locationAccessMessage = locationAccessMessage;
    }

    
    public String getLocationConfirmationStatus() {
        return locationConfirmationStatus;
    }

    public void setLocationConfirmationStatus(String locationConfirmationStatus) {
        this.locationConfirmationStatus = locationConfirmationStatus;
    }

    
    
    public String getLocationStateRegion()
    {
        return locationStateRegion;
    }

    public void setLocationStateRegion(String locationStateRegion)
    {
        this.locationStateRegion = locationStateRegion;
    }

    
    
    public String getLocationCountryRegion()
    {
        return locationCountryRegion;
    }

    public void setLocationCountryRegion(String locationRegion)
    {
        this.locationCountryRegion = locationRegion;
    }

    public int getDirectorUserId() {
        return directorUserId;
    }

    public void setDirectorUserId(int directorUserId) {
        this.directorUserId = directorUserId;
    }

    
    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    
    public Student[] getRegisteredStudents()
    {
        return registeredStudents;
    }

    public void setRegisteredStudents(Student[] registeredStudents)
    {
        this.registeredStudents = registeredStudents;
    }


    public Leader getLeader()
    {
        return leader;
    }

    public void setLeader(Leader leader)
    {
        this.leader = leader;
    }



    

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitute)
    {
        this.latitude = latitute;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitute)
    {
        this.longitude = longitute;
    }


    /**
     * Get the value of locationCode
     *
     * @return the value of locationCode
     */
    public String getLocationCode()
    {
        return locationCode;
    }

    /**
     * Set the value of locationCode
     *
     * @param locationCode new value of locationCode
     */
    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
    }


    /**
     * Get the value of website
     *
     * @return the value of website
     */
    public String getWebsite()
    {
        return website;
    }

    /**
     * Set the value of website
     *
     * @param website new value of website
     */
    public void setWebsite(String website)
    {
        this.website = website;
    }

    /**
     * Get the value of zipcode
     *
     * @return the value of zipcode
     */
    public String getLocationZipcode()
    {
        return locationZipcode;
    }

    /**
     * Set the value of zipcode
     *
     * @param zipcode new value of zipcode
     */
    public void setLocationZipcode(String zipcode)
    {
        this.locationZipcode = zipcode;
    }

    /**
     * Get the value of state
     *
     * @return the value of state
     */
    public String getLocationState()
    {
        return locationState;
    }

    /**
     * Set the value of state
     *
     * @param state new value of state
     */
    public void setLocationState(String state)
    {
        this.locationState = state;
    }

    /**
     * Get the value of city
     *
     * @return the value of city
     */
    public String getLocationCity()
    {
        return locationCity;
    }

    /**
     * Set the value of city
     *
     * @param city new value of city
     */
    public void setLocationCity(String city)
    {
        this.locationCity = city;
    }

    /**
     * Get the value of address2
     *
     * @return the value of address2
     */
    public String getLocationAddress2()
    {
        return locationAddress2;
    }

    /**
     * Set the value of address2
     *
     * @param address2 new value of address2
     */
    public void setLocationAddress2(String address2)
    {
        this.locationAddress2 = address2;
    }

    /**
     * Get the value of address1
     *
     * @return the value of address1
     */
    public String getLocationAddress1()
    {
        return locationAddress1;
    }

    /**
     * Set the value of address1
     *
     * @param address1 new value of address1
     */
    public void setLocationAddress1(String address1)
    {
        this.locationAddress1 = address1;
    }

    /**
     * Get the value of locationName
     *
     * @return the value of locationName
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * Set the value of locationName
     *
     * @param locationName new value of locationName
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    /**
     * Get the value of locationID
     *
     * @return the value of locationID
     */
    public int getLocationID()
    {
        return locationID;
    }

    /**
     * Set the value of locationID
     *
     * @param locationID new value of locationID
     */
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
