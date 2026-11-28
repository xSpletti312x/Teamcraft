package dev.spletti.teamcraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.FilterMask;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;
import java.util.Locale;
import net.minecraft.ChatFormatting;

public class Teamcraft implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("Teamcaft loaded");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, enviroment) -> {
            dispatcher.register(Commands.literal("teamcraft")
                .executes(context -> {
                    context.getSource().sendSuccess(
                        () -> Component.literal("Teamcraft loaded"),
                        false
                    );
                    return 1;   
                })
                .then(Commands.literal("help")
                    .executes(context -> {
                        context.getSource().sendSuccess(
                            () -> Component.literal("/teamcraft help -> shows all commands"),
                            false
                        );
                        return 1;
                    })
                )
                .then(Commands.literal("create")
                    .then(Commands.argument("name", StringArgumentType.word())
                        .executes(context -> {
                            String name = StringArgumentType.getString(context, "name");
                            var player = context.getSource().getPlayerOrException();
                            Team team = new Team(name, player.getUUID());
                            TeamData teamData = TeamData.get(context.getSource().getServer());

                            if (teamData.hasTeamName(name)) {
                                context.getSource().sendFailure(
                                    Component.literal("A team with that name already exists.")
                                );
                                return 0;
                            }

                            if (!teamData.addTeam(team)) {
                                context.getSource().sendFailure(
                                    Component.literal("You are already in a Team.")
                                );
                                return 0;
                            }

                            context.getSource().sendSuccess(
                                () -> Component.literal("Team created: " + team.getName()),
                                false
                            );
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("leave")
                    .executes(context -> {
                        var player = context.getSource().getPlayerOrException();
                        TeamData teamData = TeamData.get(context.getSource().getServer());

                        if (!teamData.leaveTeam(player.getUUID())) {
                            context.getSource().sendFailure(
                                Component.literal("You are not in a team.")
                            );
                            return 0;
                        }

                        context.getSource().sendSuccess(
                            () -> Component.literal("You left your team."),
                            false
                        );
                        return 1;
                    })
                )
                .then(Commands.literal("list")
                    .executes(context -> {
                        var teamNames = TeamData.get(context.getSource().getServer()).getTeamNames();

                        if (teamNames.isEmpty()) {
                            context.getSource().sendFailure(
                                Component.literal("There are no teams.")
                            );
                            return 0;
                        }

                        context.getSource().sendSuccess(
                            () -> Component.literal(String.join(", ", teamNames)),
                            false
                        );
                        return 1;
                    })
                )
                .then(Commands.literal("modify")
                    .then(Commands.literal("name")
                        .then(Commands.argument("newName", StringArgumentType.word())
                            .executes(context -> {
                                var player = context.getSource().getPlayerOrException();
                                TeamData teamData = TeamData.get(context.getSource().getServer());
                                var foundTeam = teamData.findTeam(player.getUUID());

                                if (foundTeam.isEmpty()) {
                                    context.getSource().sendFailure(
                                        Component.literal("You are not in a Team.")
                                    );
                                    return 0;
                                }

                                Team team = foundTeam.get();

                                if (!team.getOwner().equals(player.getUUID())) {
                                    context.getSource().sendFailure(
                                        Component.literal("Only the owner can change the team name.")
                                    );
                                    return 0;
                                }

                                String newName = StringArgumentType.getString(context, "newName");

                                if(!teamData.renameTeam(team, newName)) {
                                    context.getSource().sendFailure(
                                        Component.literal("A team with that name already exists.")
                                    );
                                    return 0;
                                }

                                context.getSource().sendSuccess(
                                    () -> Component.literal("Team name changed to " + newName),
                                    false
                                );
                                return 1;
                            })
                        )
                    )
                    .then(Commands.literal("color")
                        .then(Commands.argument("color", StringArgumentType.word())
                            .executes(context -> {
                                var player = context.getSource().getPlayerOrException();
                                TeamData teamData = TeamData.get(context.getSource().getServer());
                                var foundTeam = teamData.findTeam(player.getUUID());

                                if (foundTeam.isEmpty()) {
                                    context.getSource().sendFailure(
                                        Component.literal("You are not in a team.")
                                    );
                                    return 0;
                                }

                                Team team = foundTeam.get();

                                if (!team.getOwner().equals(player.getUUID())) {
                                    context.getSource().sendFailure(
                                        Component.literal("Only the owner can change the team color.")
                                    );
                                    return 0;
                                }

                                String color = StringArgumentType.getString(context, "color")
                                    .toLowerCase(Locale.ROOT);
                                List<String> allowedColors = List.of(
                                    "black", "dark_blue", "dark_green", "dark_aqua",
                                    "dark_red", "dark_purple", "gold", "gray",
                                    "dark_gray", "blue", "green", "aqua",
                                    "red", "light_purple", "yellow", "white"
                                );

                                if (!allowedColors.contains(color)) {
                                    context.getSource().sendFailure(
                                        Component.literal("Invalid color. Use a standard Minecraft color name.")
                                    );
                                    return 0;
                                }

                                teamData.setTeamColor(team, color);
                                context.getSource().sendSuccess(
                                    () -> Component.literal("Team color changed to " + color + "."),
                                    false
                                );
                                return 1;
                            })
                        )
                    )
                )
            );
        });

        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, originalChatType) -> {
            var foundTeam = TeamData.get(sender.level().getServer())
                .findTeam(sender.getUUID());

            if (foundTeam.isEmpty()) {
                return true;
            }

            Team team = foundTeam.get();

            var teamPrefix = Component.literal("[" + team.getName() + "] ")
                .withStyle(ChatFormatting.valueOf(team.getColor().toUpperCase(Locale.ROOT)));
            var formatted = Component.empty()
            .append(teamPrefix)
                .append(sender.getName())
                .append(Component.literal(" "))
                .append(message.decoratedContent());

            PlayerChatMessage teamMessage = new PlayerChatMessage(
                message.link(),
                message.signature(),
                message.signedBody(),
                formatted,
                FilterMask.FULLY_FILTERED
            );

            ChatType.Bound teamChatType = ChatType.bind(
                TEAM_CHAT_TYPE,
                sender.level().registryAccess(),
                formatted
            );

            for (ServerPlayer recipient : sender.level().getServer().getPlayerList().getPlayers()) {
                recipient.sendChatMessage(
                    new OutgoingChatMessage.Player(teamMessage),
                    false,
                    teamChatType
                );
            }

            return false;
        });
    } 

    private static final ResourceKey<ChatType> TEAM_CHAT_TYPE =
        ResourceKey.create(
            Registries.CHAT_TYPE,
            Identifier.fromNamespaceAndPath("teamcraft", "team_chat")
        );
}
