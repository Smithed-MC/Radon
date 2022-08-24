package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow int fuseTime = 30;
    @Shadow int explosionRadius = 3;
    @Shadow static TrackedData<Boolean> CHARGED;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        CreeperEntity entity = ((CreeperEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "powered":
                    if (this.dataTracker.get(CHARGED)) {
                        nbt.putBoolean("powered", true);
                    }
                    break;
                case "Fuse":
                    nbt.putShort("Fuse", (short)this.fuseTime);
                    break;
                case "ExplosionRadius":
                    nbt.putByte("ExplosionRadius", (byte)this.explosionRadius);
                    break;
                case "ignited":
                    nbt.putBoolean("ignited", entity.isIgnited());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        CreeperEntity entity = ((CreeperEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "powered":
                    this.dataTracker.set(CHARGED, nbt.getBoolean("powered"));
                    break;
                case "Fuse":
                    if (nbt.contains("Fuse", 99))
                        this.fuseTime = nbt.getShort("Fuse");
                    break;
                case "ExplosionRadius":
                    if (nbt.contains("ExplosionRadius", 99))
                        this.explosionRadius = nbt.getByte("ExplosionRadius");
                    break;
                case "ignited":
                    if (nbt.getBoolean("ignited"))
                        entity.ignite();
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
