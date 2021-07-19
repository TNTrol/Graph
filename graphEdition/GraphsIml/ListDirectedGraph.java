package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.NodeList;

import java.util.Iterator;

public class ListDirectedGraph<T, E> extends ListGraph<T, E>
{

    @Override
    public boolean removeEdge(int i, int j)
    {
        if (edges[i] == null) {
            return false;
        }
        for (Iterator<NodeList<E>> it = edges[i].iterator(); it.hasNext();) {
            if (it.next().top == j) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public void addEdge(E e, int i, int j)
    {
        for (Iterator<NodeList<E>> it = edges[i].iterator(); it.hasNext();)
            if(it.next().top == j)
                return;
        edges[i].add(new NodeList<E>(e, j));
    }

    @Override
    public void setEdge(E value, int i, int j)
    {
        for (NodeList<E> node: edges[i]) {
            if(node.top == j)
            {
                node.setValue(value);
                return;
            }
        }
    }

    @Override
    public Iterable<Edge> allEdges()
    {
        int i = 0, size = countOfTop();
        Iterator<NodeList<E>> it1 = null;
        for (; i < size; i++)
        {
            it1 = edges[i].iterator();
            if (it1.hasNext())
                break;
        }
        Iterator<NodeList<E>> finalIt = it1;
        int finalI = i;
        return () -> new Iterator<Edge>()
        {
            private int row = finalI;
            private Iterator<NodeList<E>> it = finalIt;
            @Override
            public boolean hasNext()
            {
                return it != null && it.hasNext();
            }

            @Override
            public Edge next()
            {
                Edge edge = new Edge(row, it.next().top);
                while (!it.hasNext() && ++row < size) {
                    it = edges[row].iterator();
                }
                return edge;
            }
        };
    }
}
