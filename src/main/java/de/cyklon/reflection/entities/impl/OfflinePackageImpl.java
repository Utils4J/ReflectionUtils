package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ClassFile;
import de.cyklon.reflection.entities.OfflinePackage;
import de.cyklon.reflection.entities.ReflectPackage;
import de.cyklon.reflection.exception.PackageLoadException;
import de.cyklon.reflection.exception.PackageNotFoundException;
import de.cyklon.reflection.function.Filter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
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
		if (!checkPackage(packageName)) throw new PackageNotFoundException(packageName);
		if (BASE_PACKAGE == null) return new OfflinePackageImpl(packageName);
		return packageName.isBlank() ? BASE_PACKAGE : new OfflinePackageImpl(packageName);
	}

	//returns false if package was not found
	static boolean checkPackage(@NotNull String packageName) {
		if (packageName.isBlank()) return true;
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("\\.", "/"))) {
			return in != null;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean isLoaded() {
		return ReflectPackageImpl.getDefinedPackage(packageName) != null;
	}

	@NotNull
	@Override
	public ReflectPackage load() {
		return getDirectClasses().stream()
				.findFirst()
				.orElseThrow(() -> new PackageLoadException(getName()))
				.load()
				.getPackage();
	}

	@NotNull
	@Override
	public Set<? extends ReflectPackage> loadRecursive() {
		return Collections.unmodifiableSet(loadSubPackages(this));
	}

	private Set<? extends ReflectPackage> loadSubPackages(OfflinePackage pkg) {
		Set<ReflectPackage> result = new HashSet<>();
		if (!pkg.getDirectClasses().isEmpty()) result.add(pkg.load());
		pkg.getPackages().forEach(p -> result.addAll(loadSubPackages(p)));
		return result;
	}

	@NotNull
	@Override
	public Set<? extends ClassFile> getClasses() {
		return getClasses(getName(), Integer.MAX_VALUE).stream()
				.map(ClassFile::forName)
				.collect(Collectors.toUnmodifiableSet());
	}

	@NotNull
	@Override
	public Set<? extends ClassFile> getDirectClasses() {
		return getClasses(getName(), 1).stream()
				.map(ClassFile::forName)
				.collect(Collectors.toUnmodifiableSet());
	}

	private static Set<String> getClasses(String packageName, int maxDepth) {
		if (maxDepth <= 0) return Collections.emptySet();
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("\\.", "/"))) {
			if (in == null) throw new PackageNotFoundException(packageName);

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				Set<String> result = new HashSet<>();
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.equals("package-info.class") || line.equals("module-info.class")) continue;

					if (line.endsWith(".class")) result.add(getClassName(packageName, line));
					else if (!line.contains(".")) result.addAll(getClasses(getMemberName(packageName, line), maxDepth - 1));
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
			if (in == null) throw new PackageNotFoundException(this, packageName);

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
		if (i == -1) return BASE_PACKAGE;
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
	public int hashCode() {
		return packageName.hashCode();
	}

	@Override
	public String toString() {
		return isBasePackage() ? "base package" : String.format("package %s", packageName);
	}
}
