package de.cyklon.reflection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public interface ReflectMethod<D, R> extends ReflectEntity<D, R> {

	@NotNull
	Method getMethod();

	@Nullable
	R invoke(D obj, Object... args);

	@Nullable
	default R invokeStatic(Object... args) {
		return invoke(null, args);
	}
}
