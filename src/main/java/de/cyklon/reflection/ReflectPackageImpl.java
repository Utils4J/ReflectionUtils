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
import java.util.Set;
import java.util.stream.Collectors;

class ReflectPackageImpl implements ReflectPackage {

	private final Package pkg;

	private ReflectPackageImpl(@NotNull String packageName) {
		this.pkg = Package.getPackage(packageName);
	}

	@NotNull
	static ReflectPackage get(@NotNull String packageName) {
		return new ReflectPackageImpl(packageName);
	}

	@NotNull
	@Override
	public Set<? extends ReflectConstructor<Object>> getConstructors(@NotNull Filter<ReflectConstructor<Object>> filter) {
		return getClasses().stream()
				.flatMap(c -> c.getConstructors(filter).stream())
				.collect(Collectors.toSet());
	}

	@NotNull
	@Override
	public Set<? extends ReflectMethod<Object, ?>> getMethods(@NotNull Filter<ReflectMethod<Object, ?>> filter) {
		return getClasses().stream()
				.flatMap(c -> c.getMethods(filter).stream())
				.collect(Collectors.toSet());
	}

	@NotNull
	@Override
	public Set<? extends ReflectField<Object, ?>> getFields(@NotNull Filter<ReflectField<Object, ?>> filter) {
		return getClasses().stream()
				.flatMap(c -> c.getFields(filter).stream())
				.collect(Collectors.toSet());
	}


	@NotNull
	@Override
	@Unmodifiable
	public Set<? extends ReflectClass<?>> getClasses() {
		String packageName = getName().replaceAll("\\.", "/");
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName)) {
			if (in==null) throw new NotFoundException(packageName);

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				return reader.lines()
						.filter(l -> l.endsWith(".class"))
						.map(l -> ReflectClass.wrap(ReflectionUtils.getClass(packageName, l)))
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
}
