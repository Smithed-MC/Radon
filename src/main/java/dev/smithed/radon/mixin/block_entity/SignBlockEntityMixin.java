package dev.smithed.radon.mixin.block_entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow public SignText frontText;
    @Shadow public SignText backText;
    @Shadow public boolean waxed;
    @Shadow @Final public static Logger LOGGER;

    @Shadow public abstract SignText parseLines(SignText signText);

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "front_text" -> {
                    DataResult<NbtElement> var10000 = SignText.CODEC.encodeStart(NbtOps.INSTANCE, this.frontText);
                    Logger var10001 = LOGGER;
                    Objects.requireNonNull(var10001);
                    var10000.resultOrPartial(var10001::error).ifPresent((frontText) -> {
                        nbt.put("front_text", frontText);
                    });
                }
                case "back_text" -> {
                    DataResult<NbtElement> var10000 = SignText.CODEC.encodeStart(NbtOps.INSTANCE, this.backText);
                    Logger var10001 = LOGGER;
                    Objects.requireNonNull(var10001);
                    var10000.resultOrPartial(var10001::error).ifPresent((backText) -> {
                        nbt.put("back_text", backText);
                    });
                }
                case "is_waxed" -> nbt.putBoolean("is_waxed", this.waxed);
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "front_text" -> {
                    DataResult<SignText> var10000 = SignText.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("front_text"));
                    Logger var10001 = LOGGER;
                    Objects.requireNonNull(var10001);
                    var10000.resultOrPartial(var10001::error).ifPresent((signText) -> {
                        this.frontText = this.parseLines(signText);
                    });
                }
                case "back_text" -> {
                    DataResult<SignText> var10000 = SignText.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("back_text"));
                    Logger var10001 = LOGGER;
                    Objects.requireNonNull(var10001);
                    var10000.resultOrPartial(var10001::error).ifPresent((signText) -> {
                        this.backText = this.parseLines(signText);
                    });
                }
                case "is_waxed" -> this.waxed = nbt.getBoolean("is_waxed");
            }
        }
        return true;
    }

}
