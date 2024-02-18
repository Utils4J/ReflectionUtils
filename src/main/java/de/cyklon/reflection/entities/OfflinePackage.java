package de.cyklon.reflection.entities;

import de.cyklon.reflection.entities.impl.OfflinePackageImpl;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.types.Loadable;
import de.cyklon.reflection.types.Nameable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.stream.Collectors;

public interface OfflinePackage extends Nameable, Loadable<ReflectPackage> {

    @NotNull
    static OfflinePackage get(@NotNull String packageName) {
        return OfflinePackageImpl.get(packageName);
    }


    @NotNull
    @Unmodifiable
    Set<? extends ClassFile> getClasses();

    @NotNull
    @Unmodifiable
    default Set<? extends ClassFile> getClasses(@NotNull Filter<ClassFile> filter) {
        return getClasses().stream()
                .filter(filter::filter)
                .collect(Collectors.toUnmodifiableSet());
    }

    @NotNull
    @Unmodifiable
    Set<? extends OfflinePackage> getPackages();

    @NotNull
    @Unmodifiable
    default Set<? extends OfflinePackage> getPackages(@NotNull Filter<OfflinePackage> filter) {
        return getPackages().stream()
                .filter(filter::filter)
                .collect(Collectors.toUnmodifiableSet());
    }

    @NotNull
    OfflinePackage getParent();

    default void loadClasses() {
        loadClasses(Filter.all());
    }

    void loadClasses(Filter<ClassFile> filter);

}
