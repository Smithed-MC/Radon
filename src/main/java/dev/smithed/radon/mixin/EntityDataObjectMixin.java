package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.commands.RadonCommand;
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

@Mixin(EntityDataObject.class)
public class EntityDataObjectMixin implements DataCommandObjectMixin {
    @Final
    @Shadow
    private Entity entity;

    public NbtCompound getFilteredNbt(NbtPathArgumentType.NbtPath path) {
        return entityToNbtFiltered(this.entity, path);
    }

    private static NbtCompound entityToNbtFiltered(Entity entity, NbtPathArgumentType.NbtPath path) {
        NbtCompound nbtCompound = null;
        if (Radon.CONFIG.nbtOptimizations && entity instanceof IEntityMixin mixin) {
            if(entity instanceof PlayerEntity && path.toString().startsWith("SelectedItem")) {
                nbtCompound = new NbtCompound();
                ItemStack itemStack = ((PlayerEntity) entity).getInventory().getMainHandStack();
                if (!itemStack.isEmpty()) {
                    nbtCompound.put("SelectedItem", itemStack.writeNbt(new NbtCompound()));
                }
            } else {
                nbtCompound = mixin.writeFilteredNbt(new NbtCompound(), path.toString());
            }
        }
        if(nbtCompound == null)
            nbtCompound = entity.writeNbt(new NbtCompound());
        return nbtCompound;
    }
}
