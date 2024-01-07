package de.cyklon.reflection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public interface ReflectField<D, R> extends ReflectEntity<D, R> {

	@NotNull Field getField();

	@Nullable R getValue(D obj);

	@Nullable
	default R getStaticValue() {
		return getValue(null);
	}

}
