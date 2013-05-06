/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.newsim.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import dab.engine.newsim.interfaces.ReactorView;
import dab.engine.newsim.utils.AverageBuffer;
import dab.engine.newsim.utils.BlockedHydroState;
import dab.engine.newsim.utils.Constants;
import dab.engine.newsim.utils.Steam;
import dab.engine.newsim.utils.HydraulicState;
import dab.engine.newsim.utils.Ratio;
import dab.engine.newsim.utils.ContainerHydroState;
import dab.engine.newsim.utils.Water;
import dab.engine.newsim.utils.Kilograms;
import dab.engine.newsim.utils.Matter;
import dab.engine.simulator.CannotRepairException;
import dab.engine.simulator.GameOverException;
import dab.engine.utilities.Percentage;
import dab.engine.utilities.Pressure;
import dab.engine.utilities.Temperature;

/**
 *
 * @author eduard
 */
public class Reactor extends Container implements ReactorView {


    private static final double INIT_WATER_TEMP = Constants.ROOM_TEMP + 40;
    private static final double EXCESSWATER_THRESHOLD = 0.8; // always keep the water level below 80% of the volume
    private static final double INITIAL_WATER_RATIO = 0.5;
    private static final double CORE_MIN_HEIGHT_RATIO = 0.1;
    private static final double CORE_MAX_HEIGHT_RATIO = 0.3;
    private static final double QUENCH_PROPORTION = 0.3; // 30% of the reactor will be filled with cold water
    private static final double ROD_SPEED = 0.08 / Constants.TICKS_PER_SECOND;
    
    @JsonProperty
    private boolean hasBeenQuenched, quenchedQueued;
    
    @JsonProperty
    private double targetRodPosition;
    
    @JsonProperty
    private ReactorCore core;
   

    public Reactor(String name, double volume, double area) {
        super(
                name,
                new Water(INIT_WATER_TEMP, new Kilograms((volume * INITIAL_WATER_RATIO) * Water.getDensityAt(INIT_WATER_TEMP))),
                new Steam(Constants.ROOM_TEMP, (int) ((volume * (1 - INITIAL_WATER_RATIO)) * Constants.NORMAL_PARTICLES_PER_VOLUME_STEAM)),
                area,
                volume / area);
        core = new ReactorCore();
        hasBeenQuenched = false;
        quenchedQueued = false;
        targetRodPosition = 0;
    }

    @Override
    public double getWaterMass() {
        return water.getMass();
    }
    
    // calculate the equilibrium pressure of the steam in this container and the one described by hydroState
    private double getEqualizedSteamPressure(HydraulicState hydroValue) {
        double v1 = getCompressibleVolume(), v2 = hydroValue.getCompressibleVolume();
        double p1 = getPressure(), p2 = hydroValue.getPressure();
        return (v1 * p1 + v2 * p2) / (v1 + v2);
    }
    
    private void condenseSteam() {
        // if pressure is smaller than atmosferic one, then we don't condense anything,
        // otherwise we condense such that we reach the boiling point at that pressure
        if (getPressure() > Constants.ATMOSPHERIC_PRESSURE) {
            double boilingPoint = Water.getBoilingTemperature(getPressure() * 0.98);
            if (boilingPoint > steam.getTemperature()) { // remove steam such that it equalizes to the boiling point
                double newSteamPressure = Math.max(Water.getBoilingPressure(steam.getTemperature()), Constants.ATMOSPHERIC_PRESSURE);


                int newQuantity = steam.getParticlesAtState(newSteamPressure, getCompressibleVolume());
                int deltaQuantity = steam.getParticleNr() - newQuantity;
                System.out.println("Condensed in reactor " + deltaQuantity);
                steam.remove(deltaQuantity);
                getWater().add(new Water(steam.getTemperature(), deltaQuantity));
            }
        }
    }
    
    private Ratio getCoreSubmersedLevel() {
        final double CORE_HEIGHT_RATIO = CORE_MAX_HEIGHT_RATIO - CORE_MIN_HEIGHT_RATIO;
        if (getWaterLevelRatio() < CORE_MAX_HEIGHT_RATIO) {
            if (getWaterLevelRatio() > CORE_MIN_HEIGHT_RATIO) {
                return Ratio.convertFrom((getWaterLevelRatio() - CORE_MIN_HEIGHT_RATIO) / CORE_HEIGHT_RATIO);
            } else {
                return Ratio.CONSTANT_0;
            }
        } else {
            return Ratio.CONSTANT_1;
        }
    }
    
