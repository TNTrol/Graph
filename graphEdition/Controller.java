package graphEdition;

import graphEdition.View.ViewGraph;

public class Controller<T, E, G>
{
    protected Graph<T, E> graph;
    protected ViewGraph<T, E, G> view;

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

    public void removeTop(int x, int y)
    {
        int i = getIndexOfTop(x,y);
        if(i >= 0)
            graph.removeTop(i);
    }

    public void addTop(int x, int y)
    {
        int i = getIndexOfTop(x, y);
        if(i < 0)
            graph.addTop(view.addValueToTop(graph.countOfTop() - 1));
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
    }

    public void removeEdge(int x, int y)
    {
        Edge edge = getEdge(x, y);
        if(edge != null)
            graph.removeEdge(edge.indexTop1, edge.indexTop2);
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
}
