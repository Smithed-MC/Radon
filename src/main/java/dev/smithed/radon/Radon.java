package dev.smithed.radon;

import dev.smithed.radon.commands.RadonCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Radon implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("radon");
    public static final RadonConfig CONFIG = new RadonConfig();

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Radon");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RadonCommand.register(dispatcher));
    }

    /*
    It's better to have the config located in a central area like the main radon class
    rather than a command class
    ofc the class below can be split into a new class however i was lazy in porting this change over
     */
    public static class RadonConfig {

        /*
        Yes i know this method naming is stupid however,
        i literally just ported them over exactly
        also i suck at naming
         */

        private boolean NbtOptimizations = true; // All optimizations that run well(ish) may as well default to true
        public boolean getNbtOptimizationsEnabled() {return this.NbtOptimizations;}
        public void setNbtOptimizationsEnabled(boolean enabled) {this.NbtOptimizations = enabled;}


        private boolean EntitySelectorOptimizations = true;
        public boolean getEntitySelectorOptimizations() { return this.EntitySelectorOptimizations; }
        public void setEntitySelectorOptimizations(boolean enabled) { this.EntitySelectorOptimizations = enabled; }

    }

}
