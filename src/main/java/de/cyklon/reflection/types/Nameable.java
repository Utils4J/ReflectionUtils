package de.cyklon.reflection.types;

import org.jetbrains.annotations.NotNull;

public interface Nameable {

	@NotNull
	String getName();

	@NotNull
	default String getSimpleName() {
		return getName();
	}

}
