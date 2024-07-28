package me.usainsrht.basicVanish.listeners;

import me.usainsrht.basicVanish.BasicVanish;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    BasicVanish plugin;

    public QuitListener(BasicVanish plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (plugin.isVanished(player.getUniqueId())) {
            e.quitMessage(Component.empty());
        }
    }

}