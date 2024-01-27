package de.cyklon.reflection.entities;

import de.cyklon.reflection.entities.impl.ReflectPackageImpl;
import de.cyklon.reflection.entities.members.ReflectConstructor;
import de.cyklon.reflection.entities.members.ReflectField;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.types.ReflectEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.stream.Collectors;

public interface ReflectPackage extends ReflectEntity {

	ReflectPackage BASE_PACKAGE = get("");

	@NotNull
	static ReflectPackage get(@NotNull String packageName) {
		return ReflectPackageImpl.get(packageName);
	}

	boolean isLoaded();

	@NotNull
	Package getPackage();

	@NotNull
	@Unmodifiable
	Set<? extends ReflectClass<?>> getClasses();

	@NotNull
	@Unmodifiable
	default Set<? extends ReflectClass<?>> getClasses(@NotNull Filter<ReflectClass<?>> filter) {
		return getClasses().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Unmodifiable
	<D> Set<? extends ReflectConstructor<D>> getConstructors(@NotNull Filter<ReflectConstructor<? extends D>> filter);

	@NotNull
	@Unmodifiable
	<D> Set<? extends ReflectMethod<D, ?>> getMethods(@NotNull Filter<ReflectMethod<? extends D, ?>> filter);

	@NotNull
	@Unmodifiable
	<D> Set<? extends ReflectField<D, ?>> getFields(@NotNull Filter<ReflectField<? extends D, ?>> filter);

	@NotNull
	@Unmodifiable
	Set<? extends ReflectPackage> getPackages();

	@NotNull
	@Unmodifiable
	default Set<? extends ReflectPackage> getPackages(@NotNull Filter<ReflectPackage> filter) {
		return getPackages().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	ReflectPackage getParent();


}