package dev.smithed.radon.mixin.block_entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(JigsawBlockEntity.class)
public abstract class JigsawBlockEntityMixin extends BlockEntityMixin implements ICustomNBTMixin {
    @Shadow
    private Identifier name = new Identifier("empty");
    @Shadow
    private Identifier target = new Identifier("empty");
    @Shadow
    private RegistryKey<StructurePool> pool;
    @Shadow
    private JigsawBlockEntity.Joint joint;
    @Shadow
    private String finalState;

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
}
