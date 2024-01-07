package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ReflectEntity;
import org.jetbrains.annotations.NotNull;

abstract class ReflectEntityImpl<D, R> implements ReflectEntity<D, R> {
	protected final Class<D> declaringClass;
	protected final Class<R> returnType;

	public ReflectEntityImpl(@NotNull Class<D> declaringClass, @NotNull Class<R> returnType) {
		this.declaringClass = declaringClass;
		this.returnType = returnType;
	}

	@NotNull
	@Override
	public Class<D> getDeclaringClass() {
		return declaringClass;
	}

	@NotNull
	@Override
	public Class<R> getReturnType() {
		return returnType;
	}
}
