package de.cyklon.reflection;

import org.jetbrains.annotations.NotNull;

public interface ReflectEntity<D, R> {

	@NotNull Class<R> getReturnType();

	@NotNull Class<D> getDeclaringClass();
}

