package de.cyklon.reflection.entities;

import de.cyklon.reflection.entities.impl.ReflectClassImpl;
import de.cyklon.reflection.types.MemberContainer;
import de.cyklon.reflection.entities.members.ReflectField;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.exception.ExecutionException;
import de.cyklon.reflection.exception.FieldNotFoundException;
import de.cyklon.reflection.exception.MethodNotFoundException;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.types.Modifiable;
import de.cyklon.reflection.types.ReflectEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReflectClass<D> extends MemberContainer<D>, Type, ReflectEntity, Modifiable {
	@NotNull
	static <D> ReflectClass<D> wrap(@NotNull Class<D> clazz) {
		return ReflectClassImpl.wrap(clazz);
	}

	@NotNull
	static <D> ReflectClass<D> wrap(@NotNull Type type) {
		return ReflectClassImpl.wrap(type);
	}


	@NotNull
	Type getType();

	@Nullable
	Class<?> getInternal();

	@NotNull
	ArrayInfo getArrayInfo() throws IllegalStateException;

	@NotNull
	List<? extends ReflectClass<?>> getTypeParameters();


	@NotNull <E extends Enum<E>> List<E> getEnumConstants() throws IllegalStateException;


	boolean isPrimitive();

	boolean isArray();

	boolean isEnum();


	@NotNull
	String getFullName();


	@Nullable
	ReflectClass<?> getParentClass();

	@NotNull
	Set<? extends ReflectClass<?>> getSubclasses(@NotNull Filter<ReflectClass<?>> filter);

	@NotNull
	D[] newArrayInstance(int... dimensions) throws IllegalStateException;

	@NotNull
	D newInstance(@NotNull Object... params) throws ExecutionException, IllegalStateException;

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

	default <R> ReflectMethod<D, R> getMethod(@NotNull Class<R> returnType, @NotNull String methodName, @NotNull Class<?>... paramTypes) throws MethodNotFoundException {
		return getOptionalMethod(returnType, methodName, paramTypes).orElseThrow(() -> new MethodNotFoundException(this, methodName));
	}

	default ReflectMethod<D, Object> getMethod(@NotNull String methodName, @NotNull Class<?>... paramTypes) throws MethodNotFoundException {
		return getMethod(Object.class, methodName, paramTypes);
	}

	record ArrayInfo(@NotNull ReflectClass<?> component, int depth) {}
}
