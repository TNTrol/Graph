package graphEdition.AuxiliarySets;

public interface Node<T> {

    default T getValue() {
        return null;
    }

    default void setValue(T value) {
    }
}