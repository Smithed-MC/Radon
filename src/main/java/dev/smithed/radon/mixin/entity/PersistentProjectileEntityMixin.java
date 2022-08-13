package dev.smithed.radon.mixin.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntityMixin {
    @Shadow
    private BlockState inBlockState;
    @Shadow
    public int shake;
    @Shadow
    private int life;
    @Shadow
    protected boolean inGround;
    @Shadow
    private double damage;
    @Shadow
    private SoundEvent sound;

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
                    nbt.putString("SoundEvent", Registry.SOUND_EVENT.getId(this.sound).toString());
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
}
