package de.cyklon.reflection.entities;

import de.cyklon.reflection.exception.ExecutionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public interface ReflectMethod<D, R> extends AbstractMethod<D, R> {

	@NotNull
	Method getMethod();

	@Nullable
	R invoke(@NotNull D obj, @NotNull Object... args) throws ExecutionException;

	@Nullable
	default R invokeStatic(@NotNull Object... args) throws ExecutionException {
		return invoke(null, args);
	}
}
