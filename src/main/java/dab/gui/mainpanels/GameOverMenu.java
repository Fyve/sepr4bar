/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.mainpanels;

import dab.gui.application.MainWindow;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Aiste
 */
public class GameOverMenu extends MenuHandler implements KeyListener{
    
    private boolean playerTwoLost;
    private ImageIcon icon;
    private MainWindow mainWindow;
    private JLayeredPane invoker;
    private String text;
    
    public GameOverMenu(MainWindow mw, final JLayeredPane invoker,boolean playerOneMode, boolean playerTwoLost, String power){
      super(invoker);
      this.mainWindow = mw; 
      this.invoker = invoker;
        
       setLayout(new BoxLayout(this,BoxLayout.X_AXIS)); 
        
        this.playerTwoLost  = playerTwoLost; 
        if(playerTwoLost) {
            icon = new ImageIcon("src/main/resources/dab/gui/deadBunny.png");
        } else {          
            icon = new ImageIcon("src/main/resources/dab/gui/endGame.gif");   
        }
       
        Image img = icon.getImage();
        //load the game over gif and scale it to fit in the game over dialog
        Image newimg = img.getScaledInstance(330, 300, java.awt.Image.SCALE_DEFAULT);
        ImageIcon newIcon = new ImageIcon(newimg);
        JLabel image = new JLabel(newIcon);
           
        add(image);
        add(Box.createRigidArea(new Dimension(10,0)));
 
        addKeyListener(this);
        requestFocus();
        
        
        if(playerOneMode){
            text = "The Reactor has failed, " + mainWindow.getUserName() + "! You generated "
                + power + " of power.";
        } else if (!playerTwoLost){
            text = "The Reactor has not survived the bunny attacks! " + mainWindow.getUserName() + ", you generated "
                + power + " of power.";    
        } else {
            text = "The bunny has died! " + mainWindow.getUserName2() + ", you lost ";  
        }
        
        JLabel forTheText = new JLabel("<html><p>" +text + " Press ENTER or ESCAPE to continue </p></html>");        
       
        add(forTheText, BorderLayout.EAST);
         setBounds(200, 200, 600, 300);
         
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_ESCAPE || e.getKeyCode()== KeyEvent.VK_ENTER){
                mainWindow.changeMenu(new MainMenu(mainWindow, invoker));
            }       
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }
}
