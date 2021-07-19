/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition.MVCGraph;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.GraphNode;
import graphEdition.AuxiliarySets.TopGraph;

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

    public abstract <A extends GraphNode> void setMatrix(LinkedList<A>[] arr);

    public int countOfTop()
    {
        return tops.size();
    }

    public T getTop(int i)
    {
        return tops.get(i).getValue();
    }

    public abstract E getEdge(int i, int j);

    public abstract boolean existEdge(int i, int j);

    public void addTop(T t)
    {
        tops.add(new TopGraph<T>(t));
    }

    public abstract void addEdge(E e, int i, int j);

    public boolean removeTop(int i)
    {
        tops.remove(i);
        return true;
    }

    public abstract boolean removeEdge(int i, int j);

    public void setTop(T value, int i)
    {
        tops.get(i).setValue(value);
    }

    public abstract void setEdge(E value, int i, int j);

    public abstract Iterable<Integer> row(int i);
    //Iterable<Integer> allEdges();
    TopGraph<T> getTopGraphByIndex(int top)
    {
        return tops.get(top);
    }

    public void clear()
    {
        tops.clear();
    }

    public abstract Iterable<Edge> allEdges();
    //public void Value(T t);
}
