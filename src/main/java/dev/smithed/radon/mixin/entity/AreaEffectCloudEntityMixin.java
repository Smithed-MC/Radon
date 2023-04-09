package dev.smithed.radon.mixin.entity;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
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
                case "Age" -> nbt.putInt("Age", entity.age);
                case "Duration" -> nbt.putInt("Duration", this.duration);
                case "WaitTime" -> nbt.putInt("WaitTime", this.waitTime);
                case "ReapplicationDelay" -> nbt.putInt("ReapplicationDelay", this.reapplicationDelay);
                case "DurationOnUse" -> nbt.putInt("DurationOnUse", this.durationOnUse);
                case "RadiusOnUse" -> nbt.putFloat("RadiusOnUse", this.radiusOnUse);
                case "RadiusPerTick" -> nbt.putFloat("RadiusPerTick", this.radiusGrowth);
                case "Radius" -> nbt.putFloat("Radius", entity.getRadius());
                case "Particle" -> nbt.putString("Particle", entity.getParticleType().asString());
                case "Owner" -> {
                    if (this.ownerUuid != null)
                        nbt.putUuid("Owner", this.ownerUuid);
                }
                case "Color" -> {
                    if (this.customColor)
                        nbt.putInt("Color", entity.getColor());
                }
                case "Potion" -> {
                    if (this.potion != Potions.EMPTY) {
                        nbt.putString("Potion", Registries.POTION.getId(this.potion).toString());
                    }
                }
                case "Effects" -> {
                    if (!this.effects.isEmpty()) {
                        NbtList nbtList = new NbtList();

                        for (StatusEffectInstance statusEffectInstance : this.effects) {
                            nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
                        }
                        nbt.put("Effects", nbtList);
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
        AreaEffectCloudEntity entity = ((AreaEffectCloudEntity)(Object)this);
        if(!nbt.contains(topLevelNbt))
            return false;
        switch (topLevelNbt) {
            case "Age" -> entity.age = nbt.getInt("Age");
            case "Duration" -> this.duration = nbt.getInt("Duration");
            case "WaitTime" -> this.waitTime = nbt.getInt("WaitTime");
            case "ReapplicationDelay" -> this.reapplicationDelay = nbt.getInt("ReapplicationDelay");
            case "DurationOnUse" -> this.durationOnUse = nbt.getInt("DurationOnUse");
            case "RadiusOnUse" -> this.radiusOnUse = nbt.getFloat("RadiusOnUse");
            case "RadiusPerTick" -> this.radiusGrowth = nbt.getFloat("RadiusPerTick");
            case "Owner" -> this.ownerUuid = nbt.getUuid("Owner");
            case "Particle" -> {
                if (nbt.contains("Particle", 8)) {
                    try {
                        entity.setParticleType(ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("Particle")), Registries.PARTICLE_TYPE.getReadOnlyWrapper()));
                    } catch (CommandSyntaxException var5) {
                        Radon.LOGGER.warn("Couldn't load custom particle {}", nbt.getString("Particle"), var5);
                    }
                }
            }
            case "Color" -> {
                if (nbt.contains("Color", 99))
                    entity.setColor(nbt.getInt("Color"));
            }
            case "Potion" -> {
                if (nbt.contains("Potion", 8))
                    entity.setPotion(PotionUtil.getPotion(nbt));
            }
            case "Effects" -> {
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
            }
            default -> {
                return false;
            }
        }
        return true;
    }
}
