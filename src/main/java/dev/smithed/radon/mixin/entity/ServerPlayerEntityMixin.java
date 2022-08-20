package dev.smithed.radon.mixin.entity;

import com.mojang.serialization.DataResult;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin implements ICustomNBTMixin {

    @Shadow Vec3d enteredNetherPos;
    @Shadow boolean seenCredits;
    @Shadow @Final ServerRecipeBook recipeBook;
    @Shadow BlockPos spawnPointPosition;
    @Shadow boolean spawnForced;
    @Shadow float spawnAngle;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ServerPlayerEntity entity = ((ServerPlayerEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "playerGameType":
                    nbt.putInt("playerGameType", entity.interactionManager.getGameMode().getId());
                    break;
                case "previousPlayerGameType":
                    GameMode gameMode = entity.interactionManager.getPreviousGameMode();
                    if (gameMode != null)
                        nbt.putInt("previousPlayerGameType", gameMode.getId());
                    break;
                case "seenCredits":
                    nbt.putBoolean("seenCredits", this.seenCredits);
                    break;
                case "enteredNetherPosition":
                    if (this.enteredNetherPos != null) {
                        NbtCompound nbtCompound = new NbtCompound();
                        nbtCompound.putDouble("x", this.enteredNetherPos.x);
                        nbtCompound.putDouble("y", this.enteredNetherPos.y);
                        nbtCompound.putDouble("z", this.enteredNetherPos.z);
                        nbt.put("enteredNetherPosition", nbtCompound);
                    }
                    break;
                case "RootVehicle":
                    Entity vehicleEntity = entity.getRootVehicle();
                    Entity entity2 = entity.getVehicle();
                    if (entity2 != null && vehicleEntity != entity && entity.hasPlayerRider()) {
                        NbtCompound nbtCompound2 = new NbtCompound();
                        NbtCompound nbtCompound3 = new NbtCompound();
                        entity.saveNbt(nbtCompound3);
                        nbtCompound2.putUuid("Attach", entity2.getUuid());
                        nbtCompound2.put("Entity", nbtCompound3);
                        nbt.put("RootVehicle", nbtCompound2);
                    }
                    break;
                case "recipeBook":
                    nbt.put("recipeBook", this.recipeBook.toNbt());
                    break;
                case "Dimension":
                    nbt.putString("Dimension", entity.world.getRegistryKey().getValue().toString());
                    break;
                case "SpawnX":
                    if (this.spawnPointPosition != null)
                        nbt.putInt("SpawnX", this.spawnPointPosition.getX());
                    break;
                case "SpawnY":
                    if (this.spawnPointPosition != null)
                        nbt.putInt("SpawnY", this.spawnPointPosition.getY());
                case "SpawnZ":
                    if (this.spawnPointPosition != null)
                        nbt.putInt("SpawnZ", this.spawnPointPosition.getZ());
                    break;
                case "SpawnForced":
                    if (this.spawnPointPosition != null)
                        nbt.putBoolean("SpawnForced", this.spawnForced);
                    break;
                case "SpawnAngle":
                    if (this.spawnPointPosition != null)
                        nbt.putFloat("SpawnAngle", this.spawnAngle);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ServerPlayerEntity entity = ((ServerPlayerEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "enteredNetherPosition":
                    if (nbt.contains("enteredNetherPosition", 10)) {
                        NbtCompound nbtCompound = nbt.getCompound("enteredNetherPosition");
                        this.enteredNetherPos = new Vec3d(nbtCompound.getDouble("x"), nbtCompound.getDouble("y"), nbtCompound.getDouble("z"));
                    }
                    break;
                case "seenCredits":
                    this.seenCredits = nbt.getBoolean("seenCredits");
                    break;
                case "recipeBook":
                    if (nbt.contains("recipeBook", 10)) {
                        this.recipeBook.readNbt(nbt.getCompound("recipeBook"), entity.server.getRecipeManager());
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
