/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author irek
 */
public class CenterManagerRequest implements Serializable 
{
    private Location center;
    private ArrayList<ManagerRequest> managerRequests;

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public ArrayList<ManagerRequest> getManagerRequests() {
        return managerRequests;
    }

    public void setManagerRequests(ArrayList<ManagerRequest> managerRequests) {
        this.managerRequests = managerRequests;
    }
    
}
