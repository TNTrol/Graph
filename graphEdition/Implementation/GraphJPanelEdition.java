/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition.Implementation;

import graphEdition.GraphsIml.ArrayGraph;
import graphEdition.MVCGraph.ControllerGraph;
import graphEdition.MVCGraph.Graph;
import graphEdition.AuxiliarySets.GraphNode;
import graphEdition.AuxiliarySets.State;
import graphEdition.View.ViewCircleGraph;
import graphEdition.MVCGraph.ViewGraph;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author user
 * @param <T>
 * @param <E>
 */
public class GraphJPanelEdition<T, E> extends JPanel{
    private JPopupMenu popup;

    protected int currentX, currentY, indexStart = -1, indexEnd = -1;
    protected Point startPoint ;
    protected State state = State.AddTop;
    protected Graph<T, E> graph;
    protected ControllerGraph<T, E, Graphics> controllerGraph;
    protected ViewGraph<T,E, Graphics> view;

    public GraphJPanelEdition(Graph graph, ViewGraph<T,E, Graphics> view, int width, int height)  {
        super();
        this.graph = graph;
        this.view = view;
        this.add(new MouseEvents(this));
        this.initPopupMenu();
        this.setSize(width, height);
        this.controllerGraph = new ControllerGraph<>(graph, view);
    }

    public GraphJPanelEdition(int width, int height) {
        this(new ArrayGraph(), new ViewCircleGraph(), width, height);
    }

    public GraphJPanelEdition(ViewGraph<T,E, Graphics> view, int width, int height)
    {
        this (new ArrayGraph(), view, width, height);
    }

    private void initPopupMenu() {
        popup = new JPopupMenu();
        JMenuItem top = new JMenuItem("Add top");
        top.addActionListener((ActionEvent event) -> {
            addTop();
        });
        JMenuItem connect = new JMenuItem("Add edge");
        connect.addActionListener((ActionEvent event) -> {
            addLine();
        });
        JMenuItem deleteTop = new JMenuItem("Delete top");
        deleteTop.addActionListener((ActionEvent event) -> {
            removeTop();
        });
        JMenuItem deleteEdge = new JMenuItem("Delete Edge");
        deleteEdge.addActionListener((ActionEvent event) -> {
            removeLine();
        });
        JMenuItem valueTop = new JMenuItem("Value of top");
        valueTop.addActionListener((ActionEvent event) -> {
            setValueTop();
        });
        JMenuItem valueEdge = new JMenuItem("Value of edge");
        valueEdge.addActionListener((ActionEvent event) -> {
            setValueEdge();
        });
        popup.add(top);
        popup.add(connect);
        popup.add(deleteEdge);
        popup.add(deleteTop);
        popup.add(valueTop);
        popup.add(valueEdge);
        this.setComponentPopupMenu(popup);
    }

    private class MouseEvents extends Applet implements MouseListener, MouseMotionListener{

        private JPanel panel;
        public MouseEvents(JPanel panel) {
            panel.addMouseListener(this);
            panel.addMouseMotionListener(this);
            this.panel = panel;
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
                int x = evt.getX(), y = evt.getY();
                indexStart = controllerGraph.getIndexOfTop(x, y);
                if (State.AddLine == state && indexStart >= 0) {
                    currentY = y;
                    currentX = x;
                    startPoint = controllerGraph.getPointOfTop(indexStart);
                    panel.repaint();
                }
                if (State.RemoveLine == state && indexStart < 0 && controllerGraph.removeEdge(x, y)){
                    panel.repaint();
                }
                if (State.RemoveTop == state && indexStart >= 0) {
                    controllerGraph.removeTop(indexStart);
                    panel.repaint();
                }
                if (State.AddTop == state && indexStart < 0 && controllerGraph.addTop(x, y)) {
                    panel.repaint();
                }
            } else if (evt.getButton() == 3) {
                popup.show(panel, evt.getX(), evt.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            if (evt.getButton() == 1) {
                if (State.AddLine == state && indexStart >= 0) {
                    indexEnd = controllerGraph.getIndexOfTop(evt.getX(), evt.getY());
                    if (indexEnd >= 0 && indexEnd != indexStart ) {
                        controllerGraph.createEdge(indexStart, indexEnd);
                    }
                    indexEnd = -1;
                    indexStart = -1;
                }
                if (State.ValueEdge == state) {
                    controllerGraph.valueOfEdge(evt.getX(), evt.getY());
                }
                if (State.ValueTop == state) {
                    controllerGraph.valueOfTop(evt.getX(), evt.getY());
                }
                panel.repaint();
            }
            indexStart = -1;
        }


        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
            if (State.AddLine == state && indexStart >= 0) {
                currentX = evt.getX();
                currentY = evt.getY();
                panel.repaint();
            }
            if ((State.RemoveTop != state && State.AddLine != state) && indexStart >= 0) {
                controllerGraph.replaceTop(indexStart, evt.getX(), evt.getY());
                panel.repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) { //To change body of 
        }

    }

    public void SetMatrix(E[][] arr) {
        graph.setMatrix(arr);
    }

    public E[][] GetMatrix(E obj) {
        return graph.getMatrixValue((Class<E>) obj.getClass());
    }

    public boolean[][] getAdjMatrixBool() {
        return graph.getMatrixBool();
    }

    public int countTop() {
        return graph.countOfTop();
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

    public void setValueEdge() {
        state = State.ValueEdge;
    }

    public void setValueTop() {
        state = State.ValueTop;
    }

    @Override
    public void paint (Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        controllerGraph.paint(g);
        if (State.AddLine == state && indexStart >= 0 ) {
            g.setColor(Color.BLACK);
            view.drawEdge(startPoint.x, startPoint.y, currentX, currentY, indexStart, -1, g);
        }
    }

    public void click()
    {
        controllerGraph.defaultPlace(getWidth(), getHeight());
        repaint();
    }
}
