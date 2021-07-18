package graphEdition.Nodes;

public class NodeList<E> implements Node<E>
{
    private E value;
    public int top = -1;

    public NodeList(E value)
    {
        this.value = value;
    }

    public NodeList(E value, int top)
    {
        this.value = value;
        this.top = top;
    }

    public NodeList()
    {}
    @Override
    public E getValue()
    {
        return value;
    }

    @Override
    public void setValue(E value)
    {
        this.value = value;
    }
}
