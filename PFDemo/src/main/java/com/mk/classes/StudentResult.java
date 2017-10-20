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
public class StudentResult extends RegisteredStudent implements Serializable
{
    private String locationName = "";
    private String locationCity = "";
    private String locationState = "";
    private double score = 0.0;
    private double percentage = 0.0;
    private double statePercentile = 0.0;
    private int statePlace = 0;
    private int nationPlace = 0;
    private double nationPercentile = 0.0;
    private String calculationState = "";

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationState() {
        return locationState;
    }

    public void setLocationState(String locationState) {
        this.locationState = locationState;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getStatePercentile() {
        return statePercentile;
    }

    public void setStatePercentile(double statePercentile) {
        this.statePercentile = statePercentile;
    }

    public int getStatePlace() {
        return statePlace;
    }

    public void setStatePlace(int statePlace) {
        this.statePlace = statePlace;
    }

    public int getNationPlace() {
        return nationPlace;
    }

    public void setNationPlace(int nationPlace) {
        this.nationPlace = nationPlace;
    }

    public double getNationPercentile() {
        return nationPercentile;
    }

    public void setNationPercentile(double nationPercentile) {
        this.nationPercentile = nationPercentile;
    }

    public String getCalculationState() {
        return calculationState;
    }

    public void setCalculationState(String calculationState) {
        this.calculationState = calculationState;
    }
    
    public String showStatePlace() {
        if (this.getCalculationState() != null && !this.getCalculationState().equals("")) {
            return this.calculationState + "-" + this.getStatePlace();
        } else {
            if (this.getLocationState() != null && !this.getLocationState().equals("")) {
                return this.getLocationState() + "-" + this.getStatePlace();
            } else {
                return "" + this.getStatePlace();
            }
        }
    }

}
