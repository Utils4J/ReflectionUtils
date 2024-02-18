package de.cyklon.reflection.types;

public interface Loadable<T> {

	boolean isLoaded();

	T load();

}
