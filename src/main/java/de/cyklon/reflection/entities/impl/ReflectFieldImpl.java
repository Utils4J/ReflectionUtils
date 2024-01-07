package de.cyklon.reflection.entities.impl;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.ReflectField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class ReflectFieldImpl<D, R> extends ReflectEntityImpl<D, R> implements ReflectField<D, R> {
	private final Field field;

	public ReflectFieldImpl(@NotNull ReflectClass<D> declaringClass, @NotNull Field field) {
		super(declaringClass, ReflectClass.wrap(field.getGenericType()));

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

	@NotNull
	@Override
	public String getName() {
		return field.getName();
	}


	@Override
	public boolean equals(Object obj) {
		return obj instanceof ReflectField<?, ?> rf && rf.getField().equals(field);
	}

	@Override
	public String toString() {
		return field.toString();
	}
}
