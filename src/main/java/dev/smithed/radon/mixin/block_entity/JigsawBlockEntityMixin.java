package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(JigsawBlockEntity.class)
public abstract class JigsawBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {

    @Shadow Identifier name = new Identifier("empty");
    @Shadow Identifier target = new Identifier("empty");
    @Shadow RegistryKey<StructurePool> pool;
    @Shadow JigsawBlockEntity.Joint joint;
    @Shadow String finalState;

    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt)) {
            switch (topLevelNbt) {
                case "name":
                    nbt.putString("name", this.name.toString());
                    break;
                case "target":
                    nbt.putString("target", this.target.toString());
                    break;
                case "pool":
                    nbt.putString("pool", this.pool.getValue().toString());
                    break;
                case "final_state":
                    nbt.putString("final_state", this.finalState);
                    break;
                case "joint":
                    nbt.putString("joint", this.joint.asString());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean readCustomDataFromNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        if (!super.readCustomDataFromNbtFiltered(nbt, path, topLevelNbt)) {
            if(!nbt.contains(topLevelNbt))
                return false;
            switch (topLevelNbt) {
                case "name":
                    this.name = new Identifier(nbt.getString("name"));
                    break;
                case "target":
                    this.target = new Identifier(nbt.getString("target"));
                    break;
                case "pool":
                    this.pool = RegistryKey.of(Registry.STRUCTURE_POOL_KEY, new Identifier(nbt.getString("pool")));
                    break;
                case "final_state":
                    this.finalState = nbt.getString("final_state");
                    break;
                case "joint":
                    this.joint = JigsawBlockEntity.Joint.byName(nbt.getString("joint")).orElseGet(() -> JigsawBlock.getFacing(this.getCachedState()).getAxis().isHorizontal() ? JigsawBlockEntity.Joint.ALIGNED : JigsawBlockEntity.Joint.ROLLABLE);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
