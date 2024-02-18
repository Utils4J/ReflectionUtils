package de.cyklon.reflection.exception;

import de.cyklon.reflection.entities.ReflectClass;
import org.jetbrains.annotations.NotNull;

public class MemberNotFoundException extends NotFoundException {
	private final ReflectClass<?> parent;

	public MemberNotFoundException(@NotNull ReflectClass<?> parent, @NotNull String name, @NotNull String type) {
		super(name, type, parent.getName());
		this.parent = parent;
	}

	@NotNull
	public ReflectClass<?> getParent() {
		return parent;
	}
}
