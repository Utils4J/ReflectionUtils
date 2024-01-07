package de.cyklon.reflection.entities.members;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.types.ReflectEntity;
import org.jetbrains.annotations.NotNull;

public interface ReflectMember<D, R> extends ReflectEntity {

	@NotNull
	ReflectClass<R> getReturnType();

	@NotNull
	ReflectClass<D> getDeclaringClass();
}

