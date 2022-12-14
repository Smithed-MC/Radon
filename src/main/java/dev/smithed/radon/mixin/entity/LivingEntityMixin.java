package dev.smithed.radon.mixin.entity;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin implements ICustomNBTMixin {

    @Shadow @Final static Logger field_36332;
    @Shadow int lastAttackedTime;
    @Shadow Map<StatusEffect, StatusEffectInstance> activeStatusEffects;
    @Shadow Brain<?> brain;
    @Shadow abstract Brain<?> deserializeBrain(Dynamic<?> dynamic);
    @Shadow abstract void setPositionInBed(BlockPos pos);

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

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        LivingEntity entity = ((LivingEntity)(Object)this);
        if(!nbt.contains(topLevelNbt))
            return false;
        switch (topLevelNbt) {
            case "AbsorptionAmount":
                entity.setAbsorptionAmount(nbt.getFloat("AbsorptionAmount"));
                break;
            case "Attributes":
                if (nbt.contains("Attributes", 9) && this.world != null && !this.world.isClient)
                    entity.getAttributes().readNbt(nbt.getList("Attributes", 10));
                break;
            case "ActiveEffects":
                if (nbt.contains("ActiveEffects", 9)) {
                    NbtList nbtList = nbt.getList("ActiveEffects", 10);
                    for (int i = 0; i < nbtList.size(); ++i) {
                        NbtCompound nbtCompound = nbtList.getCompound(i);
                        StatusEffectInstance statusEffectInstance = StatusEffectInstance.fromNbt(nbtCompound);
                        if (statusEffectInstance != null) {
                            this.activeStatusEffects.put(statusEffectInstance.getEffectType(), statusEffectInstance);
                        }
                    }
                }
                break;
            case "Health":
                if (nbt.contains("Health", 99))
                    entity.setHealth(nbt.getFloat("Health"));
                break;
            case "HurtTime":
                entity.hurtTime = nbt.getShort("HurtTime");
                break;
            case "DeathTime":
                entity.deathTime = nbt.getShort("DeathTime");
                break;
            case "HurtByTimestamp":
                this.lastAttackedTime = nbt.getInt("HurtByTimestamp");
                break;
            case "Team":
                if (nbt.contains("Team", 8)) {
                    String string = nbt.getString("Team");
                    Team team = this.world.getScoreboard().getTeam(string);
                    boolean bl = team != null && this.world.getScoreboard().addPlayerToTeam(entity.getUuidAsString(), team);
                    if (!bl) {
                        field_36332.warn("Unable to add mob to team \"{}\" (that team probably doesn't exist)", string);
                    }
                }
                break;
            case "FallFlying":
                if (nbt.getBoolean("FallFlying"))
                    this.setFlag(7, true);
                break;
            case "Brain":
                if (nbt.contains("Brain", 10))
                    this.brain = this.deserializeBrain(new Dynamic(NbtOps.INSTANCE, nbt.get("Brain")));
                break;
            case "SleepingX":
            case "SleepingY":
            case "SleepingZ":
                Optional<BlockPos> currentPos = entity.getSleepingPosition();
                if(currentPos.isPresent()) {
                    int i = nbt.contains("SleepingX") ? nbt.getInt("SleepingX") : currentPos.get().getX();
                    int j = nbt.contains("SleepingY") ? nbt.getInt("SleepingY") : currentPos.get().getY();
                    int k = nbt.contains("SleepingZ") ? nbt.getInt("SleepingZ") : currentPos.get().getZ();
                    BlockPos blockPos = new BlockPos(i,j,k);
                    entity.setSleepingPosition(blockPos);
                    this.dataTracker.set(POSE, EntityPose.SLEEPING);
                    if (!this.firstUpdate) {
                        this.setPositionInBed(blockPos);
                    }
                }
                break;
            default:
                return false;
        }
        return true;
    }
}
