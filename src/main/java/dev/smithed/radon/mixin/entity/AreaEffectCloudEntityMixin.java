package dev.smithed.radon.mixin.entity;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
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
    @Shadow @Final List<StatusEffectInstance> effects;
    @Shadow Potion potion;
    @Shadow int duration;
    @Shadow int waitTime;
    @Shadow int reapplicationDelay;
    @Shadow boolean customColor;
    @Shadow int durationOnUse;
    @Shadow float radiusOnUse;
    @Shadow float radiusGrowth;
    @Shadow UUID ownerUuid;

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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AreaEffectCloudEntity entity = ((AreaEffectCloudEntity)(Object)this);
        if(!nbt.contains(topLevelNbt))
            return false;
        switch (topLevelNbt) {
            case "Age":
                entity.age = nbt.getInt("Age");
                break;
            case "Duration":
                this.duration = nbt.getInt("Duration");
                break;
            case "WaitTime":
                this.waitTime = nbt.getInt("WaitTime");
                break;
            case "ReapplicationDelay":
                this.reapplicationDelay = nbt.getInt("ReapplicationDelay");
                break;
            case "DurationOnUse":
                this.durationOnUse = nbt.getInt("DurationOnUse");
                break;
            case "RadiusOnUse":
                this.radiusOnUse = nbt.getFloat("RadiusOnUse");
                break;
            case "RadiusPerTick":
                this.radiusGrowth = nbt.getFloat("RadiusPerTick");
                break;
            case "Owner":
                this.ownerUuid = nbt.getUuid("Owner");
                break;
            case "Particle":
                if (nbt.contains("Particle", 8)) {
                    try {
                        entity.setParticleType(ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("Particle"))));
                    } catch (CommandSyntaxException var5) {
                        LOGGER.warn("Couldn't load custom particle {}", nbt.getString("Particle"), var5);
                    }
                }
                break;
            case "Color":
                if (nbt.contains("Color", 99))
                    entity.setColor(nbt.getInt("Color"));
                break;
            case "Potion":
                if (nbt.contains("Potion", 8))
                    entity.setPotion(PotionUtil.getPotion(nbt));
                break;
            case "Effects":
                if (nbt.contains("Effects", 9)) {
                    NbtList nbtList = nbt.getList("Effects", 10);
                    this.effects.clear();
                    for (int i = 0; i < nbtList.size(); ++i) {
                        StatusEffectInstance statusEffectInstance = StatusEffectInstance.fromNbt(nbtList.getCompound(i));
                        if (statusEffectInstance != null) {
                            entity.addEffect(statusEffectInstance);
                        }
                    }
                }
                break;
            default:
                return false;
        }
        return true;
    }
}
