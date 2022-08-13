package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends ProjectileEntityMixin implements ICustomNBTMixin {
    @Final
    @Shadow
    private static TrackedData<ItemStack> ITEM;
    @Final
    @Shadow
    private static TrackedData<Boolean> SHOT_AT_ANGLE;
    @Shadow
    private int life;
    @Shadow
    private int lifeTime;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FireworkRocketEntity entity = ((FireworkRocketEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Life":
                    nbt.putInt("Life", this.life);
                    break;
                case "LifeTime":
                    nbt.putInt("LifeTime", this.lifeTime);
                    break;
                case "FireworksItem":
                    ItemStack itemStack = (ItemStack)this.dataTracker.get(ITEM);
                    if (!itemStack.isEmpty()) {
                        nbt.put("FireworksItem", itemStack.writeNbt(new NbtCompound()));
                    }
                    break;
                case "ShotAtAngle":
                    nbt.putBoolean("ShotAtAngle", (Boolean)this.dataTracker.get(SHOT_AT_ANGLE));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
