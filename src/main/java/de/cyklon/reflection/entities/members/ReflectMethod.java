package de.cyklon.reflection.entities.members;

import de.cyklon.reflection.entities.members.impl.ReflectMethodImpl;
import de.cyklon.reflection.exception.ExecutionException;
import de.cyklon.reflection.types.AbstractMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public interface ReflectMethod<D, R> extends AbstractMethod<D, R> {
	@NotNull
	static <D, R> ReflectMethod<D, R> wrap(@NotNull Method method) {
		return ReflectMethodImpl.wrap(method);
	}

	@NotNull
	Method getMethod();

	@Nullable
	R invoke(@NotNull D obj, @NotNull Object... args) throws ExecutionException;

	@Nullable
	default R invokeStatic(@NotNull Object... args) throws ExecutionException {
		return invoke(null, args);
	}
}
