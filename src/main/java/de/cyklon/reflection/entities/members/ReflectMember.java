package de.cyklon.reflection.entities.members;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.types.Annotatable;
import de.cyklon.reflection.types.Nameable;
import de.cyklon.reflection.types.ReflectEntitiy;
import org.jetbrains.annotations.NotNull;

public interface ReflectMember<D, R> extends ReflectEntitiy {

	@NotNull
	ReflectClass<R> getReturnType();

	@NotNull
	ReflectClass<D> getDeclaringClass();
}

