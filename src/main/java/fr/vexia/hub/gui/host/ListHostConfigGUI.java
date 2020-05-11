package fr.vexia.hub.gui.host;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.vexia.api.data.manager.HostManager;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.servers.hosts.VexiaHostConfig;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.hub.gui.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ListHostConfigGUI implements InventoryProvider {

    private GUIManager guiManager;

    public ListHostConfigGUI(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void selectConfig(Player player, InventoryClickEvent event) {

    }

    public void createServer(Player player, InventoryClickEvent event) {

    }


    public static SmartInventory getSmartInventory(GUIManager guiManager, InventoryManager inventoryManager) {
        return SmartInventory.builder()
                .id("host_list_config_menu")
                .provider(new CreateHostGUI(guiManager))
                .size(3, 9)
                .manager(inventoryManager)
                .title("Hosts » Mes configurations")
                .build();
    }
}
