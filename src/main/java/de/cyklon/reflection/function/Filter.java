package de.cyklon.reflection.function;

import de.cyklon.reflection.Annotatable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface Filter<T> {

	@NotNull
	static <T extends Annotatable> Filter<T> hasAnnotation(@NotNull Class<? extends Annotation> annotation) {
		return a -> a.hasAnnotation(annotation);
	}

	@NotNull
	static <T> Filter<T> all() {
		return obj -> true;
	}

	@NotNull
	static <T> Filter<T> allOf(@NotNull Collection<Filter<T>> filters) {
		return allOf(all(), filters.toArray(Filter[]::new));
	}

	@NotNull
	@SafeVarargs
	static <T> Filter<T> allOf(@NotNull Filter<T> filter, @NotNull Filter<T>... filters) {
		for (Filter<T> f : filters) filter = filter.and(f);
		return filter;
	}

	@NotNull
	static <T> Filter<T> anyOf(@NotNull Collection<Filter<T>> filters) {
		return anyOf(all(), filters.toArray(Filter[]::new));
	}

	@NotNull
	@SafeVarargs
	static <T> Filter<T> anyOf(@NotNull Filter<T> filter, @NotNull Filter<T>... filters) {
		for (Filter<T> f : filters) filter = filter.or(f);
		return filter;
	}

	boolean filter(@NotNull T obj);

	@Contract("null -> false")
	default boolean apply(@Nullable T obj) {
		return obj != null && filter(obj);
	}

	@NotNull
	default Filter<T> not() {
		return obj -> !filter(obj);
	}

	@NotNull
	default Filter<T> and(@NotNull Filter<T> other) {
		return obj -> filter(obj) && other.filter(obj);
	}

	@NotNull
	default Filter<T> or(@NotNull Filter<T> other) {
		return obj -> filter(obj) || other.filter(obj);
	}

	@NotNull
	default Filter<T> xOr(@NotNull Filter<T> other) {
		return obj -> filter(obj) ^ other.filter(obj);
	}
}
