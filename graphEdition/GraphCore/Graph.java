/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition.GraphCore;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.GraphNode;
import graphEdition.AuxiliarySets.TopGraph;
import shaforostov.vadim.SecondFrame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @param <T>
 * @param <E>
 * @author user
 */
public abstract class Graph<T, E> {

    List<TopGraph<T>> tops = new ArrayList<>();

    public abstract E[][] getMatrixValue(Class<E> clazz);

    public abstract  boolean[][] getMatrixBool();

    public abstract void setMatrix(E[][] adjMatrix);

    public abstract <A extends GraphNode<E>> void setMatrix(List<A>[] adjMatrix);

    public int countOfTop()
    {
        return tops.size();
    }

    public T getTop(int indexTop)
    {
        if (indexTop < 0 || indexTop >= tops.size())
            throw new IndexOutOfBoundsException();
        return tops.get(indexTop).getValue();
    }

    public abstract E getEdge(int i, int j);

    public abstract boolean existEdge(int i, int j);

    public void addTop(T value)
    {
        tops.add(new TopGraph<T>(value));
    }

    public abstract void addEdge(E value, int i, int j);

    public boolean removeTop(int indexTop)
    {
        if (indexTop < 0 || indexTop >= tops.size())
            throw new IndexOutOfBoundsException();
        tops.remove(indexTop);
        return true;
    }

    public abstract boolean removeEdge(int i, int j);

    public void setTop(T value, int top)
    {
        if (top < 0 || top >= tops.size())
            throw new IndexOutOfBoundsException();
        tops.get(top).setValue(value);
    }

    public abstract void setEdge(E value, int i, int j);

    public abstract Iterable<Integer> row(int indexRow);

    TopGraph<T> getTopGraphByIndex(int indexTop)
    {
        if (indexTop < 0 || indexTop >= tops.size())
            throw new IndexOutOfBoundsException();
        return tops.get(indexTop);
    }

    public void clear()
    {
        tops.clear();
    }

    public abstract Iterable<Edge> allEdges();

}
