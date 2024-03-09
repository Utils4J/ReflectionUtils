package de.cyklon.reflection.exception;

import de.cyklon.reflection.types.Nameable;
import org.jetbrains.annotations.NotNull;

public class PackageNotFoundException extends NotFoundException {

    public PackageNotFoundException(@NotNull String name) {
        this(null, name);
    }

    public PackageNotFoundException(Nameable parent, @NotNull String name) {
        super(name, "package", parent==null ? "" : parent.getName());
    }
}
