package graphEdition.GraphsIml;

import graphEdition.AuxiliarySets.NodeArray;

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
}
