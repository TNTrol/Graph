/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition;

import graphEdition.View.ViewCircleGraph;
import graphEdition.View.ViewGraph;

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
public class GraphEdition<T, E> extends JPanel{
    private JPopupMenu popup;

    protected int currentX, currentY, indexStart = -1, indexEnd = -1;
    protected State state = State.AddTop;
    protected Graph<T, E> graph;
    protected ViewGraph<T,E, Graphics> view;

    public GraphEdition(Graph graph, ViewGraph view, int width, int height)  {
        super();
        this.graph = graph;
        this.view = view;
        this.add(new MouseEvents(this));
        this.initPopupMenu();
        this.setSize(width, height);
    }

    public GraphEdition(int width, int height) {
        this(new ArrayGraph(), new ViewCircleGraph(), width, height);
    }

    public GraphEdition(ViewCircleGraph view, int width, int height)
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
                indexStart = PointOfIndex(x, y);
                if (State.AddLine == state && indexStart >= 0) {
                    currentY = y;
                    currentX = x;
                    panel.repaint();
                   // this.paint(getGraphics());
                }
                if (State.RemoveLine == state && indexStart < 0) {
                    Edge p = LineOfIndex(x, y);
                    if (p != null) {
                        graph.removeEdge(p.indexTop1, p.indexTop2);
                        panel.repaint();

                    }
                }
                if (State.RemoveTop == state && indexStart >= 0) {
                    graph.removeTop(indexStart);
                    panel.repaint();
                }
                if (State.AddTop == state && indexStart < 0) {
                    graph.addTop(view.addValueToTop(graph.countOfTop()));
                    TopGraph top = graph.getTopGraphByIndex(graph.countOfTop() - 1);
                    top.setX(x);
                    top.setY(y);
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
                    indexEnd = PointOfIndex(evt.getX(), evt.getY());
                    if (indexEnd >= 0 && indexEnd != indexStart && !graph.existEdge(indexStart, indexEnd)) {
                        graph.addEdge(view.addValueToEdge(indexStart, indexEnd), indexStart, indexEnd);
                        view.showEdge(indexStart, indexEnd);
                    }
                    indexEnd = -1;
                }
                if (State.ValueEdge == state) {
                    Edge p = LineOfIndex(evt.getX(), evt.getY());
                        if (p != null) {
                            view.showEdge(p.indexTop1, p.indexTop2);
                        }
                }
                if (State.ValueTop == state && indexStart >= 0) {
                    view.showTop(indexStart);
                }
                panel.repaint();
                indexStart = -1;
            }
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
                TopGraph top = graph.getTopGraphByIndex(indexStart);
                top.setX(evt.getX());
                top.setY(evt.getY());
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
        for (int i = 0; i < graph.countOfTop(); i++) {
            for (Integer j : graph.row(i)) {
                    g.setColor(Color.BLACK);
                    TopGraph top1 = graph.getTopGraphByIndex(i);
                    TopGraph top2 = graph.getTopGraphByIndex(j);
                    view.drawEdge(top1.getX(), top1.getY(), top2.getX(), top2.getY(), i, j, g);
            }
        }
        for (int i = 0; i < graph.countOfTop(); i++) {
            g.setColor(Color.BLACK);
            TopGraph top = graph.getTopGraphByIndex(i);
            view.drawTop(top.getX(), top.getY(), i, g);
        }
        if (State.AddLine == state && indexStart >= 0 ) {
            g.setColor(Color.BLACK);
            TopGraph top = graph.getTopGraphByIndex(indexStart);
            view.drawEdge(top.getX(), top.getY(), currentX, currentY, indexStart, -1, g);
        }
    }

    private Edge LineOfIndex(int x, int y) {
        for (int i = 0; i < graph.countOfTop(); i++) {
            for (Integer j: graph.row(i)) {
                TopGraph top1 = graph.getTopGraphByIndex(i);
                TopGraph top2 = graph.getTopGraphByIndex(j);
                if (view.checkEdge(top1.getX(), top1.getY(), top2.getX(), top2.getY(), x, y)) {
                    return new Edge(i, j);
                }
            }
        }
        return null;
    }

    private int PointOfIndex(int x, int y) {
        for (int i = 0; i < graph.countOfTop(); i++) {
            if (view.checkPoint(graph.getTopGraphByIndex(i).getX(), graph.getTopGraphByIndex(i).getY(), x, y)) {
                return i;
            }
        }
        return -1;
    }

}
