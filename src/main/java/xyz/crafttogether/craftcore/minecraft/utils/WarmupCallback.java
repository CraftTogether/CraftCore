package xyz.crafttogether.craftcore.minecraft.utils;

/**
 * Callback interface for Warmups
 */
public interface WarmupCallback {
    /**
     * The callback method which will be invoked when the warmup has finished
     *
     * @param successful true if the warmup period expires and the player hasn't moved, otherwise returns false
     */
    void callback(boolean successful);
}
