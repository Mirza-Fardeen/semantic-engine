package ie.nuig.i3market.semantic.engine.repository;

@FunctionalInterface
public interface CreateNewObjectClazz<T> {
    T createNewObject() throws ClassNotFoundException;
}
