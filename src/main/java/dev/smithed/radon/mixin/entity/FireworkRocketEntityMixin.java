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

    @Shadow @Final static TrackedData<ItemStack> ITEM;
    @Shadow @Final static TrackedData<Boolean> SHOT_AT_ANGLE;
    @Shadow int life;
    @Shadow int lifeTime;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FireworkRocketEntity entity = ((FireworkRocketEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Life" -> nbt.putInt("Life", this.life);
                case "LifeTime" -> nbt.putInt("LifeTime", this.lifeTime);
                case "FireworksItem" -> {
                    ItemStack itemStack = this.dataTracker.get(ITEM);
                    if (!itemStack.isEmpty()) {
                        nbt.put("FireworksItem", itemStack.writeNbt(new NbtCompound()));
                    }
                }
                case "ShotAtAngle" -> nbt.putBoolean("ShotAtAngle", this.dataTracker.get(SHOT_AT_ANGLE));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        FireworkRocketEntity entity = ((FireworkRocketEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Life" -> this.life = nbt.getInt("Life");
                case "LifeTime" -> this.lifeTime = nbt.getInt("LifeTime");
                case "FireworksItem" -> {
                    ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("FireworksItem"));
                    if (!itemStack.isEmpty())
                        this.dataTracker.set(ITEM, itemStack);
                }
                case "ShotAtAngle" -> this.dataTracker.set(SHOT_AT_ANGLE, nbt.getBoolean("ShotAtAngle"));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
