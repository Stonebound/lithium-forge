package me.jellysquid.mods.lithium.mixin.entity.simple_entity_block_collisions;

import me.jellysquid.mods.lithium.common.shapes.LithiumEntityCollisions;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.stream.Stream;

/**
 * Replaces collision testing methods with jumps to our own (faster) entity collision testing code.
 */
@Mixin(IWorldReader.class)
public interface MixinICollisionReader {
    /**
     * @reason Use a faster implementation
     * @author JellySquid
     */
    @Overwrite
    default Stream<VoxelShape> getCollisionShapes(final Entity entity, AxisAlignedBB box) {
        return LithiumEntityCollisions.getBlockCollisions((IWorldReader) this, entity, box);
    }
}