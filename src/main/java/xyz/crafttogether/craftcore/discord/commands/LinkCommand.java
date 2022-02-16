package xyz.crafttogether.craftcore.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.crafttogether.craftcore.CraftCore;
import xyz.crafttogether.craftcore.discord.DiscordCommand;
import xyz.crafttogether.craftcore.discord.VerifyCode;

import java.time.Instant;
import java.util.Random;

public class LinkCommand implements DiscordCommand {
    private static final String COMMAND_NAME = "link";
    private static final String COMMAND_DESCRIPTION = "link your discord account to your minecraft account";

    @Override
    public void invoke(SlashCommandInteractionEvent event) {
        if (CraftCore.doesCodeAlreadyExists(event.getUser().getIdLong())) {
            event.reply("You have already been given a verification code which is still valid, check your dms")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        event.getUser().openPrivateChannel().queue(privateChannel -> {
            VerifyCode code = new VerifyCode(Long.toHexString(new Random().nextInt(1000, 9999)).toString(), System.currentTimeMillis());
            CraftCore.addVerifyCode(event.getUser().getIdLong(), code);
            final MessageEmbed embed = new EmbedBuilder()
                    .setTitle("Verification")
                    .setDescription(String.format("You can verify your minecraft account by using ```/verify %s``` on the craft together server", code.getCode()))
                    .setTimestamp(Instant.now())
                    .build();
            privateChannel.sendMessageEmbeds(embed).queue();
        });
        event.reply(":thumbsup: Check your dms")
                .setEphemeral(true)
                .queue();
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
