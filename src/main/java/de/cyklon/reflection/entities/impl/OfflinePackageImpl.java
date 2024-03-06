package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ClassFile;
import de.cyklon.reflection.entities.OfflinePackage;
import de.cyklon.reflection.entities.ReflectPackage;
import de.cyklon.reflection.exception.NotFoundException;
import de.cyklon.reflection.function.Filter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class OfflinePackageImpl implements OfflinePackage {

	private final String packageName;

	OfflinePackageImpl(String packageName) {
		this.packageName = packageName;
	}

	@NotNull
	@SuppressWarnings("ConstantConditions")
	public static OfflinePackage get(@NotNull String packageName) {
		packageName = packageName.strip();
		if (checkPackage(packageName)) throw new NotFoundException(packageName, "package", "");
		if (ReflectPackage.BASE_PACKAGE == null) return new OfflinePackageImpl(packageName);
		return packageName.isBlank() ? ReflectPackage.BASE_PACKAGE : new OfflinePackageImpl(packageName);
	}

	//returns true if package was not found
	static boolean checkPackage(@NotNull String packageName) {
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("\\.", "/"))) {
			return in == null;
		} catch (IOException e) {
			return true;
		}
	}

	@Override
	public boolean isLoaded() {
		return ReflectPackageImpl.getPackage(packageName) != null;
	}

	@Override
	public ReflectPackage load() {
		return getClasses().stream()
				.findFirst()
				.orElseThrow(() -> new NotFoundException("any", "class", String.format("package %s, therefore this package cannot be loaded!", getName())))
				.load()
				.getPackage();
	}

	@NotNull
	@Override
	@Unmodifiable
	public Set<? extends ClassFile> getClasses() {
		return getClasses(getName()).stream()
				.map(ClassFile::forName)
				.collect(Collectors.toUnmodifiableSet());
	}

	private static Set<String> getClasses(String packageName) {
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("\\.", "/"))) {
			if (in == null) throw new NotFoundException(packageName, "package", "");

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				Set<String> result = new HashSet<>();
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.endsWith(".class")) result.add(getClassName(packageName, line));
					else if (!line.contains(".")) result.addAll(getClasses(getMemberName(packageName, line)));
				}
				return result;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected static String getMemberName(String packageName, String packageMember) {
		if (packageName.isBlank()) return packageMember;
		return String.format("%s.%s", packageName, packageMember);
	}

	private static String getClassName(String packageName, String className) {
		int i = className.endsWith(".class") ? className.lastIndexOf(".") : -1;
		return getMemberName(packageName.replace("/", "."), i != -1 ? className.substring(0, i) : className);
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
						.map(l -> get(getMemberName(packageName, l)))
						.collect(Collectors.toUnmodifiableSet());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Nullable
	public OfflinePackage getParent() {
		if (isBasePackage()) return null;
		String currentName = getName();
		int i = currentName.lastIndexOf('.');
		if (i == -1) return ReflectPackage.BASE_PACKAGE;
		return get(currentName.substring(0, i));
	}

	@Override
	public void loadClasses(Filter<ClassFile> filter) {
		getClasses(filter).forEach(ClassFile::load);
	}

	@NotNull
	@Override
	public String getName() {
		return packageName;
	}

	@NotNull
	@Override
	public String getSimpleName() {
		OfflinePackage parent = getParent();
		if (parent == null || parent.isBasePackage()) return getName();
		String name = getName();
		return name.substring(name.lastIndexOf('.') + 1);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof OfflinePackage pkg && pkg.getName().equals(getName());
	}

	@Override
	public String toString() {
		return isBasePackage() ? "base package" : String.format("package %s", packageName);
	}
}
