/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.StudentResult;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author irek
 */
@ManagedBean
@ViewScoped
public class ResultsBean implements Serializable {


    @ManagedProperty(value="#{bestStudentsBean}")
    private BestStudentsBean bestStudentsBean;

    private ArrayList<StudentResult> filteredNationStudents;
    private ArrayList<StudentResult> filteredStatesStudents;

    private int tableToShow = 1;
    private int nationLevelToShow = 0;
    private int statesLevelToShow = 0;
    
    /**
     * Creates a new instance of ResultsBean
     */
    public ResultsBean() 
    {
    }

    public BestStudentsBean getBestStudentsBean() {
        return bestStudentsBean;
    }

    public void setBestStudentsBean(BestStudentsBean bestStudentsBean) {
        this.bestStudentsBean = bestStudentsBean;
    }


    public int getTableToShow() {
        return tableToShow;
    }

    public void setTableToShow(int tableToShow) {
        this.tableToShow = tableToShow;
    }

    public int getNationLevelToShow() {
        return nationLevelToShow;
    }

    public void setNationLevelToShow(int nationLevelToShow) {
        this.nationLevelToShow = nationLevelToShow;
    }

    public int getStatesLevelToShow() {
        return statesLevelToShow;
    }

    public void setStatesLevelToShow(int statesLevelToShow) {
        this.statesLevelToShow = statesLevelToShow;
    }

    public ArrayList<StudentResult> getFilteredNationStudents() {
        return filteredNationStudents;
    }

    public void setFilteredNationStudents(ArrayList<StudentResult> filteredNationStudents) {
        this.filteredNationStudents = filteredNationStudents;
    }

    public ArrayList<StudentResult> getFilteredStatesStudents() {
        return filteredStatesStudents;
    }

    public void setFilteredStatesStudents(ArrayList<StudentResult> filteredStatesStudents) {
        this.filteredStatesStudents = filteredStatesStudents;
    }

    public void showNationalWiners(ActionEvent actionEvent)
    {
        this.tableToShow = 1;
    }

    public void showStatesWiners(ActionEvent actionEvent)
    {
        this.tableToShow = 2;
    }
    
    public void showAwards(ActionEvent actionEvent)
    {
        this.tableToShow = 3;
    }
    
    public ArrayList<StudentResult> getNationLevelStudents()
    {
        return bestStudentsBean.getNationStudents()[this.nationLevelToShow];
    }
    
    public ArrayList<StudentResult> getStatesLevelStudents()
    {
        return bestStudentsBean.getStatesStudents()[this.statesLevelToShow];
    }
}
