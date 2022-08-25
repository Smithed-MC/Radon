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

    public static void logDebug(Object obj) {
        if(CONFIG.debug && obj != null)
            LOGGER.info(obj.toString());
    }

    public static class RadonConfig {

        public boolean debug = false;
        public boolean nbtOptimizations = true;
        public boolean entitySelectorOptimizations = true;
        public boolean fixBlockAccessForceload = true;

    }

}
