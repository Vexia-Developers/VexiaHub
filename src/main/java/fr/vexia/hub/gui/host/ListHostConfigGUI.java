package fr.vexia.hub.gui.host;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.vexia.api.data.manager.HostManager;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.servers.VexiaServer;
import fr.vexia.api.servers.hosts.VexiaHostConfig;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.hub.gui.GUIManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.DyeColor;
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
        List<VexiaHostConfig> configs = HostManager.getConfigsByOwner(player.getUniqueId());
        for (int i = 0; i < Math.min(configs.size(), 2 * 9); i++) {
            VexiaHostConfig config = configs.get(i);
            contents.add(ClickableItem.of(new ItemBuilder(HostGUI.iconHostType[config.getType().ordinal()]).setName("§6"+config.getType().getName()+ " #"+config.getId())
                            .setLore(config.buildConfig()).toItemStack(),
                    event -> selectConfig(player, config)));
        }
        contents.add(ClickableItem.of(new ItemBuilder(Material.COMMAND).setName("§aCréer une configuration").setLore("§7Prépare ta configuration", "§7et sauvegarde là").toItemStack(),
                event -> createConfig(player, configs.size())));
        contents.fillRow(2, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDyeColor(DyeColor.ORANGE)
                .toItemStack(), event -> event.setCancelled(true)));
        contents.set(2, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§cRetour").toItemStack(),
                event -> guiManager.getHostMenu().open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void createConfig(Player player, int sizeConfigs){
        if(sizeConfigs >= 8){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cVous ne pouvez pas créer plus de configuration"));
        }else {
            ConfigHostGUI.getGUI(guiManager, new VexiaHostConfig(), ConfigHostGUI.ConfigStatus.SELECT_MODE).open(player);
        }
    }

    public void selectConfig(Player player, VexiaHostConfig config) {
        ConfigHostGUI.getGUI(guiManager, config, ConfigHostGUI.ConfigStatus.EDIT_CONFIG).open(player);
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
