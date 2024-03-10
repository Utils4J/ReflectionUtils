package de.cyklon.reflection.types;

import org.jetbrains.annotations.NotNull;

public interface Loadable<T> {

	boolean isLoaded();

	@NotNull
	T load();

}
