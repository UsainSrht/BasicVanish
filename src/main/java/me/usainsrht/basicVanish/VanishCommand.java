package me.usainsrht.basicVanish;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class VanishCommand implements BasicCommand {

    BasicVanish plugin;

    public VanishCommand(BasicVanish plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {
        if (commandSourceStack.getSender() instanceof Player player) {
            UUID uuid = player.getUniqueId();
            if (plugin.isVanished(uuid)) {
                plugin.vanishedPlayers.remove(uuid);
                plugin.leaveVanish(player);
            } else {
                plugin.vanishedPlayers.add(uuid);
                plugin.enterVanish(player);
            }
        } else commandSourceStack.getSender().sendMessage("only players");
    }

    @Override
    public Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return this.permission() == null || sender.hasPermission(this.permission());
    }

    @Override
    public @Nullable String permission() {
        return "basicvanish.use";
    }

}
