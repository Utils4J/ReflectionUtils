package de.cyklon.reflection.types;

import org.jetbrains.annotations.NotNull;

public interface Nameable {

	@NotNull
	static Nameable wrap(@NotNull String name) {
		return () -> name;
	}

	@NotNull
	String getName();

	@NotNull
	default String getSimpleName() {
		return getName();
	}

}
