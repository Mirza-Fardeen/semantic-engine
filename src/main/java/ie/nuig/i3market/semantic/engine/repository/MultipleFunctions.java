package ie.nuig.i3market.semantic.engine.repository;

@FunctionalInterface
public interface MultipleFunctions<A, B, C, D, E> {

    E accept(A a, B b, C c, D d);

}
