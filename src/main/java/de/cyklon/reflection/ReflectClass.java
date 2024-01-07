package de.cyklon.reflection;

import org.jetbrains.annotations.NotNull;

public interface ReflectClass<D> extends ReflectEntity<D, D> {

	@NotNull
	static <D> ReflectClass<D> wrap(Class<D> clazz) {
		return ReflectClassImpl.wrap(clazz);
	}

	@NotNull
	D newInstance(Object... params);

	@NotNull
	default ReflectField<D, Object> getField(@NotNull String fieldName) {
		return getField(Object.class, fieldName);
	}

	@NotNull
	<R> ReflectField<D, R> getField(@NotNull Class<R> returnType, @NotNull String fieldName);

	@NotNull
	default ReflectMethod<D, Object> getMethod(@NotNull String methodName, @NotNull Class<?>... paramTypes) {
		return getMethod(Object.class, methodName, paramTypes);
	}

	@NotNull
	<R> ReflectMethod<D, R> getMethod(@NotNull Class<R> returnType, @NotNull String methodName, @NotNull Class<?>... paramTypes);

}
