package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends AbstractPiglinEntityMixin implements ICustomNBTMixin {

    @Shadow @Final SimpleInventory inventory;
    @Shadow boolean cannotHunt;
    @Shadow abstract void setCannotHunt(boolean cannotHunt);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PiglinEntity entity = ((PiglinEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "IsBaby" -> {
                    if (entity.isBaby())
                        nbt.putBoolean("IsBaby", true);
                }
                case "CannotHunt" -> {
                    if (this.cannotHunt)
                        nbt.putBoolean("CannotHunt", true);
                }
                case "Inventory" -> nbt.put("Inventory", this.inventory.toNbtList());
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PiglinEntity entity = ((PiglinEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "IsBaby" -> entity.setBaby(nbt.getBoolean("IsBaby"));
                case "CannotHunt" -> this.setCannotHunt(nbt.getBoolean("CannotHunt"));
                case "Inventory" -> this.inventory.readNbtList(nbt.getList("Inventory", 10));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
