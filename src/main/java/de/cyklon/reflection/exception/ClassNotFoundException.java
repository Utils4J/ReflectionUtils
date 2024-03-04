package de.cyklon.reflection.exception;

import de.cyklon.reflection.types.Nameable;
import org.jetbrains.annotations.NotNull;

public class ClassNotFoundException extends NotFoundException {
	public ClassNotFoundException(@NotNull Nameable parent, @NotNull String name) {
		super(name, "class", parent.getName());
	}
}
