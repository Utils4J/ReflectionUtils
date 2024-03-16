package de.cyklon.reflection.function;

import de.cyklon.reflection.entities.members.ReflectMember;
import de.cyklon.reflection.types.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Predicate;

@FunctionalInterface
public interface Filter<T> {

	@NotNull
	static <T> Filter<T> filterNullable(Predicate<T> predicate) {
		return new Filter<>() {
			@Override
			public boolean test(@NotNull T obj) {
				return true;
			}

			@Override
			public boolean filter(@Nullable T obj) {
				return predicate.test(obj);
			}
		};
	}

	@NotNull
	static <T extends Modifiable> Filter<T> hasModifier(@NotNull Modifier modifier) {
		return m -> m.hasModifier(modifier);
	}

	@NotNull
	static <T extends AbstractMethod<?, ?>> Filter<T> hasNoArgs() {
		return m -> m.getParameters().isEmpty();
	}

	@NotNull
	static <T extends Annotatable> Filter<T> hasAnnotation(@NotNull Class<? extends Annotation> annotation) {
		return a -> a.hasAnnotation(annotation);
	}

	@NotNull
	static <T extends Nameable> Filter<T> hasName(@NotNull String name) {
		return n -> n.getName().equals(name);
	}

	@NotNull
	static <T extends Nameable> Filter<T> hasSimpleName(@NotNull String simpleName) {
		return n -> n.getSimpleName().equals(simpleName);
	}

	@NotNull
	static <T extends Nameable> Filter<T> matchesName(@NotNull String regex) {
		return n -> n.getName().matches(regex);
	}

	@NotNull
	static <T extends Nameable> Filter<T> matchesSimpleName(@NotNull String regex) {
		return n -> n.getSimpleName().matches(regex);
	}

	@NotNull
	static <T extends Class<?>> Filter<T> isSubClass(@NotNull Class<T> clazz) {
		return clazz::isAssignableFrom;
	}

	static <T, D extends ReflectMember<T, ?>> Filter<D> isDeclaredIn(@NotNull Class<T> clazz) {
		return obj -> obj.getDeclaringClass().getType().equals(clazz);
	}

	@NotNull
	static <T, D extends Loadable<T>> Filter<D> isLoaded() {
		return Loadable::isLoaded;
	}

	@NotNull
	static <T> Filter<T> notNull() {
		return obj -> true;
	}

	@NotNull
	static <T> Filter<T> all() {
		return filterNullable(obj -> true);
	}

	@NotNull
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
	static <T> Filter<T> anyOf(@NotNull Collection<Filter<T>> filters) {
		return anyOf(all(), filters.toArray(Filter[]::new));
	}

	@NotNull
	@SafeVarargs
	static <T> Filter<T> anyOf(@NotNull Filter<T> filter, @NotNull Filter<T>... filters) {
		for (Filter<T> f : filters) filter = filter.or(f);
		return filter;
	}

	@Contract("null -> false")
	default boolean filter(@Nullable T obj) {
		return obj != null && test(obj);
	}

	default boolean filterInverted(@NotNull T obj) {
		return !filter(obj);
	}

	boolean test(@NotNull T obj);

	@NotNull
	default Filter<T> not() {
		return filterNullable(obj -> !filter(obj));
	}

	@NotNull
	default <E extends T> Filter<E> and(@NotNull Filter<E> other) {
		return filterNullable(obj -> filter(obj) && other.filter(obj));
	}

	@NotNull
	default <E extends T> Filter<E> or(@NotNull Filter<E> other) {
		return filterNullable(obj -> filter(obj) || other.filter(obj));
	}

	@NotNull
	default <E extends T> Filter<E> xOr(@NotNull Filter<E> other) {
		return filterNullable(obj -> filter(obj) ^ other.filter(obj));
	}
}
