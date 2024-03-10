module ReflectionUtils {
	requires java.base;
	requires org.jetbrains.annotations;

	exports de.cyklon.reflection;
	exports de.cyklon.reflection.entities;
	exports de.cyklon.reflection.entities.impl;
	exports de.cyklon.reflection.entities.members;
	exports de.cyklon.reflection.entities.members.impl;
	exports de.cyklon.reflection.exception;
	exports de.cyklon.reflection.function;
	exports de.cyklon.reflection.types;
}