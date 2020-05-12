package fr.vexia.hub.gui.host;

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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfigHostGUI implements InventoryProvider {

    private GUIManager guiManager;
    private VexiaHostConfig config;
    private ConfigStatus status;
    private boolean saveDbOnFinish;

    public ConfigHostGUI(GUIManager manager, VexiaHostConfig config, ConfigStatus status, boolean saveDbOnFinish){
        this.guiManager = manager;
        this.config = config;
        this.status = status;
        this.saveDbOnFinish = saveDbOnFinish;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDyeColor(DyeColor.ORANGE)
                .toItemStack()));
        if(status == ConfigStatus.SELECT_MODE){
            contents.set(3,3, ClickableItem.of(new ItemBuilder(HostGUI.iconHostType[0]).setName("§6"+HostGameType.UHC.getName()).setLore(HostGameType.UHC.getDescription()).toItemStack(),
                    event -> selectMode(player, HostGameType.UHC, true)));
            contents.set(3,4, ClickableItem.of(new ItemBuilder(HostGUI.iconHostType[1]).setName("§6"+HostGameType.LGUHC.getName()).setLore(HostGameType.LGUHC.getDescription()).toItemStack(),
                    event -> selectMode(player, HostGameType.LGUHC, true)));
            contents.set(3,5, ClickableItem.of(new ItemBuilder(HostGUI.iconHostType[2]).setName("§6"+HostGameType.TaupeGunUHC.getName()).setLore(HostGameType.TaupeGunUHC.getDescription()).toItemStack(),
                    event -> selectMode(player, HostGameType.TaupeGunUHC, true)));
        }else{
            contents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.SKULL).setName("§eNombre de joueurs maximum")
                            .setLore("§7Paramétrez le nombre", "§7de joueurs maximum", "", "§e» §7Joueurs : §e"+config.getMaxPlayer(),"",
                                    "§a➥ Clic Gauche §7modifier la valeur", "§d➥ Clic Molette §7valeur par défaut").toItemStack(),
                    event -> editValue(player, "Nombre de joueurs maximum", config.getMaxPlayer(), event)));
        }
        if(status == ConfigStatus.EDIT_CONFIG){
            contents.set(3, 0, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName("§cSupprimer la configuration").setLore("§4/!\\ Cette action est irréversible !").toItemStack(),
                    event -> deleteConfig(player)));
        }
        if(status == ConfigStatus.EDIT_CONFIG || status == ConfigStatus.CREATE_CONFIG){
            contents.set(3, 4, ClickableItem.of(new ItemBuilder(Material.SLIME_BALL).setName("§aSauvegarder").setLore("§7Sauvegarder la configuration actuel").toItemStack(),
                    event -> saveConfig(player)));
        }
        contents.set(3, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§cAnnuler").toItemStack(),
                event -> guiManager.getHostMenu().open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void saveConfig(Player player){
        HostManager.create(config);
        HostGUI.listHostConfigGUI.open(player);
    }

    private void editValue(Player player, String name, int value, InventoryClickEvent event){

    }

    private void deleteConfig(Player player){
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
                .provider(new ConfigHostGUI(manager, config, status, true))
                .size(6, 9)
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
