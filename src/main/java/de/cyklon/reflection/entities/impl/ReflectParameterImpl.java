package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.AbstractMethod;
import de.cyklon.reflection.entities.ReflectParameter;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ReflectParameterImpl<D, R> extends ReflectEntityImpl<D, R> implements ReflectParameter<D, R> {
	private final Parameter parameter;
	private final AbstractMethod<D, ?> method;

	@SuppressWarnings("unchecked")
	public ReflectParameterImpl(@NotNull Parameter parameter, @NotNull AbstractMethod<D, ?> method) {
		super(method.getDeclaringClass(), (Class<R>) parameter.getType());

		this.parameter = parameter;
		this.method = method;
	}

	@NotNull
	@Override
	public Parameter getParameter() {
		return parameter;
	}

	@Override
	@NotNull
	public AbstractMethod<D, ?> getDeclaringMethod() {
		return method;
	}

	@NotNull
	@Override
	public Annotation[] getAnnotations() {
		return parameter.getAnnotations();
	}

	@NotNull
	@Override
	public Annotation[] getDeclaredAnnotations() {
		return parameter.getDeclaredAnnotations();
	}

	@Override
	public @NotNull String getName() {
		return parameter.getName();
	}

	@Override
	public String toString() {
		return parameter.toString();
	}
}
