package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntityMixin implements ICustomNBTMixin {

    @Shadow @Final static TrackedData<Byte> LOYALTY;
    @Shadow ItemStack tridentStack;
    @Shadow boolean dealtDamage;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TridentEntity entity = ((TridentEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Trident" -> nbt.put("Trident", this.tridentStack.writeNbt(new NbtCompound()));
                case "DealtDamage" -> nbt.putBoolean("DealtDamage", this.dealtDamage);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TridentEntity entity = ((TridentEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Trident" -> {
                    if (nbt.contains("Trident", 10)) {
                        this.tridentStack = ItemStack.fromNbt(nbt.getCompound("Trident"));
                        this.dataTracker.set(LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.tridentStack));
                    }
                }
                case "DealtDamage" -> this.dealtDamage = nbt.getBoolean("DealtDamage");
                default -> {
                    return false;
                }
            }

        }
        return true;
    }
}
