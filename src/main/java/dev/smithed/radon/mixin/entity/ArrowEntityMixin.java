package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Set;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends PersistentProjectileEntityMixin implements ICustomNBTMixin {

    @Shadow @Final Set<StatusEffectInstance> effects;
    @Shadow Potion potion;
    @Shadow boolean colorSet;
    @Shadow abstract void setColor(int color);
    @Shadow abstract void initColor();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ArrowEntity entity = ((ArrowEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Potion" -> {
                    if (this.potion != Potions.EMPTY)
                        nbt.putString("Potion", Registries.POTION.getId(this.potion).toString());
                }
                case "Color" -> {
                    if (this.colorSet)
                        nbt.putInt("Color", entity.getColor());
                }
                case "CustomPotionEffects" -> {
                    if (!this.effects.isEmpty()) {
                        NbtList nbtList = new NbtList();

                        for (StatusEffectInstance statusEffectInstance : this.effects) {
                            nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
                        }
                        nbt.put("CustomPotionEffects", nbtList);
                    }
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ArrowEntity entity = ((ArrowEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "Potion" -> {
                    if (nbt.contains("Potion", 8))
                        this.potion = PotionUtil.getPotion(nbt);
                }
                case "CustomPotionEffects" -> {
                    for (StatusEffectInstance statusEffectInstance : PotionUtil.getCustomPotionEffects(nbt))
                        entity.addEffect(statusEffectInstance);
                }
                case "Color" -> {
                    if (nbt.contains("Color", 99))
                        this.setColor(nbt.getInt("Color"));
                }
                default -> {
                    if (!this.colorSet)
                        this.initColor();
                    return false;
                }
            }
        }
        return true;
    }
}
