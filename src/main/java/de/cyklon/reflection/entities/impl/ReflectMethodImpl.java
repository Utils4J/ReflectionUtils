package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ReflectMethod;
import de.cyklon.reflection.exception.ExecutionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectMethodImpl<D, R> extends ReflectEntityImpl<D, R> implements ReflectMethod<D, R> {
	private final Method method;

	public ReflectMethodImpl(@NotNull Class<D> declaringClass, @NotNull Class<R> returnType, @NotNull Method method) {
		super(declaringClass, returnType);
		method.setAccessible(true);
		this.method = method;
	}

	@NotNull
	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	@Nullable
	@SuppressWarnings("unchecked")
	public R invoke(@Nullable D obj, @NotNull Object... args) throws ExecutionException {
		try {
			return (R) method.invoke(obj, args);
		} catch(IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch(InvocationTargetException e) {
			throw new ExecutionException(e.getCause());
		}
	}

	@Override
	public Annotation[] getAnnotations() {
		return method.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return method.getDeclaredAnnotations();
	}
}
