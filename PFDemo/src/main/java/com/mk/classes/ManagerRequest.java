/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import java.io.Serializable;

/**
 *
 * @author irek
 */
public class ManagerRequest implements Serializable
{
    private int requestId;
    private int centerId;
    private User user;
    private User requestorUser;
    private Leader requestorManager;
    //private Location center;
    private String comment = "";
    private String status = "";
    private String requestDate;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getRequestorUser() {
        return requestorUser;
    }

    public void setRequestorUser(User requestorUser) {
        this.requestorUser = requestorUser;
    }

    public Leader getRequestorManager() {
        return requestorManager;
    }

    public void setRequestorManager(Leader requestorManager) {
        this.requestorManager = requestorManager;
    }
/*
    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
    }
*/
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
}
