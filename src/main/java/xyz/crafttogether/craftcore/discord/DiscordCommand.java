package xyz.crafttogether.craftcore.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface DiscordCommand {
    void invoke(SlashCommandInteractionEvent event);

    String getCommandName();

    String getCommandDescription();
}
