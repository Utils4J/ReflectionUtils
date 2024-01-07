package de.cyklon.reflection.exception;

import org.jetbrains.annotations.NotNull;

public class MemberNotFoundException extends NotFoundException {
	private final Class<?> parent;

	public MemberNotFoundException(@NotNull Class<?> parent, @NotNull String name) {
		super(name);
		this.parent = parent;
	}

	@NotNull
	public Class<?> getParent() {
		return parent;
	}
}
