/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;


public class GraphArray<T, E> implements Graph<T, E> {

    protected class Node<E> implements Graph.Node<E> {

        public E value;

        public Node() {
        }

        public Node(E a) {
            value = a;
        }
    }

    protected class Point implements Graph.Point {

        int ind1;
        int ind2;

        public Point(int ind1, int ind2) {
            this.ind1 = ind1;
            this.ind2 = ind2;
        }
    }

    protected State state = State.AddTop;
    protected JPanel paintJP;
    protected Callback callback;
    protected Show show;

    protected List<TopGraph<T>> Note = new ArrayList<>();
    protected int currentX, currentY, indexStart = -1, w, h, indexEnd = -1;
    protected Node<E>[][] Edge = new Node[10][10];

    public GraphArray() {
    }

    public GraphArray(JPanel paintPanel, int width, int height) {
        setPanel(paintPanel, width, height, null, null);
    }

    public GraphArray(JPanel paintPanel, int width, int height, Callback value) {
        setPanel(paintPanel, width, height, value, null);
    }

    public GraphArray(JPanel paintPanel, int width, int height, Callback value, Show show) {
        setPanel(paintPanel, width, height, value, show);
    }

    @Override
    public void setPanel(JPanel paintPanel, int width, int height, Callback value, Show show) {
        w = width;
        h = height;
        this.show = show;
        this.callback = value;
        paintPanel.setLayout(new BorderLayout());
        paintJP = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                addToPanel(g);
                paintGraph(g);
            }
        };
        SwingUtils.setFixedSize(paintPanel, width, height);
        paintJP.setBackground(Color.WHITE);
        paintPanel.add(paintJP);

    }

    private Point LineOfIndex(int x, int y) {
        for (int i = 0; i < Edge.length; i++) {
            for (int j = i; j < Edge[0].length; j++) {
                if (Edge[i][j] != null && checkEdge(Note.get(i).getX(), Note.get(i).getY(), Note.get(j).getX(), Note.get(j).getY(), x, y)) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    protected int PointOfIndex(int x, int y) {
        for (int i = 0; i < Note.size(); i++) {
            if (checkPoint(Note.get(i).getX(), Note.get(i).getY(), x, y)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void mouseReleased(int x, int y) {
        if (State.AddLine == state && indexStart >= 0) {
            indexEnd = PointOfIndex(x, y);
            if (indexEnd >= 0 && indexEnd != indexStart && Edge[indexStart][indexEnd] == null && Edge[indexEnd][indexStart] == null) {
                Edge[indexStart][indexEnd] = new Node();
                Edge[indexEnd][indexStart] = Edge[indexStart][indexEnd];
                paintJP.repaint();
                if (callback != null) {
                    E temp = (E) callback.addValueToEdge(indexStart, indexEnd);
                    Edge[indexStart][indexEnd] = temp == null ? null : new Node(temp);
                    Edge[indexEnd][indexStart] = temp == null ? null : Edge[indexStart][indexEnd];
                }
            }
            paintJP.repaint();
            indexEnd = -1;
        }
        if (callback != null) {
            if (State.ValueEdge == state) {
                Point p = LineOfIndex(x, y);
                if (p != null) {
                    Edge[p.ind1][p.ind2].value = (E) callback.addValueToEdge(p.ind1, p.ind2);
                    paintJP.repaint();
                }
            }
            if (State.ValueTop == state && indexStart >= 0) {
                Note.get(indexStart).setValue((T) callback.addValueToTop(indexStart));
                paintJP.repaint();
            }
        }
        if (show != null) {
            if (indexStart >= 0 && State.RemoveTop != state) {
                show.showValue(indexStart, -1);
            } else if (indexStart < 0 && State.RemoveLine != state) {
                Point p = LineOfIndex(x, y);
                if (p != null) {
                    show.showValue(p.ind1, p.ind2);
                }
            }
        }
        indexStart = -1;
    }

    @Override
    public void mouseDragged(int x, int y) {
        if (State.AddLine == state && indexStart >= 0) {
            currentX = x;
            currentY = y;
            paintJP.repaint();
        }
        if ((State.AddTop == state || State.RemoveLine == state) && indexStart >= 0) {
            Note.get(indexStart).setX(x);
            Note.get(indexStart).setY(y);
            paintJP.repaint();
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        indexStart = PointOfIndex(x, y);
        if (State.AddLine == state && indexStart >= 0) {
            currentY = y;
            currentX = x;
            paintJP.repaint();
        }
        if (State.RemoveLine == state && indexStart < 0) {
            Point p = LineOfIndex(x, y);
            if (p != null) {
                Edge[p.ind1][p.ind2] = null;
                Edge[p.ind2][p.ind1] = null;
                paintJP.repaint();
            }
        }
        if (State.RemoveTop == state && indexStart >= 0) {
            removeTop(indexStart);
            paintJP.repaint();
        }
        if (State.AddTop == state && indexStart < 0) {
            Note.add(new TopGraph(x, y));
            if (callback != null) {
                int a = Note.size() - 1;
                T temp = (T) callback.addValueToTop(a);
                if (temp != null) {
                    Note.get(a).setValue(temp);
                } else {
                    Note.remove(a);
                }
            }
            if (Note.size() > Edge.length) {
                Node[][] arr = new Node[Edge.length * 2][Edge.length * 2];
                for (int i = 0; i < Edge.length; i++) {
                    for (int j = 0; j < Edge.length; j++) {
                        arr[i][j] = Edge[i][j];
                    }
                }
                Edge = arr;
            }
            paintJP.repaint();
        }
    }

    protected void removeTop(int x) {
        int size = Note.size();
        for (int i = 0; i < size; i++) {
            for (int j = x; j < size; j++) {
                Edge[i][j] = j < size - 1 ? Edge[i][j + 1] : null;
            }
        }
        for (int i = x; i < size; i++) {
            Edge[i] = i < size - 1 ? Edge[i + 1] : new Node[Edge.length];
        }
        Note.remove(x);
    }

    private void paintGraph(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i < Note.size(); i++) {
            for (int j = i; j < Note.size(); j++) {
                if (Edge[i][j] != null) {
                    g.setColor(Color.BLACK);
                    drawEdge(Note.get(i).getX(), Note.get(i).getY(), Note.get(j).getX(), Note.get(j).getY(), i, j, g);
                }
            }
        }
        for (int i = 0; i < Note.size(); i++) {
            g.setColor(Color.BLACK);
            drawTop(Note.get(i).getX(), Note.get(i).getY(), i, g);
        }
        if (State.AddLine == state && indexStart >= 0 && indexEnd < 0) {
            g.setColor(Color.BLACK);
            drawEdge(Note.get(indexStart).getX(), Note.get(indexStart).getY(),
                    currentX, currentY, indexStart, -1, g);
        }
    }

    public void removeLine() {
        state = State.RemoveLine;
    }

    public void addLine() {
        state = State.AddLine;
    }

    public void addTop() {
        state = State.AddTop;
    }

    public void removeTop() {
        state = State.RemoveTop;
    }

    public JPanel getPanel() {
        return paintJP;
    }

    public void setValueEdge() {
        state = State.ValueEdge;
    }

    public void setValueTop() {
        state = State.ValueTop;
    }

    public E[][] getMatrixValue() {
        E obj = null;
        stop:
        for (int i = 0; i < Note.size(); i++) {
            for (int j = 0; j < Note.size(); j++) {
                if (Edge[i][j] != null) {
                    obj = Edge[i][j].value;
                    break stop;
                }
            }
        }
        if (obj != null) {
            E[][] arr = (E[][]) Array.newInstance(obj.getClass(), Note.size(), Note.size());
            for (int i = 0; i < Note.size(); i++) {
                for (int j = 0; j < Note.size(); j++) {
                    if (Edge[i][j] != null) {
                        arr[i][j] = Edge[i][j].value;
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
            int high = (arr.length % c) == 0 ? arr.length / c : (arr.length - c * c) / c + c + 1;
            Edge = new Node[arr.length][arr.length];
            Note = new ArrayList<>();
            for (int i = 0; i < high; i++) {
                for (int j = 0; j < c; j++) {
                    Note.add(new TopGraph<>(w / (c + 1) * (j + 1), h / (high + 1) * (i + 1)));
                    if (arr.length % c != 0 && i == high - 1 && arr.length % c - 1 == j) {
                        break;
                    }
                }
            }
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    Edge[i][j] = arr[i][j] == null ? null : new Node(arr[i][j]);
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
                Note.add(new TopGraph<>(w / (c + 1) * (j + 1), h / (high + 1) * (i + 1)));
                if (arr.length - c * high != 0 && i == high - 1 && arr.length % c - 1 == j) {
                    break;
                }
            }
        }
        Edge = new Node[arr.length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                for (A temp : arr[i]) {
                    Edge[i][temp.getNumber()] = new Node(temp.getValue());
                }
            }
        }
        paintJP.repaint();
    }

    @Override
    public boolean[][] getMatrixBool() {
        boolean[][] arr = new boolean[Note.size()][Note.size()];
        for (int i = 0; i < Note.size(); i++) {
            for (int j = i; j < Note.size(); j++) {
                if (Edge[i][j] != null) {
                    arr[i][j] = true;
                    arr[j][i] = true;
                }
            }
        }
        return arr;
    }

    @Override
    public int size() {
        return Note.size();
    }

    @Override
    public T getTop(int i) {
        return i < Note.size() ? Note.get(i).getValue() : null;
    }

    @Override
    public E getEdge(int i, int j) {
        return i < Note.size() && j < Note.size() && i > -1 && j > -1 ? Edge[i][j].value : null;
    }
}
