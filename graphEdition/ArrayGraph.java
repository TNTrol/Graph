package graphEdition;

import graphEdition.Nodes.NodeArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
            E[][] arr = (E[][]) Array.newInstance(clazz, tops.size(), tops.size());
            for (int i = 0; i < tops.size(); i++) {
                for (int j = 0; j < tops.size(); j++) {
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
        boolean[][] arr = new boolean[tops.size()][tops.size()];
        for (int i = 0; i < tops.size(); i++) {
            for (int j = i; j < tops.size(); j++) {
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
            tops = new ArrayList<>();
            for (int i = 0; i < high; i++) {
                for (int j = 0; j < c; j++) {
                    tops.add(new TopGraph<>(0, 0));
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
        tops = new ArrayList<>();
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < c; j++) {
                tops.add(new TopGraph<>(0, 0));
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
        if (i >= tops.size() || j >= tops.size())
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
        TopGraph<T> top = new TopGraph<>();
        top.setValue(t);
        tops.add(top);
        if (tops.size() > edges.length) {
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
        int size = tops.size();
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
        tops.remove(x);
        return true;
    }

    @Override
    public boolean removeEdge(int i, int j)
    {
        if(i >= tops.size() || j >= tops.size())
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
                for (int i = ind + 1; i < tops.size(); i++)
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
