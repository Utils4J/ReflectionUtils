package de.cyklon.reflection.types;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public interface Modifiable {
	@NotNull EnumSet<Modifier> getModifiers() throws UnsupportedOperationException;

	default boolean hasModifier(@NotNull Modifier modifier) {
		return getModifiers().contains(modifier);
	}
}
