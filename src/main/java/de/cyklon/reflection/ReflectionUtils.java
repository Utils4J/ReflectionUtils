package de.cyklon.reflection;

import de.cyklon.reflection.exception.ClassNotFoundException;
import de.cyklon.reflection.types.Nameable;
import org.jetbrains.annotations.NotNull;

public final class ReflectionUtils {

	private ReflectionUtils() {
	}

	@NotNull
	public static Class<?> getClass(@NotNull String packageName, @NotNull String className) throws ClassNotFoundException {
		int i = className.lastIndexOf('.');
		return getClass(String.format("%s.%s", packageName, className.substring(0, i == -1 ? className.length() : i)));
	}

	@NotNull
	public static Class<?> getClass(@NotNull String className) throws ClassNotFoundException {
		try {
			return Class.forName(className);
		} catch (java.lang.ClassNotFoundException e) {
			throw new ClassNotFoundException(Nameable.wrap(className.substring(0, className.lastIndexOf('.'))), className);
		}
	}

}
