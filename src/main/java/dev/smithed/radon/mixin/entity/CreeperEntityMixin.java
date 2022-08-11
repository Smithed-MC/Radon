package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends MobEntityMixin implements ICustomNBTMixin {
    @Shadow
    private int fuseTime = 30;
    @Shadow
    private int explosionRadius = 3;
    @Shadow
    private static TrackedData<Boolean> CHARGED;

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
                case "ignited":
                    nbt.putBoolean("ignited", entity.isIgnited());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
