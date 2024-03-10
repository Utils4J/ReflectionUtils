package de.cyklon.reflection.entities;

import de.cyklon.reflection.ReflectionUtils;
import de.cyklon.reflection.entities.impl.ReflectClassImpl;
import de.cyklon.reflection.entities.members.ReflectConstructor;
import de.cyklon.reflection.entities.members.ReflectField;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.exception.ClassNotFoundException;
import de.cyklon.reflection.exception.ExecutionException;
import de.cyklon.reflection.exception.FieldNotFoundException;
import de.cyklon.reflection.exception.MethodNotFoundException;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.types.Modifiable;
import de.cyklon.reflection.types.ReflectEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReflectClass<D> extends ClassFile, Type, ReflectEntity, Modifiable {
	@NotNull
	static <D> ReflectClass<D> wrap(@NotNull Class<D> clazz) {
		return ReflectClassImpl.wrap(clazz);
	}

	@NotNull
	static <D> ReflectClass<D> wrap(@NotNull Type type) {
		return ReflectClassImpl.wrap(type);
	}

	@NotNull
	@SuppressWarnings("unchecked")
	static <D> ReflectClass<D> getClass(@NotNull D obj) {
		return wrap((Class<D>) obj.getClass());
	}

	@NotNull
	@SuppressWarnings("unchecked")
	static <D> ReflectClass<D> forName(@NotNull String className) {
		return wrap((Class<D>) ReflectionUtils.getClass(className));
	}


	@NotNull
	Type getType();

	@NotNull
	Class<?> getInternal();

	@NotNull
	ArrayInfo getArrayInfo() throws IllegalStateException;

	@NotNull
	List<? extends ReflectClass<?>> getTypeParameters();


	@NotNull
	<E extends Enum<E>> List<E> getEnumConstants() throws IllegalStateException;

	@NotNull
	List<? extends ReflectClass<?>> getUpperWildcardBounds() throws IllegalStateException;

	@NotNull
	List<? extends ReflectClass<?>> getLowerWildcardBounds() throws IllegalStateException;


	boolean isPrimitive();

	boolean isArray();

	boolean isEnum();

	boolean isWildcard();


	@Nullable <T> ReflectClass<T> getParentClass();

	@Nullable <T> ReflectClass<T> getNestParent();

	@NotNull
	@Override
	ReflectPackage getPackage();

	@NotNull
	Set<? extends ReflectClass<?>> getSubclasses(@NotNull Filter<ReflectClass<?>> filter);

	@NotNull
	default Optional<? extends ReflectClass<?>> getOptionalSubclass(@NotNull String name) {
		return getSubclasses(c -> {
			String n = c.getName();
			n = n.substring(n.indexOf('$') + 1);
			return n.equals(name) || c.getName().equals(name);
		}).stream().findFirst();
	}

	@NotNull
	default ReflectClass<?> getSubclass(@NotNull String name) throws ClassNotFoundException {
		return getOptionalSubclass(name).orElseThrow(() -> new ClassNotFoundException(this, name));
	}

	@NotNull
	D[] newArrayInstance(int... dimensions) throws IllegalStateException;

	@NotNull
	D newInstance(@NotNull Object... params) throws ExecutionException, IllegalStateException;


	@NotNull
	@Unmodifiable
	Set<? extends ReflectConstructor<D>> getConstructors(@NotNull Filter<ReflectConstructor<? extends D>> filter);

	@NotNull
	@Unmodifiable
	Set<? extends ReflectMethod<D, ?>> getMethods(@NotNull Filter<ReflectMethod<? extends D, ?>> filter);

	@NotNull
	@Unmodifiable
	Set<? extends ReflectField<D, ?>> getFields(@NotNull Filter<ReflectField<? extends D, ?>> filter);


	@NotNull <R> Optional<ReflectField<D, R>> getOptionalField(@NotNull Class<R> returnType, @NotNull String fieldName);

	@NotNull
	default Optional<ReflectField<D, Object>> getOptionalField(@NotNull String fieldName) {
		return getOptionalField(Object.class, fieldName);
	}

	@NotNull
	default <R> ReflectField<D, R> getField(@NotNull Class<R> returnType, @NotNull String fieldName) throws FieldNotFoundException {
		return getOptionalField(returnType, fieldName).orElseThrow(() -> new FieldNotFoundException(this, fieldName));
	}

	@NotNull
	default ReflectField<D, Object> getField(@NotNull String fieldName) throws FieldNotFoundException {
		return getField(Object.class, fieldName);
	}


	@NotNull <R> Optional<ReflectMethod<D, R>> getOptionalMethod(@NotNull Class<R> returnType, @NotNull String methodName, @NotNull Class<?>... paramTypes);

	@NotNull
	default Optional<ReflectMethod<D, Object>> getOptionalMethod(@NotNull String methodName, @NotNull Class<?>... paramTypes) {
		return getOptionalMethod(Object.class, methodName, paramTypes);
	}

	@NotNull
	default <R> ReflectMethod<D, R> getMethod(@NotNull Class<R> returnType, @NotNull String methodName, @NotNull Class<?>... paramTypes) throws MethodNotFoundException {
		return getOptionalMethod(returnType, methodName, paramTypes).orElseThrow(() -> new MethodNotFoundException(this, methodName));
	}

	@NotNull
	default ReflectMethod<D, Object> getMethod(@NotNull String methodName, @NotNull Class<?>... paramTypes) throws MethodNotFoundException {
		return getMethod(Object.class, methodName, paramTypes);
	}

	@Override
	default boolean isLoaded() {
		return true;
	}

	@NotNull
	@Override
	default ReflectClass<?> load() {
		return this;
	}

	record ArrayInfo(@NotNull ReflectClass<?> component, int depth) {
	}
}
