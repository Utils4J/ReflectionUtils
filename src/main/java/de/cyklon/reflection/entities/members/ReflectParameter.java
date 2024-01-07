package de.cyklon.reflection.entities.members;

import de.cyklon.reflection.types.AbstractMethod;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;

public interface ReflectParameter<D, R> extends ReflectMember<D, R> {
	@NotNull
	Parameter getParameter();

	@NotNull
	AbstractMethod<D, ?> getDeclaringMethod();
}
