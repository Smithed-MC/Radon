package dev.smithed.radon.mixin.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntityMixin {

    @Shadow BlockState inBlockState;
    @Shadow int shake;
    @Shadow int life;
    @Shadow boolean inGround;
    @Shadow double damage;
    @Shadow SoundEvent sound;
    @Shadow abstract SoundEvent getHitSound();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PersistentProjectileEntity entity = ((PersistentProjectileEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "life":
                    nbt.putShort("life", (short)this.life);
                    break;
                case "inBlockState":
                    if (this.inBlockState != null)
                        nbt.put("inBlockState", NbtHelper.fromBlockState(this.inBlockState));
                    break;
                case "shake":
                    nbt.putByte("shake", (byte)this.shake);
                    break;
                case "inGround":
                    nbt.putBoolean("inGround", this.inGround);
                    break;
                case "pickup":
                    nbt.putByte("pickup", (byte)entity.pickupType.ordinal());
                    break;
                case "damage":
                    nbt.putDouble("damage", this.damage);
                    break;
                case "crit":
                    nbt.putBoolean("crit", entity.isCritical());
                    break;
                case "PierceLevel":
                    nbt.putByte("PierceLevel", entity.getPierceLevel());
                    break;
                case "SoundEvent":
                    nbt.putString("SoundEvent", Registries.SOUND_EVENT.getId(this.sound).toString());
                    break;
                case "ShotFromCrossbow":
                    nbt.putBoolean("ShotFromCrossbow", entity.isShotFromCrossbow());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        PersistentProjectileEntity entity = ((PersistentProjectileEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "life":
                    this.life = nbt.getShort("life");
                    break;
                case "inBlockState":
                    if (nbt.contains("inBlockState", 10))
                        this.inBlockState = NbtHelper.toBlockState(this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("inBlockState"));
                    break;
                case "shake":
                    this.shake = nbt.getByte("shake") & 255;
                    break;
                case "inGround":
                    this.inGround = nbt.getBoolean("inGround");
                    break;
                case "damage":
                    if (nbt.contains("damage", 99))
                        this.damage = nbt.getDouble("damage");
                    break;
                case "pickup":
                    entity.pickupType = PersistentProjectileEntity.PickupPermission.fromOrdinal(nbt.getByte("pickup"));
                    break;
                case "crit":
                    entity.setCritical(nbt.getBoolean("crit"));
                    break;
                case "PierceLevel":
                    entity.setPierceLevel(nbt.getByte("PierceLevel"));
                    break;
                case "SoundEvent":
                    if (nbt.contains("SoundEvent", 8))
                        this.sound = Registries.SOUND_EVENT.getOrEmpty(new Identifier(nbt.getString("SoundEvent"))).orElse(this.getHitSound());
                    break;
                case "ShotFromCrossbow":
                    entity.setShotFromCrossbow(nbt.getBoolean("ShotFromCrossbow"));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
