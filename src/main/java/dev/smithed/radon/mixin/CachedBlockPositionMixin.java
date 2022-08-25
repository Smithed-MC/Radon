package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IWorldExtender;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CachedBlockPosition.class)
public abstract class CachedBlockPositionMixin {

    @Shadow @Final WorldView world;
    @Shadow @Final BlockPos pos;
    @Shadow @Final boolean forceLoad;
    @Shadow @Nullable BlockState state;
    @Shadow @Nullable BlockEntity blockEntity;
    @Shadow boolean cachedEntity;

    /**
     * @author ImCoolYeah105
     * @reason redirect getBlockState -> getBlockStateNoLoad, may be replaceable with a @redirect
     */
    @Overwrite
    public BlockState getBlockState() {
        if (this.state == null && (this.forceLoad || this.world.isChunkLoaded(this.pos))) {
            if(Radon.CONFIG.fixBlockAccessForceload && this.world instanceof IWorldExtender mixin)
                this.state = mixin.getBlockStateNoLoad(this.pos);
            else
                this.state = this.world.getBlockState(this.pos);
        }
        return this.state;
    }

    /**
     * @author ImCoolYeah105
     * @reason redirect getBlockEntity -> getBlockEntityNoLoad, may be replaceable with a @redirect
     */
    @Overwrite
    public BlockEntity getBlockEntity() {
        if (this.blockEntity == null && !this.cachedEntity) {
            if(Radon.CONFIG.fixBlockAccessForceload && this.world instanceof IWorldExtender mixin)
                this.blockEntity = mixin.getBlockEntityNoLoad(this.pos);
            else
                this.blockEntity = this.world.getBlockEntity(this.pos);
            this.cachedEntity = true;
        }
        return this.blockEntity;
    }
}
