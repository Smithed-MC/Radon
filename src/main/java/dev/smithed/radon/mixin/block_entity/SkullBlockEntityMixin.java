package dev.smithed.radon.mixin.block_entity;

import com.mojang.authlib.GameProfile;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.StringHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(SkullBlockEntity.class)
public abstract class SkullBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow GameProfile owner;
    @Shadow abstract void setOwner(@Nullable GameProfile owner);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("SkullOwner")) {
                if (this.owner != null) {
                    NbtCompound nbtCompound = new NbtCompound();
                    NbtHelper.writeGameProfile(nbtCompound, this.owner);
                    nbt.put("SkullOwner", nbtCompound);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "SkullOwner" -> {
                    if (nbt.contains("SkullOwner", 10))
                        this.setOwner(NbtHelper.toGameProfile(nbt.getCompound("SkullOwner")));
                }
                case "ExtraType" -> {
                    if (nbt.contains("ExtraType", 8)) {
                        String string = nbt.getString("ExtraType");
                        if (!StringHelper.isEmpty(string)) {
                            this.setOwner(new GameProfile(null, string));
                        }
                    }
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
