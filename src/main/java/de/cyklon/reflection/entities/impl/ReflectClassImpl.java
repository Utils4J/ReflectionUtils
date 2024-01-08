package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectConstructor;
import de.cyklon.reflection.entities.members.ReflectField;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.entities.members.impl.ReflectConstructorImpl;
import de.cyklon.reflection.entities.members.impl.ReflectFieldImpl;
import de.cyklon.reflection.entities.members.impl.ReflectMethodImpl;
import de.cyklon.reflection.exception.ExecutionException;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.types.Modifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectClassImpl<D> implements ReflectClass<D> {
	private final Class<D> clazz;
	private final Type type;

	@Nullable
	@SuppressWarnings("unchecked")
	private static <D> Class<D> getClazz(@NotNull Type type) {
		if (type instanceof Class<?> c) return (Class<D>) c;
		else if (type instanceof ParameterizedType pt) return getClazz(pt.getRawType());
		else if (type instanceof GenericArrayType) return null;

		throw new IllegalArgumentException();
	}

	private ReflectClassImpl(@NotNull Type type) {
		this.type = type;
		this.clazz = getClazz(type);
	}

	@NotNull
	public static <D> ReflectClass<D> wrap(@NotNull Type type) {
		return new ReflectClassImpl<>(type);
	}

	@NotNull
	public static <D> ReflectClass<D> wrap(@NotNull Class<D> clazz) {
		return new ReflectClassImpl<>(clazz);
	}

	@NotNull
	@Override
	public Type getType() {
		return type;
	}

	@Nullable
	@Override
	public Class<?> getInternal() {
		return clazz;
	}


	@NotNull
	@Override
	public ArrayInfo getArrayInfo() throws IllegalStateException {
		if(!isArray()) throw new IllegalStateException("This ReflectClass does not represent an array!");

		int i = 0;
		ReflectClass<?> component = this;

		while(component.isArray()) {
			if (component.getInternal() != null && component.getInternal().isArray()) component = wrap(clazz.getComponentType());
			if (component.getType() instanceof GenericArrayType at) component = wrap(at.getGenericComponentType());

			i++;
		}

		return new ArrayInfo(component, i);
	}

	@NotNull
	@Override
	public List<? extends ReflectClass<?>> getTypeParameters() {
		if (!(type instanceof ParameterizedType pt)) return Collections.emptyList();
		return Arrays.stream(pt.getActualTypeArguments())
				.map(ReflectClass::wrap)
				.toList();
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> List<E> getEnumConstants() throws IllegalStateException {
		if (!isEnum()) throw new IllegalStateException("This ReflectClass does not represent an enum!");
		return Arrays.stream(clazz.getEnumConstants())
				.map(e -> (E) e)
				.toList();
	}

	@Override
	public boolean isPrimitive() {
		return clazz != null && clazz.isPrimitive();
	}

	@Override
	public boolean isArray() {
		return clazz == null || clazz.isArray();
	}

	@Override
	public boolean isEnum() {
		return clazz != null && clazz.isEnum();
	}

	@NotNull
	@Override
	public EnumSet<Modifier> getModifiers() {
		if(clazz == null) return EnumSet.noneOf(Modifier.class);
		return Modifier.parse(clazz.getModifiers());
	}

	@Nullable
	@Override
	public ReflectClass<?> getParentClass() {
		if (clazz == null) return null;
		return clazz.getSuperclass() == null ? null : wrap(clazz.getSuperclass());
	}

	@NotNull
	@Override
	public Set<? extends ReflectClass<?>> getSubclasses(@NotNull Filter<ReflectClass<?>> filter) {
		if (clazz == null) return Collections.emptySet();
		return Arrays.stream(clazz.getClasses())
				.map(ReflectClassImpl::new)
				.filter(filter::filter)
				.collect(Collectors.toSet());
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public Set<? extends ReflectConstructor<D>> getConstructors(@NotNull Filter<ReflectConstructor<?>> filter) {
		if (clazz == null) return Collections.emptySet();
		return Arrays.stream(clazz.getDeclaredConstructors())
				.map(c -> new ReflectConstructorImpl<>(this, (Constructor<D>) c))
				.filter(filter::filter)
				.collect(Collectors.toSet());
	}

	@NotNull
	@Override
	public Set<? extends ReflectField<D, ?>> getFields(@NotNull Filter<ReflectField<?, ?>> filter) {
		if (clazz == null) return Collections.emptySet();
		return Arrays.stream(clazz.getDeclaredFields())
				.map(f -> new ReflectFieldImpl<>(this, f))
				.filter(filter::filter)
				.collect(Collectors.toSet());
	}

	@NotNull
	@Override
	public Set<? extends ReflectMethod<D, ?>> getMethods(@NotNull Filter<ReflectMethod<?, ?>> filter) {
		if (clazz == null) return Collections.emptySet();
		return Arrays.stream(clazz.getDeclaredMethods())
				.map(m -> new ReflectMethodImpl<>(this, m))
				.filter(filter::filter)
				.collect(Collectors.toSet());
	}

	private Constructor<D> getConstructor(Object[] params) {
		try {
			return clazz.getConstructor(Arrays.stream(params).map(Object::getClass).toArray(Class[]::new));
		} catch (NoSuchMethodException e) {
			throw new RuntimeException();
		}
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public D[] newArrayInstance(int... dimensions) throws IllegalStateException {
		if (!isArray()) throw new IllegalStateException("This ReflectClass does not represent an array");

		ArrayInfo info = getArrayInfo();

		int[] newDimensions = new int[dimensions.length + info.depth()];
		System.arraycopy(dimensions, 0, newDimensions, info.depth(), dimensions.length);

		return (D[]) Array.newInstance(info.component().getInternal(), newDimensions);
	}

	@Override
	@NotNull
	public D newInstance(@NotNull Object... params) throws ExecutionException, IllegalStateException {
		if (isArray()) throw new IllegalStateException("Cannot use newInstance on array type. Use newArrayInstance instead!");

		try {
			return getConstructor(params).newInstance(params);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException();
		} catch (InvocationTargetException e) {
			throw new ExecutionException(e);
		}
	}

	@Override
	public @NotNull <R> Optional<ReflectField<D, R>> getOptionalField(@NotNull Class<R> returnType, @NotNull String fieldName) {
		try {
			return Optional.of(new ReflectFieldImpl<>(this, clazz.getDeclaredField(fieldName)));
		} catch (NoSuchFieldException e) {
			return Optional.empty();
		}
	}

	@Override
	public @NotNull <R> Optional<ReflectMethod<D, R>> getOptionalMethod(@NotNull Class<R> returnType, @NotNull String methodName, @NotNull Class<?>... paramTypes) {
		try {
			return Optional.of(new ReflectMethodImpl<>(this, clazz.getMethod(methodName, paramTypes)));
		} catch (NoSuchMethodException e) {
			return Optional.empty();
		}
	}

	@Override
	public Annotation[] getAnnotations() {
		if (clazz == null) return new Annotation[0];
		return clazz.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		if (clazz == null) return new Annotation[0];
		return clazz.getDeclaredAnnotations();
	}

	@Override
	public String getTypeName() {
		return type.getTypeName();
	}

	@NotNull
	@Override
	public String getName() {
		if (clazz != null) return clazz.getSimpleName();
		else return getArrayInfo().component().getName() + "[]";
	}

	@NotNull
	@Override
	public String getFullName() {
		return getTypeName();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ReflectClass<?> rc && rc.getType().equals(type);
	}

	@Override
	public String toString() {
		return getFullName();
	}
}
