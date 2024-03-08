package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.OfflinePackage;
import de.cyklon.reflection.entities.ReflectPackage;
import de.cyklon.reflection.entities.members.ReflectConstructor;
import de.cyklon.reflection.entities.members.ReflectField;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.exception.NotFoundException;
import de.cyklon.reflection.exception.NotLoadedException;
import de.cyklon.reflection.function.Filter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectPackageImpl extends OfflinePackageImpl implements ReflectPackage {
	private final Package pkg;

	private ReflectPackageImpl(@NotNull String packageName) {
		super(packageName);
		this.pkg = getDefinedPackage(packageName);
		if (!isLoaded()) throw new NotLoadedException(packageName, "package", "");
		if (checkPackage(packageName)) throw new NotFoundException(packageName, "package", "");
	}

	@NotNull
	@SuppressWarnings("ConstantConditions")
	public static ReflectPackage get(@NotNull String packageName) {
		if (ReflectPackage.BASE_PACKAGE == null) return new ReflectPackageImpl(packageName);
		return packageName.isBlank() ? ReflectPackage.BASE_PACKAGE : new ReflectPackageImpl(packageName);
	}

	@Nullable
	static Package getDefinedPackage(@NotNull String packageName) {
		return ClassLoader.getSystemClassLoader().getDefinedPackage(packageName);
	}

	@Override
	public boolean isLoaded() {
		return pkg != null;
	}

	@Override
	public ReflectPackage load() {
		return this;
	}

	@NotNull
	@Override
	@Unmodifiable
	@SuppressWarnings({"unchecked", "rawtypes"})
	public <D> Set<ReflectConstructor<D>> getConstructors(@NotNull Filter<ReflectConstructor<? extends D>> filter) {
		return (Set<ReflectConstructor<D>>) getLoadedClasses().stream()
				.flatMap(c -> c.getConstructors((Filter) filter).stream())
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	@Unmodifiable
	@SuppressWarnings({"unchecked", "rawtypes"})
	public <D> Set<ReflectMethod<D, ?>> getMethods(@NotNull Filter<ReflectMethod<? extends D, ?>> filter) {
		return (Set<ReflectMethod<D, ?>>) getLoadedClasses().stream()
				.flatMap(c -> c.getMethods((Filter) filter).stream())
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	@Unmodifiable
	@SuppressWarnings({"unchecked", "rawtypes"})
	public <D> Set<ReflectField<D, ?>> getFields(@NotNull Filter<ReflectField<? extends D, ?>> filter) {
		return (Set<ReflectField<D, ?>>) getLoadedClasses().stream()
				.flatMap(c -> c.getFields((Filter) filter).stream())
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	public Package getPackage() {
		return pkg;
	}

	@NotNull
	@Override
	@Unmodifiable
	public Set<? extends ReflectPackage> getLoadedPackages() {
		return getPackages().stream()
				.filter(OfflinePackage::isLoaded)
				.map(p -> get(p.getName()))
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	@Unmodifiable
	public Set<? extends OfflinePackage> getPackages() {
		String packageName = getName();
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("\\.", "/"))) {
			if (in == null) throw new NotFoundException(packageName, "package", "");

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				return reader.lines()
						.filter(l -> !l.contains("."))
						.map(l -> OfflinePackage.get(getMemberName(packageName, l)))
						.collect(Collectors.toUnmodifiableSet());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Nullable
	public ReflectPackage getParent() {
		if (isBasePackage()) return null;
		String currentName = getName();
		int i = currentName.lastIndexOf('.');
		if (i == -1) return ReflectPackage.BASE_PACKAGE;
		return get(currentName.substring(0, i));
	}

	@Override
	public Annotation[] getAnnotations() {
		return pkg.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return pkg.getDeclaredAnnotations();
	}

	@NotNull
	@Override
	public String getName() {
		return pkg.getName();
	}

	@Override
	public String toString() {
		return isBasePackage() ? "base package" : pkg.toString();
	}
}
