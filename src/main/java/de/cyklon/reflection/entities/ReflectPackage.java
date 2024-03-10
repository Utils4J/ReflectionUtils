package de.cyklon.reflection.entities;

import de.cyklon.reflection.entities.impl.ReflectPackageImpl;
import de.cyklon.reflection.entities.members.ReflectConstructor;
import de.cyklon.reflection.entities.members.ReflectField;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.exception.ClassNotFoundException;
import de.cyklon.reflection.exception.NotLoadedException;
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

	/**
	 * the java base package
	 */
	ReflectPackage BASE_PACKAGE = get("");

	/**
	 * get a ReflectPackage by the name
	 * @param packageName the package name
	 * @return the ReflectPackage
	 * @throws NotLoadedException if the package matches the given name is not loaded yet
	 * @throws PackageNotFoundException if no package matches the given name
	 */
	@NotNull
	static ReflectPackage get(@NotNull String packageName) throws NotLoadedException, PackageNotFoundException {
		return ReflectPackageImpl.get(packageName);
	}

	/**
	 * @return the default java reflect Package
	 * @see Package
	 */
	@NotNull
	Package getPackage();

	/**
	 * @return all classes in the package as ReflectClass. Unloaded packages will be automatically loaded
	 * @see ReflectClass
	 */
	@NotNull
	@Unmodifiable
	default Set<? extends ReflectClass<?>> getLoadedClasses() {
		return getClasses().stream()
				.map(ClassFile::load)
				.collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * @param filter the filter to search for specific classes
	 * @return all classes matches the filter in the package as ReflectClass. Unloaded packages will be automatically loaded
	 * @see Filter
	 * @see ReflectClass
	 */
	@NotNull
	@Unmodifiable
	default Set<? extends ReflectClass<?>> getLoadedClasses(@NotNull Filter<ReflectClass<?>> filter) {
		return getLoadedClasses().stream()
				.filter(filter::filter)
				.collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * search for a class in this package matches the given name
	 * <p>
	 * the class name can be the full class name: java.lang.reflect.Field
	 * <p>
	 * or the relativ name (as example, the reflect package is java.lang): reflect.Field
	 * @param name the full class name or relativ class name
	 * @return a Optional with a reflect class if a matching class was found or a empty Optional
	 */
	@NotNull
	default Optional<? extends ReflectClass<?>> getOptionalLoadedClass(@NotNull String name) {
		return getLoadedClasses(isBasePackage() ? Filter.hasName(name) : c -> {
			String n = c.getName();
			n = n.substring(getName().length() + 1);
			return n.equals(name) || c.getName().equals(name);
		}).stream().findFirst();
	}

	/**
	 * search for a class in this package matches the given name
	 * <p>
	 * the class name can be the full class name: java.lang.reflect.Field
	 * <p>
	 * or the relativ name (as example, the reflect package is java.lang): reflect.Field
	 * @param name the full class name or relativ class name
	 * @return the ReflectClass if a matching class was found
	 * @throws ClassNotFoundException if no class was found with the given name
	 */
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