package me.usainsrht.basicVanish;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.usainsrht.basicVanish.listeners.AdvancementListener;
import me.usainsrht.basicVanish.listeners.JoinListener;
import me.usainsrht.basicVanish.listeners.QuitListener;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BasicVanish extends JavaPlugin {

    private static BasicVanish instance;

    protected List<UUID> vanishedPlayers;
    private File dataFile;

    public static BossBar vanishedBossBar = BossBar.bossBar(
            Component.text("VANISHED").decorate(TextDecoration.BOLD),
            1f,
            BossBar.Color.WHITE,
            BossBar.Overlay.PROGRESS
    );

    @Override
    public void onEnable() {
        instance = this;

        vanishedPlayers = new ArrayList<>();

        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.mkdirs();
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("An error occurred while trying to create data file", e);
            }

        }
        ConfigurationSection config = YamlConfiguration.loadConfiguration(dataFile);
        config.getStringList("vanished_players").forEach(string -> vanishedPlayers.add(UUID.fromString(string)));

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("vanish", List.of("basicvanish", "v", "bv"), new VanishCommand(this));
        });

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        getServer().getPluginManager().registerEvents(new AdvancementListener(this), this);
    }

    @Override
    public void onDisable() {
        save();
    }

    public static BasicVanish getInstance() {
        return instance;
    }

    public void save() {
        ConfigurationSection config = YamlConfiguration.loadConfiguration(dataFile);
        config.getStringList("vanished_players").clear();
        config.getStringList("vanished_players").addAll(vanishedPlayers.stream().map(Object::toString).toList());
    }

    public boolean isVanished(UUID uuid) {
        return vanishedPlayers.contains(uuid);
    }

    public void enterVanish(Player player) {
        hideFromEveryone(player);
        player.showBossBar(vanishedBossBar);
    }

    public void leaveVanish(Player player) {
        showPlayerToEveryone(player);
        player.hideBossBar(vanishedBossBar);
    }

    public void hideVanishedPlayersFor(Player player) {
        vanishedPlayers.forEach(uuid -> {
            Player vanishedPlayer = Bukkit.getPlayer(uuid);
            if (vanishedPlayer == null || !vanishedPlayer.isOnline()) return;
            player.hidePlayer(this, vanishedPlayer);
        });
    }

    public void hideFromEveryone(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(this, player));
    }

    public void showPlayerToEveryone(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(this, player));
    }
}
