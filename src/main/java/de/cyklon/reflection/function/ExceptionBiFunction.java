package de.cyklon.reflection.function;

public interface ExceptionBiFunction<T, U, R, E extends Exception> {
	R apply(T param1, U param2) throws E;
}
