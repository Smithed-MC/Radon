package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    private DyeColor textColor;
    @Shadow
    private boolean glowingText;
    @Final
    @Shadow
    private Text[] texts;
    @Final
    @Shadow
    private Text[] filteredTexts;
    @Final
    @Shadow
    private static String[] TEXT_KEYS;
    @Final
    @Shadow
    private static String[] FILTERED_TEXT_KEYS;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "GlowingText":
                    nbt.putBoolean("GlowingText", this.glowingText);
                    break;
                case "Color":
                    nbt.putString("Color", this.textColor.getName());
                    break;
                case "Text1":
                    writeSignLineToNbt(1, nbt);
                case "Text2":
                    writeSignLineToNbt(2, nbt);
                case "Text3":
                    writeSignLineToNbt(3, nbt);
                case "Text4":
                    writeSignLineToNbt(4, nbt);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private void writeSignLineToNbt(int line, NbtCompound nbt) {
        Text text = this.texts[line];
        String string = Text.Serializer.toJson(text);
        nbt.putString(TEXT_KEYS[line], string);
        Text text2 = this.filteredTexts[line];
        if (!text2.equals(text)) {
            nbt.putString(FILTERED_TEXT_KEYS[line], Text.Serializer.toJson(text2));
        }
    }
}
