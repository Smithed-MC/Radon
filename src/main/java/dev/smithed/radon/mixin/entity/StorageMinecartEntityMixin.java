package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StorageMinecartEntity.class)
public abstract class StorageMinecartEntityMixin extends AbstractMinecartEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        StorageMinecartEntity entity = ((StorageMinecartEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "LootTable" -> {
                    if (entity.getLootTableId() != null)
                        nbt.putString("LootTable", entity.getLootTableId().toString());
                }
                case "LootTableSeed" -> {
                    if (entity.getLootTableId() != null && entity.getLootTableSeed() != 0L)
                        nbt.putLong("LootTableSeed", entity.getLootTableSeed());
                }
                case "Items" -> {
                    if (entity.getLootTableId() == null)
                        Inventories.writeNbt(nbt, entity.getInventory());
                }
                default -> {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        StorageMinecartEntity entity = ((StorageMinecartEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {

            switch (topLevelNbt) {
                case "Items" -> entity.readInventoryFromNbt(nbt);
                case "LootTable" -> {
                    if (nbt.contains("LootTable", 8))
                        entity.setLootTableId(new Identifier(nbt.getString("LootTable")));
                }
                case "LootTableSeed" -> entity.setLootTableSeed(nbt.getLong("LootTableSeed"));
                default -> {
                    return false;
                }
            }
        }
        return true;
    }
}
