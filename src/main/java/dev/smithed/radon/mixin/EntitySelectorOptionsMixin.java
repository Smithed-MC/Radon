package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.commands.RadonCommand;
import dev.smithed.radon.mixin_interface.IEntityMixin;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.command.EntitySelectorOptions;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(EntitySelectorOptions.class)
public class EntitySelectorOptionsMixin {
    @Shadow
    private static void putOption(String id, EntitySelectorOptions.SelectorHandler handler, Predicate<EntitySelectorReader> condition, Text description) {}

    @Inject(method = "register()V", at = @At("TAIL"))
    private static void register(CallbackInfo ci) {
        putOption("nbt", (reader) -> {
            boolean bl = reader.readNegationCharacter();
            NbtCompound nbtCompound = (new StringNbtReader(reader.getReader())).parseCompound();
            reader.setPredicate((entity) -> {
                NbtCompound nbtCompound2 = null;
                if(RadonCommand.getIsEnabled() && entity instanceof IEntityMixin mixin) {
                    nbtCompound2 = new NbtCompound();
                    String[] topLevelNbt = NBTUtils.getTopLevelPaths(nbtCompound.toString());
                    for(String nbt: topLevelNbt) {
                        if (entity instanceof ServerPlayerEntity player && nbt.equals("SelectedItem")) {
                            ItemStack itemStack = player.getInventory().getMainHandStack();
                            if (!itemStack.isEmpty()) {
                                nbtCompound2.put("SelectedItem", itemStack.writeNbt(new NbtCompound()));
                            }
                        } else {
                            nbtCompound2 = mixin.writeFilteredNbt(nbtCompound2, nbt);
                            if (nbtCompound2 == null)
                                break;
                        }
                    }
                }
                if(nbtCompound2 == null) {
                    nbtCompound2 = entity.writeNbt(new NbtCompound());
                    if (entity instanceof ServerPlayerEntity player) {
                        ItemStack itemStack = player.getInventory().getMainHandStack();
                        if (!itemStack.isEmpty()) {
                            nbtCompound2.put("SelectedItem", itemStack.writeNbt(new NbtCompound()));
                        }
                    }
                }
                return NbtHelper.matches(nbtCompound, nbtCompound2, true) != bl;
            });
        }, (reader) -> true, Text.translatable("argument.entity.options.nbt.description"));
    }

}
