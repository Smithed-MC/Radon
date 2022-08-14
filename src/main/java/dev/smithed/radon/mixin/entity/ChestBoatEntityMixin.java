package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChestBoatEntity.class)
public abstract class ChestBoatEntityMixin extends BoatEntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ChestBoatEntity entity = ((ChestBoatEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "LootTable":
                    if (entity.getLootTableId() != null)
                        nbt.putString("LootTable", entity.getLootTableId().toString());
                    break;
                case "LootTableSeed":
                    if (entity.getLootTableId() != null && entity.getLootTableSeed() != 0L)
                        nbt.putLong("LootTableSeed", entity.getLootTableSeed());
                    break;
                case "Items":
                    if (entity.getLootTableId() == null)
                        Inventories.writeNbt(nbt, entity.getInventory());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
