package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PatrolEntity.class)
public abstract class PatrolEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow
    private BlockPos patrolTarget;
    @Shadow
    private boolean patrolLeader;
    @Shadow
    private boolean patrolling;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PatrolEntity entity = ((PatrolEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "PatrolTarget":
                    if (this.patrolTarget != null)
                        nbt.put("PatrolTarget", NbtHelper.fromBlockPos(this.patrolTarget));
                    break;
                case "PatrolLeader":
                    nbt.putBoolean("PatrolLeader", this.patrolLeader);
                    break;
                case "Patrolling":
                    nbt.putBoolean("Patrolling", this.patrolling);
                    break;
                default:
                    return false;
            }
        }
        return true;

    }
}
