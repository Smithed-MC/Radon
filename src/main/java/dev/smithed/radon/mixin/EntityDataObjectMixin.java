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
        NbtCompound nbtCompound = new NbtCompound();
        if (Radon.CONFIG.getNbtOptimizationsEnabled() && entity instanceof PlayerEntity && path.toString().startsWith("SelectedItem")) {
            ItemStack itemStack = ((PlayerEntity)entity).getInventory().getMainHandStack();
            if (!itemStack.isEmpty()) {
                nbtCompound.put("SelectedItem", itemStack.writeNbt(new NbtCompound()));
            }
        } else if(Radon.CONFIG.getNbtOptimizationsEnabled() && entity instanceof IEntityMixin) {
            NbtCompound check = ((IEntityMixin)entity).writeFilteredNbt(nbtCompound, path.toString());
            if(check == null)
                entity.writeNbt(nbtCompound);
        } else {
            entity.writeNbt(nbtCompound);
        }

        return nbtCompound;
    }
}
