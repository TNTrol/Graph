package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.GraphNode;
import graphEdition.GraphCore.Graph;
import graphEdition.AuxiliarySets.NodeArray;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Класс для хранения неориентированного графа на основе двумерного массива, реализующий класс Graph
 * @param <T> Тип вершины
 * @param <E> Тип ребра
 * @see Graph
 * @author tntrol
 */
public class ArrayGraph<T, E> extends Graph<T, E>
{
    protected NodeArray<E>[][] edges = new NodeArray[10][10];

    public ArrayGraph()
    { }
    @Override
    public E[][] getMatrixValue(Class<E> clazz)
    {
        if (clazz != null) {
            int size = countOfTop();
            E[][] arr = (E[][]) Array.newInstance(clazz, size, size);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (edges[i][j] != null) {
                        arr[i][j] = edges[i][j].getValue();
                    }
                }
            }
            return arr;
        }
        return null;
    }

    @Override
    public boolean[][] getMatrixBool()
    {
        int size = countOfTop();
        boolean[][] arr = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (edges[i][j] != null) {
                    arr[i][j] = true;
                    arr[j][i] = true;
                }
            }
        }
        return arr;
    }

    @Override
    public void setMatrix(E[][] adjMatrix)
    {
        if (adjMatrix != null) {
            edges = new NodeArray[adjMatrix.length][adjMatrix.length];
            clear();
            for (int i = 0; i < adjMatrix.length; i++) {
                    super.addTop(null);
            }
            for (int i = 0; i < adjMatrix.length; i++) {
                for (int j = 0; j < adjMatrix[0].length; j++) {
                    edges[i][j] = adjMatrix[i][j] == null ? null : new NodeArray(adjMatrix[i][j]);
                }
            }
        }
    }

    @Override
    public <A extends GraphNode<E>>  void setMatrix(List<A>[] adjMatrix)
    {
        if (adjMatrix == null) {
            return;
        }
        int c = (int) Math.sqrt(adjMatrix.length);
        int high = (adjMatrix.length % c) == 0 ? adjMatrix.length / c : (adjMatrix.length - c * c) / (c + 1) + c + 1;
        clear();
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < c; j++) {
                super.addTop(null);
                if (adjMatrix.length - c * high != 0 && i == high - 1 && adjMatrix.length % c - 1 == j) {
                    break;
                }
            }
        }
        edges = new NodeArray[adjMatrix.length][adjMatrix.length];
        for (int i = 0; i < adjMatrix.length; i++) {
            if (adjMatrix[i] != null) {
                for (GraphNode<E> temp : adjMatrix[i]) {
                    edges[i][temp.getIndexOfTop()] = new NodeArray(temp.getValue());
                }
            }
        }
    }

    @Override
    public E getEdge(int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        return edges[i][j] == null ? null : edges[i][j].getValue();
    }

    @Override
    public boolean existEdge(int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        return edges[i][j] != null;
    }

    @Override
    public void addTop(T value)
    {
        super.addTop(value);
        if (countOfTop() > edges.length) {
            NodeArray[][] arr = new NodeArray[edges.length * 2][edges.length * 2];
            for (int i = 0; i < edges.length; i++) {
                for (int j = 0; j < edges.length; j++) {
                    arr[i][j] = edges[i][j];
                }
            }
            edges = arr;
        }
    }

    @Override
    public void addEdge(E value, int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        NodeArray<E> edge = new NodeArray<>(value);
        edges[i][j] = edge;
        edges[j][i] = edge;
    }

    @Override
    public boolean removeTop(int indexTop)
    {
        int size = countOfTop();
        if (indexTop >= size || indexTop < 0)
            throw new IndexOutOfBoundsException();
        for (int i = 0; i < size; i++) {
            for (int j = indexTop; j < size; j++) {
                edges[i][j] = j < size - 1 ? edges[i][j + 1] : null;
            }
        }
        for (int i = indexTop; i < size; i++) {
            edges[i] = i < size - 1 ? edges[i + 1] : new NodeArray[edges.length];
        }
        super.removeTop(indexTop);
        return true;
    }

    @Override
    public boolean removeEdge(int i, int j)
    {
        if(i >= countOfTop() || i < 0 || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        edges[i][j] = null;
        edges[j][i] = null;
        return true;
    }

    @Override
    public void setEdge(E value, int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        if(edges[i][j] != null)
            edges[i][j].setValue(value);
    }

    @Override
    public Iterable<Integer> row(int indexRow)
    {
        if (indexRow < 0 || indexRow >= countOfTop())
            throw new IndexOutOfBoundsException();
        int index = -1, size = countOfTop();
        for (int i = 0; i < size; i++)
            if (edges[indexRow][i] != null)
            {
                index = i;
                break;
            }
        int finalI = index;
        return () -> new Iterator<>() {
            private int ind = finalI;

            @Override
            public boolean hasNext()
            {
                return ind >= 0;
            }

            @Override
            public Integer next()
            {
                if (ind < 0)
                    throw new NoSuchElementException();
                int t = ind;
                for (ind++; ind < size; ind++)
                    if (edges[indexRow][ind] != null) {
                        return t;
                    }
                ind = -1;
                return t;
            }
        };
    }

    @Override
    public void clear()
    {
        super.clear();
        edges = new NodeArray[10][10];
    }

    @Override
    public Iterable<Edge> allEdges()
    {
        int i = 0, top = -1, size = countOfTop();
        for (; i < size; i++){
            for (int j = i; j < size; j++) {
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
                        index = row;
                }
                index = -1;
                return edge;
            }
        };
    }
}
