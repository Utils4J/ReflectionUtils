package de.cyklon.reflection.entities;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;

public interface ReflectParameter<D, R> extends ReflectEntity<D, R> {
	@NotNull
	Parameter getParameter();

	@NotNull
	AbstractMethod<D, ?> getDeclaringMethod();
}