    private void discardExcessWater() {
        if (getWater().getVolume() > getTotalVolume() * EXCESSWATER_THRESHOLD) {
            double dm = getWater().getDensity() * (getWater().getVolume() - EXCESSWATER_THRESHOLD * getTotalVolume());
            System.out.println("Too much water in reactor! Discarding " + dm + " kg");

            getWater().remove((int) (getWater().getParticlesPerKilo() * dm));
        }
    }

    private void equalizePressure() {
        for (int i = 1; i <= 5; ++i) {
            //System.out.println(i);
            // equalize

            HydraulicState otherHydro = getOutputComponent().getHydroState();
            if (otherHydro instanceof BlockedHydroState) {
                return; // don't have anything to equalize
            }
            double equalPressure = getEqualizedSteamPressure(otherHydro);
            

            // if the resulting pressure is greater it means
            // stuff should arrive at us, but that is not allowed (send stuff only one way)
            // (equivalent of a valve basically)
            if (equalPressure < getPressure()) { // send stuff
                int qty = steam.getParticlesAtState(equalPressure, getCompressibleVolume());
                int deltaAmmount = steam.getParticleNr() - qty;
                //System.out.println(String.format("R: Before P: %f\tOP: %f", getPressure(), equalPressure));
                outputComponent.receiveMatter(new Steam(steam.getTemperature(), deltaAmmount));
                steam.remove(deltaAmmount);
                //System.out.println(String.format("R: After P: %f\tOP: %f", getPressure(), getEqualizedSteamPressure(getOutputComponent().getHydroState())));
                //System.out.println(getPressure());
            } else {
                break;
            }
        }
    }

    @Override
    protected void receiveMatter(Matter m) {
        getWater().add((Water) m);
        discardExcessWater();
        //equalizePressure();
    }

    @Override
    protected HydraulicState getHydroState() {
        // FIXME: don't really need containerHydroState, just hydroState would suffice
        return new ContainerHydroState(getBottomPressure(), getCompressibleVolume(), area);
    }
    
    private void updateRodPosition() {
        if (core.getRodPosition() < targetRodPosition) {
            core.setRodPosition(new Ratio(Math.min(targetRodPosition, core.getRodPosition() + ROD_SPEED)));
        } else {
            core.setRodPosition(new Ratio(Math.max(targetRodPosition, core.getRodPosition() - ROD_SPEED)));
        }
    }
    
    public void step() throws GameOverException {
        updateRodPosition();
        // condense any super-heated steam
        condenseSteam();
        
        // heatup water, convert some of it into steam
        steam.add(getWater().addEnergy(core.getEnergyPerTick(getCoreSubmersedLevel()), steam.getPressure(getCompressibleVolume())));

        if (quenchedQueued) {
            // water ad 280 degrees. That should settle things.
            System.out.println(Constants.WATER_PARTICLES_PER_KILOGRAM);
            double quenchWaterVolume = getTotalVolume() * QUENCH_PROPORTION;
            double quenchWaterMass = quenchWaterVolume * Constants.NORMAL_DENSITY_WATER;
            water.add(new Water(280, (int)(quenchWaterMass * Constants.WATER_PARTICLES_PER_KILOGRAM)));
            hasBeenQuenched = true;
            quenchedQueued = false;
        }
        
        discardExcessWater();
        equalizePressure();
        
        // heatup the core (only in case of low water level. eg. not fully submersed)
        core.step(getCoreSubmersedLevel(), getWater());  
    }

    @Override
    public String toString() {
        return "R: " + super.toString();
    }

    //<editor-fold desc="implemented interfaces">
    @Override
    public void quench() {
        if (!hasBeenQuenched)
            quenchedQueued = true;
    }
    
    @Override
    public Temperature temperature() {
        return new Temperature(water.getTemperature());
    }
    
    @Override
    public Temperature coreTemperature() {
        return new Temperature(core.getTemperature());
    }
    
    @Override
    public void moveControlRods(Percentage extracted) {
        targetRodPosition = extracted.ratio();
    }

    @Override
    public Percentage controlRodPosition() {
        return new Percentage(core.getRodPosition());
    }

    @Override
    public boolean hasFailed() {
        return false;
    }

    @Override
    public void fixDamage() {
        ;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public void fail(int dmg) {
        ;
    }
    
    //</editor-fold>
}
