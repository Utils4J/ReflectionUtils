package de.cyklon.reflection.function;

import de.cyklon.reflection.types.Loadable;
import de.cyklon.reflection.types.Modifiable;
import de.cyklon.reflection.types.Nameable;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface Sorter<T> extends Comparator<T> {

	@NotNull
	static <T extends Loadable<?>> Sorter<T> byLoadingState() {
		return (l1, l2) -> Boolean.compare(l1.isLoaded(), l2.isLoaded());
	}

	@NotNull
	static <T extends Modifiable> Sorter<T> byModifier() {
		return (m1, m2) -> Integer.compare(m1.getEncodedModifiers(), m2.getEncodedModifiers());
	}

	@NotNull
	static <T extends Nameable> Sorter<T> byName() {
		return (n1, n2) -> n1.getName().compareTo(n2.getName());
	}

	@NotNull
	static <T extends Nameable> Sorter<T> bySimpleName() {
		return (n1, n2) -> n1.getSimpleName().compareTo(n2.getSimpleName());
	}

}
