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
 * Абстрактный класс, с помощью которого можно совершать стандартные действия над графом (добавление вершин, ребер и т.д.), а еще и визуализировать.
 * @param <T> - тип вершин графа
 * @param <E> - тип ребер графа
 * @author TNTrol
 */
public abstract class Graph<T, E> {

    /**
     * Пакетная переменная для хранения всех вершин
     */
    List<TopGraph<T>> tops = new ArrayList<>();

    /**
     * Метод для получение матрицы смежности, содержащая значения типа Е
     * @param clazz Метакласс для создания матрицы
     * @return
     */
    public abstract E[][] getAdjMatrixValue(Class<E> clazz);

    /**
     * Метод для получения матрицы смежности типа boolean
     * @return
     */
    public abstract  boolean[][] getAdjMatrixBool();

    /**
     * Записывает граф из матрицы смежности типа Е
     * @param adjMatrix Матрица смежности типа Е
     */
    public abstract void setAdjMatrix(E[][] adjMatrix);

    /**
     * Записывает граф из разряженной матрицы смежности
     * @param adjMatrix - Разряженная матрица смежности
     * @param <A> - Тип данных реализующий интерфейс
     * @see GraphNode
     */
    public abstract <A extends GraphNode<E>> void setAdjMatrix(List<A>[] adjMatrix);

    /**
     * Метод для получения кол-ва вершин в графе
     * @return - возвращает кол-во вершин
     */
    public int countOfTop()
    {
        return tops.size();
    }

    /**
     * Метод для получения значения вершины по её индексу
     * @param indexTop индекс вершины
     * @return
     */
    public T getTop(int indexTop)
    {
        if (indexTop < 0 || indexTop >= tops.size())
            throw new IndexOutOfBoundsException();
        return tops.get(indexTop).getValue();
    }

    /**
     * Метод для получения значения ребра по индексам начальной и конечной вершины, если такого ребра нет, то возвращает null
     * @param indexTop1 Первая вершина
     * @param indexTop2 Вторая вершина
     * @return
     */
    public abstract E getEdge(int indexTop1, int indexTop2);

    /**
     * Метод для проверки на существование ребра
     * @param indexTop1 Первая вершина
     * @param indexTop2 Вторая вершина
     * @return
     */
    public abstract boolean existEdge(int indexTop1, int indexTop2);

    /**
     * Метод для добавление вершины
     * @param value Значение вершины
     */
    public void addTop(T value)
    {
        tops.add(new TopGraph<T>(value));
    }

    /**
     * Метод для добавление ребра, если такое ребро есть, то нечего не происходит
     * @param value Значение ребра
     * @param indexTop1 Первая вершина
     * @param indexTop2 Вторая вершина
     */
    public abstract boolean addEdge(E value, int indexTop1, int indexTop2);

    /**
     * Метод для удаления вершины, если вершина удалена, то вернет true
     * @param indexTop Индекс вершины
     * @return
     */
    public boolean removeTop(int indexTop)
    {
        if (indexTop < 0 || indexTop >= tops.size())
            throw new IndexOutOfBoundsException();
        tops.remove(indexTop);
        return true;
    }

    /**
     * Метод для удаления ребра по индексам двух вершин, если такое ребро существует, то будет удалено и вернется true
     * @param indexTop1 Первая вершина
     * @param indexTop2 Вторая вершина
     * @return Если ребро успешно удалилось, то вернется true
     */
    public abstract boolean removeEdge(int indexTop1, int indexTop2);

    /**
     * Метод для изменения значения у вершины по индексу
     * @param value Новое значение вершины
     * @param indexTop Индекс вершины
     */
    public void setValueOfTop(T value, int indexTop)
    {
        if (indexTop < 0 || indexTop >= tops.size())
            throw new IndexOutOfBoundsException();
        tops.get(indexTop).setValue(value);
    }

    /**
     * Метод для изменения значения ребра по индексам вершин
     * @param value Новое значение ребра
     * @param indexTop1 Первая вершина
     * @param indexTop2 Вторая вершина
     */
    public abstract void setValueOfEdge(E value, int indexTop1, int indexTop2);

    /**
     * Метод для получение объекта типа Iterable для итерации по всем смежным ребрам заданной вершины
     * @param indexTop Индекс вершины
     * @return
     */
    public abstract Iterable<Integer> row(int indexTop);

    /**
     * Пакетный метод для получения вершины с ее координатами
     * @param indexTop Индекс вершины графа
     * @return
     * @see Iterable
     */
    TopGraph<T> getTopGraphByIndex(int indexTop)
    {
        if (indexTop < 0 || indexTop >= tops.size())
            throw new IndexOutOfBoundsException();
        return tops.get(indexTop);
    }

    /**
     * Метод для очистки всего графа
     */
    public void clear()
    {
        tops.clear();
    }

    /**
     * Метод для получение объекта типа Iterable для итерации по всем ребрам графа
     * @return
     * @see Iterable
     */
    public abstract Iterable<Edge> allEdges();

}
