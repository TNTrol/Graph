package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.NodeArray;

import java.util.Iterator;

public class ArrayDirectedGraph<T, E> extends ArrayGraph<T,E>
{
    @Override
    public boolean removeEdge(int i, int j)
    {
        if(i >= countOfTop() || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        edges[i][j] = null;
        return true;
    }

    @Override
    public void addEdge(E e, int i, int j)
    {
        if(edges[i][j] != null)
            return;
        NodeArray<E> edge = new NodeArray<>(e);
        edges[i][j] = edge;
    }

    @Override
    public Iterable<Edge> allEdges()
    {
        return () -> new Iterator<Edge>()
        {
            private int row = 0, index = -1;
            @Override
            public boolean hasNext()
            {
                if(index < -1)
                    return false;
                while (true) {
                    if (row >= countOfTop()) {
                        index = -2;
                        return false;
                    }
                    int i = index + 1;
                    for (; i < countOfTop(); i++) {
                        if (edges[row][i] != null) {
                            index = i;
                            return true;
                        }
                    }
                    if (i == countOfTop()) {
                        index = -1;
                        row++;
                        continue;
                    }
                    return true;
                }
            }

            @Override
            public Edge next()
            {
                NodeArray node = edges[row][index];
                return new Edge(row, index);
            }
        };
    }
}
