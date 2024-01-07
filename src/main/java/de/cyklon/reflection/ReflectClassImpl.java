package de.cyklon.reflection;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectClassImpl<D> implements ReflectClass<D> {

	@NotNull
	static <D> ReflectClass<D> wrap(Class<D> clazz) {
		return new ReflectClassImpl<>(clazz);
	}

	private final Class<D> clazz;

	public ReflectClassImpl(Class<D> clazz) {
		this.clazz = clazz;
	}

	private Constructor<D> getConstructor(Object[] params) {
		Class<?>[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}
		try {
			return clazz.getConstructor(types);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException();
		}
	}

	@Override
	public @NotNull D newInstance(Object... params) {
		try {
			return getConstructor(params).newInstance(params);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException();
		}
	}

	@Override
	public <R> @NotNull ReflectField<D, R> getField(@NotNull Class<R> returnType, @NotNull String fieldName) {
		Field field;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
		return new ReflectField<>() {
			@Override
			public @NotNull Field getField() {
				return field;
			}

			@Override
			public R getValue(D obj) {
				try {
					return getReturnType().cast(getField().get(obj));
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public @NotNull Class<R> getReturnType() {
				return returnType;
			}

			@Override
			public @NotNull Class<D> getDeclaringClass() {
				return clazz;
			}
		};
	}

	@Override
	public <R> @NotNull ReflectMethod<D, R> getMethod(@NotNull Class<R> returnType, @NotNull String methodName, Class<?>... paramTypes) {
		Method method;
		try {
			method = clazz.getDeclaredMethod(methodName, returnType);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		return new ReflectMethod<>() {
			@Override
			public @NotNull Method getMethod() {
				return method;
			}

			@Override
			public R invoke(D obj, Object... args) {
				try {
					return getReturnType().cast(getMethod().invoke(obj, args));
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public @NotNull Class<R> getReturnType() {
				return returnType;
			}

			@Override
			public @NotNull Class<D> getDeclaringClass() {
				return clazz;
			}
		};
	}

	@Override
	public @NotNull Class<D> getReturnType() {
		return clazz;
	}

	@Override
	public @NotNull Class<D> getDeclaringClass() {
		return clazz;
	}
}
