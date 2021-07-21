package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.*;
import graphEdition.GraphCore.Graph;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class ListGraph<T, E> extends Graph<T, E>
{
    protected List<NodeList<E>>[] edges = new LinkedList[10];
    @Override
    public E[][] getMatrixValue(Class<E> clazz)
    {
        if (clazz != null) {
            int size = countOfTop();
            E[][] arr = (E[][]) Array.newInstance(clazz, size, size);
            for (int i = 0; i < size; i++) {
                if (edges[i] != null) {
                    for (NodeList<E> node : edges[i]) {
                        arr[i][node.top] = node.getValue();
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
            if (edges[i] != null) {
                for (NodeList<E> node : edges[i]) {
                    arr[i][node.top] = true;
                }
            }
        }
        return arr;
    }

    @Override
    public void setMatrix(E[][] arr)
    {
        if (arr == null) {
            return;
        }
        clear();
        for (int i = 0; i < arr.length; i++)
        {
            if(arr.length != arr[i].length) {
                clear();
                throw new IndexOutOfBoundsException("count vertex don't equals count elements of " + i + " row");
            }
            super.addTop(null);
            if (edges[i] == null)
                edges[i] = new LinkedList<>();
            for (int j = 0; j < arr.length; j++)
            {
                if(arr[i][j] != null)
                    createEdge(arr[i][j], i, j);
            }
        }
    }

    void createEdge(E value, int row, int top)
    {
        if(top > row)
        {
            if(edges[top] == null)
                edges[top] = new LinkedList<>();
            Wrapper<E> wrapper = new Wrapper<>(value);
                edges[row].add( new NodeListWithWrapper<>(wrapper, top));
            edges[top].add( new NodeListWithWrapper<>(wrapper, row));
        }
    }

    @Override
    public <A extends GraphNode<E>> void setMatrix(List<A>[] arr)
    {
        if (arr == null) {
            return;
        }
        clear();
        for (int i = 0; i < arr.length; i++) {
                super.addTop(null);
        }
        edges = new LinkedList[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                if (edges[i] == null) {
                    edges[i] = new LinkedList();
                }
                for (GraphNode<E> temp : arr[i]) {
                        if(temp.getIndexOfTop() >= arr.length)
                        {
                            clear();
                            throw new IndexOutOfBoundsException("Count top of graph " + arr.length + " but find top # " + temp.getIndexOfTop());
                        }
                        createEdge(temp.getValue(), i, temp.getIndexOfTop());
                }
            }
        }
    }

    @Override
    public E getEdge(int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        for (NodeList<E> node: edges[i]) {
            if(node.top == j)
                return node.getValue();
        }
        return null;
    }

    @Override
    public boolean existEdge(int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        for (NodeList<E> node: edges[i]) {
            if(node.top == j)
                return true;
        }
        return false;
    }

    @Override
    public void addEdge(E e, int indexTop1, int indexTop2)
    {
        if (indexTop1 < 0 || indexTop1 >= countOfTop() || indexTop2 < 0 || indexTop2 >= countOfTop())
            throw new IndexOutOfBoundsException();
        for (Iterator<NodeList<E>> it = edges[indexTop1].iterator(); it.hasNext();)
            if(it.next().top == indexTop2)
                return;
        Wrapper<E> wrap = new Wrapper<>(e);
        edges[indexTop1].add(new NodeListWithWrapper<>(wrap, indexTop2));
        edges[indexTop2].add(new NodeListWithWrapper<>(wrap, indexTop1));
    }

    @Override
    public boolean removeEdge(int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        for (Iterator<NodeList<E>> it = edges[i].iterator(); it.hasNext();) {
            if (it.next().top == j) {
                it.remove();
                for (Iterator<NodeList<E>> it2 = edges[j].iterator(); it2.hasNext();) {
                    if (it2.next().top == i) {
                        it2.remove();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void setEdge(E value, int i, int j)
    {
        if (i < 0 || i >= countOfTop() || j < 0 || j >= countOfTop())
            throw new IndexOutOfBoundsException();
        for (NodeList<E> node: edges[i]) {
            if(node.top == j)
            {
                node.setValue(value);
                return;
            }
        }
    }

    @Override
    public Iterable<Integer> row(int row)
    {
        if (row < 0 || row >= countOfTop())
            throw new IndexOutOfBoundsException();
        return () -> new Iterator<>() {
            private Iterator<NodeList<E>> it = edges[row].iterator();

            @Override
            public boolean hasNext()
            {
                return it.hasNext();
            }

            @Override
            public Integer next()
            {
                if (!it.hasNext())
                    throw new NoSuchElementException();
                return it.next().top;
            }
        };
    }

    @Override
    public void addTop(T t)
    {
        super.addTop(t);
        int size = countOfTop();
        if (size > edges.length) {
            List<NodeList<E>>[] arr = new LinkedList[edges.length * 2];
            for (int i = 0; i < edges.length; i++) {
                arr[i] = edges[i];
            }
            edges = arr;
        }
        edges[size - 1] = new LinkedList();
    }

    @Override
    public boolean removeTop(int top)
    {
        int size = countOfTop();
        super.removeTop(top);
        for (int i = top; i < size; i++) {
            edges[i] = i != size - 1 ? edges[i + 1] : new LinkedList();
        }
        for (int i = 0; i < size; i++) {
            if (edges[i] != null) {
                for (Iterator<NodeList<E>> it = edges[i].iterator(); it.hasNext();) {
                    if (it.next().top == top) {
                        it.remove();
                        break;
                    }

                }

                for (NodeList node : edges[i]) {
                    if (node.top > top) {
                        node.top--;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void clear()
    {
        super.clear();
        edges = new LinkedList[10];
    }

    @Override
    public Iterable<Edge> allEdges()
    {
        int i = 0, size = countOfTop();
        Iterator<NodeList<E>> it1 = null;
        Edge edge1 = null;
        for (; i < size && edge1 == null; i++) {
            it1 = edges[i].iterator();
            while (it1.hasNext())
            {
                int t = it1.next().top;
                if(t > i) {
                    edge1 = new Edge(i, t);
                    break;
                }
            }
        }
        i--;

        Iterator<NodeList<E>> finalIt = it1;
        int finalI = i;
        Edge finalEdge = edge1;
        return () -> new Iterator<Edge>()
        {
            private int row = finalI;
            private Iterator<NodeList<E>> it = finalIt;
            private Edge edge = finalEdge;

            @Override
            public boolean hasNext()
            {
                return edge != null;
            }

            @Override
            public Edge next()
            {
                Edge temp = edge;
                edge = null;
                while (row < size)
                {
                    while (it.hasNext())
                    {
                        int top = it.next().top;
                        if(top > row)
                        {
                            edge = new Edge(row, top);
                            return temp;
                        }
                    }
                    it = ++row == size ? it : edges[row].iterator();
                }
                return temp;
            }
        };
    }
}
