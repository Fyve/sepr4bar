/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.newsim.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author eduard
 */
public class RandomBuffer<T> {
    @JsonProperty
    ArrayList<T> storage;
    
    @JsonProperty
    int current = 0;
    
    RandomGenerator randomGen = new RandomGenerator();
    public RandomBuffer(Collection <? extends T> x) {
        storage = new ArrayList<>(x);
    }
    
    public void add(T element) {
        storage.add(element);
    }
    
    public void shuffle() {
        for (int i = 0; i < storage.size() * 2; ++i) {
            int index1 = randomGen.rollInt(0, storage.size() - 1);
            int index2 = randomGen.rollInt(0, storage.size() - 1);
            // swap them
            T aux = storage.get(index1);
            storage.set(index1, storage.get(index2));
            storage.set(index2, aux);
        }
    }
    
    public T get() {
        if (current == storage.size()) {
            shuffle();
            current = 0;
        } 
        return storage.get(current++);
    }
}
