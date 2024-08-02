package me.usainsrht.basicVanish.listeners;

import me.usainsrht.basicVanish.BasicVanish;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener {

    BasicVanish plugin;

    public AdvancementListener(BasicVanish plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();

        if (plugin.isVanished(player.getUniqueId())) {
            e.message(null);
        }
    }

}
