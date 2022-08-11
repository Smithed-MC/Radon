package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.village.TradeOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin extends PassiveEntityMixin implements ICustomNBTMixin {
    @Shadow
    private SimpleInventory inventory;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        MerchantEntity entity = ((MerchantEntity)(Object)this);
        if(!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "Offers":
                    TradeOfferList tradeOfferList = entity.getOffers();
                    if (!tradeOfferList.isEmpty()) {
                        nbt.put("Offers", tradeOfferList.toNbt());
                    }
                    break;
                case "Inventory":
                    nbt.put("Inventory", this.inventory.toNbtList());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
