/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.mainpanels;

import com.fasterxml.jackson.core.JsonProcessingException;
import dab.engine.newsim.SinglePlayerSimulator;
import dab.engine.persistence.FileSystem;
import dab.gui.application.MainWindow;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

/**
 *
 * @author Aiste
 */
public class MainMenu extends MenuHandler{
    private MainWindow mainWindow;
    private JLayeredPane invoker;
    
    public MainMenu(MainWindow mw, final JLayeredPane invoker) {
        
        super(invoker);        
        this.mainWindow = mw;
        this.invoker = invoker;       
           
        JButton one_player = new JButton(new ImageIcon("src/main/resources/dab/gui/Buttons/onePlayer.png") {});         
        one_player.setBackground(Color.black);
        one_player.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
               startGame(true);
            }
        });

        JButton two_player = new JButton(new ImageIcon("src/main/resources/dab/gui/Buttons/twoPlayer.png"));
        two_player.setBackground(Color.black);
        two_player.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){                
                startGame(false);
            }
        });
        
       
        JButton save_menu = new JButton();
        save_menu.setBackground(Color.black);
        save_menu.setIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/save.png"));
        save_menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mainWindow.saveGame();
                    JOptionPane.showMessageDialog(null, "Game Saved");
                } catch (JsonProcessingException e1) {
                    e1.printStackTrace();
                }

            }
        });
        
         
            JButton load_menu = new JButton();            
            load_menu.setBackground(Color.black);
            load_menu.setIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/load.png"));
            load_menu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<String> saved_games_array = new ArrayList<String>();
                    String[] saved_games = FileSystem.listSaveGames();
                    for (String game : saved_games) {
                        String[] bits = game.split("\\.");
                        game = game.replace(".", "?");
                        Timestamp t = new Timestamp(Long.parseLong(bits[3]));
                        Date d = new Date(t.getTime());
                        SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        //an array of the saved games in the format : username/date of save
                        saved_games_array.add(bits[2] + " " + date.format(d));
                    }
                    JButton save_menu = new JButton();
                    save_menu.setBackground(Color.black);
                    save_menu.setIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/load.png"));

                    //a jlist of saved games to be used
                    final JList sampleJList = new JList(saved_games_array.toArray(new String[saved_games_array.size()]));
                    sampleJList.setVisibleRowCount(8);
                    //make a the jlist scrollable
                    JScrollPane listPane = new JScrollPane(sampleJList);

                    final JPopupMenu popupMenu = new JPopupMenu();

                    popupMenu.setBorder(BorderFactory.createEmptyBorder());
                    JButton load = new JButton("load");
                    load.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //load the selected game
                            SinglePlayerSimulator simulator = new SinglePlayerSimulator();
                            simulator.loadGame(sampleJList.getSelectedIndex());
                            mainWindow.startSinglePlayerGame(simulator);
                            setVisible(false);
                            mainWindow.removeMenu(MainMenu.this, invoker);
                            popupMenu.setVisible(false);
                        }
                    });
                    getParent().add(popupMenu);
                    popupMenu.add(listPane);
                    popupMenu.add(load);
                    add(popupMenu);
                    popupMenu.show(mainWindow, 650, 350);      
                }
            });        

        JButton options = new JButton(new ImageIcon("src/main/resources/dab/gui/Buttons/options.png"));
        options.setBackground(Color.black);
        options.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){                
                mainWindow.changeMenu(new Options(mainWindow, invoker));                
            }
        });
     
        JButton resume = new JButton(new ImageIcon("src/main/resources/dab/gui/Buttons/resume.png"));
        resume.setBackground(Color.black);
        resume.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){                
               mainWindow.removeMenu(MainMenu.this, invoker);
               mainWindow.resume();             
            }
        });  
        
        JButton quit = new JButton(new ImageIcon("src/main/resources/dab/gui/Buttons/quit.png"));
        quit.setBackground(Color.black);
        quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
   
       boolean addResume = false;
       boolean addSave = false;
       
       if(!(invoker instanceof DaMMenu)) {
            if(!mainWindow.getGameOver()){        
                addResume = true;
                if(invoker instanceof SinglePlayerPanel){  
                    addSave = true;
                }
            }
        }
        
        if(addResume) {
            add(resume);
        }
                
        add(one_player);
        add(two_player);
        if (addSave) {
            add(save_menu);
        }
        add(load_menu);
        add(options);      
        add(quit);
              
        //different coorditations for buttons depending on whether they are used in the 
        //game, or start menu (different alignment and different amount of buttons)
        int h;
        if(getComponentCount() >5){
            h = 40;
        } else {
            h=150;
        }
        if(invoker instanceof DaMMenu) {           
            setBounds(getTheX(), h, (int)one_player.getMinimumSize().getWidth(),
                (int)one_player.getMinimumSize().getHeight()*getComponentCount());
        } else {
            setBounds(getTheX(), h, (int)one_player.getMinimumSize().getWidth(),
                (int)one_player.getMinimumSize().getHeight()*getComponentCount());
        }       
    }
    
    private void startGame(boolean playerMode){
         mainWindow.stopMusic();
         if (playerMode == true) {
             startSinglePlayerGame();
         } else {
             startTwoPlayerGame();
         }
    }
    
    private void startSinglePlayerGame() {
        mainWindow.changeMenu(new NameMenu(mainWindow, invoker, mainWindow.getUserName()));
    }
    
    private void startTwoPlayerGame() {
        mainWindow.changeMenu(new NameMenu(mainWindow, invoker, mainWindow.getUserName(), mainWindow.getUserName2()));
    }
    
}
