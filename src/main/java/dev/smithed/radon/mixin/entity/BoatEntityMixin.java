package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends EntityMixin implements ICustomNBTMixin {

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        BoatEntity entity = ((BoatEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            if (topLevelNbt.equals("Type")) {
                nbt.putString("Type", entity.getVariant().asString());
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        BoatEntity entity = ((BoatEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            if (topLevelNbt.equals("Type")) {
                if (nbt.contains("Type", 8))
                    entity.setVariant(BoatEntity.Type.getType(nbt.getString("Type")));
            } else {
                return false;
            }
        }
        return true;
    }
}
