package de.cyklon.reflection.entities;

import de.cyklon.reflection.Annotatable;
import de.cyklon.reflection.Nameable;
import org.jetbrains.annotations.NotNull;

public interface ReflectEntity<D, R> extends Annotatable, Nameable {

	@NotNull
	ReflectClass<R> getReturnType();

	@NotNull
	ReflectClass<D> getDeclaringClass();
}

