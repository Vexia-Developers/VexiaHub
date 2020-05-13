package fr.vexia.hub.gui.host;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.vexia.api.data.manager.HostManager;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.data.redis.pubsub.PubSubAPI;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.servers.hosts.VexiaHostConfig;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.hub.gui.GUIManager;
import org.bukkit.DyeColor;
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
                    .setLore(hostConfig.buildConfig()).toItemStack(), event -> selectConfig(player, hostConfig, event)));
        }
        contents.add(ClickableItem.of(new ItemBuilder(Material.COMMAND).setName("§aCréer un serveur")
                .setLore("§7sans configuration prédéfinie").toItemStack(), event -> createServer(player, event)));
        contents.fillRow(1, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDyeColor(DyeColor.ORANGE)
                .toItemStack(), event -> event.setCancelled(true)));
        contents.set(1, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§cRetour").toItemStack(),
                event -> guiManager.getHostMenu().open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void selectConfig(Player player, VexiaHostConfig config, InventoryClickEvent event){
        player.closeInventory();
        HostManager.pubCreateServer(config);
    }

    public void createServer(Player player, InventoryClickEvent event){
        VexiaHostConfig config = new VexiaHostConfig();
        config.setOwnerUUID(player.getUniqueId());
        config.setServerName("instant");
        ConfigHostGUI.getGUI(guiManager, config, ConfigHostGUI.ConfigStatus.SELECT_MODE).open(player);
    }


    public static SmartInventory getSmartInventory(GUIManager guiManager, InventoryManager inventoryManager){
        return SmartInventory.builder()
                .id("host_create_menu")
                .provider(new CreateHostGUI(guiManager))
                .size(2,9)
                .manager(inventoryManager)
                .title("Choisir la configuration")
                .build();
    }
}
