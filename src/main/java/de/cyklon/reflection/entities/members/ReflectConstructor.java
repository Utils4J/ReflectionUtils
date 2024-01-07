package de.cyklon.reflection.entities.members;

import de.cyklon.reflection.types.AbstractMethod;
import de.cyklon.reflection.exception.ExecutionException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

public interface ReflectConstructor<D> extends AbstractMethod<D, D> {
	@NotNull Constructor<D> getConstructor();

	@NotNull
	D newInstance(@NotNull Object... args) throws ExecutionException;
}
