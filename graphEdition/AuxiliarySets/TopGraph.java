/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition.AuxiliarySets;

public class TopGraph<T> {
    private int x = 0, y = 0;
    private T value;
    
    public TopGraph(){        
    }
    
    public TopGraph(int x, int y){
        this.x=x;
        this.y=y;       
    }

    public TopGraph(T value)
    {
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setCoordinates(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
   
}
