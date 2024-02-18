package de.cyklon.reflection.entities;

import de.cyklon.reflection.entities.impl.ReflectPackageImpl;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.types.MemberContainer;
import de.cyklon.reflection.types.ReflectEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.stream.Collectors;

public interface ReflectPackage extends OfflinePackage, MemberContainer<Object>, ReflectEntity {

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