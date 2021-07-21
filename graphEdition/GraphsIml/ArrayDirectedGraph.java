package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.NodeArray;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDirectedGraph<T, E> extends ArrayGraph<T,E>
{
    @Override
    public boolean removeEdge(int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        edges[i][j] = null;
        return true;
    }

    @Override
    public void addEdge(E e, int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        NodeArray<E> edge = new NodeArray<>(e);
        edges[i][j] = edge;
    }

    @Override
    public Iterable<Edge> allEdges()
    {
        int i = 0, top = -1, size = countOfTop();
        for (; i < size; i++){
            for (int j = 0; j < size; j++) {
                if (edges[i][j] != null) {
                    top = j;
                    break;
                }
            }
            if(top >= 0)
                break;
        }
        int startIndex = top, startRow = i;
        return () -> new Iterator<Edge>()
        {
            private int row = startRow, index = startIndex;
            @Override
            public boolean hasNext()
            {
                return index >= 0;
            }

            @Override
            public Edge next()
            {
                if (index < 0)
                    throw new NoSuchElementException();
                Edge edge = new Edge(row, index);
                for(; row < size; row++) {
                    for (index++; index < size; index++)
                        if (edges[row][index] != null)
                            return edge;
                    index = -1;
                }
                return edge;
            }
        };
    }
}
