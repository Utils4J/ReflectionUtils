package de.cyklon.reflection.types;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public interface Modifiable {

	int getIntModifiers();

	@NotNull
	default EnumSet<Modifier> getModifiers() {
		return Modifier.parse(getIntModifiers());
	}

	default boolean hasModifier(@NotNull Modifier modifier) {
		return getModifiers().contains(modifier);
	}
}
