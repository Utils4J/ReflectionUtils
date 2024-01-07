package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.ReflectConstructor;
import de.cyklon.reflection.entities.ReflectField;
import de.cyklon.reflection.entities.ReflectMethod;
import de.cyklon.reflection.exception.ExecutionException;
import de.cyklon.reflection.function.Filter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectClassImpl<D> extends ReflectEntityImpl<D, D> implements ReflectClass<D> {
	private ReflectClassImpl(@NotNull Class<D> declaringClass) {
		super(declaringClass, declaringClass);
	}

	@NotNull
	public static <D> ReflectClass<D> wrap(Class<D> clazz) {
		return new ReflectClassImpl<>(clazz);
	}

	@Nullable
	@Override
	public ReflectClass<?> getParentClass() {
		return declaringClass.getSuperclass() == null ? null : wrap(declaringClass.getSuperclass());
	}

	@NotNull
	@Override
	public Set<? extends ReflectClass<?>> getSubclasses(@NotNull Filter<ReflectClass<?>> filter) {
		return Arrays.stream(declaringClass.getClasses())
				.map(ReflectClassImpl::new)
				.filter(filter::filter)
				.collect(Collectors.toSet());
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public Set<? extends ReflectConstructor<D>> getConstructors(@NotNull Filter<ReflectConstructor<D>> filter) {
		return Arrays.stream(declaringClass.getDeclaredConstructors())
				.map(c -> new ReflectConstructorImpl<>(declaringClass, (Constructor<D>) c))
				.filter(filter::filter)
				.collect(Collectors.toSet());
	}

	@NotNull
	@Override
	public Set<? extends ReflectField<D, ?>> getFields(@NotNull Filter<ReflectField<D, ?>> filter) {
		return Arrays.stream(declaringClass.getDeclaredFields())
				.map(f -> new ReflectFieldImpl<>(declaringClass, Object.class, f))
				.filter(filter::filter)
				.collect(Collectors.toSet());
	}

	@NotNull
	@Override
	public Set<? extends ReflectMethod<D, ?>> getMethods(@NotNull Filter<ReflectMethod<D, ?>> filter) {
		return Arrays.stream(declaringClass.getDeclaredMethods())
				.map(m -> new ReflectMethodImpl<>(declaringClass, Object.class, m))
				.filter(filter::filter)
				.collect(Collectors.toSet());
	}

	private Constructor<D> getConstructor(Object[] params) {
		try {
			return declaringClass.getConstructor(Arrays.stream(params).map(Object::getClass).toArray(Class[]::new));
		} catch (NoSuchMethodException e) {
			throw new RuntimeException();
		}
	}

	@Override
	@NotNull
	public D newInstance(Object... params) throws ExecutionException {
		try {
			return getConstructor(params).newInstance(params);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException();
		} catch(InvocationTargetException e) {
			throw new ExecutionException(e);
		}
	}

	@Override
	public @NotNull <R> Optional<ReflectField<D, R>> getOptionalField(@NotNull Class<R> returnType, @NotNull String fieldName) {
		try {
			return Optional.of(new ReflectFieldImpl<>(declaringClass, returnType, declaringClass.getDeclaredField(fieldName)));
		} catch (NoSuchFieldException e) {
			return Optional.empty();
		}
	}

	@Override
	public @NotNull <R> Optional<ReflectMethod<D, R>> getOptionalMethod(@NotNull Class<R> returnType, @NotNull String methodName, @NotNull Class<?>... paramTypes) {
		try {
			return Optional.of(new ReflectMethodImpl<>(declaringClass, returnType, declaringClass.getMethod(methodName, paramTypes)));
		} catch(NoSuchMethodException e) {
			return Optional.empty();
		}
	}

	@Override
	public Annotation[] getAnnotations() {
		return declaringClass.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return declaringClass.getDeclaredAnnotations();
	}
}
