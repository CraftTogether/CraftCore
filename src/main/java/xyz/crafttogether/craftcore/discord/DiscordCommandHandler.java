package xyz.crafttogether.craftcore.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.crafttogether.craftcore.CraftCore;

/**
 * Class which handles the discord command invocation
 */
public class DiscordCommandHandler extends ListenerAdapter {

    /**
     * Method invoked when a slash command is executed
     *
     * @param event The SlashCommandInteractionEvent object
     */
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
