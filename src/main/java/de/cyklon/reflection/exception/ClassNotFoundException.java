package de.cyklon.reflection.exception;

import de.cyklon.reflection.types.Nameable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClassNotFoundException extends NotFoundException {
	public ClassNotFoundException(@Nullable Nameable parent, @NotNull String name) {
		super(name, "class", parent==null ? "" : parent.getName());
	}
}
