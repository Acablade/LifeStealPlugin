package me.sirhenry.lifesteal;

import me.sirhenry.lifesteal.listeners.PlayerInteractListener;
import me.sirhenry.lifesteal.listeners.PlayerJoinListener;
import me.sirhenry.lifesteal.listeners.PlayerKilledListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public final class LifeSteal extends JavaPlugin {

    public boolean MAX_HEALTH_ENABLED;
    public boolean SPECTATOR_ON_0_LIVES;

    public String MAX_HEALTH_MESSAGE;
    public String ITEM_NAME;
    public String BAN_MESSAGE;

    public List<String> ITEM_LORE;

    public double MAX_HEALTH;
    public double DEFAULT_HEALTH;
    public double HEALTH_GAIN;
    public double HEALTH_LOSS;

    public boolean LOSE_LIFE_IF_NOT_KILLED_BY_PLAYER;

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new PlayerKilledListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getCommand("withdraw").setExecutor(new WithdrawCommand());
        getCommand("lifestealreload").setExecutor((sender, command, label, args) -> {
            if(!sender.hasPermission("lifesteal.reload")) return false;
            reloadConfig();
            initVariables();
            Bukkit.removeRecipe(new NamespacedKey(this, "Heart"));
            Bukkit.addRecipe(heartRecipe());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&eSuccessfully reloaded LifeSteal plugin"));
            return false;
        });

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        initVariables();

        Bukkit.addRecipe(heartRecipe());

        Bukkit.broadcastMessage(ChatColor.GREEN + "Life Steal Plugin has Finished Loading!");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ShapedRecipe heartRecipe() {

        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ITEM_NAME);
        meta.setLore(ITEM_LORE);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "Heart");
        ShapedRecipe sr = new ShapedRecipe(key, item);

        sr.shape("ABC", "DEF", "GHI");

        char Alphabet[] = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};

        for(int i = 0; i < 9; i++) sr.setIngredient(Alphabet[i], Material.valueOf((String) getConfig().get("HeartRecipe.Slot" + i)));

        return sr;
    }

    public void initVariables(){

        MAX_HEALTH = getConfig().getDouble("MaxHealth");
        DEFAULT_HEALTH = getConfig().getDouble("DefaultHealth");
        HEALTH_GAIN = getConfig().getDouble("HealthGainedOnKill");
        HEALTH_LOSS = getConfig().getDouble("HealthLostOnDeath");

        LOSE_LIFE_IF_NOT_KILLED_BY_PLAYER = getConfig().getBoolean("LoseLifeIfNotKilledByPlayer");
        SPECTATOR_ON_0_LIVES = getConfig().getBoolean("SpectatorOn0Lives");
        MAX_HEALTH_ENABLED = MAX_HEALTH != -1;

        ITEM_LORE = getConfig().getStringList("HeartLore");
        ITEM_LORE.replaceAll(s-> ChatColor.translateAlternateColorCodes('&',s));

        ITEM_NAME = ChatColor.translateAlternateColorCodes('&',getConfig().getString("HeartName"));
        MAX_HEALTH_MESSAGE = ChatColor.translateAlternateColorCodes('&',getConfig().getString("MaxHealthMessage"));
        BAN_MESSAGE = ChatColor.translateAlternateColorCodes('&',getConfig().getString("BanMessage"));
    }

}

