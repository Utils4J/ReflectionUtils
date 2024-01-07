package de.cyklon.reflection.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public interface ReflectField<D, R> extends ReflectEntity<D, R> {

	@NotNull Field getField();

	@Nullable R getValue(@NotNull D obj);

	@Nullable
	@SuppressWarnings("ConstantConditions")
	default R getStaticValue() {
		return getValue(null);
	}

}
