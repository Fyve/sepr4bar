package dab.engine.newsim;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dab.engine.newsim.interfaces.FailableObject;
import dab.engine.newsim.utils.Constants;
import dab.engine.newsim.utils.RandomBuffer;
import dab.engine.newsim.utils.RandomGenerator;
import dab.engine.simulator.FailMode;
import dab.engine.simulator.GameOverException;
import dab.engine.simulator.SoftFailReport;
import dab.engine.simulator.UserCommands;
import static dab.engine.simulator.UserCommands.CLOSE;
import static dab.engine.simulator.UserCommands.MOVE;
import static dab.engine.simulator.UserCommands.OPEN;
import static dab.engine.simulator.UserCommands.TURNOFF;
import static dab.engine.simulator.UserCommands.TURNON;
import dab.engine.utilities.Pressure;
import static dab.engine.utilities.Units.percent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Manages software and hardware failures.
 *
 * The FailureModel is responsible for inducing software and hardware failures
 * in the plant.
 *
 * Software failures are currently unimplemented; however, as all calls to the
 * PhysicalModel are delegated through the FailureModel, it is easy to alter
 * this delegation in arbitrary ways to simulate control and/or reporting
 * failures.
 *
 * Hardware failures are determined by inspecting the PhysicalModel to determine
 * the status, wear, and operating conditions of the various components, and
 * then instructing the PhysicalModel to mark certain components as having
 * failed.
 *
 * Failures are constrained to one per timestep, which is enabled by the
 * delegation of the step() method through the FailureModel.
 *
 * @author Marius Dumitrescu
 */

public abstract class FailureModel {
    
    protected SoftFailReport lastFailReport = new SoftFailReport();
    
    protected RandomGenerator dice = new RandomGenerator();
    
    @JsonProperty
    protected PowerPlant powerPlant;
    
    @JsonProperty
    private int difficulty;
    ;

    public FailureModel(PowerPlant plant) {
        this.powerPlant = plant;
    }

     /**
     * Step the PhysicalModel and determine any failures.
     *
     * Also implements reactor safety rules.
     *
     */
    public void step() throws GameOverException {
        powerPlant.step();
        
        // implement turbine safety rules
        if (powerPlant.getTurbine().hasFailed()) {
            powerPlant.getReactor().moveControlRods(percent(0));
        }
    }
    
    PowerPlant getPowerPlant() {
        return powerPlant;
    }
    
    protected final void setDamagesToComponents(int maxDamage, int damageIncrease) {
        for(FailableObject component : powerPlant.getFailableComponents()){
            component.getFailureController().setDamageValues(maxDamage, damageIncrease);
        }     
    }
    
    protected double getDifficultyModifier() {
        return difficulty;
    }
    
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    
    /*protected abstract int getTicksUntilComponentFail();
    protected abstract void resetHardFailTime();
    protected abstract void updateSoftwareFailureState();
    */
    protected abstract FailMode getSoftwareFailureMode();
    public abstract SoftFailReport generateSoftwareReport(UserCommands targetCommand, double targetParameter);
    
    /*protected int getTicksPassed() {
        return ticksPassed;
    }*/
    

    
    // names of failed components
    public ArrayList<String> listFailedComponents() {
        ArrayList<String> out = new ArrayList<>();
        
        for (FailableObject fo : powerPlant.getFailableComponents()) {
            if (fo.getFailureController().hasFailed()) {
                out.add(fo.getName());
            }
        }

        return out;

    }

    /**
     * @param UserCommand
     * @param double target
     * @return boolean (whether a software failure has occurred)
     */
    //public abstract boolean softFailCheck(UserCommands targetCommand, double targetParameter); 
    
    /* {
        if (softFailCheck(targetCommand, targetParameter) == false) {
            return lastFailReport;
        } else {
            return new SoftFailReport(FailMode.WORKING, targetCommand, targetParameter);
        }
    }*/

    /**
     * @param UserCommand
     * @return double (a random failure for each command)
     */
    protected double generateFailedParameter(UserCommands command) {
        Random rand = new Random();
        switch (command) {
            case TURNON:
            case TURNOFF:
                //System.out.println(rand.nextInt(2) + 1);
                return rand.nextInt(2) + 1;
            case OPEN:
            case CLOSE:
                return rand.nextInt(2) + 1;
            case MOVE:
                return rand.nextInt(51);
            default:
                return 0;
        }
    }

    /**
     * @param UserCommand
     * @param double paramater Execute the failed command to the command
     * specified with the parameter specified
     */
    /*public void doFailedCommand(UserCommands command, double parameter) {
        try {
            switch (command) {
                case TURNON:
                    controller.changePumpState((int) parameter, true);
                    break;
                case TURNOFF:
                    controller.changePumpState((int) parameter, false);
                    break;
                case OPEN:
                    controller.changeValveState((int) parameter, true);
                    break;
                case CLOSE:
                    controller.changeValveState((int) parameter, false);
                    break;
                case MOVE:
                    try {
                        controller.moveControlRods(new Percentage(parameter));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (KeyNotFoundException e) {
            e.printStackTrace();
        } catch (CannotControlException e) {
            //TODO:Might need to do something more here, not sure.
        }
    }*/

    /**
     * @return Software failure report
     */
    
    
    /*public SoftFailReport getSoftFailReport() {
        if (lastFailReport == null) {
            return new SoftFailReport();
        } else {
            return lastFailReport.getCopy();
        }
    }*/
    

    
    /*public void setPlayerMode(boolean onePlayerMode){
        this.onePlayerMode = onePlayerMode;
    }*/
}
