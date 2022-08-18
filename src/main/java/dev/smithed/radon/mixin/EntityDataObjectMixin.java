package dev.smithed.radon.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import dev.smithed.radon.mixin_interface.IEntityMixin;
import net.minecraft.command.EntityDataObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(EntityDataObject.class)
public class EntityDataObjectMixin implements IDataCommandObjectMixin {
    @Final @Shadow private Entity entity;
    @Final @Shadow private static SimpleCommandExceptionType INVALID_ENTITY_EXCEPTION;

    @Override
    public NbtCompound getNbtFiltered(NbtPathArgumentType.NbtPath path) {
        NbtCompound nbtCompound = null;
        if (Radon.CONFIG.nbtOptimizations && this.entity instanceof IEntityMixin mixin) {
            if (this.entity instanceof PlayerEntity player && path.toString().startsWith("SelectedItem")) {
                nbtCompound = new NbtCompound();
                ItemStack itemStack = player.getInventory().getMainHandStack();
                if (!itemStack.isEmpty()) {
                    nbtCompound.put("SelectedItem", itemStack.writeNbt(new NbtCompound()));
                }
            } else {
                nbtCompound = mixin.writeNbtFiltered(new NbtCompound(), path.toString());
            }
        }
        if (nbtCompound == null)
            nbtCompound = entity.writeNbt(new NbtCompound());
        return nbtCompound;
    }

    @Override
    public boolean setNbtFiltered(NbtCompound nbt, NbtPathArgumentType.NbtPath path) throws CommandSyntaxException {
        if (this.entity instanceof PlayerEntity) {
            throw INVALID_ENTITY_EXCEPTION.create();
        } else {
            UUID uUID = this.entity.getUuid();
            if(this.entity instanceof IEntityMixin mixin && mixin.readNbtFiltered(nbt, path.toString())) {
                this.entity.setUuid(uUID);
                return true;
            } else {
                return false;
            }
        }
    }
}