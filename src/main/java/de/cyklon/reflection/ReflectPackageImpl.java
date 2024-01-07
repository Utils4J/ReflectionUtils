package de.cyklon.reflection;

import de.cyklon.reflection.exception.NotFoundException;
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

	private ReflectPackageImpl(String packageName) {
		this.pkg = Package.getPackage(packageName);
	}

	static ReflectPackage get(String packageName) {
		return new ReflectPackageImpl(packageName);
	}

	@NotNull
	@Override
	@Unmodifiable
	public Set<Class<?>> getClasses() {
		String packageName = pkg.getName().replaceAll("\\.", "/");
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName)) {
			if (in==null) throw new NotFoundException(packageName);

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				return reader.lines()
						.filter(l -> l.endsWith(".class"))
						.map(l -> ReflectionUtils.getClass(packageName, l))
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
}
