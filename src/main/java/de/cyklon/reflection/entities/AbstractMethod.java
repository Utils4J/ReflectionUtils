package de.cyklon.reflection.entities;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AbstractMethod<D, R> extends ReflectEntity<D, R> {

	@NotNull
	List<? extends ReflectParameter<D, ?>> getParameters();

}
