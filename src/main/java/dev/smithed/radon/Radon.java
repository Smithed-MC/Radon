package dev.smithed.radon;

import dev.smithed.radon.commands.RadonCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Radon implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("radon");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Radon");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RadonCommand.register(dispatcher));
    }

}
