package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LootableContainerBlockEntity.class)
public abstract class LootableContainerBlockEntityMixin extends LockableContainerBlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    protected Identifier lootTableId;
    @Shadow
    protected long lootTableSeed;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "LootTable":
                    if (nbt.contains("LootTable", 8))
                        this.lootTableId = new Identifier(nbt.getString("LootTable"));
                    break;
                case "LootTableSeed":
                    if (nbt.contains("LootTable", 8))
                        this.lootTableSeed = nbt.getLong("LootTableSeed");
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
