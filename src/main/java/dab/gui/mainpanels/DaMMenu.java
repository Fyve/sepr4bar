/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.mainpanels;

/**
 *
 * @author eduard
 */

import dab.engine.simulator.Simulator;
import dab.gui.application.MainWindow;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;


/**
 * The main menu for the game, has new game, load game, two_player and quit buttons.
 * New game points onto an instance of Player.
 * Load Game displays a menu of existing save games, and loads a selected game.
 * Help ...
 * Quit exits the JRE.
 * 
 * @author Team Haddock
 *
 */
public class DaMMenu extends JPanel {
    MainWindow mainWindow;
    
    public DaMMenu(MainWindow mw) {
        this.mainWindow = mw;
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        JButton new_game = new JButton("new game");
        new_game.setContentAreaFilled(true);
        new_game.setBorderPainted(true);
        //new_game.setBorder(BorderFactory.createEmptyBorder());
        //new_game.setContentAreaFilled(false);
        //new_game.setBorderPainted(false);
        //new_game.setAlignmentX(Component.CENTER_ALIGNMENT);
        new_game.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                Simulator sim = new Simulator();
                sim.setUsername("muah");
                mainWindow.startSinglePlayer(sim);
            }
        });

        /*JButton load_game = new JButton();
        load_game.setContentAreaFilled(false);
        load_game.setBorderPainted(false);
        load_game.setAlignmentX(Component.CENTER_ALIGNMENT);*/


        JButton two_player = new JButton("bunny rabbit");
        two_player.setContentAreaFilled(true);
        two_player.setBorderPainted(true);
        //help.setAlignmentX(Component.CENTER_ALIGNMENT);
        two_player.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                
                mainWindow.startTwoPlayer();
            }
        });
        
        
        JButton options = new JButton("options");
        options.setContentAreaFilled(true);
        options.setBorderPainted(true);
        //help.setAlignmentX(Component.CENTER_ALIGNMENT);
        options.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                
                mainWindow.showOptions();
            }
        });
        
        
       
        
        /*
        JButton exit_game = new JButton();
        exit_game.setContentAreaFilled(false);
        exit_game.setBorderPainted(false);
        exit_game.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit_game.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        add(Box.createRigidArea(new Dimension(0,280)));
        // add all of the buttons to the Menu
        add(new_game);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(load_game);
        add(two_player);
        add(exit_game);
        setBackground(Color.BLACK);
        setVisible(true);*/
        setBackground(Color.BLACK);
        add(new_game);
        add(two_player);
        add(options);
    }
}
