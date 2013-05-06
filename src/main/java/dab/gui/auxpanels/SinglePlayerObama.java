/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.auxpanels;

import dab.engine.newsim.SinglePlayerSimulator;
import dab.engine.simulator.FailMode;
import dab.gui.mainpanels.GameInterface;

/**
 *
 * @author eduard
 */
public class SinglePlayerObama extends ObamaPanel {

    SinglePlayerSimulator simulator;

    public SinglePlayerObama(SinglePlayerSimulator sim) {
        super();
        this.simulator = sim;
    }

    @Override
    public void update() {
        String temp = "<html>";

        if (simulator.getReactor().coreTemperature().inKelvin() > 800) {
            temp += "WARNING, " + simulator.getUsername() + ": REACTOR CORE TEMPERATURE TOO HIGH! QUENCH IT!" + "<br>";
        }

        /*if (simulator.condenserPressure().greaterThan(GameInterface.CONDENSER_WARNING_PRESSURE)) {
            temp += "WARNING, " + simulator.getUsername() + ": CONDENSER PRESSURE TOO HIGH" + "<br>";
        }

        for (String failedComponent : simulator.listFailedComponents()) {
            if (failedComponent.equals("Pump 1")) {
                temp += "WARNING, " + simulator.getUsername() + ": The Water pump HAS FAILED<br>";

            } else if (failedComponent.equals("Pump 2")) {
                temp += "WARNING, " + simulator.getUsername() + ": The coolant pump HAS FAILED<br>";

            } else {
                temp += "WARNING, " + simulator.getUsername() + ": " + failedComponent + " HAS FAILED<br>";
            }
        }*/

        if (simulator.getSoftwareStatus() != FailMode.WORKING) {
            temp += "WARNING, " + simulator.getUsername() + ": A software failure has occured!";
        }
        temp += "</html>";

        lblWords.setText(temp);

    }
}
