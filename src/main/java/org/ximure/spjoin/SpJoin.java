package org.ximure.spjoin;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpJoin implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("spjoin");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Loaded SpJoin");
	}
}
