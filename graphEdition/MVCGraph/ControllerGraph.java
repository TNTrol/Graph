package graphEdition.MVCGraph;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.TopGraph;

import java.awt.*;

public class ControllerGraph<T, E, G>
{
    protected Graph<T, E> graph;
    protected ViewGraph<T, E, G> view;

    public ControllerGraph(Graph<T, E> graph, ViewGraph<T, E, G> view)
    {
        this.graph = graph;
        this.view = view;
    }

    public int getIndexOfTop(int x, int y) {
        for (int i = 0; i < graph.countOfTop(); i++) {
            if (view.checkPoint(graph.getTopGraphByIndex(i).getX(), graph.getTopGraphByIndex(i).getY(), x, y)) {
                return i;
            }
        }
        return -1;
    }

    private Edge getEdge(int x, int y) {
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

    public Point getPointOfTop(int top)
    {
        TopGraph topGraph = graph.getTopGraphByIndex(top);
        return new Point(topGraph.getX(), topGraph.getY());
    }

    public boolean removeTop(int x, int y)
    {
        int i = getIndexOfTop(x,y);
        if(i < 0)
            return false;
        graph.removeTop(i);
        return true;
    }

    public void removeTop(int indexTop)
    {
        graph.removeTop(indexTop);
    }

    public boolean addTop(int x, int y)
    {
        int i = graph.countOfTop();
        graph.addTop(view.addValueToTop(i));
        graph.getTopGraphByIndex(i).setCoordinates(x, y);
        return true;
    }


    public T valueOfTop(int x, int y)
    {
        int i = getIndexOfTop(x,y);
        if(i >= 0)
            return graph.getTop(i);
        return null;
    }

    public void createEdge(int top1, int top2)
    {
        graph.addEdge(view.addValueToEdge(top1, top2), top1, top2);
        view.showEdge(top1, top2);
    }

    public boolean removeEdge(int x, int y)
    {
        Edge edge = getEdge(x, y);
        if(edge == null)
            return false;
        graph.removeEdge(edge.indexTop1, edge.indexTop2);
        return true;
    }

    public E valueOfEdge(int x, int y)
    {
        Edge edge = getEdge(x, y);
        if (edge != null)
            return graph.getEdge(edge.indexTop1, edge.indexTop2);
        return null;
    }

    public void replaceTop(int top, int x, int y)
    {
        graph.getTopGraphByIndex(top).setCoordinates(x, y);
    }

    public void paint(G g)
    {
        for (int i = 0; i < graph.countOfTop(); i++) {
            for (Integer j : graph.row(i)) {
                TopGraph top1 = graph.getTopGraphByIndex(i);
                TopGraph top2 = graph.getTopGraphByIndex(j);
                view.drawEdge(top1.getX(), top1.getY(), top2.getX(), top2.getY(), i, j, g);
            }
        }
        for (int i = 0; i < graph.countOfTop(); i++) {
            TopGraph top = graph.getTopGraphByIndex(i);
            view.drawTop(top.getX(), top.getY(), i, g);
        }
    }

    public void defaultPlace(int w, int h)
    {
        int size = graph.countOfTop();
        int c = (int) Math.sqrt(size);
        int top = 0;
        int high = (size % c) == 0 ? size / c : (size - c * c) / (c + 1) + c + 1;
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < c; j++) {
                graph.getTopGraphByIndex(top).setCoordinates (w / (c + 1) * (j + 1), h / (high + 1) * (i + 1));
                top++;
                if (size % c != 0 && i == high - 1 && size % c - 1 == j) {
                    break;
                }
            }
        }
    }
}
