package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractPiglinEntity.class)
public abstract class AbstractPiglinEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Shadow int timeInOverworld;
    @Shadow abstract boolean isImmuneToZombification();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AbstractPiglinEntity entity = ((AbstractPiglinEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "IsImmuneToZombification" -> {
                    if (this.isImmuneToZombification())
                        nbt.putBoolean("IsImmuneToZombification", true);
                }
                case "TimeInOverworld" -> nbt.putInt("TimeInOverworld", this.timeInOverworld);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AbstractPiglinEntity entity = ((AbstractPiglinEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "IsImmuneToZombification" ->
                        entity.setImmuneToZombification(nbt.getBoolean("IsImmuneToZombification"));
                case "TimeInOverworld" -> this.timeInOverworld = nbt.getInt("TimeInOverworld");
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
