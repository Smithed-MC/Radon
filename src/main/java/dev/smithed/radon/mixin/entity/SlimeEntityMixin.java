package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Shadow boolean onGroundLastTick;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SlimeEntity entity = ((SlimeEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Size" -> nbt.putInt("Size", entity.getSize() - 1);
                case "wasOnGround" -> nbt.putBoolean("wasOnGround", this.onGroundLastTick);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        SlimeEntity entity = ((SlimeEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "Size" -> entity.setSize(nbt.getInt("Size") + 1, false);
                case "wasOnGround" -> this.onGroundLastTick = nbt.getBoolean("wasOnGround");
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
