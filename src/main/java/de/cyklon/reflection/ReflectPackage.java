package de.cyklon.reflection;

import de.cyklon.reflection.entities.MemberContainer;
import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.function.Filter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.stream.Collectors;

public interface ReflectPackage extends Annotatable, Nameable, MemberContainer<Object> {

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