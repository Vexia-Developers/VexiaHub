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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            contents.set(0,4, ClickableItem.of(new ItemBuilder(HostGUI.iconHostType[config.getType().ordinal()]).setName("§6"+config.getType().getName())
                            .setLore(config.getType().getDescription()).addLoreLine(" ").addLoreLine("§a➥ Clic gauche §7changer de mode de jeu").toItemStack(),
                    event -> getGUI(guiManager, config, ConfigStatus.SELECT_MODE)));
            for (ConfigType value : ConfigType.values()) {
                contents.set(value.position / 8, value.position % 8, ClickableItem.of(value.buildItem(config, true),
                        event -> editValue(player, value, event)));
            }
        }
        if(status == ConfigStatus.EDIT_CONFIG){
            contents.set(5, 0, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName("§cSupprimer la configuration").setLore("§7Cette action est irréversible !").toItemStack(),
                    event -> deleteConfig(player)));
        }
        if(status == ConfigStatus.EDIT_CONFIG || status == ConfigStatus.CREATE_CONFIG){
            contents.set(5, 4, ClickableItem.of(new ItemBuilder(Material.SLIME_BALL).setName("§aSauvegarder").setLore("§7Sauvegarder la configuration actuel").toItemStack(),
                    event -> saveConfig(player)));
        }
        contents.set(5, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§cAnnuler").toItemStack(),
                event -> guiManager.getHostMenu().open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void saveConfig(Player player){
        HostManager.create(config);
        HostGUI.listHostConfigGUI.open(player);
    }

    private void editValue(Player player, ConfigType type, InventoryClickEvent event){
        if(event.getClick() == ClickType.MIDDLE){
            type.executor.apply(config, type.getter.apply(new VexiaHostConfig()));
            event.getInventory().setItem(event.getSlot(), type.buildItem(config, true));
            player.updateInventory();
        }else{
            player.sendMessage("EDIT "+type.name);
        }
    }

    private void deleteConfig(Player player){
        HostManager.delete(config.getId());
        HostGUI.listHostConfigGUI.open(player);
    }

    private void selectMode(Player player, HostGameType type, boolean next){
        config.setType(type);
        if(next){
            getGUI(guiManager, config, ConfigStatus.CREATE_CONFIG).open(player);
        }
    }

    public static SmartInventory getGUI(GUIManager manager, VexiaHostConfig config, ConfigStatus status){
        if(status == ConfigStatus.CREATE_CONFIG && config.getId() != 0) status = ConfigStatus.EDIT_CONFIG;
        return SmartInventory.builder()
                .id("host_config_menu")
                .provider(new ConfigHostGUI(manager, config, status, true))
                .size(6, 9)
                .manager(manager.inventoryManager)
                .title(status.getTitle() + ((config.getId() != 0) ? " §7(#"+config.getId()+")" : ""))
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

    public enum ValueType {
        BOOLEAN(is -> ((boolean)is) ? "§aActivé" : "§cDésactivé"),
        STRING(null),
        INTEGER(null),
        FLOAT(null);

        private Function<Object, String> format;

        ValueType(Function<Object, String> format){
            this.format = format;
        }

        public Function<Object, String> getFormat() {
            return (format != null) ? format : Object::toString;
        }
    }

    public enum ConfigType {
        PLAYER_SIZE("Nombre de joueurs maximum", new String[]{"Paramétrez le nombre", "de joueurs maximum"},
                new ItemStack(Material.SKULL, 1, (short) 3), (8)+2, "Joueurs", VexiaHostConfig::getMaxPlayer, (hostConfig, value) -> hostConfig.setMaxPlayer((int)value), ValueType.INTEGER),
        TEAMS("Nombre de joueurs par team", new String[]{"Choisir le nombre de", "joueurs maximum dans chaque", "teams"},
                new ItemStack(Material.STANDING_BANNER, 1, (short) 10), (8)+4, "Equipes", VexiaHostConfig::getTeams,
                (hostConfig, value) -> hostConfig.setTeams((int)value), ValueType.INTEGER),
        BORDER_SIZE("Taille des bordure", new String[]{"Définir la taille des", "bordure au début de la partie"},
                new ItemStack(Material.STAINED_GLASS, 1, (byte)5), (8*2)+1, "Taille (en blocks)", VexiaHostConfig::getBorderSize,
                (hostConfig, value) -> hostConfig.setBorderSize((int)value), ValueType.INTEGER),
        BORDER_END_SIZE("Taille des bordure de fin de partie", new String[]{"Définir la taille des", "bordure a la fin de la partie", "de la partie"},
                new ItemStack(Material.STAINED_GLASS, 1, (byte)14), (8*2)+2, "Taille (en blocks)", VexiaHostConfig::getBorderEndSize,
                (hostConfig, value) -> hostConfig.setBorderEndSize((int)value), ValueType.INTEGER),
        BORDER_SPEED("Vitesse des bordure", new String[]{"Définir la vitesse de", "déplacement des bordures"},
                new ItemStack(Material.WATCH), (8*2)+3, "Vitesse (en blocks/s)", VexiaHostConfig::getBorderSpeed,
                (hostConfig, value) -> hostConfig.setBorderSpeed((int)value), ValueType.FLOAT),
        BORDER_REDUCE("Temps avant les borudre", new String[]{"Définir un temps avant", "que les bordures commence", "à rédure"},
                new ItemStack(Material.IRON_FENCE), (8*2)+5, "Temps (en minutes)", VexiaHostConfig::getBorderReduce,
                (hostConfig, value) -> hostConfig.setBorderReduce((int)value), ValueType.INTEGER),
        TIME_BEFORE_PVP("Temps avant le PvP", new String[]{"Définir un temps avant", "l'activation du PvP"},
                new ItemStack(Material.DIAMOND_SWORD), (8*2)+6, "Temps (en minutes)", VexiaHostConfig::getTimeBeforePVP,
                (hostConfig, value) -> hostConfig.setTimeBeforePVP((int)value), ValueType.INTEGER),
        NETHER("Nether" , new String[]{"Définir la présence du", "nether ou non"},
                new ItemStack(Material.NETHERRACK), (8*3)+4, "Status", VexiaHostConfig::isNether,
                (hostConfig, value) -> hostConfig.setNether((boolean) value), ValueType.BOOLEAN);

        String name;
        private String[] description;
        private ItemStack stack;
        private int position;
        private String value;
        ConfigGetter getter;
        ConfigExecutor executor;
        ValueType type;


        ConfigType(String name, String[] description, ItemStack stack, int position, String value, ConfigGetter getter, ConfigExecutor executor, ValueType type){
            this.name = name;
            this.description = description;
            this.stack = stack;
            this.position = position;
            this.value = value;
            this.getter = getter;
            this.executor = executor;
            this.type = type;
        }


        public ItemStack buildItem(VexiaHostConfig config, boolean withInstructions) {
            List<String> formatDescription = Arrays.stream(description).map((value) -> "§7" + value).collect(Collectors.toList());
            ItemBuilder builder = new ItemBuilder(stack).setName("§6"+name)
                    .setLore(formatDescription).addLoreLine("").addLoreLine("§e➲ §7"+value+": §e"+type.getFormat().apply(getter.apply(config)));
            if(withInstructions){
                builder.addLoreLine(" ");
                builder.addLoreLine("§a➥ Clic Gauche §7modifer la valeur");
                builder.addLoreLine("§d➥ Clic Molette §7valeur par défaut");
            }
            if(type == ValueType.INTEGER){
                ItemStack stack = builder.toItemStack();
                stack.setAmount((int)getter.apply(config));
                builder = new ItemBuilder(stack);
            }
            return builder.toItemStack();
        }


    }

    @FunctionalInterface
    interface ConfigGetter {
        public Object apply(VexiaHostConfig hostConfig);
    }

    @FunctionalInterface
    interface ConfigExecutor {
        public void apply(VexiaHostConfig hostConfig, Object value);
    }

}
