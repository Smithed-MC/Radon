package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Mixin(AreaEffectCloudEntity.class)
public abstract class AreaEffectCloudEntityMixin extends EntityMixin implements ICustomNBTMixin {
    @Final
    @Shadow
    private List<StatusEffectInstance> effects;
    @Shadow
    private Potion potion;
    @Shadow
    private int duration;
    @Shadow
    private int waitTime;
    @Shadow
    private int reapplicationDelay;
    @Shadow
    private boolean customColor;
    @Shadow
    private int durationOnUse;
    @Shadow
    private float radiusOnUse;
    @Shadow
    private float radiusGrowth;
    @Shadow
    private UUID ownerUuid;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AreaEffectCloudEntity entity = ((AreaEffectCloudEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Age":
                    nbt.putInt("Age", entity.age);
                    break;
                case "Duration":
                    nbt.putInt("Duration", this.duration);
                    break;
                case "WaitTime":
                    nbt.putInt("WaitTime", this.waitTime);
                    break;
                case "ReapplicationDelay":
                    nbt.putInt("ReapplicationDelay", this.reapplicationDelay);
                    break;
                case "DurationOnUse":
                    nbt.putInt("DurationOnUse", this.durationOnUse);
                    break;
                case "RadiusOnUse":
                    nbt.putFloat("RadiusOnUse", this.radiusOnUse);
                    break;
                case "RadiusPerTick":
                    nbt.putFloat("RadiusPerTick", this.radiusGrowth);
                    break;
                case "Radius":
                    nbt.putFloat("Radius", entity.getRadius());
                    break;
                case "Particle":
                    nbt.putString("Particle", entity.getParticleType().asString());
                    break;
                case "Owner":
                    if (this.ownerUuid != null)
                        nbt.putUuid("Owner", this.ownerUuid);
                    break;
                case "Color":
                    if (this.customColor)
                        nbt.putInt("Color", entity.getColor());
                    break;
                case "Potion":
                    if (this.potion != Potions.EMPTY) {
                        nbt.putString("Potion", Registry.POTION.getId(this.potion).toString());
                    }
                    break;
                case "Effects":
                    if (!this.effects.isEmpty()) {
                        NbtList nbtList = new NbtList();
                        Iterator var3 = this.effects.iterator();

                        while (var3.hasNext()) {
                            StatusEffectInstance statusEffectInstance = (StatusEffectInstance) var3.next();
                            nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
                        }
                        nbt.put("Effects", nbtList);
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
