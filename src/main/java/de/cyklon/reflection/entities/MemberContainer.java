package de.cyklon.reflection.entities;

import de.cyklon.reflection.function.Filter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface MemberContainer<D> {
	@NotNull
	Set<? extends ReflectConstructor<D>> getConstructors(@NotNull Filter<ReflectConstructor<D>> filter);

	@NotNull
	Set<? extends ReflectMethod<D, ?>> getMethods(@NotNull Filter<ReflectMethod<D, ?>> filter);

	@NotNull
	Set<? extends ReflectField<D, ?>> getFields(@NotNull Filter<ReflectField<D, ?>> filter);
}
