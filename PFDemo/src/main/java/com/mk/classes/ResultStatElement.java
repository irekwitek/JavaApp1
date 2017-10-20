/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import java.io.Serializable;

/**
 *
 * @author Irek
 */
public class ResultStatElement  implements Serializable
{
    private int scorePos = 0;
    private int pctPos = 0;
    private double pctTile = 0.0;

    public ResultStatElement()
    {
    }

    public ResultStatElement( int sPos ) 
    {
        scorePos = sPos;
    }

    public ResultStatElement( int sPos, int pPos ) 
    {
        scorePos = sPos;
        pctPos = pPos;
    }


    public double getPctTile() {
        return pctTile;
    }


    public void setPctTile(double pctTile) {
        this.pctTile = pctTile;
    }
    
    public int getPctPos() {
        return pctPos;
    }

    public void setPctPos(int pctPos) {
        this.pctPos = pctPos;
    }

    public int getScorePos() {
        return scorePos;
    }

    public void setScorePos(int scorePos) {
        this.scorePos = scorePos;
    }
    
}
