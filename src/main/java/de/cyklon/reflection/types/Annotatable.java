package de.cyklon.reflection.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public interface Annotatable {

	@NotNull
	Annotation[] getAnnotations();

	@NotNull
	Annotation[] getDeclaredAnnotations();

	@Nullable
	default <T extends Annotation> T getAnnotation(@NotNull Class<T> annotation) {
		return Arrays.stream(getAnnotations())
				.filter(a -> a.annotationType().equals(annotation))
				.map(annotation::cast)
				.findFirst()
				.orElse(null);
	}

	@Nullable
	default <T extends Annotation> T getDeclaredAnnotation(@NotNull Class<T> annotation) {
		return Arrays.stream(getDeclaredAnnotations())
				.filter(a -> a.getClass().equals(annotation))
				.map(annotation::cast)
				.findFirst()
				.orElse(null);
	}

	default boolean hasAnnotation(@NotNull Class<? extends Annotation> annotation) {
		return getAnnotation(annotation) != null;
	}

	default boolean hasDeclaredAnnotation(@NotNull Class<? extends Annotation> annotation) {
		return getDeclaredAnnotation(annotation) != null;
	}

}
