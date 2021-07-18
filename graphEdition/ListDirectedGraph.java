package graphEdition;

import graphEdition.Nodes.NodeArray;
import graphEdition.Nodes.NodeList;

import java.util.Iterator;

public class ListDirectedGraph<T, E> extends ListGraph<T, E>
{

    @Override
    public boolean removeEdge(int i, int j)
    {
        if (edge[i] == null) {
            return false;
        }
        for (Iterator<NodeList<E>> it = edge[i].iterator(); it.hasNext();) {
            if (it.next().top == j) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public void addEdge(E e, int i, int j)
    {
        for (Iterator<NodeList<E>> it = edge[i].iterator(); it.hasNext();)
            if(it.next().top == j)
                return;
        edge[i].add(new NodeList<E>(e, j));
    }

    @Override
    public void setEdge(E value, int i, int j)
    {
        for (NodeList<E> node: edge[i]) {
            if(node.top == j)
            {
                node.setValue(value);
                return;
            }
        }
    }
}
