package de.cyklon.reflection;

import de.cyklon.reflection.exception.NotFoundException;
import org.jetbrains.annotations.NotNull;

public final class ReflectionUtils {

	private ReflectionUtils() {
	}

	public static Class<?> getClass(@NotNull String packageName, @NotNull String className) throws NotFoundException {
		int i = className.lastIndexOf('.');
		return getClass(String.format("%s.%s", packageName, className.substring(0, i == -1 ? className.length() : i)));
	}

	public static Class<?> getClass(@NotNull String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new NotFoundException(className, "class", className.substring(0, className.lastIndexOf('.')));
		}
	}

}
