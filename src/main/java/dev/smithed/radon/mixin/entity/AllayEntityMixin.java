package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AllayEntity.class)
public abstract class AllayEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Final
    @Shadow
    private SimpleInventory inventory;
    @Shadow
    private long duplicationCooldown;
    @Shadow
    abstract boolean canDuplicate();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AllayEntity entity = ((AllayEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Inventory":
                    nbt.put("Inventory", this.inventory.toNbtList());
                    break;
                case "DuplicationCooldown":
                    nbt.putLong("DuplicationCooldown", this.duplicationCooldown);
                    break;
                case "CanDuplicate":
                    nbt.putBoolean("CanDuplicate", this.canDuplicate());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
