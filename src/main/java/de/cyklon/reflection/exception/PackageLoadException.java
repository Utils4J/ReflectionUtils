package de.cyklon.reflection.exception;

import org.jetbrains.annotations.NotNull;

public class PackageLoadException extends RuntimeException {
	private final String name;

	public PackageLoadException(@NotNull String name) {
		super("Could not find any class in" + String.format("package %s, therefore this package cannot be loaded!", name));
		this.name = name;
	}

	@NotNull
	public String getName() {
		return name;
	}
}
