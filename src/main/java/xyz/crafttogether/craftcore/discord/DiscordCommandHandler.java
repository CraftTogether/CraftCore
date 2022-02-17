package xyz.crafttogether.craftcore.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.crafttogether.craftcore.CraftCore;


public class DiscordCommandHandler extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        DiscordCommand command = CraftCore.getDiscordCommand(event.getName());
        if (command == null) {
            event.reply("Command not found").setEphemeral(true).queue();
        } else {
            command.invoke(event);
        }
    }
}
