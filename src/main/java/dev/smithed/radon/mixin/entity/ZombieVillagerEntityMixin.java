package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(ZombieVillagerEntity.class)
public abstract class ZombieVillagerEntityMixin extends ZombieEntityMixin implements ICustomNBTMixin {
    @Shadow
    private int conversionTimer;
    @Shadow
    private UUID converter;
    @Shadow
    private NbtElement gossipData;
    @Shadow
    private NbtCompound offerData;
    @Shadow
    private int xp;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ZombieVillagerEntity entity = ((ZombieVillagerEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Gossips":
                    if (this.gossipData != null) {
                        nbt.put("Gossips", this.gossipData);
                    }
                    break;
                case "Offers":
                    if (this.offerData != null) {
                        nbt.put("Offers", this.offerData);
                    }
                    break;
                case "ConversionTime":
                    nbt.putInt("ConversionTime", entity.isConverting() ? this.conversionTimer : -1);
                case "ConversionPlayer":
                    if (this.converter != null) {
                        nbt.putUuid("ConversionPlayer", this.converter);
                    }
                    break;
                case "Xp":
                    if (this.converter != null) {
                        nbt.putInt("Xp", this.xp);
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
