package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.ReflectionUtils;
import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.ReflectPackage;
import de.cyklon.reflection.entities.members.ReflectConstructor;
import de.cyklon.reflection.entities.members.ReflectField;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.exception.NotFoundException;
import de.cyklon.reflection.function.Filter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectPackageImpl implements ReflectPackage {

	private final String packageName;
	private final Package pkg;

	private ReflectPackageImpl(@NotNull String packageName) {
		this.packageName = packageName;
		this.pkg = ClassLoader.getSystemClassLoader().getDefinedPackage(packageName);
		if (pkg == null && checkPackage(packageName)) throw new NotFoundException(packageName, "package", "");
	}

	@NotNull
	@SuppressWarnings("ConstantConditions")
	public static ReflectPackage get(@NotNull String packageName) {
		if (ReflectPackage.BASE_PACKAGE == null) return new ReflectPackageImpl(packageName);
		return packageName.isBlank() ? ReflectPackage.BASE_PACKAGE : new ReflectPackageImpl(packageName);
	}

	//returns true if package was not found
	private static boolean checkPackage(String packageName) {
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("\\.", "/"))) {
			return in==null;
		} catch (IOException e) {
			return true;
		}
	}

	@Override
	public boolean isLoaded() {
		return pkg != null;
	}

	private void checkLoaded() {
		if (!isLoaded()) throw new IllegalStateException("Can not execute this method to an unloaded package");
	}

	@NotNull
	@Override
	@Unmodifiable
	@SuppressWarnings("unchecked")
	public Set<? extends ReflectConstructor<Object>> getConstructors(@NotNull Filter<ReflectConstructor<?>> filter) {
		return (Set<? extends ReflectConstructor<Object>>) getClasses().stream()
				.flatMap(c -> c.getConstructors(filter).stream())
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	@Unmodifiable
	@SuppressWarnings("unchecked")
	public Set<? extends ReflectMethod<Object, ?>> getMethods(@NotNull Filter<ReflectMethod<?, ?>> filter) {
		return (Set<? extends ReflectMethod<Object, ?>>) getClasses().stream()
				.flatMap(c -> c.getMethods(filter).stream())
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	@Unmodifiable
	@SuppressWarnings("unchecked")
	public Set<? extends ReflectField<Object, ?>> getFields(@NotNull Filter<ReflectField<?, ?>> filter) {
		return (Set<? extends ReflectField<Object, ?>>) getClasses().stream()
				.flatMap(c -> c.getFields(filter).stream())
				.collect(Collectors.toUnmodifiableSet());
	}

	@Override
	public @NotNull Package getPackage() {
		checkLoaded();
		return pkg;
	}

	@NotNull
	@Override
	@Unmodifiable
	public Set<? extends ReflectClass<?>> getClasses() {
		return getClasses(getName()).stream()
				.map(ReflectClass::wrap)
				.collect(Collectors.toUnmodifiableSet());
	}

	private static Set<Class<?>> getClasses(String packageName) {
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("\\.", "/"))) {
			if (in == null) throw new NotFoundException(packageName, "package", "");

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				Set<Class<?>> result = new HashSet<>();
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.endsWith(".class")) result.add(ReflectionUtils.getClass(packageName, line));
					else if (!line.contains(".")) result.addAll(getClasses(String.format("%s.%s", packageName, line)));
				}
				return result;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@NotNull
	@Override
	@Unmodifiable
	public Set<? extends ReflectPackage> getPackages() {
		String packageName = getName();
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("\\.", "/"))) {
			if (in == null) throw new NotFoundException(packageName, "package", "");

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				return reader.lines()
						.filter(l -> !l.contains("."))
						.map(l -> get(String.format("%s.%s", packageName, l)))
						.collect(Collectors.toUnmodifiableSet());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public @NotNull ReflectPackage getParent() {
		if (this.equals(BASE_PACKAGE)) throw new IllegalStateException("Base Package has no parent!");
		String currentName = getName();
		return get(currentName.substring(0, currentName.lastIndexOf('.')));
	}

	@Override
	public Annotation[] getAnnotations() {
		checkLoaded();
		return pkg.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		checkLoaded();
		return pkg.getDeclaredAnnotations();
	}

	@Override
	public @NotNull String getName() {
		return isLoaded() ? pkg.getName() : packageName;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ReflectPackage rp && rp.getPackage().getName().equals(pkg.getName());
	}

	@Override
	public String toString() {
		return isLoaded() ? pkg.toString() : String.format("package %s", packageName);
	}
}
