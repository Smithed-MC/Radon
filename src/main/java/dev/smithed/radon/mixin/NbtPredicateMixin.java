package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IEntityMixin;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NbtPredicate.class)
public class NbtPredicateMixin {

    @Shadow @Final private NbtCompound nbt;

    /**
     * @author ImCoolYeah105
     * @reason overwrite get nbt function to add filter support
     */
    @Overwrite
    public boolean test(Entity entity) {
        NbtPredicate predicate = ((NbtPredicate)(Object)this);
        if(predicate == NbtPredicate.ANY) {
            return true;
        } else {
            NbtCompound nbt = null;
            if(Radon.CONFIG.nbtOptimizations && entity instanceof IEntityMixin mixin) {
                nbt = new NbtCompound();
                String[] topLevelNbt = NBTUtils.getTopLevelPaths(this.nbt);
                for(String topNbt: topLevelNbt) {
                    if (entity instanceof ServerPlayerEntity player && topNbt.equals("SelectedItem")) {
                        ItemStack itemStack = player.getInventory().getMainHandStack();
                        if (!itemStack.isEmpty()) {
                            nbt.put("SelectedItem", itemStack.writeNbt(new NbtCompound()));
                        }
                    } else {
                        nbt = mixin.writeNbtFiltered(nbt, topNbt);
                        if (nbt == null)
                            break;
                    }
                }
            }
            if(nbt == null)
                nbt = NbtPredicate.entityToNbt(entity);
            if(Radon.CONFIG.debug)
                Radon.logDebug(nbt);
            return predicate.test(nbt);
        }
    }
}
