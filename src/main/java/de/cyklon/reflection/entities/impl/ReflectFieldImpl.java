package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ReflectField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class ReflectFieldImpl<D, R> extends ReflectEntityImpl<D, R> implements ReflectField<D, R> {
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
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Annotation[] getAnnotations() {
		return field.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return field.getDeclaredAnnotations();
	}

	@Override
	public @NotNull String getName() {
		return field.getName();
	}

	@Override
	public String toString() {
		return field.toString();
	}
}
