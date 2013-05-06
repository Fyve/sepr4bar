package dab.gui.auxpanels;

import dab.engine.newsim.AbstractSimulator;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * The button panel contains the basic reactor controls,
 * it is initialised to a fixed layout.
 * @author Team Haddock
 *
 */
public class ControlPanel extends JPanel {
	private dab.gui.auxpanels.PumpButton  btnPump1, btnPump2;
    private dab.gui.auxpanels.ValveButton btnValve1, btnValve2;
	private ControlRodSlider controlRodSlider;
	private JLabel pump1_label;
	private JLabel pump2_label;
	private JLabel valve1_label;
	private JLabel valve2_label;
	private JLabel rods_label;
        private AbstractSimulator simulator;
	private BufferedImage background;
    
	/**
	 * Initialises the ControlPanel to a standard layout.
	 */
	public ControlPanel(AbstractSimulator sim){
        simulator = sim;
        
        setLayout(new BorderLayout());
        JSplitPane topPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane buttonPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel subButtonPanel = new JPanel(new GridLayout(2, 2));
        JPanel bottomPanel = new JPanel();
        //JPanel sliderPanel = new JPanel();
        //sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
     
        
        topPane.setDividerSize(0);
        buttonPane.setDividerSize(0);
        
        topPane.setOpaque(false);
        subButtonPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        buttonPane.setOpaque(false);
        
		btnPump2 = new PumpButton(sim.getPump(2), "Coolant Pump");
        subButtonPanel.add(btnPump2);  
		btnPump1 = new dab.gui.auxpanels.PumpButton(sim.getPump(1), "Water Pump");
        subButtonPanel.add(btnPump1);
		btnValve2 = new dab.gui.auxpanels.ValveButton(sim.getValve(2), "Condenser Valve");
        subButtonPanel.add(btnValve2);      
		btnValve1 = new dab.gui.auxpanels.ValveButton(sim.getValve(1), "Reactor Valve");
        subButtonPanel.add(btnValve1);
        
        buttonPane.setLeftComponent(subButtonPanel);
        buttonPane.setRightComponent(bottomPanel);
        bottomPanel.add(new QuenchButton(sim.getReactor()));
        buttonPane.setResizeWeight(0.5);
        
        /*rods_label = new JLabel("Control Rods");
        rods_label.setBounds(25, 40, 100, 30);
        add(rods_label);*/
        controlRodSlider = new ControlRodSlider(sim.getReactor());
        
        //sliderPanel.add(new JLabel("Rods"));
        //sliderPanel.add(controlRodSlider);
        topPane.setLeftComponent(controlRodSlider);
        topPane.setRightComponent(buttonPane);
        topPane.setResizeWeight(0.4);
        
       add(topPane);
		
        try {
            background = ImageIO.read(new File("src/main/resources/dab/gui/panel.png"));
        } catch (Exception e) {
            System.err.println("Image not found");
        }
       
	}
    
    public void update() {
        btnPump1.update();
        btnPump2.update();
        btnValve1.update();
        btnValve2.update();
        
    }
	
	@Override
        public void paintComponent(Graphics g) {
        super.paintComponent(g);
       // Image img = new ImageIcon("src/main/resources/dab/gui/panel.png").getImage();
         Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(background, 0, 0, this.getWidth(), this.getHeight(),  null);

    }
}
