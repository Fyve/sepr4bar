/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.mainpanels;

import dab.engine.newsim.SinglePlayerSimulator;
import dab.engine.newsim.TwoPlayerSimulator;
import dab.gui.application.MainWindow;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Aiste
 */
public class NameMenu extends MenuHandler{

    private final JTextField enter_name, enter_name2;
    private final JLabel name_label;
    private MainWindow mainWindow;
    private boolean onePlayerMode;
    private int theWidth;
    private BufferedImage background;
   
    public NameMenu(MainWindow mw, final JLayeredPane invoker, String singlePlayerName) {
        this(mw, invoker, true, singlePlayerName, "");
    }
    
    public NameMenu(MainWindow mw, final JLayeredPane invoker, String singlePlayerName, String dualPlayerName) {
        this(mw, invoker, false, singlePlayerName, dualPlayerName);
    }
    
    private NameMenu(MainWindow mw, final JLayeredPane invoker, final boolean onePlayerMode, String previousName1, String previousName2){
        super(invoker);
        this.mainWindow = mw;
        this.onePlayerMode = onePlayerMode;
        
        
        if(onePlayerMode){
            name_label = new JLabel("Please enter your name");
        } else {
            name_label = new JLabel("Please enter your names");
        }
        name_label.setFont(new Font("Bookman Old Style", Font.BOLD, 18));
        
        
        name_label.setForeground(Color.DARK_GRAY);
        
        //ok button has the same functionality as when the user presses enter
        JButton ok = new JButton(new ImageIcon("src/main/resources/dab/gui/Buttons/ok.png"));
        ok.setBackground(Color.black);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryStart();
            }
        });
        
        theWidth = (int)ok.getMinimumSize().getWidth();
        
        enter_name = new JTextField(20);
        enter_name.setFont(new Font("Bookman Old Style", Font.BOLD, 18));
        enter_name.setPreferredSize(new Dimension(theWidth, 40));
        enter_name.setMaximumSize(new Dimension(theWidth, 40));
        enter_name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
              //  super.keyReleased(e);
               
            }

            @Override
            public void keyTyped(KeyEvent e) {            }

            @Override
            public void keyPressed(KeyEvent e) {    
             if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    if(onePlayerMode){
                        tryStart();
                    } else {
                        enter_name2.requestFocus();
                    }
                }}
        });
        enter_name.setText(previousName1);
        
        enter_name2 = new JTextField(20);
        enter_name2.setFont(new Font("Bookman Old Style", Font.BOLD, 18));
        enter_name2.setPreferredSize(new Dimension(theWidth, 40));
        enter_name2.setMaximumSize(new Dimension(theWidth, 40));
        enter_name2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
               
            }

            @Override
            public void keyTyped(KeyEvent e) {            }

            @Override
            public void keyPressed(KeyEvent e) {  
             super.keyReleased(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    tryStart();
                }}
        });
        enter_name2.setText(previousName2);
        
        //ok button has the same functionality as when the user presses enter
        JButton back = new JButton(new ImageIcon("src/main/resources/dab/gui/Buttons/back.png"));       
        back.setBackground(Color.black);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               mainWindow.changeMenu(new MainMenu(mainWindow, invoker));
            }
        });
        
       
 
        add(name_label);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(enter_name);
        add(Box.createRigidArea(new Dimension(0,10)));
        if(!onePlayerMode){
            add(enter_name2);
            add(Box.createRigidArea(new Dimension(0,10)));
            setBounds(getTheX(),200,theWidth,280);
        } else{
            setBounds(getTheX(),200,theWidth,220);
        }
        add(ok); 
        add(back);
        
       setOpaque(false);
    };

    private void tryStart() {
        if (enter_name.getText().length() == 0 ||(!onePlayerMode&&enter_name2.getText().length() == 0)) {
            JOptionPane.showMessageDialog(null, "Name can't be empty!");
        } else{
            mainWindow.setGameOver(false);
            if (onePlayerMode) {
                SinglePlayerSimulator sim = new SinglePlayerSimulator(enter_name.getText());
                mainWindow.startSinglePlayerGame(sim);
            } else {
                TwoPlayerSimulator sim = new TwoPlayerSimulator(enter_name.getText(), enter_name2.getText());
                mainWindow.startTwoPlayerGame(sim);
            }
        }
    }
   
    @Override
    public void requestFocus() {
        enter_name.requestFocusInWindow();
    }  
    
}
