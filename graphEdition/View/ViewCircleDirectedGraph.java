package graphEdition.View;

import java.awt.*;

/**
 * Класс отображающий вершины ориентированного графа в вииде кругов и индексов вершин и ребра в виде линий cо стрелками
 * @param <T> Тип вершины
 * @param <E> Тип ребра
 * @author TNTrol
 */
public class ViewCircleDirectedGraph<T, E> extends ViewCircleGraph<T, E>
{
    @Override
    public void drawEdge(int x1, int y1, int x2, int y2, int start, int end, Graphics g) {
        g.drawLine(x1, y1, x2, y2);
        double angle = Math.atan2(x2 - x1, y2 - y1);
        g.drawLine(x2, y2, Math.toIntExact(Math.round(x2 - 10 * Math.sin(angle - 45))), Math.toIntExact(Math.round(y2 - 10 * Math.cos(angle - 45))));
        g.drawLine(x2, y2, Math.toIntExact(Math.round(x2 - 10 * Math.sin(angle + 45))), Math.toIntExact(Math.round(y2 - 10 * Math.cos(angle + 45))));
    }

}
