package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.NodeList;
import graphEdition.AuxiliarySets.NodeListWithWrapper;
import graphEdition.AuxiliarySets.Wrapper;

import java.util.Iterator;

public class ListDirectedGraph<T, E> extends ListGraph<T, E>
{

    @Override
    public boolean removeEdge(int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop()) {
            throw new IndexOutOfBoundsException();
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
    void createEdge(E value, int row, int top)
    {
        edges[row].add( new NodeList<>(value, top));
    }

    @Override
    public void addEdge(E value, int indexTop1, int indexTop2)
    {
        if (indexTop1 < 0 || indexTop1 >= countOfTop() || indexTop2 < 0 || indexTop2 >= countOfTop())
            throw new IndexOutOfBoundsException();
        for (Iterator<NodeList<E>> it = edges[indexTop1].iterator(); it.hasNext();)
            if(it.next().top == indexTop2)
                return;
        edges[indexTop1].add(new NodeList<E>(value, indexTop2));
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
                if(!hasNext())
                    throw new IndexOutOfBoundsException();
                Edge edge = new Edge(row, it.next().top);
                while (!it.hasNext() && ++row < size) {
                    it = edges[row].iterator();
                }
                return edge;
            }
        };
    }
}
