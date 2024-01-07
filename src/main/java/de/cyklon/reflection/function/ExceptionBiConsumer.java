package de.cyklon.reflection.function;

public interface ExceptionBiConsumer<T, U, E extends Exception> {
	void accept(T param1, U param2) throws E;
}
