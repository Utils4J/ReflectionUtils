package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ReflectField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class ReflectFieldImpl<D, R> extends ReflectEntityImpl<D, R> implements ReflectField<D, R> {
	private final Field field;

	public ReflectFieldImpl(@NotNull Class<D> declaringClass, @NotNull Class<R> returnType, @NotNull Field field) {
		super(declaringClass, returnType);

		field.setAccessible(true);
		this.field = field;
	}

	@Override
	@NotNull
	public Field getField() {
		return field;
	}

	@Override
	@Nullable
	@SuppressWarnings("unchecked")
	public R getValue(@Nullable D obj) {
		try {
			return (R) field.get(obj);
		} catch(IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
