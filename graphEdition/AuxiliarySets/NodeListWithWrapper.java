package graphEdition.AuxiliarySets;

public class NodeListWithWrapper<E> extends NodeList<E>{
    private Wrapper<E> value;

    public NodeListWithWrapper(Wrapper<E> wrap, int top)
    {
        this.value = wrap;
        this.top = top;
    }

    @Override
    public void setValue(E value)
    {
        this.value.value = value;
    }

    @Override
    public E getValue()
    {
        return this.value.value;
    }
}
