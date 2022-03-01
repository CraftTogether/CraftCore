package xyz.crafttogether.craftcore.minecraft.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarmupHandler {
    private static final List<Warmup> commandWarmups = new ArrayList<>();

    public static void schedule(UUID playerUUID, int warmup, WarmupCallback warmupCallback) {
        commandWarmups.add(new Warmup(playerUUID, warmup, warmupCallback, System.currentTimeMillis() / 1000));
    }

    public static List<Warmup> getCommandWarmups() {
        return commandWarmups;
    }
}
