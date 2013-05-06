/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.mainpanels;

import dab.gui.application.MainWindow;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JToggleButton;

/**
 *
 * @author Aiste
 */
public class Options extends MenuHandler{
    private MainWindow mainWindow;
    private final int EASY = 1;
    private final int NORMAL = 2;
    private final int HARD = 3;
    private JLayeredPane invoker;
    private JToggleButton easy, normal, hard;
    

    public Options(MainWindow mw, final JLayeredPane invoker) {
        super(invoker);
        this.mainWindow = mw;
        this.invoker = invoker;
        
        
        easy = new JToggleButton();
        easy.setSelectedIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/easy.png"));
        easy.setIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/easyOff.png"));
        easy.setBackground(Color.BLACK);
        
        normal = new JToggleButton();
        normal.setSelectedIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/normal.png"));
        normal.setIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/normalOff.png"));
        normal.setBackground(Color.black);
        
        hard = new JToggleButton("hard");
        hard.setSelectedIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/hard.png"));
        hard.setIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/hardOff.png"));
        hard.setBackground(Color.black);
        setSelected();
        
        easy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                mainWindow.setDifficulty(EASY);
                setSelected();              
            }
        });
              
        normal.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                mainWindow.setDifficulty(NORMAL);
                setSelected(); 
            }
        });
               
        hard.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                mainWindow.setDifficulty(HARD);
                setSelected(); 
            }
        });
        
        JToggleButton sound = new JToggleButton();
        sound.setSelectedIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/music.png"));
        sound.setIcon(new ImageIcon("src/main/resources/dab/gui/Buttons/musicOff.png"));
        sound.setBackground(Color.black);
        sound.setSelected(mainWindow.getMusic());
        sound.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                mainWindow.setMusic();
            }
        });
           
        JButton help = new JButton(new ImageIcon("src/main/resources/dab/gui/Buttons/help.png"));
         help.setBackground(Color.black);
         help.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
               mainWindow.changeMenu(new HelpScreen(mainWindow, invoker));
            }
        });
         
        JButton back = new JButton(new ImageIcon("src/main/resources/dab/gui/Buttons/back.png"));
        back.setBackground(Color.black);
        back.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                mainWindow.changeMenu(new MainMenu(mainWindow, invoker));
            }
        });
        
      add(help);   
      add(easy);
      add(normal);
      add(hard);
      add(sound);
      add(back);
     
        setBounds(getTheX(), 100, (int)easy.getMinimumSize().getWidth(), 
                (int)easy.getMinimumSize().getHeight()*getComponentCount());
     }
      
    private void setSelected(){
      easy.setSelected(false);
      normal.setSelected(false);
      hard.setSelected(false);
      if(mainWindow.getDifficulty()==EASY){
          easy.setSelected(true);
      } else if (mainWindow.getDifficulty() == NORMAL){
          normal.setSelected(true);
      } else {
          hard.setSelected(true);
      }
      
    }
    
}
