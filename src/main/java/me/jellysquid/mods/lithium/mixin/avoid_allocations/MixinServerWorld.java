package me.jellysquid.mods.lithium.mixin.avoid_allocations;

import me.jellysquid.mods.lithium.common.world.ExtendedWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {
    private final BlockPos.Mutable randomPosInChunkCachedPos = new BlockPos.Mutable();

    /**
     * @reason Avoid allocating BlockPos every invocation through using our allocation-free variant
     */
    @Redirect(method = "tickEnvironment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/server/ServerWorld;getBlockRandomPos(IIII)Lnet/minecraft/util/math/BlockPos;"))
    private BlockPos redirectTickGetRandomPosInChunk(ServerWorld serverWorld, int x, int y, int z, int mask) {
        ((ExtendedWorld) serverWorld).getRandomPosInChunk(x, y, z, mask, this.randomPosInChunkCachedPos);

        return this.randomPosInChunkCachedPos;
    }
}
