package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.NodeArray;
import graphEdition.GraphCore.Graph;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * Класс для хранения ориентированного графа на основе двумерного массива, реализующий класс Graph
 * @param <T> Тип вершины
 * @param <E> Тип ребра
 * @see Graph
 * @author TNTrol
 */
public class ArrayDirectedGraph<T, E> extends ArrayGraph<T,E>
{
    @Override
    public boolean removeEdge(int indexTop1, int indexTop2)
    {
        if (indexTop1 < 0 || indexTop1 >= countOfTop() || indexTop2 < 0 || indexTop2 >= countOfTop())
            throw new IndexOutOfBoundsException();
        edges[indexTop1][indexTop2] = null;
        return true;
    }

    @Override
    public boolean addEdge(E value, int indexTop1, int indexTop2)
    {
        if (indexTop1 < 0 || indexTop1 >= countOfTop() || indexTop2 < 0 || indexTop2 >= countOfTop())
            throw new IndexOutOfBoundsException();
        if(edges[indexTop1][indexTop2] != null)
            return false;
        NodeArray<E> edge = new NodeArray<>(value);
        edges[indexTop1][indexTop2] = edge;
        return true;
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
