package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin implements ICustomNBTMixin {
    @Shadow
    private Vec3d enteredNetherPos;
    @Shadow
    private boolean seenCredits;
    @Final
    @Shadow
    private ServerRecipeBook recipeBook;
    @Shadow
    private BlockPos spawnPointPosition;
    @Shadow
    private boolean spawnForced;
    @Shadow
    private float spawnAngle;

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
}
