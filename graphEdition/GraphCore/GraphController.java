package graphEdition.GraphCore;

import graphEdition.AuxiliarySets.Edge;
import graphEdition.AuxiliarySets.TopGraph;

import java.awt.*;

/**
 * Класс для корректной визуализации графа
 * @param <T> Тип вершины графа
 * @param <E> Тип ребер графа
 * @param <G> Тип объектна, с помощью которого будет производится отрисовка
 * @author TNTrol
 */
public class GraphController<T, E, G>
{
    /**
     * Поле графа
     * @see Graph
     */
    protected Graph<T, E> graph;
    /**
     * Поле отображения
     * @see ViewGraph
     */
    protected ViewGraph<T, E, G> view;

    /**
     *
     * @param graph Граф, который нужно визуализировать
     * @param view Визуальная прослойка для взаимодествия с пользователем
     */
    public GraphController(Graph<T, E> graph, ViewGraph<T, E, G> view)
    {
        this.graph = graph;
        this.view = view;
    }

    /**
     * Метод для получения индекса вершины по координатам, если вершины нет, то -1
     * @param x х состовляющая точки
     * @param y у состовляющая точки
     * @return
     */
    public int getIndexOfTop(int x, int y) {
        for (int i = 0; i < graph.countOfTop(); i++) {
            if (view.checkPoint(graph.getTopGraphByIndex(i).getX(), graph.getTopGraphByIndex(i).getY(), x, y)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Метод для получения ребра по координатам, если нет такого ребра то вернет null
     * @param x х состовляющая точки
     * @param y у состовляющая точки
     * @return
     */
    public Edge getEdge(int x, int y) {
        for (Edge edge : graph.allEdges())
        {
            TopGraph top1 = graph.getTopGraphByIndex(edge.indexTop1);
            TopGraph top2 = graph.getTopGraphByIndex(edge.indexTop2);
            if (view.checkEdge(top1.getX(), top1.getY(), top2.getX(), top2.getY(), x, y)) {
                    return edge;
            }
        }
        return null;
    }

    /**
     * Метод для возвращения координат типа Point вершины по её индексу
     * @param top Индекс вершины
     * @return
     * @see Point
     */
    public Point getPointOfTop(int top)
    {
        TopGraph topGraph = graph.getTopGraphByIndex(top);
        return new Point(topGraph.getX(), topGraph.getY());
    }

    /**
     * Метод для получения вершины типа TopGraph по её индексу
     * @param indexTop Индекс вершины
     * @return
     * @see TopGraph
     */
    public TopGraph<T> getTopGraph(int indexTop)
    {
        return graph.getTopGraphByIndex(indexTop);
    }

    /**
     * Метод для удалеия вершины по ёё индексу
     * @param indexTop Индекс вершины
     */
    public void removeTop(int indexTop)
    {
        graph.removeTop(indexTop);
    }

    /**
     * Метод для создания новой вершины в заданных координатах
     * @param x х состовляющая точки
     * @param y у состовляющая точки
     * @return
     */
    public boolean addTop(int x, int y)
    {
        int i = graph.countOfTop();
        graph.addTop(view.addValueToTop(i));
        graph.getTopGraphByIndex(i).setCoordinates(x, y);
        return true;
    }

    /**
     * Вывести значение вершины по координатам во ViewGraph
     * @param x х состовляющая точки
     * @param y у состовляющая точки
     * @see ViewGraph
     */
    public void valueOfTop(int x, int y)
    {
        int i = getIndexOfTop(x,y);
        if(i >= 0)
            view.showTop(i);
    }

    /**
     * Метод для создание ребра между вершинами по их индексам, Запрашивает значение у View и выводит индексы ребра
     * @param indexTop1 Первая вершина
     * @param indexTop2 Вторая вершина
     */
    public void createEdge(int indexTop1, int indexTop2)
    {
        graph.addEdge(view.addValueToEdge(indexTop1, indexTop2), indexTop1, indexTop2);
        view.showEdge(indexTop1, indexTop2);
    }

    /**
     * Метод для удаления ребра по точке приндлежащей этому ребру
     * @param x
     * @param y
     * @return
     */
    public boolean removeEdge(int x, int y)
    {
        Edge edge = getEdge(x, y);
        if(edge == null)
            return false;
        graph.removeEdge(edge.indexTop1, edge.indexTop2);
        return true;
    }

    /**
     * Метод для получения индексов вершин, которые соединияет ребро
     * @param x
     * @param y
     */
    public void valueOfEdge(int x, int y)
    {
        Edge edge = getEdge(x, y);
        if (edge != null)
            view.showEdge(edge.indexTop1, edge.indexTop2);
    }

    /**
     * Метод для перемещения вершины в определенные координаты
     * @param top Индекс вершины
     * @param x
     * @param y
     */
    public void replaceTop(int top, int x, int y)
    {
        graph.getTopGraphByIndex(top).setCoordinates(x, y);
    }

    /**
     * Метод для отрисовки всего графа
     * @param g Объект с помощью которого можно отрисовать
     */
    public void paint(G g)
    {
        for (Edge edge : graph.allEdges())
        {
            TopGraph top1 = graph.getTopGraphByIndex(edge.indexTop1);
            TopGraph top2 = graph.getTopGraphByIndex(edge.indexTop2);
            view.drawEdge(top1.getX(), top1.getY(), top2.getX(), top2.getY(), edge.indexTop1, edge.indexTop2, g);
        }
        for (int i = 0; i < graph.countOfTop(); i++) {
            TopGraph top = graph.getTopGraphByIndex(i);
            view.drawTop(top.getX(), top.getY(), i, g);
        }
    }

    /**
     * Метод для задания дефолтного расположения вершин по области
     * @param w Ширина области
     * @param h Высота области
     */
    public void defaultPlace(int w, int h)
    {
        int size = graph.countOfTop();
        int c = (int) Math.sqrt(size);
        int top = 0;
        int high = (size % c) == 0 ? size / c : (size - c * c) / (c + 1) + c + 1;
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < c; j++) {
                graph.getTopGraphByIndex(top).setCoordinates (w / (c + 1) * (j + 1), h / (high + 1) * (i + 1));
                top++;
                if (size % c != 0 && i == high - 1 && size % c - 1 == j) {
                    break;
                }
            }
        }
    }
}
