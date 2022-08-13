package dev.smithed.radon.mixin.block_entity;

import com.mojang.authlib.GameProfile;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SkullBlockEntity.class)
public abstract class SkullBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    private GameProfile owner;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "SkullOwner":
                    if (this.owner != null) {
                        NbtCompound nbtCompound = new NbtCompound();
                        NbtHelper.writeGameProfile(nbtCompound, this.owner);
                        nbt.put("SkullOwner", nbtCompound);
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
