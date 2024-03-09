package de.cyklon.reflection.entities;

import de.cyklon.reflection.entities.impl.ReflectPackageImpl;
import de.cyklon.reflection.entities.members.ReflectConstructor;
import de.cyklon.reflection.entities.members.ReflectField;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.exception.ClassNotFoundException;
import de.cyklon.reflection.exception.PackageNotFoundException;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.types.ReflectEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;
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
	default Set<? extends ReflectClass<?>> getLoadedClasses() {
		return getClasses().stream()
				.map(ClassFile::load)
				.collect(Collectors.toUnmodifiableSet());
	}

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
	default Optional<? extends ReflectClass<?>> getOptionalLoadedClass(@NotNull String name) {
		return getLoadedClasses(isBasePackage() ? Filter.hasName(name) : c -> {
			String n = c.getName();
			n = n.substring(getName().length() + 1);
			return n.equals(name) || c.getName().equals(name);
		}).stream().findFirst();
	}

	@NotNull
	default ReflectClass<?> getLoadedClass(@NotNull String name) throws ClassNotFoundException {
		return getOptionalLoadedClass(name).orElseThrow(() -> new ClassNotFoundException(this, name));
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
	Set<? extends ReflectPackage> getLoadedPackages();

	@NotNull
	@Unmodifiable
	default Set<? extends ReflectPackage> getLoadedPackages(@NotNull Filter<ReflectPackage> filter) {
		return getLoadedPackages().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	default Optional<? extends ReflectPackage> getOptionalLoadedPackage(String name) {
		return getLoadedPackages(Filter.hasName(name)).stream().findFirst();
	}

	@NotNull
	default ReflectPackage getLoadedPackage(String name) throws PackageNotFoundException {
		return getOptionalLoadedPackage(name).orElseThrow(() -> new PackageNotFoundException(this, name));
	}

	@Override
	@Nullable
	ReflectPackage getParent();

	@NotNull
	@Override
	default ReflectPackage load() {
		return this;
	}
}