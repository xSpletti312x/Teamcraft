package dev.spletti.teamcraft;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Teamcraft implements ModInitializer {
	public static final String MOD_ID = "teamcraft";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Teamcraft loaded.");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(Commands.literal("team")
				.executes(context -> {
					context.getSource().sendSuccess(
						() -> Component.literal("Teamcraft is loaded. Team features are not implemented yet."),
						false
					);
					return Command.SINGLE_SUCCESS;
				})
			)
		);
	}
}
