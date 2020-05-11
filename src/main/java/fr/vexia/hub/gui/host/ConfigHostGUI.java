package fr.vexia.hub.gui.host;

import com.sun.scenario.effect.light.SpotLight;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.vexia.api.data.manager.HostManager;
import fr.vexia.api.servers.hosts.HostGameType;
import fr.vexia.api.servers.hosts.VexiaHostConfig;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.hub.gui.GUIManager;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConfigHostGUI implements InventoryProvider {

    private GUIManager guiManager;
    private VexiaHostConfig config;
    private ConfigStatus status;

    public ConfigHostGUI(GUIManager manager, VexiaHostConfig config, ConfigStatus status){
        this.guiManager = manager;
        this.config = config;
        this.status = status;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        if(status == ConfigStatus.SELECT_MODE){
            contents.set(0,3, ClickableItem.of(new ItemBuilder(HostGUI.iconHostType[0]).setName("§6"+HostGameType.UHC.getName()).setLore(HostGameType.UHC.getDescription()).toItemStack(),
                    event -> selectMode(player, HostGameType.UHC, true)));
            contents.set(0,4, ClickableItem.of(new ItemBuilder(HostGUI.iconHostType[1]).setName("§6"+HostGameType.LGUHC.getName()).setLore(HostGameType.LGUHC.getDescription()).toItemStack(),
                    event -> selectMode(player, HostGameType.LGUHC, true)));
            contents.set(0,5, ClickableItem.of(new ItemBuilder(HostGUI.iconHostType[2]).setName("§6"+HostGameType.TaupeGunUHC.getName()).setLore(HostGameType.TaupeGunUHC.getDescription()).toItemStack(),
                    event -> selectMode(player, HostGameType.TaupeGunUHC, true)));
        }else{

        }
        if(status == ConfigStatus.EDIT_CONFIG){
            contents.set(3, 0, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName("§cSupprimer la configuration").setLore("§4/!\\ Cette action est irréversible !").toItemStack(),
                    event -> deleteConfig(player)));
        }
        if(status == ConfigStatus.EDIT_CONFIG || status == ConfigStatus.CREATE_CONFIG){
            contents.set(3, 4, ClickableItem.of(new ItemBuilder(Material.SLIME_BALL).setName("§aSauvegarder").setLore("§7Sauvegarder la configuration actuel").toItemStack(),
                    event -> saveConfig(player)));
        }
        contents.fillRow(3, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDyeColor(DyeColor.ORANGE)
                .toItemStack(), event -> event.setCancelled(true)));
        contents.set(3, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§cAnnuler").setLore("§4Les paramères ne seront pas sauvegarder").toItemStack(),
                event -> guiManager.getHostMenu().open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void saveConfig(Player player){
        HostManager.create(config);
        HostGUI.listHostConfigGUI.open(player);
    }

    public void deleteConfig(Player player){
        HostManager.delete(config.getId());
        HostGUI.listHostConfigGUI.open(player);
    }

    private void selectMode(Player player, HostGameType type, boolean next){
        config.setType(type);
        if(next){
            HostManager.create(config);
            getGUI(guiManager, config, ConfigStatus.CREATE_CONFIG);
        }
    }

    public static SmartInventory getGUI(GUIManager manager, VexiaHostConfig config, ConfigStatus status){
        return SmartInventory.builder()
                .id("host_config_menu")
                .provider(new ConfigHostGUI(manager, config, status))
                .size(3, 9)
                .manager(manager.inventoryManager)
                .title("Hosts » "+status.getTitle() + ((config.getId() != 0) ? " §7(#"+config.getId()+")" : ""))
                .build();
    }

    public enum ConfigStatus {
        SELECT_MODE("Choisir le mode de jeu"),
        CREATE_CONFIG("Choisir la configuration"),
        EDIT_CONFIG("Personnalisé la configuration");

        private String title;

        ConfigStatus(String title){
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

}
