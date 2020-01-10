/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition;

import java.awt.Graphics;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JPanel;

public class OrGraphList<T, E> extends GraphList<T, E> {

    public OrGraphList() {
    }

    public OrGraphList(JPanel paintPanel, int width, int height) {
        setPanel(paintPanel, width, height, null, null);
    }

    public OrGraphList(JPanel paintPanel, int width, int height, Callback value) {
        setPanel(paintPanel, width, height, value, null);
        this.callback = value;
    }

    public OrGraphList(JPanel paintPanel, int width, int height, Callback value, Show show) {
        setPanel(paintPanel, width, height, value, show);
    }

    @Override
    public void drawEdge(int x1, int y1, int x2, int y2, int start, int end, Graphics g) {
        g.drawLine(x1, y1, x2, y2);
        double angle = Math.atan2(x2 - x1, y2 - y1);
        g.drawLine(x2, y2, Math.toIntExact(Math.round(x2 - 10 * Math.sin(angle - 45))), Math.toIntExact(Math.round(y2 - 10 * Math.cos(angle - 45))));
        g.drawLine(x2, y2, Math.toIntExact(Math.round(x2 - 10 * Math.sin(angle + 45))), Math.toIntExact(Math.round(y2 - 10 * Math.cos(angle + 45))));
    }

    @Override
    public E[][] getMatrixValue() {
        E obj = null;
        stop:
        for (int i = 0; i < Note.size(); i++) {
            if (Edge[i] != null) {
                for (Node node : Edge[i]) {
                    if (node.value != null) {
                        obj = (E) node.value;
                        break stop;
                    }
                }
            }
        }
        if (obj != null) {
            E[][] arr = (E[][]) Array.newInstance(obj.getClass(), Note.size(), Note.size());
            for (int i = 0; i < Note.size(); i++) {
                if (Edge[i] != null) {
                    for (Node node : Edge[i]) {
                        arr[i][indexOf(node)] = (E) node.value;
                    }
                }
            }
            return arr;
        }
        return null;
    }

    @Override
    public void setMatrix(Object[][] arr) {
        if (arr != null) {
            int c = (int) Math.sqrt(arr.length);
            int high = (arr.length % c) == 0 ? arr.length / c : (arr.length - c * c) / (c + 1) + c + 1;
            Edge = new LinkedList[arr.length];
            Note = new ArrayList<>();
            for (int i = 0; i < high; i++) {
                for (int j = 0; j < c; j++) {
                    Note.add(new TopGraph<>(w / (c + 1) * (j + 1), h / (high + 1 ) * (i + 1)));
                    if (arr.length % c != 0 && i == high - 1 && arr.length % c - 1 == j) {
                        break;
                    }
                }
            }
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    if (arr[i][j] != null) {
                        if (Edge[i] == null) {
                            Edge[i] = new LinkedList();
                        }
                        Edge[i].add(new Node(arr[i][j], j));
                    }
                }
            }
            paintJP.repaint();
        }
    }

    @Override
    public <A extends GraphNode> void setMatrix(LinkedList<A>[] arr) {
        if (arr == null) {
            return;
        }
        int c = (int) Math.sqrt(arr.length);
        int high = (arr.length % c) == 0 ? arr.length / c : (arr.length - c * c) / (c + 1) + c + 1;
        Note = new ArrayList<>();
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < c; j++) {
                Note.add(new TopGraph<>(w / (c + 1) * (j + 1), h / (high + 1 ) * (i + 1)));
                if (arr.length % c != 0 && i == high - 1 && arr.length % c - 1 == j) {
                    break;
                }
            }
        }
        Edge = new LinkedList[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                for (A temp : arr[i]) {
                    if (Edge[i] == null) {
                        Edge[i] = new LinkedList();
                    }
                    Edge[i].add(new Node(temp.getValue(), temp.getNumber()));
                }
            }
        }
        paintJP.repaint();
    }

    @Override
    public boolean[][] getMatrixBool() {
        boolean[][] arr = new boolean[Note.size()][Note.size()];
        for (int i = 0; i < Note.size(); i++) {
            if (Edge[i] != null) {
                for (Node node : Edge[i]) {
                    arr[i][node.top] = true;
                }
            }
        }
        return arr;
    }

}
