package dab.gui.auxpanels;

import dab.engine.newsim.AbstractSimulator;
import dab.engine.simulator.FailMode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The InfoPanel contains a text area for holding important information about
 * the system, such as temperatures, pressures and power levels.
 *
 * @author Team Haddock
 *
 */
public class InfoPanel extends JPanel {

    private AbstractSimulator simulator;
    private JLabel screenText;
    private Image background;

    public InfoPanel(AbstractSimulator simulator) {
        this.simulator = simulator;
        
        setLayout(new BorderLayout());
        screenText = new JLabel();
        screenText.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
        

        screenText.setForeground(Color.GREEN);
        screenText.setVerticalAlignment(SwingConstants.CENTER);

        screenText.setBackground(Color.BLACK);

        screenText.setBorder(BorderFactory.createMatteBorder(25, 25, 18, 18, new ImageIcon("src/main/resources/dab/gui/panel.png")));

        screenText.setOpaque(true);
        screenText.setHorizontalAlignment(SwingConstants.CENTER);
        screenText.setHorizontalTextPosition(SwingConstants.RIGHT);
        
        add(screenText);
    }

    public void update() {
        if (simulator.getSoftwareStatus() != FailMode.WORKING) {
            screenText.setText("DI9087W43JLSD[]");
        } else {
            screenText.setText(
                    "<html>Control Rod Position " + simulator.controlRodPosition().toString() + "<br>"
                    + "<br>"
                    + "R Water Level " + simulator.reactorWaterLevel() + "<br>"
                    + "R Temperature " + simulator.reactorTemperature() + "<br>"
                    + "R Core temperature" + simulator.getReactor().coreTemperature() + "<br>"
                    + "R Pressure " + simulator.reactorPressure() + "<br>"
                    + "R Bottom Pressure" + (int) simulator.getReactor().getBottomPressure() + "<br>"
                    + "R Water mass" + (int) (simulator.getReactor().getWaterMass() * 10) + "<br>" + "<br>"
                    + "C Water Level " + simulator.condenserWaterLevel() + "<br>"
                    + "C Temperature " + simulator.condenserTemperature() + "<br>"
                    + "C Pressure " + simulator.condenserPressure() + "<br>"
                    + "C Bottom Pressure" + (int) simulator.getCondenser().getBottomPressure() + "<br>"
                    + "C Water Mass" + (int) (simulator.getCondenser().getWaterMass() * 10) + "<br>"
                    + "ENERGY GENERATED " + simulator.energyGenerated() + "</html>");
        }
    }
}
