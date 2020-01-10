/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 * @param <T>
 * @param <E>
 * @author user
 */
public interface Graph<T, E> {

    public static interface Callback<T, E> {

        T addValueToTop(int i);

        E addValueToEdge(int i, int j);

    }

    public interface Show {
        void showValue(int i, int j);
    }

    interface Node<T> {

        default T getValue() {
            return null;
        }

        default void setValue(T value) {
        }

        default Node<T> getNext() {
            return null;
        }
    }

    // public interface get{
    //     int number();
    //}

    interface Point {
    }

    enum State {
        AddLine,
        RemoveLine,
        AddTop,
        RemoveTop,
        ValueEdge,
        ValueTop;
    }

    //check of conection or top
    default boolean checkEdge(int x1, int y1, int x2, int y2, int x, int y) {
        if (x1 >= x && x >= x2 || x1 <= x && x <= x2) {
            if (x1 < x2)
                return 0 <= (y - y1 + 3) * (x2 - x1) - (x - x1) * (y2 - y1) && 0 >= (y - y1 - 3) * (x2 - x1) - (x - x1) * (y2 - y1);
            return 0 <= (y - y2 + 3) * (x1 - x2) - (x - x2) * (y1 - y2) && 0 >= (y - y2 - 3) * (x1 - x2) - (x - x2) * (y1 - y2);
        }
        return false;
    }

    default boolean checkPoint(int x1, int y1, int x, int y) {
        return ((y - y1) * (y - y1) + (x - x1) * (x - x1) <= 7 * 7);
    }

    //drawihg object
    default void drawEdge(int x1, int y1, int x2, int y2, int start, int end, Graphics g) {
        //g.drawArc(x1, y1, 200, 100, -60, 120);
        g.drawLine(x1, y1, x2, y2);
    }

    default void drawTop(int x, int y, int number, Graphics g) {
        String str = Integer.toString(number);
        g.drawOval(x - 7, y - 7, 14, 14);
        g.drawChars(str.toCharArray(), 0, str.length(), x - 5, y - 5);
    }

    //Point LineOfIndex(int x, int y);
    //int PointOfIndex(int x, int y);
    //invention of mouse /// DON'T TOCH IT
    void mouseReleased(int x, int y);

    void mouseDragged(int x, int y);

    void mousePressed(int x, int y);

    //add options to panel
    void setPanel(JPanel paintPanel, int width, int height, Callback value, Show show);

    default void addToPanel(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    //void removeTop(int x);
    //state 
    void removeLine();

    void addLine();

    void addTop();

    void removeTop();

    JPanel getPanel();

    void setValueEdge();

    void setValueTop();

    //set and get adjMatrix
    E[][] getMatrixValue();

    boolean[][] getMatrixBool();

    void setMatrix(E[][] arr);

    //GraphNode - строгое правило
    <A extends GraphNode> void setMatrix(LinkedList<A>[] arr);

    int size();

    T getTop(int i);

    E getEdge(int i, int j);

    //public void Value(T t);
}
