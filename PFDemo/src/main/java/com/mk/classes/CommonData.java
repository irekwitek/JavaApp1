/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import com.mk.dao.DBManager;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author giw
 */
public class CommonData implements Serializable
{

    private ArrayList<ListCode> registrationStatus;
    private ArrayList<ListCode> centerStatus;
    private ArrayList<ListCode> tShirtSizes;
    private ArrayList<ListCode> genders;
    private ArrayList<ListCode> countries;
    private ArrayList<ListCode> states;
    private ArrayList<ListCode> levels;
    private ArrayList<ListCode> userRoles;
    private ArrayList<ListCode> ethnicGroups;
    private ArrayList<ListCode> paymentMethods;
    private ArrayList<ListCode> paymentTypes;
    private ArrayList<ListCode> adCodes;
    private ArrayList<ListCode> discountCodes;

    private final DBManager dbManager;
    

    public CommonData(DBManager dbMgr)
    {
        this.dbManager = dbMgr;
        this.registrationStatus = ApplicationProperties.getRegistrationStatusCodes(dbMgr);
        this.centerStatus = ApplicationProperties.getCenterStatusCodes(dbMgr);
        this.tShirtSizes = ApplicationProperties.getTShirtSizeCodes(dbMgr);
        this.paymentMethods = ApplicationProperties.getPaymentMethodCodes(dbMgr);
        this.paymentTypes = ApplicationProperties.getPaymentTypeCodes(dbMgr);
        this.userRoles = ApplicationProperties.getUserRoleCodes(dbMgr);
        this.genders = ApplicationProperties.getGendersCodes(dbMgr);
        this.ethnicGroups = ApplicationProperties.getEthicGroupCodes(dbMgr);
        this.levels = ApplicationProperties.getParticipationLevelCodes(dbMgr);
        this.countries = ApplicationProperties.getCountryCodes(dbMgr);
        this.states = ApplicationProperties.getStateCodes(dbMgr);
        this.adCodes = ApplicationProperties.getAdCodes(dbMgr);
        this.discountCodes = ApplicationProperties.getDiscountCodes(dbMgr);
    }

    public ArrayList<ListCode> getDiscountCodes() {
        return discountCodes;
    }

    public void setDiscountCodes(ArrayList<ListCode> discountCodes) {
        this.discountCodes = discountCodes;
    }

    public ArrayList<ListCode> getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(ArrayList<ListCode> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public ArrayList<ListCode> getRegistrationStatus() {
        return registrationStatus;
    }

    public ArrayList<ListCode> getCenterStatus() {
        return centerStatus;
    }

    public ArrayList<ListCode> gettShirtSizes() {
        return tShirtSizes;
    }

    public ArrayList<ListCode> getGenders() {
        return genders;
    }

    public ArrayList<ListCode> getCountries() {
        return countries;
    }

    public ArrayList<ListCode> getStates() {
        return states;
    }

    public ArrayList<ListCode> getLevels() {
        return levels;
    }

    public ArrayList<ListCode> getUserRoles() {
        return userRoles;
    }

    public ArrayList<ListCode> getEthnicGroups() {
        return ethnicGroups;
    }

    public ArrayList<ListCode> getPaymentMethods() {
        return paymentMethods;
    }

    public ArrayList<ListCode> getAdCodes() {
        return adCodes;
    }

    
    public String getRegistrationStatusCodeName(int code)
    {
        String ret = "";
        for ( ListCode ls : registrationStatus )
        {
            if (ls.getCode() == code)
            {
                ret = ls.getDescription();
                break;
            }
        }
        return ret;
    }

    public int getRegistrationStatusCode(String name)
    {
        int ret = -1;
        for ( ListCode ls : registrationStatus )
        {
            if (ls.getCodeAlpha().equalsIgnoreCase(name))
            {
                ret = ls.getCode();
                break;
            }
        }
        return ret;
    }

    public String getAdCodeName(int code)
    {
        String ret = "";
        for ( ListCode ls : adCodes )
        {
            if ( ls.getCode() == code )
            {
                ret = ls.getCodeAlpha();
                break;
            }
        }
        return ret;
    }

    public int getAdCode(String name)
    {
        int ret = -1;
        for ( ListCode ls : adCodes )
        {
            if ( ls.getCodeAlpha().equalsIgnoreCase(name) )
            {
                ret = ls.getCode();
                break;
            }
        }
        return ret;
    }


    public int getGenderCode(String name)
    {
        int ret = -1;
        for ( ListCode ls : genders )
        {
            if ( ls.getCodeAlpha().trim().equals(name) )
            {
                ret = ls.getCode();
                break;
            }
        }
        return ret;
    }


    public String getGenderName(int code)
    {
        String ret = "";
        for ( ListCode ls : genders )
        {
            if ( ls.getCode() == code )
            {
                ret = ls.getCodeAlpha();
                break;
            }
        }
        return ret;
    }



    public int getEthnicGroupCode(String name)
    {
        int ret = -1;
        for ( ListCode ls : ethnicGroups )
        {
            if ( ls.getCodeAlpha().trim().equals(name) )
            {
                ret = ls.getCode();
                break;
            }
        }
        return ret;
    }

    public String getEthnicGroupName(int code)
    {
        String ret = "";
        for ( ListCode ls : ethnicGroups )
        {
            if ( ls.getCode() == code )
            {
                ret = ls.getCodeAlpha();
                break;
            }
        }
        return ret;
    }

    public String getCountryOfOriginName(int code)
    {
        String ret = "";
        for ( ListCode ls : countries )
        {
            if ( ls.getCode() == code )
            {
                ret = ls.getCodeAlpha();
                break;
            }
        }
        return ret;
    }


    public int getPaymentMethodCode(String name)
    {
        int ret = -1;
        for ( ListCode ls : paymentMethods )
        {
            if ( ls.getCodeAlpha().equalsIgnoreCase(name))
            {
                ret = ls.getCode();
                break;
            }
        }
        return ret;
    }

    public String getPaymentMethodName(int code)
    {
        String ret = "";
        for ( ListCode ls : paymentMethods )
        {
            if ( ls.getCode() == code )
            {
                ret = ls.getCodeAlpha();
                break;
            }
        }
        return ret;
    }

    public String getUserRoleName(int code)
    {
        String ret = "";
        for ( ListCode ls : userRoles )
        {
            if ( ls.getCode() == code)
            {
                ret = ls.getCodeAlpha();
                break;
            }
        }
        return ret;
    }


    public String getCenterStatusName(int code)
    {
        String ret = "";
        for ( ListCode ls : this.centerStatus )
        {
            if ( ls.getCode() == code)
            {
                ret = ls.getCodeAlpha();
                break;
            }
        }
        return ret;
    }

    public String getTshirtCodeName(int code)
    {
        String ret = "";
        for ( ListCode ls : tShirtSizes )
        {
            if ( ls.getCode() == code)
            {
                ret = ls.getCodeAlpha();
                break;
            }
        }
        return ret;
    }

    public int getTshirtCode(String name)
    {
        int ret = -1;
        for ( ListCode ls : tShirtSizes )
        {
            if ( ls.getCodeAlpha().equalsIgnoreCase(name))
            {
                ret = ls.getCode();
                break;
            }
        }
        return ret;
    }

    public String getDiscountCodeName(int code)
    {
        String ret = "";
        for ( ListCode ls : this.discountCodes )
        {
            if ( ls.getCode() == code)
            {
                ret = ls.getCodeAlpha();
                break;
            }
        }
        return ret;
    }

}
