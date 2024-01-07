package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ReflectConstructor;
import de.cyklon.reflection.entities.ReflectParameter;
import de.cyklon.reflection.exception.ExecutionException;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

class ReflectConstructorImpl<D> extends ReflectEntityImpl<D, D> implements ReflectConstructor<D> {
	private final Constructor<D> constructor;

	public ReflectConstructorImpl(@NotNull Class<D> declaringClass, @NotNull Constructor<D> constructor) {
		super(declaringClass, declaringClass);
		constructor.setAccessible(true);
		this.constructor = constructor;
	}

	@Override
	public @NotNull String getName() {
		return constructor.getName();
	}

	@NotNull
	@Override
	public Constructor<D> getConstructor() {
		return constructor;
	}

	@NotNull
	@Override
	public List<? extends ReflectParameter<D, Object>> getParameters() {
		return Arrays.stream(constructor.getParameters())
				.map(p -> new ReflectParameterImpl<>(p, this))
				.toList();
	}

	@NotNull
	@Override
	public D newInstance(@NotNull Object... args) {
		try {
			return constructor.newInstance(args);
		} catch (IllegalAccessException | InstantiationException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new ExecutionException(e.getCause());
		}
	}

	@NotNull
	@Override
	public Annotation[] getAnnotations() {
		return constructor.getAnnotations();
	}

	@NotNull
	@Override
	public Annotation[] getDeclaredAnnotations() {
		return constructor.getDeclaredAnnotations();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ReflectConstructor<?> rc && rc.getConstructor().equals(constructor);
	}

	@Override
	public String toString() {
		return constructor.toString();
	}
}
