package de.cyklon.reflection.types;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public enum Modifier {
	PUBLIC,
	PRIVATE,
	PROTECTED,
	STATIC,
	FINAL,
	SYNCHRONIZED,
	VOLATILE,
	TRANSIENT,
	NATIVE,
	INTERFACE,
	ABSTRACT,
	STRICT;

	private final int id;

	Modifier(int id) {
		this.id = id;
	}

	Modifier() {
		id = 1 << ordinal();
	}

	public int getId() {
		return id;
	}

	@NotNull
	public static EnumSet<Modifier> parse(int modifiers) {
		return EnumSet.copyOf(Arrays.stream(values())
				.filter(m -> (modifiers & m.id) > 0)
				.collect(Collectors.toSet())
		);
	}
}
