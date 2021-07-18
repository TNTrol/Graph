package graphEdition.Nodes;

import graphEdition.Nodes.Node;

public class NodeArray <T> implements Node<T>
{
    private T value;

    public NodeArray(T value)
    {
        this.value = value;
    }

    public NodeArray()
    {}
    @Override
    public T getValue()
    {
        return value;
    }

    @Override
    public void setValue(T value)
    {
        this.value = value;
    }
}
