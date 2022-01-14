package me.sirhenry.lifesteal.listeners;

import me.sirhenry.lifesteal.LifeSteal;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import javax.security.auth.login.Configuration;
import java.util.Date;

public class PlayerKilledListener implements Listener {

    LifeSteal plugin = LifeSteal.getPlugin(LifeSteal.class);

    @EventHandler
    public void onPlayerKilled(PlayerDeathEvent e) {

        Player victim = e.getEntity();

        if(victim.getKiller() instanceof Player) {

            Player killer = victim.getKiller();
            if(killer != victim) {

                double vHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                double kHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

                victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(vHealth - plugin.HEALTH_LOSS);

                if(plugin.MAX_HEALTH_ENABLED && kHealth < plugin.MAX_HEALTH)
                    killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(kHealth + plugin.HEALTH_GAIN);
                else
                    killer.sendMessage(plugin.MAX_HEALTH_MESSAGE);



                if(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() <= 0.0) {

                    if(plugin.SPECTATOR_ON_0_LIVES)
                        victim.setGameMode(GameMode.SPECTATOR);
                    else{                                                                                                                 // 6 HOURS
                        Bukkit.getBanList(BanList.Type.NAME).addBan(victim.getName(),plugin.BAN_MESSAGE,new Date(System.currentTimeMillis()+60*60*1000),"LifeStealPlugin");
                        victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(plugin.DEFAULT_HEALTH);
                        victim.kickPlayer(plugin.BAN_MESSAGE);
                    }

                }
            }

            e.setDeathMessage(ChatColor.LIGHT_PURPLE + victim.getDisplayName() + ChatColor.GOLD + " ("  + victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + ")" + ChatColor.GRAY + " Was Killed By " + ChatColor.LIGHT_PURPLE + killer.getDisplayName() + ChatColor.GOLD + " ("  + killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + ")");

        }

        else {

            if(!plugin.LOSE_LIFE_IF_NOT_KILLED_BY_PLAYER) return;

            double vHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

            victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(vHealth - plugin.HEALTH_LOSS);

            if (victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() <= 0.0) {

                if(plugin.SPECTATOR_ON_0_LIVES)
                    victim.setGameMode(GameMode.SPECTATOR);
                else{                                                                                                                 // 6 HOURS
                    Bukkit.getBanList(BanList.Type.NAME).addBan(victim.getName(),plugin.BAN_MESSAGE,new Date(System.currentTimeMillis()+60*60*1000),"LifeStealPlugin");
                    victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(plugin.DEFAULT_HEALTH);
                    victim.kickPlayer(plugin.BAN_MESSAGE);
                }

            }
        }

    }

}
