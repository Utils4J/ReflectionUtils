package de.cyklon.reflection.function;

public interface ExceptionConsumer<T, E extends Exception> {
	void accept(T arg) throws E;
}
