package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Set;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends PersistentProjectileEntityMixin implements ICustomNBTMixin {
    @Shadow
    private Potion potion;
    @Final
    @Shadow
    private Set<StatusEffectInstance> effects;
    @Shadow
    private boolean colorSet;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ArrowEntity entity = ((ArrowEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Potion":
                    if (this.potion != Potions.EMPTY)
                        nbt.putString("Potion", Registry.POTION.getId(this.potion).toString());
                    break;
                case "Color":
                    if (this.colorSet)
                        nbt.putInt("Color", entity.getColor());
                    break;
                case "CustomPotionEffects":
                    if (!this.effects.isEmpty()) {
                        NbtList nbtList = new NbtList();
                        Iterator var3 = this.effects.iterator();

                        while(var3.hasNext()) {
                            StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var3.next();
                            nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
                        }
                        nbt.put("CustomPotionEffects", nbtList);
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
