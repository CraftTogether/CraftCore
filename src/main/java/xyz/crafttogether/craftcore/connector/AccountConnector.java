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

public class AccountConnector {
    private static final Logger logger = LoggerFactory.getLogger(AccountConnector.class);
    private static final String PATH = CraftCore.getPlugin().getDataFolder() + "/accounts.json";
    private static List<AccountConnection> accounts = new ArrayList<>();
    private static Gson gson = new Gson();

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
            Type type = new TypeToken<List<AccountConnection>>(){}.getType();
            accounts = gson.fromJson(reader, type);
        } catch (IOException e) {
            logger.error("Failed to get accounts from accounts.json");
            e.printStackTrace();
        }
    }

    public static synchronized void saveAccounts() {
        try (FileWriter writer = new FileWriter(PATH)) {
            writer.write(gson.toJson(accounts));
        } catch (IOException e) {
            logger.error("Failed to save accounts to disk");
            e.printStackTrace();
        }
    }

    public static void addAccount(long discordId, UUID minecraftUUID) {
        AccountConnection account = new AccountConnection(discordId, minecraftUUID.toString());
        accounts.add(account);
        saveAccounts();
    }

    public static List<AccountConnection> getAccounts() {
        return accounts;
    }

    public static boolean containsAccount(long discordId) {
        for (AccountConnection account : accounts) {
            if (account.getDiscordId() == discordId) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAccount(UUID minecraftUUID) {
        for (AccountConnection account : accounts) {
            if (account.getMinecraftUUID().equals(minecraftUUID.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAccount(String minecraftUUID) {
        for (AccountConnection account : accounts) {
            if (account.getMinecraftUUID().equals(minecraftUUID)) {
                return true;
            }
        }
        return false;
    }

    public static Optional<AccountConnection> getAccount(long discordId) {
        for (AccountConnection account : accounts) {
            if (account.getDiscordId() == discordId) {
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }

    public static Optional<AccountConnection> getAccount(UUID minecraftUUID) {
        for (AccountConnection account : accounts) {
            if (account.getMinecraftUUID().equals(minecraftUUID.toString())) {
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }

    public static Optional<AccountConnection> getAccount(String minecraftUUID) {
        for (AccountConnection account : accounts) {
            if (account.getMinecraftUUID().equals(minecraftUUID)) {
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }

    public static void removeAccount(long discordId) {
        for (int i = 0; i < accounts.size(); i++) {
            if (discordId == accounts.get(i).getDiscordId()) {
                accounts.remove(i);
                saveAccounts();
                return;
            }
        }
    }

    public static void removeAccount(UUID minecraftUUID) {
        for (int i = 0; i < accounts.size(); i++) {
            if (minecraftUUID.toString().equals(accounts.get(i).getMinecraftUUID())) {
                accounts.remove(i);
                saveAccounts();
                return;
            }
        }
    }

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
