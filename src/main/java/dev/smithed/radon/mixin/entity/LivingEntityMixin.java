package dev.smithed.radon.mixin.entity;

import com.mojang.serialization.DataResult;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin implements ICustomNBTMixin {
    @Shadow
    private static Logger field_36332;
    @Shadow
    private int lastAttackedTime;
    @Shadow
    private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;
    @Shadow
    protected Brain<?> brain;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        LivingEntity entity = ((LivingEntity)(Object)this);

        switch (topLevelNbt) {
            case "Health":
                nbt.putFloat("Health", entity.getHealth());
                break;
            case "HurtTime":
                nbt.putShort("HurtTime", (short) entity.hurtTime);
                break;
            case "HurtByTimestamp":
                nbt.putInt("HurtByTimestamp", this.lastAttackedTime);
                break;
            case "DeathTime":
                nbt.putShort("DeathTime", (short) entity.deathTime);
                break;
            case "AbsorptionAmount":
                nbt.putFloat("AbsorptionAmount", entity.getAbsorptionAmount());
                break;
            case "Attributes":
                nbt.put("Attributes", entity.getAttributes().toNbt());
                break;
            case "ActiveEffects":
                if (!this.activeStatusEffects.isEmpty()) {
                    NbtList nbtList = new NbtList();
                    Iterator var3 = this.activeStatusEffects.values().iterator();

                    while (var3.hasNext()) {
                        StatusEffectInstance statusEffectInstance = (StatusEffectInstance) var3.next();
                        nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
                    }

                    nbt.put("ActiveEffects", nbtList);
                }
                break;
            case "FallFlying":
                nbt.putBoolean("FallFlying", entity.isFallFlying());
                break;
            case "SleepingX":
            case "SleepingY":
            case "SleepingZ":
                entity.getSleepingPosition().ifPresent((pos) -> {
                    nbt.putInt("SleepingX", pos.getX());
                    nbt.putInt("SleepingY", pos.getY());
                    nbt.putInt("SleepingZ", pos.getZ());
                });
                break;
            case "brain":
                DataResult<NbtElement> dataResult = this.brain.encode(NbtOps.INSTANCE);
                Logger var10001 = field_36332;
                java.util.Objects.requireNonNull(var10001);
                dataResult.resultOrPartial(var10001::error).ifPresent((brain) -> {
                    nbt.put("Brain", brain);
                });
                break;
            default:
                return false;
        }
        return true;
    }
}
