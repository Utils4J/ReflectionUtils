package de.cyklon.reflection.types;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public interface Modifiable {

	int getEncodedModifiers();

	@NotNull
	default EnumSet<Modifier> getModifiers() {
		return Modifier.parse(getEncodedModifiers());
	}

	default boolean hasModifier(@NotNull Modifier modifier) {
		return getModifiers().contains(modifier);
	}
}
