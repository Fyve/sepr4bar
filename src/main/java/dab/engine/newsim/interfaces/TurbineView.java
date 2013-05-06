/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.newsim.interfaces;

/**
 *
 * @author eduard
 */
public interface TurbineView extends FailableComponentView {
    public double outputPower();
    public void reducePower(double gunshotReduction);
}
