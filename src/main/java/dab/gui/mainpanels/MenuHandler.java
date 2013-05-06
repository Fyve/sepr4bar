/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.mainpanels;

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author Aiste
 */
public abstract class MenuHandler extends JPanel{
     
     private JLayeredPane invoker;
    
    public MenuHandler(final JLayeredPane invoker){
        
        this.invoker = invoker;
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    }
    
    public JLayeredPane getInvoker(){
        return invoker;
    }
    
    public int getTheX(){
        if(invoker instanceof DaMMenu){
            return 50;
        } else {
            return 400;
        }
    }
}
