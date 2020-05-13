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
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
        }else if(configType.type == ConfigHostGUI.ValueType.INTEGER){
            ItemStack builder1 = new ItemBuilder(Material.STANDING_BANNER, 1, (short) 14).setName("§c-3").addPatterns(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.RED, PatternType.BORDER)).toItemStack();
            contents.set(0, 0, ClickableItem.of(builder1, event -> {
                int value = (int) configType.getter.apply(config);
                if(value - 3 >= 0){
                    configType.executor.apply(config, value - 3);
                    contents.set(0, 0, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder2 = new ItemBuilder(Material.BANNER, 1, (short) 1).setName("§c-2").addPatterns(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.ORANGE, PatternType.BORDER)).toItemStack();
            contents.set(0, 1, ClickableItem.of(builder2, event -> {
                int value = (int) configType.getter.apply(config);
                if(value - 2 >= 0){
                    configType.executor.apply(config, value - 2);
                    contents.set(0, 1, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder3 = new ItemBuilder(Material.BANNER, 1, (short) 4).setName("§c-1").addPatterns(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.YELLOW, PatternType.BORDER)).toItemStack();
            contents.set(0, 2, ClickableItem.of(builder3, event -> {
                int value = (int) configType.getter.apply(config);
                if(value - 1 >= 0){
                    configType.executor.apply(config, value - 1);
                    contents.set(0, 2, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder4 = new ItemBuilder(Material.BANNER, 1, (short) 9).setName("§a+1").addPatterns(new Pattern(DyeColor.WHITE, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.CYAN, PatternType.BORDER),
                    new Pattern(DyeColor.CYAN, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP)).toItemStack();
            contents.set(0, 0, ClickableItem.of(builder4, event -> {
                int value = (int) configType.getter.apply(config);
                if(value + 1 <= 120){
                    configType.executor.apply(config, value + 1);
                    contents.set(0, 6, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder5 = new ItemBuilder(Material.BANNER, 1, (short) 5).setName("§a+2").addPatterns(new Pattern(DyeColor.WHITE, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.LIME, PatternType.BORDER),
                    new Pattern(DyeColor.LIME, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.LIME, PatternType.STRIPE_TOP)).toItemStack();
            contents.set(0, 0, ClickableItem.of(builder5, event -> {
                int value = (int) configType.getter.apply(config);
                if(value + 2 <= 120){
                    configType.executor.apply(config, value + 2);
                    contents.set(0, 7, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder6 = new ItemBuilder(Material.BANNER, 1, (short) 13).setName("§a+3").addPatterns(new Pattern(DyeColor.WHITE, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.GREEN, PatternType.BORDER),
                    new Pattern(DyeColor.GREEN, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP)).toItemStack();
            contents.set(0, 0, ClickableItem.of(builder6, event -> {
                int value = (int) configType.getter.apply(config);
                if(value + 3 <= 120){
                    configType.executor.apply(config, value + 3);
                    contents.set(0, 8, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
        }

        contents.fillRow(1, ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDyeColor(DyeColor.ORANGE)
                .toItemStack()));
        contents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.SLIME_BALL).setName("§aValider").toItemStack(), e -> back(player)));
    }

    public void back(Player player){
        ConfigHostGUI.getGUI(guiManager, config, ConfigHostGUI.ConfigStatus.CREATE_CONFIG).open(player);
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
