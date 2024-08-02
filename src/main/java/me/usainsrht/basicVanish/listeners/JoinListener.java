package me.usainsrht.basicVanish.listeners;

import me.usainsrht.basicVanish.BasicVanish;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    BasicVanish plugin;

    public JoinListener(BasicVanish plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        plugin.hideVanishedPlayersFor(player);

        if (plugin.isVanished(player.getUniqueId())) {
            plugin.enterVanish(player);
            e.joinMessage(null);
        }
    }

}
