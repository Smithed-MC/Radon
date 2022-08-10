package dev.smithed.radon.mixin;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.SharedConstants;
import net.minecraft.block.entity.SculkShriekerWarningManager;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntityMixin implements ICustomNBTMixin {
    @Shadow
    private static Logger field_38197;
    @Shadow
    private PlayerInventory inventory;
    @Shadow
    private int sleepTimer;
    @Shadow
    protected int enchantmentTableSeed;
    @Shadow
    protected HungerManager hungerManager;
    @Shadow
    protected SculkShriekerWarningManager sculkShriekerWarningManager;
    @Shadow
    private PlayerAbilities abilities;
    @Shadow
    protected EnderChestInventory enderChestInventory;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, NbtPathArgumentType.NbtPath path, String topLevelNbt) {
        PlayerEntity entity = ((PlayerEntity)(Object)this);

        if(!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "DataVersion":
                    nbt.putInt("DataVersion", SharedConstants.getGameVersion().getWorldVersion());
                    break;
                case "Inventory":
                    nbt.put("Inventory", this.inventory.writeNbt(new NbtList()));
                    break;
                case "SelectedItemSlot":
                    nbt.putInt("SelectedItemSlot", this.inventory.selectedSlot);
                    break;
                case "SleepTimer":
                    nbt.putShort("SleepTimer", (short) this.sleepTimer);
                    break;
                case "XpP":
                    nbt.putFloat("XpP", entity.experienceProgress);
                    break;
                case "XpLevel":
                    nbt.putInt("XpLevel", entity.experienceLevel);
                    break;
                case "XpTotal":
                    nbt.putInt("XpTotal", entity.totalExperience);
                    break;
                case "XpSeed":
                    nbt.putInt("XpSeed", this.enchantmentTableSeed);
                    break;
                case "Score":
                    nbt.putInt("Score", entity.getScore());
                    break;
                case "foodLevel":
                case "foodTickTimer":
                case "foodSaturationLevel":
                case "foodExhaustionLevel":
                    this.hungerManager.writeNbt(nbt);
                    break;
                case "abilities":
                    this.abilities.writeNbt(nbt);
                    break;
                case "EnderItems":
                    nbt.put("EnderItems", this.enderChestInventory.toNbtList());
                    break;
                case "ShoulderEntityLeft":
                    if (!entity.getShoulderEntityLeft().isEmpty()) {
                        nbt.put("ShoulderEntityLeft", entity.getShoulderEntityLeft());
                    }
                    break;
                case "ShoulderEntityRight":
                    if (!entity.getShoulderEntityRight().isEmpty()) {
                        nbt.put("ShoulderEntityRight", entity.getShoulderEntityRight());
                    }
                break;
                default:
                    return false;
            }
        }
        return true;
    }
}
