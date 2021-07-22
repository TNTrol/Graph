package graphEdition.AuxiliarySets;

/**
 * Класс для хранения индексов смежных вершин
 * @author TNTrol
 */
public class Edge
{
    public int indexTop1, indexTop2;

    public Edge(){}
    public Edge(int indexTop1, int indexTop2)
    {
        this.indexTop1 = indexTop1;
        this.indexTop2 = indexTop2;
    }
}
