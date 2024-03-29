package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Shadow @Final ServerBossBar bossBar;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        WitherEntity entity = ((WitherEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("Invul")) {
                nbt.putInt("Invul", entity.getInvulnerableTimer());
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        WitherEntity entity = ((WitherEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            if (topLevelNbt.equals("Invul")) {
                entity.setInvulTimer(nbt.getInt("Invul"));
            } else {
                return false;
            }
        }
        if (entity.hasCustomName())
            this.bossBar.setName(entity.getDisplayName());
        return true;
    }
}
