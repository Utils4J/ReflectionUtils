package de.cyklon.reflection.entities;

import de.cyklon.reflection.entities.impl.OfflinePackageImpl;
import de.cyklon.reflection.exception.ClassNotFoundException;
import de.cyklon.reflection.exception.PackageNotFoundException;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.types.Loadable;
import de.cyklon.reflection.types.Nameable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface OfflinePackage extends Nameable, Loadable<ReflectPackage> {

	OfflinePackage BASE_PACKAGE = get("");

	@NotNull
	static OfflinePackage get(@NotNull String packageName) {
		return OfflinePackageImpl.get(packageName);
	}


	default boolean isBasePackage() {
		return ReflectPackage.BASE_PACKAGE.equals(this);
	}

	@NotNull
	@Unmodifiable
	Set<? extends ClassFile> getClasses();

	@NotNull
	@Unmodifiable
	default Set<? extends ClassFile> getClasses(@NotNull Filter<ClassFile> filter) {
		return getClasses().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	default Optional<? extends ClassFile> getOptionalClass(@NotNull String name) {
		return getClasses(Filter.hasName(name)).stream().findFirst();
	}

	@NotNull
	default ClassFile getClass(@NotNull String name) throws ClassNotFoundException {
		return getOptionalClass(name).orElseThrow(() -> new ClassNotFoundException(this, name));
	}

	@NotNull
	@Unmodifiable
	Set<? extends ClassFile> getDirectClasses();

	@NotNull
	@Unmodifiable
	default Set<? extends ClassFile> getDirectClasses(@NotNull Filter<ClassFile> filter) {
		return getDirectClasses().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	default Optional<? extends ClassFile> getDirectOptionalClass(@NotNull String name) {
		return getDirectClasses(Filter.hasName(name)).stream().findFirst();
	}

	@NotNull
	default ClassFile getDirectClass(@NotNull String name) throws ClassNotFoundException {
		return getDirectOptionalClass(name).orElseThrow(() -> new ClassNotFoundException(this, name));
	}

	@NotNull
	@Unmodifiable
	Set<? extends OfflinePackage> getPackages();

	@NotNull
	@Unmodifiable
	default Set<? extends OfflinePackage> getPackages(@NotNull Filter<OfflinePackage> filter) {
		return getPackages().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	default Optional<? extends OfflinePackage> getOptionalPackage(String name) {
		return getPackages(isBasePackage() ? Filter.hasName(name) : c -> {
			String n = c.getName();
			n = n.substring(getName().length() + 1);
			return n.equals(name) || c.getName().equals(name);
		}).stream().findFirst();
	}

	@NotNull
	default OfflinePackage getPackage(String name) throws PackageNotFoundException {
		return getOptionalPackage(name).orElseThrow(() -> new PackageNotFoundException(this, name));
	}

	@Nullable
	OfflinePackage getParent();

	default void loadClasses() {
		loadClasses(Filter.all());
	}

	void loadClasses(Filter<ClassFile> filter);

}
