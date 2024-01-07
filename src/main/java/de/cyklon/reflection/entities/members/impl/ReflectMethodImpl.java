package de.cyklon.reflection.entities.members.impl;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.entities.members.ReflectParameter;
import de.cyklon.reflection.exception.ExecutionException;
import de.cyklon.reflection.types.Modifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.IntStream;

public class ReflectMethodImpl<D, R> extends ReflectMemberImpl<D, R> implements ReflectMethod<D, R> {
	private final Method method;

	public ReflectMethodImpl(@NotNull ReflectClass<D> declaringClass, @NotNull Method method) {
		super(declaringClass, ReflectClass.wrap(method.getGenericReturnType()));
		method.setAccessible(true);
		this.method = method;
	}

	@NotNull
	@Override
	public Method getMethod() {
		return method;
	}

	@NotNull
	@Override
	public List<? extends ReflectParameter<D, Object>> getParameters() {
		return IntStream.range(0, method.getParameterCount())
				.mapToObj(i -> new ReflectParameterImpl<>(method.getParameters()[i], ReflectClass.wrap(method.getGenericParameterTypes()[i]), this))
				.toList();
	}

	@Override
	@Nullable
	@SuppressWarnings("unchecked")
	public R invoke(@Nullable D obj, @NotNull Object... args) throws ExecutionException {
		try {
			return (R) method.invoke(obj, args);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new ExecutionException(e.getCause());
		}
	}

	@NotNull
	@Override
	public EnumSet<Modifier> getModifiers() throws UnsupportedOperationException {
		return Modifier.parse(method.getModifiers());
	}

	@Override
	public Annotation[] getAnnotations() {
		return method.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return method.getDeclaredAnnotations();
	}

	@Override
	public @NotNull String getName() {
		return method.getName();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ReflectMethod<?, ?> rm && rm.getMethod().equals(method);
	}

	@Override
	public String toString() {
		return method.toString();
	}
}
