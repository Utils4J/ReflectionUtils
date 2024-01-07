package de.cyklon.reflection.entities.members;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.types.Modifiable;
import de.cyklon.reflection.types.Modifier;
import de.cyklon.reflection.types.ReflectEntity;
import org.jetbrains.annotations.NotNull;

public interface ReflectMember<D, R> extends ReflectEntity, Modifiable {

	@NotNull
	ReflectClass<R> getReturnType();

	@NotNull
	ReflectClass<D> getDeclaringClass();
}

