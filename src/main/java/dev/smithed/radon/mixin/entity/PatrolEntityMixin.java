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

    @Shadow BlockPos patrolTarget;
    @Shadow boolean patrolLeader;
    @Shadow boolean patrolling;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PatrolEntity entity = ((PatrolEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "PatrolTarget" -> {
                    if (this.patrolTarget != null)
                        nbt.put("PatrolTarget", NbtHelper.fromBlockPos(this.patrolTarget));
                }
                case "PatrolLeader" -> nbt.putBoolean("PatrolLeader", this.patrolLeader);
                case "Patrolling" -> nbt.putBoolean("Patrolling", this.patrolling);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PatrolEntity entity = ((PatrolEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "PatrolTarget" -> this.patrolTarget = NbtHelper.toBlockPos(nbt.getCompound("PatrolTarget"));
                case "PatrolLeader" -> this.patrolLeader = nbt.getBoolean("PatrolLeader");
                case "Patrolling" -> this.patrolling = nbt.getBoolean("Patrolling");
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
