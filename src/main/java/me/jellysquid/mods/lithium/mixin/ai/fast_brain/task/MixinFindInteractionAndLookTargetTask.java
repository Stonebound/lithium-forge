package me.jellysquid.mods.lithium.mixin.ai.fast_brain.task;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.task.FindInteractionAndLookTargetTask;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Mixin(FindInteractionAndLookTargetTask.class)
public abstract class MixinFindInteractionAndLookTargetTask extends Task<LivingEntity> {
    @Shadow
    @Final
    private Predicate<LivingEntity> field_220536_d;

    @Shadow
    protected abstract List<LivingEntity> func_220530_b(LivingEntity livingEntity_1);

    @Shadow
    protected abstract boolean func_220532_a(LivingEntity livingEntity_1);

    @Shadow
    @Final
    private int field_220534_b;

    public MixinFindInteractionAndLookTargetTask(Map<MemoryModuleType<?>, MemoryModuleStatus> memories) {
        super(memories);
    }

    /**
     * @reason Replace stream code with traditional iteration
     * @author JellySquid
     */
    @Overwrite
    public boolean shouldExecute(ServerWorld world, LivingEntity self) {
        if (!this.field_220536_d.test(self)) {
            return false;
        }

        List<LivingEntity> visible = this.func_220530_b(self);

        for (LivingEntity entity : visible) {
            if (this.func_220532_a(entity)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @reason Replace stream code with traditional iteration
     * @author JellySquid
     */
    @Overwrite
    public void startExecuting(ServerWorld world, LivingEntity self, long time) {
        super.startExecuting(world, self, time);

        Brain<?> brain = self.getBrain();

        List<LivingEntity> visible = brain.getMemory(MemoryModuleType.VISIBLE_MOBS)
                .orElse(Collections.emptyList());

        for (LivingEntity entity : visible) {
            if (entity.getDistanceSq(self) > (double) this.field_220534_b) {
                continue;
            }

            if (this.func_220532_a(entity)) {
                brain.setMemory(MemoryModuleType.INTERACTION_TARGET, entity);
                brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(entity));

                break;
            }
        }
    }

}
