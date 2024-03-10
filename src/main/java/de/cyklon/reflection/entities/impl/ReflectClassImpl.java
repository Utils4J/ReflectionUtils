package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.ReflectPackage;
import de.cyklon.reflection.entities.members.ReflectConstructor;
import de.cyklon.reflection.entities.members.ReflectField;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.entities.members.impl.ReflectConstructorImpl;
import de.cyklon.reflection.entities.members.impl.ReflectFieldImpl;
import de.cyklon.reflection.entities.members.impl.ReflectMethodImpl;
import de.cyklon.reflection.exception.ConstructorNotFoundException;
import de.cyklon.reflection.exception.ExecutionException;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.types.Modifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectClassImpl<D> implements ReflectClass<D> {
	private final Class<D> clazz;
	private final Type type;

	@NotNull
	@SuppressWarnings("unchecked")
	private static <D> Class<D> getClazz(@NotNull Type type) {
		if (type instanceof Class<?> c) return (Class<D>) c;
		if (type instanceof ParameterizedType pt) return getClazz(pt.getRawType());
		if (type instanceof GenericArrayType gt) return (Class<D>) Array.newInstance(getClazz(gt.getGenericComponentType()), 0).getClass();
		if (type instanceof WildcardType) return (Class<D>) void.class;

		throw new IllegalArgumentException("Cannot find Class for " + type);
	}

	private ReflectClassImpl(@NotNull Type type, @NotNull Class<D> clazz) {
		this.type = type;
		this.clazz = clazz;
	}

	@NotNull
	public static <D> ReflectClass<D> wrap(@NotNull Type type) {
		return new ReflectClassImpl<>(type, getClazz(type));
	}

	@NotNull
	public static <D> ReflectClass<D> wrap(@NotNull Class<D> clazz) {
		return new ReflectClassImpl<>(clazz, clazz);
	}

	@NotNull
	@Override
	public Type getType() {
		return type;
	}

	@NotNull
	@Override
	public Class<?> getInternal() {
		return clazz;
	}


	@NotNull
	@Override
	public ArrayInfo getArrayInfo() throws IllegalStateException {
		if (!isArray()) throw new IllegalStateException("This ReflectClass does not represent an array!");

		int i = 0;
		ReflectClass<?> component = this;

		while (component.isArray()) {
			component.getInternal();

			if (component.getType() instanceof GenericArrayType at) component = wrap(at.getGenericComponentType());
			else if (component.isArray()) component = wrap(clazz.getComponentType());

			i++;
		}

		return new ArrayInfo(component, i);
	}

	@NotNull
	@Override
	@Unmodifiable
	public List<? extends ReflectClass<?>> getTypeParameters() {
		if (!(type instanceof ParameterizedType pt)) return Collections.emptyList();
		return Arrays.stream(pt.getActualTypeArguments())
				.map(ReflectClass::wrap)
				.toList();
	}

	@NotNull
	@Override
	@Unmodifiable
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> List<E> getEnumConstants() throws IllegalStateException {
		if (!isEnum()) throw new IllegalStateException("This ReflectClass does not represent an enum!");
		return Arrays.stream(clazz.getEnumConstants())
				.map(e -> (E) e)
				.toList();
	}

	@NotNull
	@Override
	public List<? extends ReflectClass<?>> getUpperWildcardBounds() throws IllegalStateException {
		if (!(type instanceof WildcardType t)) throw new IllegalStateException();
		return Arrays.stream(t.getUpperBounds()).map(ReflectClass::wrap).toList();
	}

	@NotNull
	@Override
	public List<? extends ReflectClass<?>> getLowerWildcardBounds() throws IllegalStateException {
		if (!(type instanceof WildcardType t)) throw new IllegalStateException();
		return Arrays.stream(t.getLowerBounds()).map(ReflectClass::wrap).toList();
	}

	@Override
	public boolean isPrimitive() {
		return clazz.isPrimitive() && !isWildcard();
	}

	@Override
	public boolean isArray() {
		return clazz.isArray();
	}

	@Override
	public boolean isEnum() {
		return clazz.isEnum();
	}

	@Override
	public boolean isWildcard() {
		return type instanceof WildcardType;
	}

	@NotNull
	@Override
	public EnumSet<Modifier> getModifiers() {
		return Modifier.parse(clazz.getModifiers());
	}

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T> ReflectClass<T> getParentClass() {
		Class<T> temp = (Class<T>) clazz.getSuperclass();
		return temp == null ? null : wrap(temp);
	}

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T> ReflectClass<T> getNestParent() {
		Class<T> temp = (Class<T>) clazz.getNestHost();
		return temp == null ? null : wrap(temp);
	}

	@NotNull
	@Override
	public ReflectPackage getPackage() {
		return ReflectPackage.get(clazz.getPackageName());
	}

	@NotNull
	@Override
	@Unmodifiable
	public Set<? extends ReflectClass<?>> getSubclasses(@NotNull Filter<ReflectClass<?>> filter) {
		return Arrays.stream(clazz.getClasses())
				.map(ReflectClassImpl::wrap)
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public Set<? extends ReflectConstructor<D>> getConstructors(@NotNull Filter<ReflectConstructor<? extends D>> filter) {
		return Arrays.stream(clazz.getDeclaredConstructors())
				.map(c -> new ReflectConstructorImpl<>(this, (Constructor<D>) c))
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	public Set<? extends ReflectField<D, ?>> getFields(@NotNull Filter<ReflectField<? extends D, ?>> filter) {
		return Arrays.stream(clazz.getDeclaredFields())
				.map(f -> new ReflectFieldImpl<>(this, f))
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	public Set<? extends ReflectMethod<D, ?>> getMethods(@NotNull Filter<ReflectMethod<? extends D, ?>> filter) {
		return Arrays.stream(clazz.getDeclaredMethods())
				.map(m -> new ReflectMethodImpl<>(this, m))
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public D[] newArrayInstance(int... dimensions) throws IllegalStateException {
		if (!isArray()) throw new IllegalStateException("This ReflectClass does not represent an array");

		ArrayInfo info = getArrayInfo();

		return (D[]) Array.newInstance(info.component().getInternal(), Arrays.copyOf(dimensions, dimensions.length + info.depth()));
	}

	@Override
	@NotNull
	public D newInstance(@NotNull Object... params) throws ExecutionException, IllegalStateException {
		if (isArray()) throw new IllegalStateException("Cannot use newInstance on array type. Use newArrayInstance instead!");

		Class<?>[] types = Arrays.stream(params).map(o -> {
			if (o != null) return o.getClass();
			else throw new IllegalArgumentException("Cannot pass 'null' as parameter to newInstance. If you have nullable parameters use getConstructor instead!");
		}).toArray(Class[]::new);

		try {
			return clazz.getConstructor(types).newInstance(params);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			throw new ConstructorNotFoundException(this, types);
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
			return Optional.of(new ReflectMethodImpl<>(this, clazz.getDeclaredMethod(methodName, paramTypes)));
		} catch (NoSuchMethodException e) {
			return Optional.empty();
		}
	}

	@Override
	public Annotation[] getAnnotations() {
		return clazz.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return clazz.getDeclaredAnnotations();
	}

	@Override
	public String getTypeName() {
		return type.getTypeName();
	}

	@NotNull
	@Override
	public String getName() {
		if (isWildcard()) return "?";

		if (!clazz.isArray()) return clazz.getName();
		else return getArrayInfo().component().getName() + "[]";
	}

	@NotNull
	@Override
	public String getSimpleName() {
		return clazz.getSimpleName();
	}


	@Override
	public boolean equals(Object obj) {
		return (obj instanceof ReflectClass<?> rc && rc.getType().equals(type)) || (obj instanceof ClassFileImpl c && c.equals(this));
	}

	@Override
	public String toString() {
		return getTypeName();
	}

}
