package fr.vexia.hub.gui.host;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
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

    private final GUIManager guiManager;
    private final VexiaHostConfig config;
    private final ConfigHostGUI.ConfigType configType;


    public EditHostGUI(GUIManager guiManager, VexiaHostConfig config, ConfigHostGUI.ConfigType configType) {
        this.guiManager = guiManager;
        this.config = config;
        this.configType = configType;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
        if (configType.type == ConfigHostGUI.ValueType.BOOLEAN) {
            contents.set(0, 3, ClickableItem.of(new ItemBuilder(Material.BANNER).setBanner(DyeColor.RED).setName("§cDésactivé").toItemStack(), event -> {
                configType.executor.apply(config, false);
                contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
            }));
            contents.set(0, 5, ClickableItem.of(new ItemBuilder(Material.BANNER).setBanner(DyeColor.LIME).setName("§aActivé").toItemStack(), event -> {
                configType.executor.apply(config, true);
                contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
            }));
        } else if (configType.type == ConfigHostGUI.ValueType.INTEGER) {
            ItemStack builder1 = new ItemBuilder(Material.BANNER).setName("§c-10").setBanner(DyeColor.RED, new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.RED, PatternType.BORDER)).toItemStack();
            contents.set(0, 0, ClickableItem.of(builder1, event -> {
                int value = (int) configType.getter.apply(config);
                if (value - 10 >= 0) {
                    configType.executor.apply(config, value - 10);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder2 = new ItemBuilder(Material.BANNER).setName("§c-5").setBanner(DyeColor.ORANGE, new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.ORANGE, PatternType.BORDER)).toItemStack();
            contents.set(0, 1, ClickableItem.of(builder2, event -> {
                int value = (int) configType.getter.apply(config);
                if (value - 5 >= 0) {
                    configType.executor.apply(config, value - 5);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder3 = new ItemBuilder(Material.BANNER).setName("§c-1").setBanner(DyeColor.YELLOW, new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.YELLOW, PatternType.BORDER)).toItemStack();
            contents.set(0, 2, ClickableItem.of(builder3, event -> {
                int value = (int) configType.getter.apply(config);
                if (value - 1 >= 0) {
                    configType.executor.apply(config, value - 1);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder4 = new ItemBuilder(Material.BANNER).setName("§a+1").setBanner(DyeColor.CYAN, new Pattern(DyeColor.WHITE, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.CYAN, PatternType.BORDER),
                    new Pattern(DyeColor.CYAN, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP)).toItemStack();
            contents.set(0, 6, ClickableItem.of(builder4, event -> {
                int value = (int) configType.getter.apply(config);
                if (value + 1 <= 1500) {
                    configType.executor.apply(config, value + 1);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder5 = new ItemBuilder(Material.BANNER).setName("§a+5").setBanner(DyeColor.LIME, new Pattern(DyeColor.WHITE, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.LIME, PatternType.BORDER),
                    new Pattern(DyeColor.LIME, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.LIME, PatternType.STRIPE_TOP)).toItemStack();
            contents.set(0, 7, ClickableItem.of(builder5, event -> {
                int value = (int) configType.getter.apply(config);
                if (value + 5 <= 1500) {
                    configType.executor.apply(config, value + 5);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder6 = new ItemBuilder(Material.BANNER).setName("§a+10").setBanner(DyeColor.GREEN, new Pattern(DyeColor.WHITE, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.GREEN, PatternType.BORDER),
                    new Pattern(DyeColor.GREEN, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP)).toItemStack();
            contents.set(0, 8, ClickableItem.of(builder6, event -> {
                int value = (int) configType.getter.apply(config);
                if (value + 10 <= 1500) {
                    configType.executor.apply(config, value + 10);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
        } else if (configType.type == ConfigHostGUI.ValueType.FLOAT) {
            ItemStack builder1 = new ItemBuilder(Material.BANNER).setName("§c-1.0").setBanner(DyeColor.RED, new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.RED, PatternType.BORDER)).toItemStack();
            contents.set(0, 0, ClickableItem.of(builder1, event -> {
                float value = (float) configType.getter.apply(config);
                if (value - 1.0f >= 0) {
                    configType.executor.apply(config, value - 1.0f);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder2 = new ItemBuilder(Material.BANNER).setName("§c-0.5").setBanner(DyeColor.ORANGE, new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.ORANGE, PatternType.BORDER)).toItemStack();
            contents.set(0, 1, ClickableItem.of(builder2, event -> {
                float value = (float) configType.getter.apply(config);
                if (value - 0.5f >= 0) {
                    configType.executor.apply(config, value - 0.5f);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder3 = new ItemBuilder(Material.BANNER).setName("§c-0.1").setBanner(DyeColor.YELLOW, new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.YELLOW, PatternType.BORDER)).toItemStack();
            contents.set(0, 2, ClickableItem.of(builder3, event -> {
                float value = (float) configType.getter.apply(config);
                if (value - 0.1f >= 0) {
                    configType.executor.apply(config, value - 0.1f);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder4 = new ItemBuilder(Material.BANNER).setName("§a+0.1").setBanner(DyeColor.CYAN, new Pattern(DyeColor.WHITE, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.CYAN, PatternType.BORDER),
                    new Pattern(DyeColor.CYAN, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP)).toItemStack();
            contents.set(0, 6, ClickableItem.of(builder4, event -> {
                float value = (float) configType.getter.apply(config);
                if (value + 0.1f <= 1500) {
                    configType.executor.apply(config, value + 0.1f);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder5 = new ItemBuilder(Material.BANNER).setName("§a+0.5").setBanner(DyeColor.LIME, new Pattern(DyeColor.WHITE, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.LIME, PatternType.BORDER),
                    new Pattern(DyeColor.LIME, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.LIME, PatternType.STRIPE_TOP)).toItemStack();
            contents.set(0, 7, ClickableItem.of(builder5, event -> {
                float value = (float) configType.getter.apply(config);
                if (value + 0.5f <= 1500) {
                    configType.executor.apply(config, value + 0.5f);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
            ItemStack builder6 = new ItemBuilder(Material.BANNER).setName("§a+1.0").setBanner(DyeColor.GREEN, new Pattern(DyeColor.WHITE, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.GREEN, PatternType.BORDER),
                    new Pattern(DyeColor.GREEN, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP)).toItemStack();
            contents.set(0, 8, ClickableItem.of(builder6, event -> {
                float value = (float) configType.getter.apply(config);
                if (value + 1.0f <= 1500) {
                    configType.executor.apply(config, value + 1.0f);
                    contents.set(0, 4, ClickableItem.empty(configType.buildItem(config, false)));
                }
            }));
        }

        contents.fillRow(1, ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDyeColor(DyeColor.ORANGE)
                .toItemStack()));
        contents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.SLIME_BALL).setName("§aValider").toItemStack(), e -> back(player)));
    }

    public void back(Player player) {
        ConfigHostGUI.getGUI(guiManager, config, ConfigHostGUI.ConfigStatus.CREATE_CONFIG).open(player);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void selectConfig(Player player, InventoryClickEvent event) {

    }

    public void createServer(Player player, InventoryClickEvent event) {

    }


    public static SmartInventory getGUI(GUIManager manager, VexiaHostConfig config, ConfigHostGUI.ConfigType configType) {
        return SmartInventory.builder()
                .id("host_edit_menu")
                .provider(new EditHostGUI(manager, config, configType))
                .size(2, 9)
                .manager(manager.inventoryManager)
                .title(configType.name)
                .build();
    }

}
