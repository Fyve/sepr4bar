/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.newsim;

import dab.bigBunny.Environment;
import dab.engine.simulator.GameOverException;

/**
 *
 * @author eduard
 */
public class TwoPlayerSimulator extends AbstractSimulator {
    private String firstPlayerName, secondPlayerName;
    private DualPlayerFailureModel failureModel;
    
    public TwoPlayerSimulator() {
        super();
        firstPlayerName = "";
        secondPlayerName = "";
        failureModel = new DualPlayerFailureModel(powerPlant);
    }
    
    public TwoPlayerSimulator(String name1, String name2) {
        this();
        this.firstPlayerName = name1;
        this.secondPlayerName = name2;
    }

    public void setEnvironment(Environment env) {
        failureModel.setEnvironment(env);
    }
    
    public void setUsername1(String name) {
        firstPlayerName = name;
    }
    
    public void setUsername2(String name) {
        secondPlayerName = name;
    }
    
    public String getUsername1() {
        return firstPlayerName;
    }
    
    public String getUsername2() {
        return secondPlayerName;
    }

    @Override
    protected FailureModel getFailureModel() {
        return failureModel;
    }
}
