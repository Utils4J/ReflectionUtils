package de.cyklon.reflection.entities;

import de.cyklon.reflection.entities.impl.ClassFileImpl;
import de.cyklon.reflection.types.Loadable;
import de.cyklon.reflection.types.Nameable;
import org.jetbrains.annotations.NotNull;

public interface ClassFile extends Nameable, Loadable<ReflectClass<?>> {
    @NotNull
    static ClassFile forName(String className) {
        return ClassFileImpl.wrap(className);
    }

    @NotNull
    String getSimpleName();

    @NotNull
    OfflinePackage getPackage();

}
