/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mk.classes;

import java.io.Serializable;

/**
 *
 * @author giw
 */
public class ListCode implements Serializable 
{

    private int code;
    private String codeAlpha;
    private String description;

    /**
     * Get the value of description
     *
     * @return the value of description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the value of description
     *
     * @param description new value of description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Get the value of codeAlpha
     *
     * @return the value of codeAlpha
     */
    public String getCodeAlpha()
    {
        return codeAlpha;
    }

    /**
     * Set the value of codeAlpha
     *
     * @param codeAlpha new value of codeAlpha
     */
    public void setCodeAlpha(String codeAlpha)
    {
        this.codeAlpha = codeAlpha;
    }

    /**
     * Get the value of code
     *
     * @return the value of code
     */
    public int getCode()
    {
        return code;
    }

    /**
     * Set the value of code
     *
     * @param code new value of code
     */
    public void setCode(int code)
    {
        this.code = code;
    }

}
