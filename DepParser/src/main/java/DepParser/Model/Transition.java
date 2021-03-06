/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepParser.Model;

/**
 *
 * @author Marco Corona
 * It describes a transition as a couple action applied and state result of that transition 
 */


public class Transition {
    
    private State result;
    private ArcSystem.operation action;
    
    public Transition(State result, ArcSystem.operation action){
    
        this.result = result;
        this.action = action;
    }
    
    
    public State getState(){
        return result;
    }
    
    public ArcSystem.operation getAction(){
        return action;
    }
    
    
}
