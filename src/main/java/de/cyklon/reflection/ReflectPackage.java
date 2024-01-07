package de.cyklon.reflection;

import de.cyklon.reflection.function.Filter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.stream.Collectors;

public interface ReflectPackage extends Annotatable {


	@NotNull
	static ReflectPackage get(@NotNull String packageName) {
		return ReflectPackageImpl.get(packageName);
	}

	@NotNull
	@Unmodifiable
	Set<Class<?>> getClasses();

	@NotNull
	@Unmodifiable
	default Set<Class<?>> getClasses(@NotNull Filter<Class<?>> filter) {
		return getClasses().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

}