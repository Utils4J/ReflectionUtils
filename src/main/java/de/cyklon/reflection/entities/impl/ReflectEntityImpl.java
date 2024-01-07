package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.ReflectEntity;
import org.jetbrains.annotations.NotNull;

abstract class ReflectEntityImpl<D, R> implements ReflectEntity<D, R> {
	protected final ReflectClass<D> declaringClass;
	protected final ReflectClass<R> returnType;

	public ReflectEntityImpl(@NotNull ReflectClass<D> declaringClass, @NotNull ReflectClass<R> returnType) {
		this.declaringClass = declaringClass;
		this.returnType = returnType;
	}

	@NotNull
	@Override
	public ReflectClass<D> getDeclaringClass() {
		return declaringClass;
	}

	@NotNull
	@Override
	public ReflectClass<R> getReturnType() {
		return returnType;
	}
}
