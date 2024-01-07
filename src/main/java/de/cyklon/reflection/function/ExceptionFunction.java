package de.cyklon.reflection.function;

public interface ExceptionFunction<T, R, E extends Exception> {
	R apply(T param) throws E;
}
