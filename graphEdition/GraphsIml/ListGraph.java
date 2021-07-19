package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.GraphNode;
import graphEdition.AuxiliarySets.NodeArray;
import graphEdition.MVCGraph.Graph;
import graphEdition.AuxiliarySets.NodeList;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ListGraph<T, E> extends Graph<T, E>
{
    protected List<NodeList<E>>[] edge = new LinkedList[10];
    @Override
    public E[][] getMatrixValue(Class<E> clazz)
    {
        if (clazz != null) {
            int size = countOfTop();
            E[][] arr = (E[][]) Array.newInstance(clazz, size, size);
            for (int i = 0; i < size; i++) {
                if (edge[i] != null) {
                    for (NodeList node : edge[i]) {
                        arr[i][node.top] = (E) node.getValue();
                        arr[node.top][i] = (E) node.getValue();
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
            if (edge[i] != null) {
                for (NodeList node : edge[i]) {
                    arr[i][node.top] = true;
                    arr[node.top][i] = true;
                }
            }
        }
        return arr;
    }

    @Override
    public void setMatrix(E[][] arr)
    {

    }

    @Override
    public <A extends GraphNode> void setMatrix(LinkedList<A>[] arr)
    {
        if (arr == null) {
            return;
        }
        clear();
        for (int i = 0; i < arr.length; i++) {
                super.addTop(null);
        }
        edge = new LinkedList[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                for (A temp : arr[i]) {
                    if (edge[i] == null) {
                        edge[i] = new LinkedList();
                    }
                        edge[i].add(new NodeList<E>((E) temp.getValue(), temp.getIndexOfTop()));

                }
            }
        }
    }

    @Override
    public E getEdge(int i, int j)
    {
        for (NodeList<E> node: edge[i]) {
            if(node.top == j)
                return node.getValue();
        }
        return null;
    }

    @Override
    public boolean existEdge(int i, int j)
    {
        for (NodeList<E> node: edge[i]) {
            if(node.top == j)
                return true;
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
        edge[j].add(new NodeList<E>(e, i));
    }

    @Override
    public boolean removeEdge(int i, int j)
    {
        if (edge[i] == null) {
            return false;
        }
        for (Iterator<NodeList<E>> it = edge[i].iterator(); it.hasNext();) {
            if (it.next().top == j) {
                it.remove();
                for (Iterator<NodeList<E>> it2 = edge[j].iterator(); it2.hasNext();) {
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
        for (NodeList<E> node: edge[i]) {
            if(node.top == j)
            {
                node.setValue(value);
                return;
            }
        }
        for (NodeList<E> node: edge[j]) {
            if(node.top == i)
            {
                node.setValue(value);
                return;
            }
        }
    }

    @Override
    public Iterable<Integer> row(int row)
    {
        return () -> new Iterator<Integer>()
        {
            private Iterator<NodeList<E>> it = edge[row].iterator();

            @Override
            public boolean hasNext()
            {
                return it.hasNext();
            }

            @Override
            public Integer next()
            {
                return it.next().top;
            }
        };
    }

    @Override
    public void addTop(T t)
    {
        super.addTop(t);
        int size = countOfTop();
        if (size > edge.length) {
            List<NodeList<E>>[] arr = new LinkedList[edge.length * 2];
            for (int i = 0; i < edge.length; i++) {
                arr[i] = edge[i];
            }
            edge = arr;
        }
        edge[size - 1] = new LinkedList();
    }

    @Override
    public boolean removeTop(int top)
    {
        int size = countOfTop();
        for (int i = top; i < size; i++) {
            edge[i] = i != size - 1 ? edge[i + 1] : new LinkedList();
        }
        for (int i = 0; i < size; i++) {
            if (edge[i] != null) {
                for (Iterator<NodeList<E>> it = edge[i].iterator(); it.hasNext();) {
                    if (it.next().top == top) {
                        it.remove();
                        break;
                    }

                }

                for (NodeList node : edge[i]) {
                    if (node.top > top) {
                        node.top--;
                    }
                }
            }
        }

        super.removeTop(top);
        return true;
    }

    @Override
    public void clear()
    {
        super.clear();
        edge = new LinkedList[10];
    }

    @Override
    public Iterable<Edge> allEdges()
    {
        return null;
    }
}
