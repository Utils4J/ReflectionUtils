package de.cyklon.reflection;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.ReflectConstructor;
import de.cyklon.reflection.entities.ReflectField;
import de.cyklon.reflection.entities.ReflectMethod;
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

class ReflectPackageImpl implements ReflectPackage {

	private final Package pkg;

	private ReflectPackageImpl(@NotNull String packageName) {
		this.pkg = ClassLoader.getSystemClassLoader().getDefinedPackage(packageName);
		if (pkg == null) throw new NotFoundException(packageName, "package", "");
	}

	@NotNull
	static ReflectPackage get(@NotNull String packageName) {
		return new ReflectPackageImpl(packageName);
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public Set<? extends ReflectConstructor<Object>> getConstructors(@NotNull Filter<ReflectConstructor<?>> filter) {
		return (Set<? extends ReflectConstructor<Object>>) getClasses().stream()
				.flatMap(c -> c.getConstructors(filter).stream())
				.collect(Collectors.toSet());
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public Set<? extends ReflectMethod<Object, ?>> getMethods(@NotNull Filter<ReflectMethod<?, ?>> filter) {
		return (Set<? extends ReflectMethod<Object, ?>>) getClasses().stream()
				.flatMap(c -> c.getMethods(filter).stream())
				.collect(Collectors.toSet());
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public Set<? extends ReflectField<Object, ?>> getFields(@NotNull Filter<ReflectField<?, ?>> filter) {
		return (Set<? extends ReflectField<Object, ?>>) getClasses().stream()
				.flatMap(c -> c.getFields(filter).stream())
				.collect(Collectors.toSet());
	}

	@Override
	public @NotNull Package getPackage() {
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

	@Override
	public @NotNull @Unmodifiable Set<? extends ReflectPackage> getPackages() {
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
	public Annotation[] getAnnotations() {
		return pkg.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return pkg.getDeclaredAnnotations();
	}

	@Override
	public @NotNull String getName() {
		return pkg.getName();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ReflectPackage rp && rp.getPackage().equals(pkg);
	}

	@Override
	public String toString() {
		return pkg.toString();
	}
}
