package util;

@FunctionalInterface
public interface ThrowingFunction<R> {
    R apply(Object o) throws Exception;
}
