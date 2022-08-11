package dev.smithed.radon.mixin;

import net.minecraft.command.DataCommandStorage;
import net.minecraft.command.StorageDataObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StorageDataObject.class)
public class StorageDataObjectMixin {
    @Final
    @Shadow
    private DataCommandStorage storage;
    @Final
    @Shadow
    private Identifier id;

    public NbtCompound getFilteredNbt(NbtPathArgumentType.NbtPath path) {
        return this.storage.get(this.id);
    }
}
