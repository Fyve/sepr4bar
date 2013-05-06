/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.newsim;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dab.engine.newsim.interfaces.FailableObject;
import dab.engine.newsim.utils.Constants;
import dab.engine.newsim.utils.RandomBuffer;
import dab.engine.simulator.FailMode;
import dab.engine.simulator.GameOverException;
import dab.engine.simulator.SoftFailReport;
import dab.engine.simulator.UserCommands;

/**
 *
 * @author eduard
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class SinglePlayerFailureModel extends FailureModel {

    private final static int MAX_DAMAGE_SINGLE = 5;
    private final static int DAMAGE_INCREASE_SINGLE = 1;
    private final static int INITIAL_HARDWARE_FAIL_INTERVAL = 5;
    private final static int MINIMUM_HARDWARE_FAIL_INTERVAL = 4;
    private final static double DEFAULT_TIMER_DECREASE = 0.1;
    private final static double DEFAULT_SOFTWARE_FAIL_INTERVAL = 6;
    private final static double MINIMUM_SOFTWARE_FAIL_INTERVAL = 10;
    //A component will have a 1 in 50 chance of failing (per second)
    //private final static double DEFAULT_FAIL_CHANCE = 1.0 / 25.0;

    @JsonProperty
    private RandomBuffer<FailableObject> failList;
    
    @JsonProperty
    private double timeLeftHardware = INITIAL_HARDWARE_FAIL_INTERVAL;
    
    @JsonProperty
    private double hardwareFailInterval = INITIAL_HARDWARE_FAIL_INTERVAL;
    
    @JsonProperty
    private double timeLeftSoftware = DEFAULT_SOFTWARE_FAIL_INTERVAL;
    
    @JsonProperty
    private double softwareFailInterval = DEFAULT_SOFTWARE_FAIL_INTERVAL;
    
    @JsonProperty
    private double softwareFailTimeRemaining = 0;
    
    @JsonProperty
    private FailMode softwareFailType = FailMode.WORKING;
    
    public SinglePlayerFailureModel(PowerPlant plant) {
        super(plant);
        setDamagesToComponents(MAX_DAMAGE_SINGLE, DAMAGE_INCREASE_SINGLE);
        failList = new RandomBuffer<>(plant.getFailableComponents());
    }

    private double getTimerDecrease() {
        return DEFAULT_TIMER_DECREASE * getDifficultyModifier();
    }
    
    @Override
    public void step() throws GameOverException {
        final double SECONDS_PER_TICK = 1.0 / Constants.TICKS_PER_SECOND;
        super.step();
        timeLeftHardware -= SECONDS_PER_TICK;
        softwareFailTimeRemaining -= SECONDS_PER_TICK;
        timeLeftSoftware -= SECONDS_PER_TICK;
        
        if (timeLeftHardware < 0) {
            hardwareFailInterval *= getTimerDecrease();
            failList.get().getFailureController().fail();
            timeLeftHardware = hardwareFailInterval;
            if (timeLeftHardware < MINIMUM_HARDWARE_FAIL_INTERVAL) {
                timeLeftHardware = MINIMUM_HARDWARE_FAIL_INTERVAL;
            }
        }
        
        if (softwareFailTimeRemaining < 0) {
            softwareFailType = FailMode.WORKING;
            softwareFailTimeRemaining = 0;
        }
        
        if (timeLeftSoftware < 0) {
            softwareFailInterval *= getTimerDecrease();
            softwareFailTimeRemaining = DEFAULT_SOFTWARE_FAIL_INTERVAL * getDifficultyModifier();
            if (timeLeftSoftware < MINIMUM_SOFTWARE_FAIL_INTERVAL) {
                timeLeftSoftware = MINIMUM_SOFTWARE_FAIL_INTERVAL;
            }
            
            double roll = dice.rollDouble();
            if (roll < 0.8) {
                softwareFailType = FailMode.UNRESPONSIVE;
            } else {
                softwareFailType = FailMode.INCORRECT;
            }
            
        }
        
    }
    
    @Override
    public SoftFailReport generateSoftwareReport(UserCommands targetCommand, double targetParameter) {
        if (softwareFailType == FailMode.UNRESPONSIVE) {
            //Unresponive commands do nothing
            return new SoftFailReport(FailMode.UNRESPONSIVE, targetCommand, targetParameter);
        } else if (softwareFailType == FailMode.INCORRECT) {

            //Incorrect commands behave as another command
            UserCommands actualCommand = UserCommands.randomCommand();
            double actualParameter = generateFailedParameter(actualCommand);
            //doFailedCommand(actualCommand, actualParameter);
            return new SoftFailReport(FailMode.INCORRECT, targetCommand, targetParameter, actualCommand, actualParameter);


        } else {
            return new SoftFailReport(FailMode.WORKING, targetCommand, targetParameter);
        }
    }

    @Override
    protected FailMode getSoftwareFailureMode() {
        return softwareFailType;
        
    }
    
}
