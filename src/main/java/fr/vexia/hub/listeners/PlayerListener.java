package fr.vexia.hub.listeners;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.core.player.PlayerUtils;
import fr.vexia.core.scoreboard.FastBoard;
import fr.vexia.core.scoreboard.TeamsTagsManager;
import fr.vexia.hub.VexiaHub;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        VexiaHub hub = VexiaHub.getInstance();

        player.teleport(hub.getWorldManager().getSpawnLocation());
        PlayerUtils.reset(player);
        giveItem(player, player.getInventory());

        VexiaPlayer account = PlayerManager.get(player.getUniqueId());
        String teamName = account.getRank().getTab() + "/" + account.getName().substring(0, 5) + "-" + account.getUUID().toString().substring(0, 5);
        TeamsTagsManager team = new TeamsTagsManager(teamName,
                account.getRank().getColoredPrefix() + " ", null,
                Bukkit.getScoreboardManager().getMainScoreboard());
        team.set(player);

        VexiaHub.getInstance().getTags().put(player.getUniqueId(), team);

        FastBoard fastBoard = new FastBoard(player);
        fastBoard.updateTitle("§6§lVEXIA §r§lNETWORK");
        fastBoard.updateLines(
                " ",
                "§6Grade:",
                " " + account.getRank().getColor() + account.getRank().getPrefix(),
                " ",
                "§6VXCoins:",
                "    §e" + account.getCoins() + " ✪",
                "§6VXCrédits:",
                "    §b" + account.getCredits() + " ⛃",
                "",
                "§6Joueurs: §a" + VexiaHub.getInstance().getTotalOnline(),
                "§6Lobby: §d#" + Bukkit.getMotd().substring(3),
                "",
                "§eplay.vexia.fr   ");

        if (account.getRank().getId() >= Rank.VX.getId()) {
            player.setAllowFlight(true);
        }

        VexiaHub.getInstance().getBoards().put(player.getUniqueId(), fastBoard);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        VexiaHub hub = VexiaHub.getInstance();
        if (hub.getBoards().containsKey(player.getUniqueId())) {
            hub.getBoards().get(player.getUniqueId()).delete();
            hub.getBoards().remove(player.getUniqueId());
        }

        if (hub.getTags().containsKey(player.getUniqueId())) {
            TeamsTagsManager team = hub.getTags().get(player.getUniqueId());
            team.remove(player);
            team.removeTeam();
            hub.getTags().remove(player.getUniqueId());
        }

        player.setAllowFlight(false);
    }

    private void giveItem(Player player, PlayerInventory inventory) {
        inventory.clear();
        inventory.setItem(0, new ItemBuilder(Material.COMPASS).setName("§6Menu Principal §7(Clic-droit)")
                .setLore("§7Accéder au menu principal", "§7pour rejoindre les jeux").toItemStack());
        ItemStack skull = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(player.getName())
                .setName("§6Profil §7(Clic-droit)")
                .setLore("§7Voir ton profil avec tes", "§7informations et statistiques").toItemStack();
        inventory.setItem(4, CraftItemStack.asBukkitCopy(CraftItemStack.asNMSCopy(skull)));
        inventory.setItem(7, new ItemBuilder(Material.EMERALD).setName("§eBoutique §7(Clic-droit)")
                .setLore("§7Acheter des kits et", "§7des gadgets").toItemStack());
        inventory.setItem(8, new ItemBuilder(Material.REDSTONE_COMPARATOR).setName("§6Paramètres §7(Clic-droit)")
                .setLore("§7Activé ou Désactivé des options").toItemStack());
    }
}
