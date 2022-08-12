package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntityMixin implements ICustomNBTMixin {
    @Shadow
    private int loveTicks;
    @Shadow
    private UUID lovingPlayer;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AnimalEntity entity = ((AnimalEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "InLove":
                    nbt.putInt("InLove", this.loveTicks);
                    break;
                case "LoveCause":
                    if (this.lovingPlayer != null)
                        nbt.putUuid("LoveCause", this.lovingPlayer);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
