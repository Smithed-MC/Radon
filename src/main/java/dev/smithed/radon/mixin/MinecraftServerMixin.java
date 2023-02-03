package dev.smithed.radon.mixin;

import com.mojang.datafixers.DataFixer;
import dev.smithed.radon.mixin_interface.IMinecraftServerExtender;
import dev.smithed.radon.utils.NBTUtils;
import net.minecraft.resource.*;
import net.minecraft.registry.tag.TagManagerLoader;
import net.minecraft.server.*;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.ApiServices;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.net.Proxy;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask> implements CommandOutput, AutoCloseable, IMinecraftServerExtender {
    public MinecraftServerMixin(String string) {
        super(string);
    }

    @Shadow private MinecraftServer.ResourceManagerHolder resourceManagerHolder;
    @Shadow @Final protected SaveProperties saveProperties;

    private final Map<String, Set<String>> entityTypes = new HashMap<>();

    /**
     * @author ImCoolYeah105
     * Injects into constructor to build entityTypes map on load
     */
    @Inject(method = "<init>(Ljava/lang/Thread;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/server/SaveLoader;Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/util/ApiServices;Lnet/minecraft/server/WorldGenerationProgressListenerFactory;)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void radon_init(Thread serverThread, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, Proxy proxy, DataFixer dataFixer, ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo cr) {
        this.constructEntityTypes(this.getDatapackTagManager());
    }

    /**
     * @author ImCoolYeah105
     * Injects into resource reloading to rebuild entityTypes map
     */
    @Inject(method = "reloadResources(Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;", at = @At("TAIL"))
    public void radon_reloadResources(CallbackInfoReturnable<CompletableFuture<Void>> ci) {
        if(ci.getReturnValue().isDone())
            this.constructEntityTypes(this.getDatapackTagManager());
        else
            ci.getReturnValue().thenAcceptAsync(resourceManagerHolder -> this.constructEntityTypes(this.getDatapackTagManager()));
    }

    private void constructEntityTypes(TagManagerLoader tags) {
        this.entityTypes.clear();
        for(TagManagerLoader.RegistryTags<?> tag: tags.getRegistryTags()) {
            final String key = tag.key().getValue().toString();
            if (key.equals("minecraft:entity_type")) {
                tag.tags().forEach( (id, entry) -> {
                    final Set<String> entries = new HashSet<>();
                    entry.forEach(item -> entries.add(NBTUtils.translationToTypeName(item.value().toString())));
                    this.entityTypes.put(id.toString(), entries);
                });
            }
        }
    }

    public TagManagerLoader getDatapackTagManager() {
        if(this.resourceManagerHolder.dataPackContents() != null)
            for(ResourceReloader resource: this.resourceManagerHolder.dataPackContents().getContents())
                if(resource instanceof TagManagerLoader tags)
                    return tags;
        return null;
    }

    public Set<String> getEntityTagEntries(String tag) {
        return this.entityTypes.get(tag);
    }

}
