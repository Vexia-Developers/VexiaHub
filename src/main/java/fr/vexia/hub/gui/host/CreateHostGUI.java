package fr.vexia.hub.gui.host;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.vexia.api.data.manager.HostManager;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.servers.hosts.VexiaHostConfig;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.hub.gui.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class CreateHostGUI implements InventoryProvider {

    private GUIManager guiManager;

    public CreateHostGUI(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        VexiaPlayer account = PlayerManager.get(player.getUniqueId());
        List<VexiaHostConfig> hostConfigs = HostManager.getConfigsByOwner(player.getUniqueId());
        for (VexiaHostConfig hostConfig : hostConfigs) {
            contents.add(ClickableItem.of(new ItemBuilder(HostGUI.iconHostType[hostConfig.getType().ordinal()])
                    .setName("§6"+hostConfig.getType().getName()+ " #" + hostConfig.getId())
                    .setLore(hostConfig.buildConfig()).toItemStack(), event -> selectConfig(player, event)));
        }
        contents.add(ClickableItem.of(new ItemBuilder(Material.COMMAND).setName("§aCréer un serveur")
                .setLore("§7sans configuration prédéfinie").toItemStack(), event -> createServer(player, event)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void selectConfig(Player player, InventoryClickEvent event){

    }

    public void createServer(Player player, InventoryClickEvent event){

    }


    public static SmartInventory getSmartInventory(GUIManager manager){
        return SmartInventory.builder()
                .id("host_create_menu")
                .provider(new CreateHostGUI(manager))
                .size(4,9)
                .title("Hosts > Création d'un serveur")
                .build();
    }
}
