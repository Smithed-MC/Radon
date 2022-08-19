package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin extends MobEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        IronGolemEntity entity = ((IronGolemEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "AngerTime":
                case "AngryAt":
                    entity.writeAngerToNbt(nbt);
                    break;
                case "PlayerCreated":
                    nbt.putBoolean("PlayerCreated", entity.isPlayerCreated());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        IronGolemEntity entity = ((IronGolemEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "PlayerCreated":
                    entity.setPlayerCreated(nbt.getBoolean("PlayerCreated"));
                    break;
                case "AngerTime":
                case "AngryAt":
                    entity.readAngerFromNbt(this.world, nbt);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
