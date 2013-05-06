package dab.engine.newsim;

import dab.engine.persistence.FileSystem;
import dab.engine.persistence.SaveGame;
import dab.engine.utilities.Energy;
import dab.engine.utilities.Percentage;
import dab.engine.utilities.Pressure;
import dab.engine.utilities.Temperature;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import dab.engine.newsim.PowerPlant;
import dab.engine.newsim.components.Condenser;
import dab.engine.newsim.components.Reactor;
import dab.engine.newsim.interfaces.CondenserView;
import dab.engine.newsim.interfaces.FailableObject;
import dab.engine.newsim.interfaces.PumpView;
import dab.engine.newsim.interfaces.ReactorView;
import dab.engine.newsim.interfaces.TurbineView;
import dab.engine.newsim.interfaces.ValveView;
import dab.engine.simulator.FailMode;
import dab.engine.simulator.GameOverException;

import dab.engine.simulator.SoftFailReport;
import dab.engine.simulator.UserCommands;


/**
 * The simulator API has been encapsulated within a single class - an example of the Facade
 * design pattern. This has simplified interactions between simulator and the TextInterface.
 * To avoid coupling issues associated with the Facade design pattern, the Simulator consists
 * primarily of delegated calls to its sub-components.
 *
 *
 * @author David
 */
public abstract class AbstractSimulator {

    protected PowerPlant powerPlant;
    private int difficulty;

    public AbstractSimulator() {
        powerPlant = new PowerPlant();
    }
    
    protected abstract FailureModel getFailureModel();
    
    public TurbineView getTurbine(){
        return powerPlant.getTurbine();
    }
    
    public PumpView getPump(int i) {
        return getPumps().get(i - 1);
    }
    
    public ReactorView getReactor() {
        return powerPlant.getReactor();
    }
    
    public CondenserView getCondenser() {
        return powerPlant.getCondenser();
    }
    
    public ArrayList<PumpView> getPumps() {
        ArrayList<PumpView> pumps = new ArrayList<>();
        for (PumpView p : powerPlant.getPumps()) 
            pumps.add(p);
        pumps.add(powerPlant.getCondenser().getCoolantPump());
        return pumps;
    }
    
    public ValveView getValve(int i) {
        return powerPlant.getValves().get(i - 1);
    }
    

    /**
     *
     * @return bool failed
     * @param int pump number
     */
    public boolean getPumpFailed(int pumpNum){
        return powerPlant.getPumps().get(pumpNum - 1).hasFailed();
    }

    /**
     *
     * @return String[] of saved games
     */
    public String[] listGames() {
        return FileSystem.listSaveGames();
    }

    /**
     *
     * @return String[] failed components
     */
    public String[] listFailedComponents() {
        return getFailureModel().listFailedComponents().toArray(new String[0]);
    }

    /**
     * Execute the calculations for every component
     * @throws GameOverException
     * 
     */
    public void step() throws GameOverException {
            getFailureModel().step();
        
    }
    

    public void moveControlRods(Percentage extracted) {
        powerPlant.getReactor().moveControlRods(extracted);
    }

    public void changeValveState(int valveNumber, boolean isOpen) {
        powerPlant.getValves().get(valveNumber - 1).setOpen(isOpen);
    }

    public void changePumpState(int pumpNumber, boolean isPumping) {
        powerPlant.getPumps().get(pumpNumber - 1).setStatus(isPumping);
    }

    public void repairPump(int pumpNumber) {
        powerPlant.getPumps().get(pumpNumber - 1).getFailureController().repair();
    }
    
    public void repairCondenser() {
        powerPlant.getCondenser().getFailureController().repair();
    }

    public void repairTurbine() {
        powerPlant.getTurbine().getFailureController().repair();
    }

    public Percentage controlRodPosition() {
        return powerPlant.getReactor().controlRodPosition();
    }

    public boolean valveIsOpen(int valveNum) {
        return powerPlant.getValves().get(valveNum - 1).getOpen();
    }

    public boolean pumpIsActive(int pumpNum) {
        return powerPlant.getPumps().get(pumpNum - 1).getStatus();
    }

    public Pressure reactorPressure() {
        return powerPlant.getReactor().pressure();
    }

    public Temperature reactorTemperature() {
        return powerPlant.getReactor().temperature();
    }

    public Percentage reactorWaterLevel() {
        return new Percentage(powerPlant.getReactor().getWaterLevelRatio());
    }

    public Energy energyGenerated() {
        return new Energy(powerPlant.getTurbine().outputPower());
    }

    public void setReactorToTurbine(boolean open) {
        powerPlant.getValves().get(0).setOpen(open);
    }

    public boolean getReactorToTurbine() {
        return powerPlant.getValves().get(0).getOpen();
    }

    public Temperature condenserTemperature() {
        return powerPlant.getCondenser().temperature();
    }

    public Pressure condenserPressure() {
        return powerPlant.getCondenser().pressure();
    }

    public Percentage condenserWaterLevel() {
        return powerPlant.getCondenser().waterLevel();
    }

    public boolean turbineHasFailed() {
        return powerPlant.getTurbine().hasFailed();
    }

    public ArrayList<FailableObject> components() {
        return powerPlant.getFailableComponents();
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return Software failures report
     */
    public SoftFailReport getSoftFailReport(UserCommands targetCommand, double targetParameter) {
        return getFailureModel().generateSoftwareReport(targetCommand, targetParameter);
    }
    
    public FailMode getSoftwareStatus() {
        return getFailureModel().getSoftwareFailureMode();
    }
    
    public void setDifficulty(int i){
        getFailureModel().setDifficulty(i);
    }
    
    /*public void setPlayerMode(boolean onePlayerMode){
        failureModel.setPlayerMode(onePlayerMode);
    }*/
}
