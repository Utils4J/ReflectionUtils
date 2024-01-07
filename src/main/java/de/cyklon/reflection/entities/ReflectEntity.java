package de.cyklon.reflection.entities;

import de.cyklon.reflection.Annotatable;
import org.jetbrains.annotations.NotNull;

public interface ReflectEntity<D, R> extends Annotatable {

	@NotNull
	Class<R> getReturnType();

	@NotNull
	Class<D> getDeclaringClass();
}

