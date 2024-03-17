package de.cyklon.reflection.entities.members.impl;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectConstructor;
import de.cyklon.reflection.entities.members.ReflectParameter;
import de.cyklon.reflection.exception.ExecutionException;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.IntStream;

public class ReflectConstructorImpl<D> extends ReflectMemberImpl<D, D> implements ReflectConstructor<D> {
	private final Constructor<D> constructor;

	public ReflectConstructorImpl(@NotNull ReflectClass<D> declaringClass, @NotNull Constructor<D> constructor) {
		super(declaringClass, declaringClass);
		constructor.setAccessible(true);
		this.constructor = constructor;
	}

	@NotNull
	public static <D> ReflectConstructor<D> wrap(@NotNull Constructor<D> constructor) {
		return new ReflectConstructorImpl<>(ReflectClass.wrap(constructor.getDeclaringClass()), constructor);
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
		return IntStream.range(0, constructor.getParameterCount())
				.mapToObj(i -> new ReflectParameterImpl<>(constructor.getParameters()[i], ReflectClass.wrap(constructor.getGenericParameterTypes()[i]), this))
				.toList();
	}

	@NotNull
	@Override
	public D invoke(@NotNull Object obj, @NotNull Object... args) throws ExecutionException {
		try {
			return constructor.newInstance(args);
		} catch (IllegalAccessException | InstantiationException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new ExecutionException(e.getCause());
		}
	}

	@Override
	public int getIntModifiers() {
		return constructor.getModifiers();
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
