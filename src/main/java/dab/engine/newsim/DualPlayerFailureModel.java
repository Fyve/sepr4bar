/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.newsim;

import dab.bigBunny.Environment;
import dab.engine.simulator.FailMode;
import dab.engine.simulator.GameOverException;
import dab.engine.simulator.SoftFailReport;
import dab.engine.simulator.UserCommands;

/**
 *
 * @author eduard
 */
public class DualPlayerFailureModel extends FailureModel {
    
    private final static int MAX_DAMAGE_DUAL = 1;    
    private final static int DAMAGE_INCREASE_DUAL = 2;
    
    //in two player mode failures have about 3 times less chance to occur randomly
    private final static double DEFAULT_FAIL_CHANCE = 1 / 1500;
    
    Environment environment = null;
    
    public DualPlayerFailureModel(PowerPlant plant) {
        super(plant);
        setDamagesToComponents(MAX_DAMAGE_DUAL, DAMAGE_INCREASE_DUAL);
    }
    
    // link up with an environment
    public void setEnvironment(Environment env) {
        if (environment != null) {
            throw new RuntimeException("Can not set environment more than once!");
        } else {
            this.environment = env;
        }
    } 
    
    @Override
    public void step() throws GameOverException {
        if (environment == null) {
            throw new RuntimeException("Environment must be set before starting DualPlayerMode");
        } else {
            super.step();
        }
    }

    @Override
    protected FailMode getSoftwareFailureMode() {
        if (environment.getSoftwareFailure()) {
            return FailMode.UNRESPONSIVE;
        } else {
            return FailMode.WORKING;
        }
    }

    @Override
    public SoftFailReport generateSoftwareReport(UserCommands targetCommand, double targetParameter) {
        return new SoftFailReport(getSoftwareFailureMode(), targetCommand, targetParameter);
    }
}
