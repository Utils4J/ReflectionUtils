package de.cyklon.reflection.entities.members.impl;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectMember;
import org.jetbrains.annotations.NotNull;

public abstract class ReflectMemberImpl<D, R> implements ReflectMember<D, R> {
	protected final ReflectClass<D> declaringClass;
	protected final ReflectClass<R> returnType;

	public ReflectMemberImpl(@NotNull ReflectClass<D> declaringClass, @NotNull ReflectClass<R> returnType) {
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
