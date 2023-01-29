package dev.smithed.radon.mixin_interface;

import net.minecraft.registry.tag.TagManagerLoader;

import java.util.Set;

public interface IMinecraftServerExtender {

    TagManagerLoader getDatapackTagManager();
    Set<String> getEntityTagEntries(String tag);

}
