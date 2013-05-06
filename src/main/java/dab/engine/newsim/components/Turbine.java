/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.newsim.components;

import dab.engine.newsim.interfaces.TurbineView;
import dab.engine.newsim.utils.BlockedHydroState;
import dab.engine.newsim.utils.HydraulicState;
import dab.engine.newsim.utils.Steam;
import dab.engine.newsim.utils.Matter;

/**
 *
 * @author eduard
 */
public class Turbine extends FailableComponent implements TurbineView {
    
    double totalEnergyGenerated;
    double energyGeneratedLast;    
    
    public Turbine(String name) {
        super(name);
        totalEnergyGenerated = 0;
        energyGeneratedLast  = 0;
    }
    
    public double getLastEnergy() {
        return energyGeneratedLast;
    }
    
    
    
    public void step() {
       ; // FIXME: don't do nothing currently
    }
    
    @Override
    protected HydraulicState getHydroState() {
        if (hasFailed() || outputComponent.getHydroState() instanceof BlockedHydroState) {
            return new BlockedHydroState();
        } else {
            return outputComponent.getHydroState();
        }
    }

    /*
     * FIXME: magicnumber: * 100
     */
    @Override
    protected void receiveMatter(Matter m) {
        if (hasFailed()) throw new RuntimeException("turbine failed, shouldn't receive any steam");
        Steam steamReceived = (Steam)m;
        energyGeneratedLast = steamReceived.getMass() * 100;
        totalEnergyGenerated += energyGeneratedLast;
        // even maybe remove some of the actual energy?
        outputComponent.receiveMatter(m);
    }

    @Override
    public double outputPower() {
        return totalEnergyGenerated;
    }

    @Override
    public void reducePower( double gunshotReduction) {
        totalEnergyGenerated -= gunshotReduction;
    }

}
