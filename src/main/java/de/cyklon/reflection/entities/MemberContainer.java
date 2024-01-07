package de.cyklon.reflection.entities;

import de.cyklon.reflection.function.Filter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface MemberContainer<D> {
	@NotNull
	Set<? extends ReflectConstructor<D>> getConstructors(@NotNull Filter<ReflectConstructor<?>> filter);

	@NotNull
	Set<? extends ReflectMethod<D, ?>> getMethods(@NotNull Filter<ReflectMethod<?, ?>> filter);

	@NotNull
	Set<? extends ReflectField<D, ?>> getFields(@NotNull Filter<ReflectField<?, ?>> filter);
}
