package de.cyklon.reflection.types;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public enum Modifier {
	PUBLIC(1),
	PRIVATE(2),
	PROTECTED(4),
	STATIC(8),
	FINAL(16),
	SYNCHRONIZED(32),
	VOLATILE(64),
	TRANSIENT(128),
	NATIVE(256),
	INTERFACE(512),
	ABSTRACT(1024),
	STRICT(2048);

	private final int id;

	Modifier(int id) {
		this.id = id;
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
