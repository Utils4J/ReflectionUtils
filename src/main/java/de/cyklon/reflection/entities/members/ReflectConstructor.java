package de.cyklon.reflection.entities.members;

import de.cyklon.reflection.entities.members.impl.ReflectConstructorImpl;
import de.cyklon.reflection.exception.ExecutionException;
import de.cyklon.reflection.types.AbstractMethod;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

public interface ReflectConstructor<D> extends AbstractMethod<D, D> {
	@NotNull
	static <D, R> ReflectConstructor<D> wrap(@NotNull Constructor<D> constructor) {
		return ReflectConstructorImpl.wrap(constructor);
	}

	@NotNull Constructor<D> getConstructor();

	@NotNull
	default D newInstance(@NotNull Object... args) throws ExecutionException {
		return invokeStatic(args);
	}
}
