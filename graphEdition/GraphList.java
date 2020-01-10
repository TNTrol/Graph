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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;

public class GraphList<T, E> implements Graph<T, E> {

    protected class Node<E> implements Graph.Node<E> {

        public E value;
        public int top;

        public Node() {
        }

        public Node(E a, int t) {
            top = t;
            value = a;
        }
    }

    protected class Point implements Graph.Point {

        int ind1;
        int ind2;

        public Point(int i, int j) {
            ind1 = i;
            ind2 = j;
        }
    }

    protected State state = Graph.State.AddTop;
    protected JPanel paintJP;
    protected Callback callback;
    protected Show show;

    protected List<TopGraph<T>> Note = new ArrayList<>();
    protected int currentX, currentY, indexStart = -1, w, h, indexEnd = -1, SIZE = 10;
    protected LinkedList<Node<E>>[] Edge = new LinkedList[10];

    public GraphList() {
    }

    public GraphList(JPanel paintPanel, int width, int height) {
        setPanel(paintPanel, width, height, null, null);
    }

    public GraphList(JPanel paintPanel, int width, int height, Callback value) {
        setPanel(paintPanel, width, height, value, null);
        this.callback = value;
    }

    public GraphList(JPanel paintPanel, int width, int height, Callback value, Show show) {
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

    protected Point LineOfIndex(int x, int y) {
        for (int i = 0; i < Edge.length; i++) {
            if (Edge[i] != null) {
                for (Node node : Edge[i]) {
                    if (checkEdge(Note.get(i).getX(), Note.get(i).getY(), Note.get(node.top).getX(), Note.get(node.top).getY(), x, y)) {
                        return new Point(i, node.top);
                    }
                }
            }
        }
        return null;
    }

    protected int indexOf(Node node) {
        for (int i = 0; i < Note.size(); i++) {
            if (Note.get(i) == Note.get(node.top)) {
                return i;
            }
        }
        return -1;
    }

    protected int PointOfIndex(int x, int y) {
        for (int i = 0; i < Note.size(); i++) {
            if (checkPoint(Note.get(i).getX(), Note.get(i).getY(), x, y)) {
                return i;
            }
        }
        return -1;
    }

    protected Node contains(int i, int j) {
        if (Edge[i] == null) {
            return null;
        }
        for (Node node : Edge[i]) {
            if (Note.get(node.top) == Note.get(j)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public void mouseReleased(int x, int y) {
        if (Graph.State.AddLine == state && indexStart >= 0) {
            indexEnd = PointOfIndex(x, y);
            if (indexEnd >= 0 && indexEnd != indexStart && contains(indexStart, indexEnd) == null && contains(indexEnd, indexStart) == null) {
                currentY = Note.get(indexEnd).getY();
                currentX = Note.get(indexEnd).getX();
                paintJP.repaint();
                if (callback != null) {
                    E temp = (E) callback.addValueToEdge(indexStart, indexEnd);
                    if (temp != null) {
                        if (Edge[indexStart] == null) {
                            Edge[indexStart] = new LinkedList();
                        }
                        Edge[indexStart].add(new Node(temp, indexEnd));
                    }
                }
            }
            paintJP.repaint();
            indexEnd = -1;
        }
        if (callback != null) {
            if (Graph.State.ValueEdge == state) {
                Point p = LineOfIndex(x, y);
                if (p != null) {

                    Node node = contains(p.ind1, p.ind2);
                    node.value = (E) callback.addValueToEdge(p.ind1, p.ind2);
                    paintJP.repaint();
                }
            }
            if (Graph.State.ValueTop == state && indexStart >= 0) {
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
        if ((State.AddTop == state || Graph.State.RemoveLine == state) && indexStart >= 0) {
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
                removeLine(p);
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
                LinkedList<Node<E>>[] arr = new LinkedList[Edge.length * 2];
                SIZE = Edge.length * 2;
                for (int i = 0; i < Edge.length; i++) {
                    arr[i] = Edge[i];
                }
                Edge = arr;
            }
            Edge[Note.size() - 1] = new LinkedList();
            paintJP.repaint();
        }
    }

    protected void removeTop(int x) {
        for (int i = 0; i < Note.size(); i++) {
            if (Edge[i] != null) {
                for (Iterator<Node<E>> it = Edge[i].iterator(); it.hasNext();) {
                    if (it.next().top == x) {
                        it.remove();
                        break;
                    }
                }

                for (Node node : Edge[i]) {
                    if (node.top > x) {
                        node.top--;
                    }
                }
            }
        }
        for (int i = x; i < Note.size(); i++) {
            Edge[i] = i != Note.size() - 1 ? Edge[i + 1] : new LinkedList();
        }
        Note.remove(x);
    }

    protected void removeLine(Point p) {
        if (Edge[p.ind1] == null) {
            return;
        }
        for (Iterator<Node<E>> it = Edge[p.ind1].iterator(); it.hasNext();) {
            if (it.next().top == p.ind2) {
                it.remove();
                break;
            }
        }
    }

    protected void paintGraph(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i < Note.size(); i++) {
            if (Edge[i] != null) {
                for (Node node : Edge[i]) {
                    drawEdge(Note.get(i).getX(), Note.get(i).getY(), Note.get(node.top).getX(), Note.get(node.top).getY(), i, node.top, g);
                }
            }
        }
        for (int i = 0; i < Note.size(); i++) {
            drawTop(Note.get(i).getX(), Note.get(i).getY(), i, g);
        }
        if (Graph.State.AddLine == state && indexStart >= 0) {
            drawEdge(Note.get(indexStart).getX(), Note.get(indexStart).getY(),
                    currentX, currentY, indexStart, -1, g);
        }
    }

    private void prin() {
        for (int i = 0; i < Note.size(); i++) {
            System.out.print(Note.get(i));
        }
    }

    @Override
    public void removeLine() {
        state = Graph.State.RemoveLine;
    }

    @Override
    public void addLine() {
        state = Graph.State.AddLine;
    }

    @Override
    public void addTop() {
        state = Graph.State.AddTop;
    }

    @Override
    public void removeTop() {
        state = Graph.State.RemoveTop;
    }

    @Override
    public JPanel getPanel() {
        return paintJP;
    }

    @Override
    public void setValueEdge() {
        state = Graph.State.ValueEdge;
    }

    @Override
    public void setValueTop() {
        state = Graph.State.ValueTop;
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
                        arr[indexOf(node)][i] = (E) node.value;
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
                    Note.add(new TopGraph<>(w / (c + 1) * (j + 1), h / (high + 1) * (i + 1)));
                    if (arr.length % c != 0 && i == high - 1 && arr.length % c - 1 == j) {
                        break;
                    }
                }
            }
            for (int i = 0; i < arr.length; i++) {
                for (int j = i; j < arr[0].length; j++) {
                    if (arr[i][j] != null && arr[j][i] != null) {
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
                Note.add(new TopGraph<>(w / (c + 1) * (j + 1), h / (high + 1) * (i + 1)));
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
                    if (i < temp.getNumber()) {
                        Edge[i].add(new Node(temp.getValue(), temp.getNumber()));
                    }
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
                    arr[node.top][i] = true;
                }
            }
        }
        return arr;
    }

    public int size() {
        return Note.size();
    }

    @Override
    public T getTop(int i) {
        return i < Note.size() ? Note.get(i).getValue() : null;
    }

    @Override
    public E getEdge(int i, int j) {
        return i < Note.size() && j < Note.size() ? (E) contains(i, j).value : null;
    }
}
