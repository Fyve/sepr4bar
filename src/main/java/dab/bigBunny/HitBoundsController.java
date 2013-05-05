/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import dab.engine.simulator.Condenser;
import dab.engine.simulator.Pump;
import dab.engine.simulator.Reactor;
import dab.engine.simulator.Turbine;
import dab.engine.simulator.views.FailableComponentView;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Aiste
 */
public class HitBoundsController {
    private ArrayList<HittableComponent> hittableComponents;
    
    public HitBoundsController() {
        hittableComponents = new ArrayList<HittableComponent>();
    }   
    
    public void addHitableComponent(FailableComponentView component, int x, int y, int width, int height) { 
        
        if (component instanceof Pump) {
            hittableComponents.add(new Circle(component, x, y, width, height));
        } else {
            Circle circle1 = new Circle(component, x, y, width, width);
            Circle circle2 = new Circle(component, x,y+height-width , width, width);
            
            RecCircle recCircle;
            TheRectangle rectangle;
                      
            if (component instanceof Turbine) { 
               rectangle = new TheRectangle(component, x, y+width/2, width, height-width);
                recCircle = new RecCircle(component, x, y, width, height, circle1, circle2, rectangle);           
            } else if(component instanceof Reactor) {
                rectangle = new TheRectangle(component, x, y+width/2, width, height-width/2);
                recCircle = new RecCircle(component, x, y, width, height, circle1, null, rectangle);
            } else {        //component is one of the condenser parts. check which, and ajust acordingly
                if(height < 120){
                    rectangle = new TheRectangle(component, x, y, width, height-width/2);
                    recCircle = new RecCircle(component, x, y, width, height, null, circle2, rectangle);        
                } else {
                    rectangle = new TheRectangle(component, x, y+width/2, width, height-width);
                    recCircle = new RecCircle(component, x, y, width, height, circle1, circle2, rectangle);              
                }
            }
            hittableComponents.add(recCircle);
        }
    }
    
   public ArrayList<HittableComponent> getHittableComponents(){
       return hittableComponents;
   }
}
