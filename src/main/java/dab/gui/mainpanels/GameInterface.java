package dab.gui.mainpanels;

import dab.engine.newsim.AbstractSimulator;
import dab.engine.simulator.GameOverException;
import dab.gui.application.MainWindow;
import dab.gui.auxpanels.ControlPanel;
import dab.gui.auxpanels.InfoPanel;
import dab.gui.auxpanels.ObamaPanel;
import dab.gui.gamepanel.GamePanel;
import dab.gui.sound.Sounds;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.Timer;

public abstract class GameInterface extends JPanel implements KeyListener {
    
    
    private Sounds music;
    private Timer animator;
    private int counter;
    
    protected MainWindow mainWindow;
    protected JSplitPane leftPane, rightPane;


    public GameInterface (MainWindow mw) {
        this.mainWindow = mw;
  
        counter = 0;
        //FIXME: play music
        music = new Sounds("resources/music/backgroundSound.wav", true);
  
        ActionListener taskStep = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    step();
                } catch (GameOverException e) {
                    // stop the game loop when game over
                    animator.stop();
                    music.interrupt();
                    mainWindow.setGameOver(true);
                    showGameOverMenu();
                }
            }
        };
        animator = new Timer(1000/30, taskStep);
        
        setupPanels();
    }
    
    protected void start() {
        // rock and roll baby!
        addKeyListener(this);
        animator.start();
        
    }
    
    protected void setupPanes() {
        leftPane.setLeftComponent(getGamePanel());
        leftPane.setRightComponent(getObamaPanel());
        rightPane.setLeftComponent(getInfoPanel());
        rightPane.setRightComponent(getButtonPanel());
    }
    
    private void setupPanels() {       
        
        setLayout(new BorderLayout());
        
        JSplitPane topLevelSplitPane;
        
        topLevelSplitPane = new JSplitPane();
        //topLevelSplitPane.setDividerSize(5);
        
        leftPane = new JSplitPane();
        //leftPane.setDividerSize(5);
        leftPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        
        rightPane = new JSplitPane();
        //rightPane.setDividerSize(5);
        rightPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        rightPane.setResizeWeight(0.7);
        
        topLevelSplitPane.setLeftComponent(leftPane);
        topLevelSplitPane.setRightComponent(rightPane);
        add(topLevelSplitPane);  
    }

    @Override
    public void keyPressed(KeyEvent e) {    
    }

    @Override
    public void keyReleased(KeyEvent e) {
          if(e.getKeyCode()== KeyEvent.VK_ESCAPE){
                handelEscape();
            }   
    }

    @Override
    public void keyTyped(KeyEvent e) {   } //Do nothing

    protected abstract void showGameOverMenu();
    protected abstract AbstractSimulator getSimulator();
    protected abstract GamePanel getGamePanel();
    protected abstract ObamaPanel getObamaPanel();
    protected abstract InfoPanel  getInfoPanel();
    protected abstract ControlPanel getButtonPanel();
    
    protected void step() throws GameOverException {
           
            if (counter % 3 == 0) {
                getSimulator().step();
                
                getInfoPanel().update();
                getObamaPanel().update();
                getGamePanel().updateComponents();
                getButtonPanel().update();
                counter = 0;
            }
            handleMusic();
            counter++;
            requestFocus();
        
         //addKeyListener(this);
    }
    
    public void handleMusic(){    
        try {      
            if(music.isAlive() && !mainWindow.getMusic()) {
                music.stopIt();
            } else if(!music.isAlive() && mainWindow.getMusic()&&animator.isRunning()) {
                music.start();
               
            } 
        } catch (IllegalThreadStateException e) {
            //do nothing
        }       
    }

    /*public void screenUpdate() {
        
        if (getFailed("Turbine") || (simulator.getSoftFailReport().getFailMode() != FailMode.WORKING)) {
            // if there is a software failure or the turbine has failed,
            // change the control rod slider to its actual position
            // why? no idea....
            buttonPanel.setSliderValue((int) simulator.controlRodPosition().points());
        }
    }

    private boolean getFailed(String component) {
        if (simulator.listFailedComponents().length > 0) {
            for (String ChangingComponent : simulator.listFailedComponents()) {
                if (ChangingComponent.equals(component)) {
                    return true;
                }
            }
        }
        return false;
    }*/

    //@Override
    /*public void update(UserCommands command, int parameter) {
        try {
            switch (command) {
                case TURNON:
                    simulator.changePumpState(parameter, true);
                    break;
                case TURNOFF:
                    simulator.changePumpState(parameter, false);
                    break;
                case OPEN:
                    simulator.changeValveState(parameter, true);
                    break;
                case CLOSE:
                    simulator.changeValveState(parameter, false);
                    break;
                case MOVE:
                    simulator.moveControlRods(new Percentage(parameter));
                    break;
            }
        } catch (KeyNotFoundException e) {
            e.printStackTrace();
        } catch (CannotControlException e) {
        }

        screenUpdate();
    }*/

    private void handelEscape()  {
        // pause the game loop when esc is pressed
        
        animator.stop();
        music.stopIt();
        mainWindow.changeMenu(new MainMenu(mainWindow, getGamePanel()));       
        
    }

    public void stopMusic() {
        music.interrupt();
    }
    
    public void resume() {
    System.out.println("resume");
        animator.start();
        
  //      handleMusic();
    }
    
    

}
