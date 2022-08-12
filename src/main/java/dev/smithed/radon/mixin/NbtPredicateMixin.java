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

    @Final
    @Shadow
    private NbtCompound nbt;

    /**
     * @author ImCoolYeah105
     * @reasaon overwrite get nbt function to add filter support
     */
    @Overwrite
    public boolean test(Entity entity) {
        if(nbt != null)
            Radon.LOGGER.info(nbt.toString());

        NbtPredicate predicate = ((NbtPredicate)(Object)this);
        if(predicate == NbtPredicate.ANY) {
            return true;
        } else {
            NbtCompound nbt = null;
            if(Radon.CONFIG.getNbtOptimizationsEnabled() && entity instanceof IEntityMixin mixin) {
                nbt = new NbtCompound();
                String[] topLevelNbt = NBTUtils.getTopLevelPaths(this.nbt.toString());
                for(String topNbt: topLevelNbt) {
                    if (entity instanceof ServerPlayerEntity player && topNbt.equals("SelectedItem")) {
                        ItemStack itemStack = player.getInventory().getMainHandStack();
                        if (!itemStack.isEmpty()) {
                            nbt.put("SelectedItem", itemStack.writeNbt(new NbtCompound()));
                        }
                    } else {
                        nbt = mixin.writeFilteredNbt(nbt, topNbt);
                        if (nbt == null)
                            break;
                    }
                }
            }
            if(nbt == null)
                nbt = NbtPredicate.entityToNbt(entity);
            Radon.LOGGER.info(nbt.toString());
            return predicate.test(nbt);
        }
    }
}
