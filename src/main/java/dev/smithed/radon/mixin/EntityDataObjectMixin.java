package dev.smithed.radon.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import dev.smithed.radon.mixin_interface.IEntityMixin;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.command.EntityDataObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(EntityDataObject.class)
public class EntityDataObjectMixin implements IDataCommandObjectMixin {

    @Final @Shadow Entity entity;
    @Final @Shadow static SimpleCommandExceptionType INVALID_ENTITY_EXCEPTION;

    @Override
    public NbtCompound getNbtFiltered(String path) {
        final NbtCompound nbtCompound = new NbtCompound();
        if (Radon.CONFIG.nbtOptimizations && this.entity instanceof IEntityMixin mixin) {
            if(path.startsWith("{")) {
                for(String str: NBTUtils.getTopLevelPaths(path))
                    getNbt(mixin,nbtCompound,str);
            } else {
                getNbt(mixin,nbtCompound,path);
            }
        }
        if (nbtCompound.getSize() == 0) {
            Radon.logDebug("Failed to get NBT data at " + path + " for " + this.entity.getClass());
            entity.writeNbt(nbtCompound);
        }
        Radon.logDebug("Retrieved NBT for " + this.entity.getClass() + " -> " + nbtCompound);
        return nbtCompound;
    }

    private void getNbt(IEntityMixin mixin, NbtCompound nbtCompound, String path) {
        if (this.entity instanceof PlayerEntity player && path.startsWith("SelectedItem")) {
            ItemStack itemStack = player.getInventory().getMainHandStack();
            if (!itemStack.isEmpty()) {
                nbtCompound.put("SelectedItem", itemStack.writeNbt(new NbtCompound()));
            }
        } else {
            mixin.writeNbtFiltered(nbtCompound, path);
        }
    }

    @Override
    public boolean setNbtFiltered(NbtCompound nbt, String path) throws CommandSyntaxException {
        if (this.entity instanceof PlayerEntity) {
            throw INVALID_ENTITY_EXCEPTION.create();
        } else {
            UUID uUID = this.entity.getUuid();
            if(this.entity instanceof IEntityMixin mixin && mixin.readNbtFiltered(nbt, path)) {
                if(Radon.CONFIG.debug)
                    Radon.logDebug("Set NBT for " + this.entity.getClass() + " -> " + nbt);
                this.entity.setUuid(uUID);
                return true;
            } else {
                Radon.logDebug("Failed to save NBT data " + nbt + " at " + path + " for " + this.entity.getClass());
                return false;
            }
        }
    }
}