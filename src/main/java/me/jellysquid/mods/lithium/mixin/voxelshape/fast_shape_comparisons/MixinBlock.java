package me.jellysquid.mods.lithium.mixin.voxelshape.fast_shape_comparisons;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Replaces a number of functions in the Block class which are used to determine if some redstone components and other
 * blocks can stand on top of another block.
 */
@Mixin(Block.class)
public class MixinBlock {
    @Shadow
    @Final
    private static VoxelShape field_220083_b;

    @Shadow
    @Final
    private static VoxelShape field_220084_c;

    /**
     * @reason Avoid the expensive call to VoxelShapes#matchesAnywhere if the block in question is a full cube
     * @author JellySquid
     */
    @Overwrite
    public static boolean hasSolidSideOnTop(IBlockReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (state.isIn(BlockTags.LEAVES)) {
            return false;
        }

        VoxelShape shape = state.getCollisionShape(world, pos).project(Direction.UP);

        return shape == VoxelShapes.fullCube() || !VoxelShapes.compare(shape, field_220083_b, IBooleanFunction.ONLY_SECOND);
    }


    /**
     * @reason Avoid the expensive call to VoxelShapes#matchesAnywhere if the block in question is a full cube
     * @author JellySquid
     */
    @Overwrite
    public static boolean hasEnoughSolidSide(IWorldReader world, BlockPos pos, Direction side) {
        BlockState state = world.getBlockState(pos);

        if (state.isIn(BlockTags.LEAVES)) {
            return false;
        }

        VoxelShape shape = state.getCollisionShape(world, pos).project(side);

        return shape == VoxelShapes.fullCube() || !VoxelShapes.compare(shape, field_220084_c, IBooleanFunction.ONLY_SECOND);
    }

}
