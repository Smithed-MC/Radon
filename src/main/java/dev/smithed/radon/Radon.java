package dev.smithed.radon;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Radon implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("radon");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Radon");
    }

}
