package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow DyeColor textColor;
    @Shadow boolean glowingText;
    @Shadow boolean editable;
    @Shadow @Final Text[] texts;
    @Shadow @Final Text[] filteredTexts;
    @Shadow @Final static String[] TEXT_KEYS;
    @Shadow @Final static String[] FILTERED_TEXT_KEYS;
    @Shadow OrderedText[] textsBeingEdited;
    @Shadow abstract Text parseTextFromJson(String json);

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
                    writeSignLineToNbt(0, nbt);
                    break;
                case "Text2":
                    writeSignLineToNbt(1, nbt);
                    break;
                case "Text3":
                    writeSignLineToNbt(2, nbt);
                    break;
                case "Text4":
                    writeSignLineToNbt(3, nbt);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        this.editable = false;
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Color":
                    this.textColor = DyeColor.byName(nbt.getString("Color"), DyeColor.BLACK);
                    break;
                case "GlowingText":
                    this.glowingText = nbt.getBoolean("GlowingText");
                    break;
                case "Text1":
                    readSignLineFromNbt(0, nbt);
                    this.textsBeingEdited = null;
                    break;
                case "Text2":
                    readSignLineFromNbt(1, nbt);
                    this.textsBeingEdited = null;
                    break;
                case "Text3":
                    readSignLineFromNbt(2, nbt);
                    this.textsBeingEdited = null;
                    break;
                case "Text4":
                    readSignLineFromNbt(3, nbt);
                    this.textsBeingEdited = null;
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

    private void readSignLineFromNbt(int line, NbtCompound nbt) {
        String string = nbt.getString(TEXT_KEYS[line]);
        Text text = this.parseTextFromJson(string);
        this.texts[line] = text;
        String string2 = FILTERED_TEXT_KEYS[line];
        if (nbt.contains(string2, 8)) {
            this.filteredTexts[line] = this.parseTextFromJson(nbt.getString(string2));
        } else {
            this.filteredTexts[line] = text;
        }
    }
}
