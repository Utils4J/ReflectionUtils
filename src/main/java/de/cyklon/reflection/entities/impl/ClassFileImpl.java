package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ClassFile;
import de.cyklon.reflection.entities.OfflinePackage;
import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.exception.ClassNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class ClassFileImpl implements ClassFile {

	private final String className;

	ClassFileImpl(String className) {
		this.className = className;
		if (!checkClass(className)) throw new ClassNotFoundException(className);
	}

	private static boolean checkClass(@NotNull String className) {
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(className.replaceAll("\\.", "/") + ".class")) {
			return in != null;
		} catch (IOException e) {
			return false;
		}
	}

	@NotNull
	public static ClassFile wrap(@NotNull String className) {
		return new ClassFileImpl(className);
	}

	@NotNull
	@Override
	public String getSimpleName() {
		return className.substring(className.lastIndexOf('.'));
	}

	@NotNull
	@Override
	public OfflinePackage getPackage() {
		return OfflinePackage.get(className.substring(0, className.lastIndexOf('.')));
	}

	@NotNull
	@Override
	public String getName() {
		return className;
	}

	@Override
	public boolean isLoaded() {
		return false;
	}

	@NotNull
	@Override
	public ReflectClass<?> load() {
		return ReflectClass.forName(className);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ClassFile c && c.getName().equals(getName());
	}

	@Override
	public String toString() {
		return getName();
	}
}
