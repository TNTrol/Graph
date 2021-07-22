/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition.AuxiliarySets;

/**
 * Интерфейс для создания элемента разряженного графа
 * @author tntrol
 */
public interface GraphNode<E> {
    /**
     * Метод для получения индекса вершины, с которым связанно ребро
     * @return
     */
    int getIndexOfTop();

    /**
     * Метод для получения значения ребра
     * @return
     */
    E getValue();
}
