package dev.smithed.radon.mixin;

import dev.smithed.radon.mixin_interface.IEntityManagerExtender;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.world.entity.EntityIndex;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerEntityManager.class)
public abstract class EntityManagerMixin<T extends EntityLike> implements AutoCloseable, IEntityManagerExtender<T> {

    @Shadow @Final private EntityIndex<T> index;

    @Override
    public EntityIndex<T> getEntityIndex() {
        return this.index;
    }
}
