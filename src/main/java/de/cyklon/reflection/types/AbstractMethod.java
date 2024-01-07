package de.cyklon.reflection.types;

import de.cyklon.reflection.entities.members.ReflectMember;
import de.cyklon.reflection.entities.members.ReflectParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AbstractMethod<D, R> extends ReflectMember<D, R> {

	@NotNull
	List<? extends ReflectParameter<D, ?>> getParameters();

}
