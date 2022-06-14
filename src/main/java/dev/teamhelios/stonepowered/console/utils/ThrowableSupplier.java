package dev.teamhelios.stonepowered.console.utils;

/**
 * @author CloudNet
 */
@FunctionalInterface
public interface ThrowableSupplier<O, T extends Throwable> {

    O get() throws T;
}
