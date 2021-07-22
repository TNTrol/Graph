/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition.Implementation;

import graphEdition.GraphsIml.ArrayGraph;
import graphEdition.GraphCore.GraphController;
import graphEdition.GraphCore.Graph;
import graphEdition.AuxiliarySets.GraphNode;
import graphEdition.AuxiliarySets.State;
import graphEdition.View.ViewCircleGraph;
import graphEdition.GraphCore.ViewGraph;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * Класс для визуализации граффа на основе JPanel
 * @param <T> Тип вершин графа
 * @param <E> Тип ребер графа
 * @author tntrol
 */
public class GraphJPanelEdition<T, E> extends JPanel{
    private JPopupMenu _popup;
    private int _currentX, _currentY, _indexStart = -1, _indexEnd = -1;
    private Point _startPoint;
    private State _state = State.AddTop;
    private Graph<T, E> _graph;
    private GraphController<T, E, Graphics> _graphController;
    private ViewGraph<T,E, Graphics> _view;

    public GraphJPanelEdition(Graph<T, E> graph, ViewGraph<T,E, Graphics> view, int width, int height)  {
        super();
        this._graph = graph;
        this._view = view;
        this.add(new MouseEvents(this));
        this.initPopupMenu();
        this.setSize(width, height);
        this._graphController = new GraphController<>(graph, view);
    }

    public GraphJPanelEdition(int width, int height) {
        this(new ArrayGraph(), new ViewCircleGraph(), width, height);
    }

    public GraphJPanelEdition(ViewGraph<T,E, Graphics> view, int width, int height)
    {
        this (new ArrayGraph(), view, width, height);
    }

    private void initPopupMenu() {
        _popup = new JPopupMenu();
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
        _popup.add(top);
        _popup.add(connect);
        _popup.add(deleteEdge);
        _popup.add(deleteTop);
        _popup.add(valueTop);
        _popup.add(valueEdge);
        this.setComponentPopupMenu(_popup);
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
                _indexStart = _graphController.getIndexOfTop(x, y);
                if (State.AddLine == _state && _indexStart >= 0) {
                    _currentY = y;
                    _currentX = x;
                    _startPoint = _graphController.getPointOfTop(_indexStart);
                    panel.repaint();
                }
                if (State.RemoveLine == _state && _indexStart < 0 && _graphController.removeEdge(x, y)){
                    panel.repaint();
                }
                if (State.RemoveTop == _state && _indexStart >= 0) {
                    _graphController.removeTop(_indexStart);
                    panel.repaint();
                }
                if (State.AddTop == _state && _indexStart < 0 && _graphController.addTop(x, y)) {
                    panel.repaint();
                }
            } else if (evt.getButton() == 3) {
                _popup.show(panel, evt.getX(), evt.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            if (evt.getButton() == 1) {
                if (State.AddLine == _state && _indexStart >= 0) {
                    _indexEnd = _graphController.getIndexOfTop(evt.getX(), evt.getY());
                    if (_indexEnd >= 0 && _indexEnd != _indexStart) {
                        _graphController.createEdge(_indexStart, _indexEnd);
                    }
                    _indexEnd = -1;
                    _indexStart = -1;
                }
                if (State.ValueEdge == _state) {
                    _graphController.valueOfEdge(evt.getX(), evt.getY());
                }
                if (State.ValueTop == _state) {
                    _graphController.valueOfTop(evt.getX(), evt.getY());
                }
                panel.repaint();
            }
            _indexStart = -1;
        }


        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
            if (State.AddLine == _state && _indexStart >= 0) {
                _currentX = evt.getX();
                _currentY = evt.getY();
                panel.repaint();
            }
            if ((State.RemoveTop != _state && State.AddLine != _state) && _indexStart >= 0) {
                _graphController.replaceTop(_indexStart, evt.getX(), evt.getY());
                panel.repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) { //To change body of 
        }

    }

    public void SetMatrix(E[][] arr) {
        _graph.setMatrix(arr);
    }

    public E[][] GetMatrix(E obj) {
        return _graph.getMatrixValue((Class<E>) obj.getClass());
    }

    public boolean[][] getAdjMatrixBool() {
        return _graph.getMatrixBool();
    }

    public int countTop() {
        return _graph.countOfTop();
    }

    public T getTop(int i) {
        return _graph.getTop(i);
    }

    public E getEdge(int i, int j) {
        return _graph.getEdge(i, j);
    }

    public void setMatrix(List<GraphNode<E>>[] arr) {
        _graph.setMatrix(arr);
    }

    public void removeLine() {
        _state = State.RemoveLine;
    }

    public void addLine() {
        _state = State.AddLine;
    }

    public void addTop() {
        _state = State.AddTop;
    }

    public void removeTop() {
        _state = State.RemoveTop;
    }

    public void setValueEdge() {
        _state = State.ValueEdge;
    }

    public void setValueTop() {
        _state = State.ValueTop;
    }

    @Override
    public void paint (Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        _graphController.paint(g);
        if (State.AddLine == _state && _indexStart >= 0 ) {
            g.setColor(Color.BLACK);
            _view.drawEdge(_startPoint.x, _startPoint.y, _currentX, _currentY, _indexStart, -1, g);
        }
    }

    public void click()
    {
        _graphController.defaultPlace(getWidth(), getHeight());
        repaint();
    }
}
