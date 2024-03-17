package de.cyklon.reflection.entities.members.impl;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectParameter;
import de.cyklon.reflection.types.AbstractMethod;
import de.cyklon.reflection.types.Modifier;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.EnumSet;

public class ReflectParameterImpl<D, R> extends ReflectMemberImpl<D, R> implements ReflectParameter<D, R> {
	private final Parameter parameter;
	private final AbstractMethod<D, ?> method;

	public ReflectParameterImpl(@NotNull Parameter parameter, @NotNull ReflectClass<R> type, @NotNull AbstractMethod<D, ?> method) {
		super(method.getDeclaringClass(), type);

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
	public EnumSet<Modifier> getModifiers() {
		return Modifier.parse(parameter.getModifiers());
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
	public boolean equals(Object obj) {
		return obj instanceof ReflectParameter<?, ?> rp && rp.getParameter().equals(parameter);
	}

	@Override
	public int hashCode() {
		return parameter.hashCode() ^ method.hashCode();
	}

	@Override
	public String toString() {
		return parameter.toString();
	}
}
