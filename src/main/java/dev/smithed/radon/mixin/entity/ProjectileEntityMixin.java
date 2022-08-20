package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends EntityMixin implements ICustomNBTMixin {

    @Shadow UUID ownerUuid;
    @Shadow boolean leftOwner;
    @Shadow boolean shot;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ProjectileEntity entity = ((ProjectileEntity) (Object) this);
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "HasBeenShot":
                    nbt.putBoolean("HasBeenShot", this.shot);
                    break;
                case "LeftOwner":
                    if (this.leftOwner)
                        nbt.putBoolean("LeftOwner", true);
                    break;
                case "Owner":
                    if (this.ownerUuid != null)
                        nbt.putUuid("Owner", this.ownerUuid);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        ProjectileEntity entity = ((ProjectileEntity)(Object)this);
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "Owner":
                    if (nbt.containsUuid("Owner"))
                        this.ownerUuid = nbt.getUuid("Owner");
                    break;
                case "LeftOwner":
                    this.leftOwner = nbt.getBoolean("LeftOwner");
                    break;
                case "HasBeenShot":
                    this.shot = nbt.getBoolean("HasBeenShot");
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
