package me.jellysquid.mods.lithium.common;

import me.jellysquid.mods.lithium.asm.patches.DevirtualizeBlockPosTransformer;
import me.jellysquid.mods.lithium.common.config.LithiumConfig;

public class LithiumPatcher implements Runnable {
    @Override
    public void run() {
        LithiumConfig config = LithiumConfig.instance();

        if (config.general.useBlockPosOptimizations) {
            DevirtualizeBlockPosTransformer.install();
        }
    }
}
