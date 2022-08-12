package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntityMixin implements ICustomNBTMixin {
    @Shadow
    private ItemStack tridentStack;
    @Shadow
    private boolean dealtDamage;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TridentEntity entity = ((TridentEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Trident":
                    nbt.put("Trident", this.tridentStack.writeNbt(new NbtCompound()));
                    break;
                case "DealtDamage":
                    nbt.putBoolean("DealtDamage", this.dealtDamage);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
