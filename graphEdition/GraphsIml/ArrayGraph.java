package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.GraphNode;
import graphEdition.MVCGraph.Graph;
import graphEdition.AuxiliarySets.NodeArray;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;

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
    public void setMatrix(E[][] arr)
    {
        if (arr != null) {
            int c = (int) Math.sqrt(arr.length);
            int high = (arr.length % c) == 0 ? arr.length / c : (arr.length - c * c) / c + c + 1;
            edges = new NodeArray[arr.length][arr.length];
            clear();
            for (int i = 0; i < high; i++) {
                for (int j = 0; j < c; j++) {
                    super.addTop(null);
                    if (arr.length % c != 0 && i == high - 1 && arr.length % c - 1 == j) {
                        break;
                    }
                }
            }
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    edges[i][j] = arr[i][j] == null ? null : new NodeArray(arr[i][j]);
                }
            }
        }
    }

    @Override
    public <A extends GraphNode> void setMatrix(LinkedList<A>[] arr)
    {
        if (arr == null) {
            return;
        }
        int c = (int) Math.sqrt(arr.length);
        int high = (arr.length % c) == 0 ? arr.length / c : (arr.length - c * c) / (c + 1) + c + 1;
        clear();
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < c; j++) {
                super.addTop(null);
                if (arr.length - c * high != 0 && i == high - 1 && arr.length % c - 1 == j) {
                    break;
                }
            }
        }
        edges = new NodeArray[arr.length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                for (A temp : arr[i]) {
                    edges[i][temp.getIndexOfTop()] = new NodeArray(temp.getValue());
                }
            }
        }
    }

    @Override
    public E getEdge(int i, int j)
    {
        if (i >= countOfTop() || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        return edges[i][j].getValue();
    }

    @Override
    public boolean existEdge(int i, int j)
    {
        return edges[i][j] != null;
    }

    @Override
    public void addTop(T t)
    {
        super.addTop(t);
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
    public void addEdge(E e, int i, int j)
    {
        if(edges[i][j] != null)
            return;
        NodeArray<E> edge = new NodeArray<>(e);
        edges[i][j] = edge;
        edges[j][i] = edge;
    }

    @Override
    public boolean removeTop(int x)
    {
        int size = countOfTop();
        if (x >= size)
            return false;
        for (int i = 0; i < size; i++) {
            for (int j = x; j < size; j++) {
                edges[i][j] = j < size - 1 ? edges[i][j + 1] : null;
            }
        }
        for (int i = x; i < size; i++) {
            edges[i] = i < size - 1 ? edges[i + 1] : new NodeArray[edges.length];
        }
        super.removeTop(x);
        return true;
    }

    @Override
    public boolean removeEdge(int i, int j)
    {
        if(i >= countOfTop() || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        edges[i][j] = null;
        edges[j][i] = null;
        return true;
    }

    @Override
    public void setEdge(E value, int i, int j)
    {
        if(edges[i][j] != null)
            edges[i][j].setValue(value);
    }

    @Override
    public Iterable<Integer> row(int row)
    {
        return () -> new Iterator<Integer>()
        {
            private int ind = -1;
            @Override
            public boolean hasNext()
            {
                if(ind < -1)
                    return false;
                for (int i = ind + 1; i < countOfTop() ; i++)
                {
                    if( edges[row][i] != null)
                    {
                        ind = i;
                        return true;
                    }
                }
                ind = -2;
                return false;
            }

            @Override
            public Integer next()
            {
                if(ind < -1)
                    throw new NullPointerException();
                return ind;
            }
        };
    }
}
