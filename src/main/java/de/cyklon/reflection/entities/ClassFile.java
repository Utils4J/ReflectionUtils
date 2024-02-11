package de.cyklon.reflection.entities;

import de.cyklon.reflection.types.Nameable;

public interface ClassFile extends Nameable {


    String getSimpleName();

    String getPackageName();

}
