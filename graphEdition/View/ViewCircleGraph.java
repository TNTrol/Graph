package graphEdition.View;

import graphEdition.GraphCore.ViewGraph;

import java.awt.*;

/**
 * Класс отображающий вершины графа в вииде кругов и индексов вершин и ребра в виде линий
 * @param <T> Тип вершины
 * @param <E> Тип ребра
 * @author TNTrol
 */
public class ViewCircleGraph<T, E> implements ViewGraph<T, E, Graphics>
{
    public int R = 10, D = 10;
    @Override
    public boolean checkEdge(int x1, int y1, int x2, int y2, int x, int y) {
        if (x1 >= x && x >= x2 || x1 <= x && x <= x2) {
            if((x1 - x2) * (x1 - x2) > D * D * 2 ) {
                if (x1 < x2)
                    return 0 <= (y - y1 + D) * (x2 - x1) - (x - x1) * (y2 - y1) && 0 >= (y - y1 - D) * (x2 - x1) - (x - x1) * (y2 - y1);
                return 0 <= (y - y2 + D) * (x1 - x2) - (x - x2) * (y1 - y2) && 0 >= (y - y2 - D) * (x1 - x2) - (x - x2) * (y1 - y2);
            }else{
                if (x1 < x2)
                    return 0 <= (y - y1) * (x2 - x1) - (x - x1 + D) * (y2 - y1) && 0 >= (y - y1 ) * (x2 - x1) - (x - x1 - D) * (y2 - y1);
                return 0 <= (y - y2 ) * (x1 - x2) - (x - x2 + D) * (y1 - y2) && 0 >= (y - y2 ) * (x1 - x2) - (x - x2 - D) * (y1 - y2);
            }
        }
        return false;
    }

    @Override
    public boolean checkPoint(int x1, int y1, int x, int y) {
        return ((y - y1) * (y - y1) + (x - x1) * (x - x1) <= R * R);
    }

    @Override
    public void drawEdge(int x1, int y1, int x2, int y2, int start, int end, Graphics g) {
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawTop(int x, int y, int number, Graphics g) {
        String str = Integer.toString(number);
        g.drawOval(x - R, y - R, 2 * R, 2 * R);
        g.drawChars(str.toCharArray(), 0, str.length(), x - 5, y - 5);
    }

    @Override
    public T addValueToTop(int i)
    {
        return null;
    }

    @Override
    public E addValueToEdge(int i, int j)
    {
        return null;
    }

    @Override
    public void showTop(int i){}

    @Override
    public void showEdge(int i, int j){}
}
