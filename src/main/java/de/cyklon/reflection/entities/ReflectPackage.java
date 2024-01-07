package de.cyklon.reflection.entities;

import de.cyklon.reflection.entities.impl.ReflectPackageImpl;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.types.Annotatable;
import de.cyklon.reflection.types.MemberContainer;
import de.cyklon.reflection.types.Nameable;
import de.cyklon.reflection.types.ReflectEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.stream.Collectors;

public interface ReflectPackage extends MemberContainer<Object>, ReflectEntity {

	@NotNull
	static ReflectPackage get(@NotNull String packageName) {
		return ReflectPackageImpl.get(packageName);
	}

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
	Set<? extends ReflectPackage> getPackages();

	@NotNull
	@Unmodifiable
	default Set<? extends ReflectPackage> getPackages(@NotNull Filter<ReflectPackage> filter) {
		return getPackages().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}


}