package de.cyklon.reflection.types;

import de.cyklon.reflection.entities.members.ReflectMember;
import de.cyklon.reflection.entities.members.ReflectParameter;
import de.cyklon.reflection.exception.ExecutionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AbstractMethod<D, R> extends ReflectMember<D, R> {
	@NotNull
	List<? extends ReflectParameter<D, ?>> getParameters();

	@Nullable
	R invoke(@NotNull Object obj, @NotNull Object... args) throws ExecutionException;

	@Nullable
	default R invokeStatic(@NotNull Object... args) throws ExecutionException {
		return invoke(null, args);
	}
}
