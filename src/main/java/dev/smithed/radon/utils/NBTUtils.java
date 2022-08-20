package dev.smithed.radon.utils;

import net.minecraft.nbt.NbtCompound;

import java.util.Set;

public class NBTUtils {

    public static int getSlot(String nbt) {
        int sIndex = nbt.indexOf('[');
        int eIndex = nbt.indexOf(']');
        if(sIndex == -1 || eIndex == -1)
            return -1;
        else
            sIndex += 1;

        String slot = nbt.substring(sIndex, eIndex);
        int slotIndex = slot.indexOf("Slot:");
        int bIndex = slot.indexOf('b', slotIndex);

        if(slotIndex == -1 || bIndex == -1)
            return -1;
        try {
            return Integer.parseInt(slot.substring(slotIndex + 5, bIndex));
        } catch(NumberFormatException e) {}
        return -1;
    }

    public static String[] getTopLevelPaths(String nbt) {
        nbt = nbt.substring(1,nbt.length()-1) + ",";
        return nbt.split(":(\\[(.*?)\\])*(\\{(.*?)\\})*(.*?),");
    }

    public static String[] getTopLevelPaths(NbtCompound nbt) {
        Set<String> set = nbt.getKeys();
        String[] strings = new String[set.size()];
        return nbt.getKeys().toArray(strings);
    }

    public static String translationToTypeName(String name) {
        String[] split = name.split("\\.");
        if(split.length > 2)
            return split[1] + ":" + split[2];
        return "";
    }
}
