package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ClassFile;
import de.cyklon.reflection.entities.OfflinePackage;
import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.exception.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class ClassFileImpl implements ClassFile {

	@SuppressWarnings("rawtypes")
	private static final ReflectMethod<ClassLoader, Class> findLoadedClass = ReflectClass.wrap(ClassLoader.class).getMethod(Class.class, "findLoadedClass", String.class);

	private final String className;

	ClassFileImpl(String className) {
		this.className = className;
		if (checkClass(className)) throw new NotFoundException(className, "class", "");
	}

	private static boolean checkClass(@NotNull String className) {
		try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(className.replaceAll("\\.", "/") + ".class")) {
			return in==null;
		} catch (IOException e) {
			return true;
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
		return findLoadedClass.invoke(ClassLoader.getSystemClassLoader(), className)!=null;
	}

	@Override
	public ReflectClass<?> load() {
		return ReflectClass.forName(className);
	}
}