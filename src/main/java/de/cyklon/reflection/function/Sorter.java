package de.cyklon.reflection.function;

import de.cyklon.reflection.types.Loadable;
import de.cyklon.reflection.types.Modifiable;
import de.cyklon.reflection.types.Nameable;

import java.util.Comparator;

public interface Sorter<T> extends Comparator<T> {

	static <T> Sorter<T> random() {
		return (o1, o2) -> Math.round((float) (Math.random()*3-2));
	}

	static <T extends Loadable<?>> Sorter<T> byLoadingState() {
		return (l1, l2) -> Boolean.compare(l1.isLoaded(), l2.isLoaded());
	}

	static <T extends Modifiable> Sorter<T> byModifier() {
		return (m1, m2) -> Integer.compare(m1.getEncodedModifiers(), m2.getEncodedModifiers());
	}

	static <T extends Nameable> Sorter<T> byName() {
		return (n1, n2) -> n1.getName().compareTo(n2.getName());
	}

	static <T extends Nameable> Sorter<T> bySimpleName() {
		return (n1, n2) -> n1.getSimpleName().compareTo(n2.getSimpleName());
	}

}
