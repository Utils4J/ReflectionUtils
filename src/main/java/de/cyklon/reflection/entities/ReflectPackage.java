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

public interface ReflectPackage extends OfflinePackage, ReflectEntity {

	ReflectPackage BASE_PACKAGE = get("");

	@NotNull
	static ReflectPackage get(@NotNull String packageName) {
		return ReflectPackageImpl.get(packageName);
	}

	@NotNull
	@Unmodifiable
	Set<? extends ReflectClass<?>> getLoadedClasses();

	@NotNull
	@Unmodifiable
	default Set<? extends ReflectClass<?>> getLoadedClasses(@NotNull Filter<ReflectClass<?>> filter) {
		return getLoadedClasses().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}


	@NotNull
	Package getPackage();

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
	Set<? extends ReflectPackage> getLoadedPackages();

	@NotNull
	@Unmodifiable
	default Set<? extends ReflectPackage> getLoadedPackages(@NotNull Filter<ReflectPackage> filter) {
		return getLoadedPackages().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	ReflectPackage getParent();


}