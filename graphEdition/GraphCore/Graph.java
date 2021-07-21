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

    public abstract void setMatrix(E[][] arr);

    public abstract <A extends GraphNode<E>> void setMatrix(List<A>[] arr);

    public int countOfTop()
    {
        return tops.size();
    }

    public T getTop(int top)
    {
        if (top < 0 || top >= tops.size())
            throw new IndexOutOfBoundsException();
        return tops.get(top).getValue();
    }

    public abstract E getEdge(int i, int j);

    public abstract boolean existEdge(int i, int j);

    public void addTop(T t)
    {
        tops.add(new TopGraph<T>(t));
    }

    public abstract void addEdge(E e, int i, int j);

    public boolean removeTop(int top)
    {
        if (top < 0 || top >= tops.size())
            throw new IndexOutOfBoundsException();
        tops.remove(top);
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

    public abstract Iterable<Integer> row(int i);

    TopGraph<T> getTopGraphByIndex(int top)
    {
        if (top < 0 || top >= tops.size())
            throw new IndexOutOfBoundsException();
        return tops.get(top);
    }

    public void clear()
    {
        tops.clear();
    }

    public abstract Iterable<Edge> allEdges();

}
