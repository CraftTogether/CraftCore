package xyz.crafttogether.craftcore.connector;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.craftcore.CraftCore;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Class which abstracts the connection of accounts
 */
public class AccountConnector {
    /**
     * SLF4J Logger instance
     */
    private static final Logger logger = LoggerFactory.getLogger(AccountConnector.class);
    /**
     * The path of the accounts.json file
     */
    private static final String PATH = CraftCore.getPlugin().getDataFolder() + "/accounts.json";
    /**
     * Gson instance
     */
    private static final Gson gson = new Gson();
    /**
     * List containing all the accounts
     */
    private static List<AccountConnection> accounts = new ArrayList<>();

    /**
     * Loads the accounts json file, or generates a new one if it does not exist, and loads the accounts into the
     * list of accounts
     */
    public static void loadAccounts() {
        Path path = Path.of(PATH);
        if (!Files.exists(path)) {
            try {
                Files.write(path, "[]".getBytes());
            } catch (IOException e) {
                logger.error("Failed to generate accounts.json file");
                e.printStackTrace();
                return;
            }
        }
        try (FileReader reader = new FileReader(PATH)) {
            Type type = new TypeToken<List<AccountConnection>>() {
            }.getType();
            accounts = gson.fromJson(reader, type);
        } catch (IOException e) {
            logger.error("Failed to get accounts from accounts.json");
            e.printStackTrace();
        }
    }

    /**
     * Saves the list of accounts to the accounts.json file
     */
    public static synchronized void saveAccounts() {
        try (FileWriter writer = new FileWriter(PATH)) {
            writer.write(gson.toJson(accounts));
        } catch (IOException e) {
            logger.error("Failed to save accounts to disk");
            e.printStackTrace();
        }
    }

    /**
     * Adds a new account connection
     *
     * @param discordId     The discord id of the user
     * @param minecraftUUID The minecraft UUID of the player
     */
    public static void addAccount(long discordId, UUID minecraftUUID) {
        AccountConnection account = new AccountConnection(discordId, minecraftUUID.toString());
        accounts.add(account);
        saveAccounts();
    }

    /**
     * Gets a list of account connections
     *
     * @return A list of account connections
     */
    public static List<AccountConnection> getAccounts() {
        return accounts;
    }

    /**
     * checks whether an account is already connected using the discord user id
     *
     * @param discordId The discord user id
     * @return Whether the discord user has already connected their minecraft account
     */
    public static boolean containsAccount(long discordId) {
        for (AccountConnection account : accounts) {
            if (account.getDiscordId() == discordId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether an account is already connected using the minecraft player UUID
     *
     * @param minecraftUUID The UUID of the minecraft player
     * @return Whether the minecraft player has already connected their discord account
     */
    public static boolean containsAccount(UUID minecraftUUID) {
        for (AccountConnection account : accounts) {
            if (account.getMinecraftUUID().equals(minecraftUUID.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether an account already exists using the discord user id
     *
     * @param minecraftUUID The UUID of the minecraft player as a String
     * @return Whether the minecraft player has already connected their discord account
     */
    public static boolean containsAccount(String minecraftUUID) {
        for (AccountConnection account : accounts) {
            if (account.getMinecraftUUID().equals(minecraftUUID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets an AccountConnection using the discord user id
     *
     * @param discordId The users discord id
     * @return An optional containing the AccountConnection if it exists, otherwise an empty Optional
     */
    public static Optional<AccountConnection> getAccount(long discordId) {
        for (AccountConnection account : accounts) {
            if (account.getDiscordId() == discordId) {
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets an AccountConnection using the minecraft player UUID
     *
     * @param minecraftUUID The minecraft players UUID
     * @return An optional containing the AccountConnection if it exists, otherwise an empty Optional
     */
    public static Optional<AccountConnection> getAccount(UUID minecraftUUID) {
        for (AccountConnection account : accounts) {
            if (account.getMinecraftUUID().equals(minecraftUUID.toString())) {
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets an AccountConnection using the minecraft player UUID as a String
     *
     * @param minecraftUUID The minecraft players UUID as a String
     * @return An Optional containing the AccountConnection if it exists, otherwise an empty Optional
     */
    public static Optional<AccountConnection> getAccount(String minecraftUUID) {
        for (AccountConnection account : accounts) {
            if (account.getMinecraftUUID().equals(minecraftUUID)) {
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }

    /**
     * Removes an account connection using the discord user id
     *
     * @param discordId The discord users id
     */
    public static void removeAccount(long discordId) {
        for (int i = 0; i < accounts.size(); i++) {
            if (discordId == accounts.get(i).getDiscordId()) {
                accounts.remove(i);
                saveAccounts();
                return;
            }
        }
    }

    /**
     * Removes an account connection using the minecraft player UUID
     *
     * @param minecraftUUID The minecraft players UUID
     */
    public static void removeAccount(UUID minecraftUUID) {
        for (int i = 0; i < accounts.size(); i++) {
            if (minecraftUUID.toString().equals(accounts.get(i).getMinecraftUUID())) {
                accounts.remove(i);
                saveAccounts();
                return;
            }
        }
    }

    /**
     * Removes an account connection using the minecraft player UUID as a String
     *
     * @param minecraftUUID The minecraft player UUID as a String
     */
    public static void removeAccount(String minecraftUUID) {
        for (int i = 0; i < accounts.size(); i++) {
            if (minecraftUUID.equals(accounts.get(i).getMinecraftUUID())) {
                accounts.remove(i);
                saveAccounts();
                return;
            }
        }
    }
}
