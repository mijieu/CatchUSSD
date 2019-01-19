package busime.cm.catchussd.util;

@FunctionalInterface
public interface Predicate<T> {
    boolean apply(T t);
}
