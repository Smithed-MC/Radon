package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.ServerConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntityMixin implements ICustomNBTMixin {
    @Shadow SimpleInventory items;
    @Shadow abstract void updateSaddle();
    @Shadow abstract void onChestedStatusChanged();

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AbstractHorseEntity entity = ((AbstractHorseEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "EatingHaystack":
                    nbt.putBoolean("EatingHaystack", entity.isEatingGrass());
                    break;
                case "Bred":
                    nbt.putBoolean("Bred", entity.isBred());
                    break;
                case "Temper":
                    nbt.putInt("Temper", entity.getTemper());
                    break;
                case "Tame":
                    nbt.putBoolean("Tame", entity.isTame());
                    break;
                case "Owner":
                    if (entity.getOwnerUuid() != null)
                        nbt.putUuid("Owner", entity.getOwnerUuid());
                    break;
                case "SaddleItem":
                    if (nbt.contains("SaddleItem", 10))
                        if (!this.items.getStack(0).isEmpty())
                            nbt.put("SaddleItem", this.items.getStack(0).writeNbt(new NbtCompound()));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        AbstractHorseEntity entity = ((AbstractHorseEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "EatingHaystack":
                    entity.setEatingGrass(nbt.getBoolean("EatingHaystack"));
                    break;
                case "Bred":
                    entity.setBred(nbt.getBoolean("Bred"));
                    break;
                case "Temper":
                    entity.setTemper(nbt.getInt("Temper"));
                    break;
                case "Tame":
                    entity.setTame(nbt.getBoolean("Tame"));
                    break;
                case "Owner":
                    UUID uUID;
                    if (nbt.containsUuid("Owner")) {
                        uUID = nbt.getUuid("Owner");
                    } else {
                        String string = nbt.getString("Owner");
                        uUID = ServerConfigHandler.getPlayerUuidByName(entity.getServer(), string);
                    }
                    if (uUID != null)
                        entity.setOwnerUuid(uUID);
                    break;
                case "ChestedHorse":
                    ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("SaddleItem"));
                    if (itemStack.isOf(Items.SADDLE))
                        this.items.setStack(0, itemStack);
                    this.updateSaddle();
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
