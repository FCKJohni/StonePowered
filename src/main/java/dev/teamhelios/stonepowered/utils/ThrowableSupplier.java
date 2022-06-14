package dev.teamhelios.stonepowered.utils;

@FunctionalInterface
public interface ThrowableSupplier<O, T extends Throwable> {

    O get() throws T;
}
