package de.cyklon.reflection.entities.members;

import de.cyklon.reflection.entities.members.impl.ReflectFieldImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public interface ReflectField<D, R> extends ReflectMember<D, R> {
	@NotNull
	static <D, R> ReflectField<D, R> wrap(@NotNull Field field) {
		return ReflectFieldImpl.wrap(field);
	}

	@NotNull Field getField();

	@Nullable R getValue(@NotNull D obj);

	@Nullable
	@SuppressWarnings("ConstantConditions")
	default R getStaticValue() {
		return getValue(null);
	}

}
