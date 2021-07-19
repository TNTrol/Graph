package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.NodeList;

import java.util.Iterator;

public class ListDirectedGraph<T, E> extends ListGraph<T, E>
{

    @Override
    public boolean removeEdge(int i, int j)
    {
        if (edge[i] == null) {
            return false;
        }
        for (Iterator<NodeList<E>> it = edge[i].iterator(); it.hasNext();) {
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
        for (Iterator<NodeList<E>> it = edge[i].iterator(); it.hasNext();)
            if(it.next().top == j)
                return;
        edge[i].add(new NodeList<E>(e, j));
    }

    @Override
    public void setEdge(E value, int i, int j)
    {
        for (NodeList<E> node: edge[i]) {
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
        return () -> new Iterator<Edge>()
        {
            private int row = 0;
            private Iterator<NodeList<E>> it = 0 < countOfTop()? edge[0].iterator(): null;
            @Override
            public boolean hasNext()
            {
                if(row < -1 || it == null)
                    return false;
                while (true) {
                    if (!it.hasNext()) {
                        if (++row >= countOfTop()) {
                            row = -2;
                            return false;
                        }
                        it = edge[row].iterator();
                        continue;
                    }
                    return true;
                }
            }

            @Override
            public Edge next()
            {
                NodeList<E> node = it.next();
                return new Edge(row, node.top);
            }
        };
    }
}
