package graphEdition.GraphCore;

public interface ViewGraph<T, E, G>
{
    boolean checkEdge(int x1, int y1, int x2, int y2, int x, int y);
    boolean checkPoint(int x1, int y1, int x, int y);
    void drawEdge(int x1, int y1, int x2, int y2, int start, int end, G g);
    void drawTop(int x, int y, int number, G g);
    T addValueToTop(int i);
    E addValueToEdge(int i, int j);
    void showTop(int i);
    void showEdge(int i, int j);

}
