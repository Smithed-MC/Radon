package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TraderLlamaEntity.class)
public abstract class TraderLlamaEntityMixin extends LlamaEntityMixin implements ICustomNBTMixin {

    @Shadow int despawnDelay;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TraderLlamaEntity entity = ((TraderLlamaEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "DespawnDelay":
                    nbt.putInt("DespawnDelay", this.despawnDelay);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        TraderLlamaEntity entity = ((TraderLlamaEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "DespawnDelay":
                    if (nbt.contains("DespawnDelay", 99))
                        this.despawnDelay = nbt.getInt("DespawnDelay");
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
