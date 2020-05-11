package fr.vexia.hub.gui;

import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.vexia.api.servers.GameType;
import fr.vexia.hub.VexiaHub;
import fr.vexia.hub.gui.main.GameGUI;
import fr.vexia.hub.gui.host.HostGUI;
import fr.vexia.hub.gui.main.HubGUI;
import fr.vexia.hub.gui.main.MainGUI;
import fr.vexia.hub.gui.profil.FriendsGUI;
import fr.vexia.hub.gui.profil.ProfilGUI;
import fr.vexia.hub.gui.profil.StatsGUI;
import fr.vexia.hub.gui.settings.SettingsGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class GUIManager implements Listener {
    private final SmartInventory mainMenu;
    private final SmartInventory hubMenu;
    private final HashMap<GameType, SmartInventory> gameInventories;
    private final SmartInventory hostMenu;

    private final SmartInventory friendsMenu;
    private final SmartInventory profilMenu;
    private final SmartInventory statsMenu;

    private final SmartInventory settingsMenu;

    public GUIManager() {
        InventoryManager inventoryManager = new InventoryManager(VexiaHub.getInstance());
        inventoryManager.init();

        this.mainMenu = SmartInventory.builder()
                .id("main_menu")
                .provider(new MainGUI(this))
                .manager(inventoryManager)
                .size(5, 9)
                .title("Menu principal")
                .build();

        this.hubMenu = SmartInventory.builder()
                .id("hub_menu")
                .provider(new HubGUI())
                .manager(inventoryManager)
                .size(2, 9)
                .title("Hubs")
                .build();

        this.hostMenu = SmartInventory.builder()
                .id("host_menu")
                .provider(new HostGUI(this))
                .size(4,9)
                .title("Hosts")
                .build();

        this.profilMenu = SmartInventory.builder()
                .id("profil_menu")
                .provider(new ProfilGUI(this))
                .manager(inventoryManager)
                .size(3, 9)
                .title("Profil")
                .build();

        this.statsMenu = SmartInventory.builder()
                .id("stats_menu")
                .provider(new StatsGUI())
                .manager(inventoryManager)
                .size(3, 9)
                .title("Statistiques")
                .build();

        this.friendsMenu = SmartInventory.builder()
                .id("friends_menu")
                .provider(new FriendsGUI(this))
                .manager(inventoryManager)
                .size(3, 9)
                .title(ChatColor.GOLD + "Amis")
                .build();

        this.settingsMenu = SmartInventory.builder()
                .id("settings_menu")
                .provider(new SettingsGUI(this))
                .manager(inventoryManager)
                .size(6, 9)
                .title("Paramètres")
                .build();

        this.gameInventories = new HashMap<>();

        for (GameType gameType : GameType.values()) {
            SmartInventory gameInv = SmartInventory.builder()
                    .id(gameType.getName().toLowerCase() + "_menu")
                    .provider(new GameGUI(gameType))
                    .manager(inventoryManager)
                    .size(2, 9)
                    .title("Jeux > " + gameType.getName())
                    .build();
            gameInventories.put(gameType, gameInv);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.PHYSICAL || event.getItem() == null || event.getItem().getType() == Material.AIR
                || !event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasDisplayName())
            return;
        event.setCancelled(true);
        String name = event.getItem().getItemMeta().getDisplayName();
        switch (name) {
            case "§6Menu Principal §7(Clic-droit)":
                mainMenu.open(player);
                break;
            case "§6Profil §7(Clic-droit)":
                profilMenu.open(player);
                break;
            case "§6Paramètres §7(Clic-droit)":
                settingsMenu.open(player);
                break;
        }
    }

    public HashMap<GameType, SmartInventory> getGameInventories() {
        return gameInventories;
    }

    public SmartInventory getStatsMenu() {
        return statsMenu;
    }

    public SmartInventory getFriendsMenu() {
        return friendsMenu;
    }

    public SmartInventory getSettingsMenu() {
        return settingsMenu;
    }

    public SmartInventory getHubMenu() {
        return hubMenu;
    }

    public SmartInventory getHostMenu() {
        return hostMenu;
    }

}
