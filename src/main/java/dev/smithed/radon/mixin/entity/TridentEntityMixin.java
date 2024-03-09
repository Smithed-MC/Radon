package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntityMixin implements ICustomNBTMixin {

    @Shadow @Final static TrackedData<Byte> LOYALTY;
    @Shadow boolean dealtDamage;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("DealtDamage")) {
                nbt.putBoolean("DealtDamage", this.dealtDamage);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TridentEntity entity = ((TridentEntity)(Object)this);
        this.dataTracker.set(LOYALTY, (byte)EnchantmentHelper.getLoyalty(entity.getItemStack()));
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("DealtDamage")) {
                this.dealtDamage = nbt.getBoolean("DealtDamage");
                return true;
            }
        }
        return false;
    }
}
