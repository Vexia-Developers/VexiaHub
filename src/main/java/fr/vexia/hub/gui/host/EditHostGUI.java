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
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class EditHostGUI implements InventoryProvider {

    private GUIManager guiManager;
    private VexiaHostConfig config;
    private ConfigHostGUI.ConfigType configType;


    public EditHostGUI(GUIManager guiManager, VexiaHostConfig config, ConfigHostGUI.ConfigType configType) {
        this.guiManager = guiManager;
        this.config = config;
        this.configType = configType;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
        if(configType.type == ConfigHostGUI.ValueType.BOOLEAN){
            contents.set(0, 3, ClickableItem.of(new ItemBuilder(Material.STANDING_BANNER).setDyeColor(DyeColor.RED).setName("§cDésactivé").toItemStack(), event -> {
                configType.executor.apply(config, false);
                contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
            }));
            contents.set(0, 5, ClickableItem.of(new ItemBuilder(Material.STANDING_BANNER).setDyeColor(DyeColor.RED).setName("§aActivé").toItemStack(), event -> {
                configType.executor.apply(config, true);
                contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
            }));
        }

        contents.fillRow(1, ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDyeColor(DyeColor.ORANGE)
                .toItemStack()));
        contents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.SLIME_BALL).setName("§aValider").toItemStack(), e -> back(player)));
    }

    public void back(Player player){

    }


    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void selectConfig(Player player, InventoryClickEvent event){

    }

    public void createServer(Player player, InventoryClickEvent event){

    }


    public static SmartInventory getGUI(GUIManager manager, VexiaHostConfig config, ConfigHostGUI.ConfigType configType){
        return SmartInventory.builder()
                .id("host_edit_menu")
                .provider(new EditHostGUI(manager, config, configType))
                .size(2, 9)
                .manager(manager.inventoryManager)
                .title("Hosts » "+configType.name)
                .build();
    }
}
