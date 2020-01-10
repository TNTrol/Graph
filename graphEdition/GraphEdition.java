/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition;

import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author user
 * @param <T>
 * @param <E>
 */
public class GraphEdition<T, E> {

    private JPanel paintPanel;
    final Graph<T, E> graph;
    private JPopupMenu popup;
    private Graph.Callback<T, E> Callback;

    public GraphEdition(JFrame frame, int width, int height) {
        init(frame);
        graph = new GraphArray<>(paintPanel, width, height);
        Pop();
    }

    public GraphEdition(JFrame frame, int width, int height, Graph.Callback<T, E> Callback) {
        init(frame);
        graph = new GraphArray<>(paintPanel, width, height, Callback);
        this.Callback = Callback;
        Pop();
    }

    public GraphEdition(JFrame frame, Graph graph, int width, int height) {
        this.graph = graph;
        init(frame);
        this.graph.setPanel(paintPanel, width, height, null, null);
        Pop();
    }

    public GraphEdition(JPanel panel, Graph graph, int width, int height, Graph.Callback<T, E> Callback) {
        this.graph = graph;
        paintPanel = panel;
        paintPanel.add(new MouseEvents(paintPanel));
        this.graph.setPanel(paintPanel, width, height, Callback, null);
        this.Callback = Callback;
        Pop();
    }

    public GraphEdition(JFrame frame, Graph graph, int width, int height, Graph.Callback<T, E> Callback) {
        this.graph = graph;
        init(frame);
        this.graph.setPanel(paintPanel, width, height, Callback, null);
        this.Callback = Callback;
        Pop();
    }

    public GraphEdition(JFrame frame, Graph graph, int width, int height, Graph.Show show) {
        this.graph = graph;
        init(frame);
        this.graph.setPanel(paintPanel, width, height, null, show);
        Pop();
    }

    public GraphEdition(JPanel panel, Graph graph, int width, int height, Graph.Callback<T, E> Callback, Graph.Show show) {
        this.graph = graph;
        paintPanel = panel;
        paintPanel.add(new MouseEvents(paintPanel));
        this.graph.setPanel(paintPanel, width, height, Callback, show);
        this.Callback = Callback;
        Pop();
    }

    public GraphEdition(JFrame frame, Graph graph, int width, int height, Graph.Callback<T, E> Callback, Graph.Show show) {
        this.graph = graph;
        init(frame);
        this.graph.setPanel(paintPanel, width, height, Callback, show);
        this.Callback = Callback;
        Pop();
    }

    private void init(JFrame frame) {
        paintPanel = new JPanel();
        paintPanel.add(new MouseEvents(paintPanel));
        frame.add(paintPanel);
    }

    private void Pop() {
        popup = new JPopupMenu();
        JMenuItem top = new JMenuItem("Add top");
        top.addActionListener((ActionEvent event) -> {
            graph.addTop();
        });
        JMenuItem conect = new JMenuItem("Add conect");
        conect.addActionListener((ActionEvent event) -> {
            graph.addLine();
        });
        JMenuItem deleteTop = new JMenuItem("Delete top");
        deleteTop.addActionListener((ActionEvent event) -> {
            graph.removeTop();
        });
        JMenuItem deleteEdge = new JMenuItem("Delete Edge");
        deleteEdge.addActionListener((ActionEvent event) -> {
            graph.removeLine();
        });
        popup.add(top);
        popup.add(conect);
        popup.add(deleteEdge);
        popup.add(deleteTop);
        if (Callback != null) {
            JMenuItem valueTop = new JMenuItem("Value of top");
            valueTop.addActionListener((ActionEvent event) -> {
                graph.setValueTop();
            });
            JMenuItem valueEdge = new JMenuItem("Volue of edge");
            valueEdge.addActionListener((ActionEvent event) -> {
                graph.setValueEdge();
            });
            popup.add(valueTop);
            popup.add(valueEdge);
        }
        paintPanel.setComponentPopupMenu(popup);
    }

    private class MouseEvents extends Applet implements MouseListener, MouseMotionListener {

        public MouseEvents(JPanel panel) {
            panel.addMouseListener(this);
            panel.addMouseMotionListener(this);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent evt) {
            if (evt.getButton() == 1) {
                graph.mousePressed(evt.getX(), evt.getY());
            } else if (evt.getButton() == 3) {
                popup.show(graph.getPanel(), evt.getX(), evt.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            if (evt.getButton() == 1) {
                graph.mouseReleased(evt.getX(), evt.getY());
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
            graph.mouseDragged(evt.getX(), evt.getY()); //To change body of generated
        }

        @Override
        public void mouseMoved(MouseEvent e) { //To change body of 
        }

    }

    public void addTop() {
        graph.addTop();
    }

    public void addLine() {
        graph.addLine();
    }

    public void removeTop() {
        graph.removeTop();
    }

    public void removeLine() {
        graph.removeLine();
    }

    public JPanel getPanel() {
        return graph.getPanel();
    }

    public void SetMatrix(E[][] arr) {
        graph.setMatrix(arr);
    }

    public E[][] GetMatrix() {
        E[][] arr = graph.getMatrixValue();
        return arr;
    }

    public void setValueEdge() {
        graph.setValueEdge();
    }

    public void setValueTop() {
        graph.setValueTop();
    }

    public boolean[][] getAdjMatrixBool() {
        return graph.getMatrixBool();
    }

    public int size() {
        return graph.size();
    }

    public T getTop(int i) {
        return graph.getTop(i);
    }

    public E getEdge(int i, int j) {
        return graph.getEdge(i, j);
    }

    public void setMatrix(LinkedList<GraphNode>[] arr) {
        graph.setMatrix(arr);
    }

}
