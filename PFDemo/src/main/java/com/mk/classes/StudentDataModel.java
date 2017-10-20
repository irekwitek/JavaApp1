/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author irek
 */
public class StudentDataModel extends ListDataModel<RegisteredStudent> implements SelectableDataModel<RegisteredStudent>, Serializable {    
  
    public StudentDataModel() {  
    }  
  
    public StudentDataModel(List<RegisteredStudent> data) {  
        super(data);  
    }  
      
    @Override  
    public RegisteredStudent getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<RegisteredStudent> students = (List<RegisteredStudent>) getWrappedData();  
          
        for(RegisteredStudent student : students) {  
            if(student.getStudentIdentificationCode().equals(rowKey)) {  
                return student;
            } else {
            }  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(RegisteredStudent student) {  
        return student.getStudentIdentificationCode();  
    }  
}