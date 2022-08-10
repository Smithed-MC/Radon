package dev.smithed.radon.mixin;

import dev.smithed.radon.mixin_interface.IDataCommandObjectMixin;
import net.minecraft.command.DataCommandObject;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DataCommandObject.class)
public interface DataCommandObjectMixin extends IDataCommandObjectMixin {}
