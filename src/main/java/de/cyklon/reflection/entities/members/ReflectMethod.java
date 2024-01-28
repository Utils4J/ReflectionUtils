package de.cyklon.reflection.entities.members;

import de.cyklon.reflection.entities.members.impl.ReflectMethodImpl;
import de.cyklon.reflection.types.AbstractMethod;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public interface ReflectMethod<D, R> extends AbstractMethod<D, R> {
	@NotNull
	static <D, R> ReflectMethod<D, R> wrap(@NotNull Method method) {
		return ReflectMethodImpl.wrap(method);
	}

	@NotNull
	Method getMethod();
}
