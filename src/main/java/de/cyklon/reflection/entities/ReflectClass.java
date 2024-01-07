package de.cyklon.reflection.entities;

import de.cyklon.reflection.entities.impl.ReflectEntityImpl;
import de.cyklon.reflection.entities.impl.ReflectClassImpl;
import de.cyklon.reflection.exception.ExecutionException;
import de.cyklon.reflection.exception.FieldNotFoundException;
import de.cyklon.reflection.exception.MethodNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ReflectClass<D> extends ReflectEntity<D, D> {

	@NotNull
	static <D> ReflectClass<D> wrap(Class<D> clazz) {
		return ReflectClassImpl.wrap(clazz);
	}

	@NotNull
	D newInstance(@NotNull Object... params) throws ExecutionException;


	@NotNull
	<R> Optional<ReflectField<D, R>> getOptionalField(@NotNull Class<R> returnType, @NotNull String fieldName);

	@NotNull
	default Optional<ReflectField<D, Object>> getOptionalField(@NotNull String fieldName) {
		return getOptionalField(Object.class, fieldName);
	}

	@NotNull
	default <R> ReflectField<D, R> getField(@NotNull Class<R> returnType, @NotNull String fieldName) throws FieldNotFoundException {
		return getOptionalField(returnType, fieldName).orElseThrow(() -> new FieldNotFoundException(getDeclaringClass(), fieldName));
	}

	@NotNull
	default ReflectField<D, Object> getField(@NotNull String fieldName) throws FieldNotFoundException {
		return getField(Object.class, fieldName);
	}


	@NotNull
	<R> Optional<ReflectMethod<D, R>> getOptionalMethod(@NotNull Class<R> returnType, @NotNull String methodName, @NotNull Class<?>... paramTypes);

	@NotNull
	default Optional<ReflectMethod<D, Object>> getOptionalMethod(@NotNull String methodName, @NotNull Class<?>... paramTypes) {
		return getOptionalMethod(Object.class, methodName, paramTypes);
	}

	default <R> ReflectMethod<D, R> getMethod(@NotNull Class<R> returnType, @NotNull String methodName, @NotNull Class<?>... paramTypes) throws MethodNotFoundException {
		return getOptionalMethod(returnType, methodName, paramTypes).orElseThrow(() -> new MethodNotFoundException(getDeclaringClass(), methodName));
	}

	default ReflectMethod<D, Object> getMethod(@NotNull String methodName, @NotNull Class<?>... paramTypes) throws MethodNotFoundException {
		return getMethod(Object.class, methodName, paramTypes);
	}
}
