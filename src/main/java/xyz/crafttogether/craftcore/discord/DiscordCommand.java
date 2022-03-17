package xyz.crafttogether.craftcore.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;

/**
 * Interface which is implemented to create a discord command, must be registered as well
 *
 * @see xyz.crafttogether.craftcore.CraftCore#addListeners(EventListener...)
 */
public interface DiscordCommand {
    /**
     * Method which is invoked when the command is executed
     *
     * @param event The SlashCommandInteractionEvent object
     */
    void invoke(SlashCommandInteractionEvent event);

    /**
     * Gets the name of the command
     *
     * @return The name of the command
     */
    String getCommandName();

    /**
     * Gets the description of the command
     *
     * @return The description of the command
     */
    String getCommandDescription();
}
