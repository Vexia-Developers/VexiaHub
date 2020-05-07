package fr.vexia.hub.gui.settings;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.options.Option;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.hub.gui.GUIManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SettingsGUI implements InventoryProvider {

    private ItemStack[] stack = new ItemStack[]{new ItemStack(Material.WATCH), new ItemStack(Material.PAPER),
            new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.BOOK_AND_QUILL),
            new ItemStack(Material.SKULL_ITEM, 1, (byte) 3), new ItemStack(Material.MINECART),
            new ItemStack(Material.IRON_BARDING), new ItemStack(Material.BLAZE_POWDER),
            new ItemStack(Material.NETHER_STAR), new ItemStack(Material.CARROT_STICK)};

    private GUIManager guiManager;

    public SettingsGUI(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        VexiaPlayer vexiaPlayer = PlayerManager.get(player.getUniqueId());
        HashMap<Option, Option.OptionValue> options = vexiaPlayer.getOptions();

        ClickableItem clickableItem = ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 1)
                .setName(" ").toItemStack(), event -> event.setCancelled(true));
        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) continue;
            contents.fillRow(i, clickableItem);
        }

        for (int i = 0; i < Option.values().length; i++) {
            Option option = Option.values()[i];
            Option.OptionValue value = options.getOrDefault(option, Option.OptionValue.ON);

            ItemBuilder itemBuilder = new ItemBuilder(stack[i])
                    .setName(ChatColor.GOLD + option.getMessage())
                    .setLore(option.getDescription());
            itemBuilder.addLoreLine(" ");
            itemBuilder.addLoreLine("§6Etat: " + value.getFormatName());
            itemBuilder.addLoreLine(" ");
            itemBuilder.addLoreLine(ChatColor.GREEN + "➥ " + Option.OptionValue.ON.getFormatName() + "§7 avec Clic Gauche");
            if (option.isAllowFriends()) {
                itemBuilder.addLoreLine(ChatColor.LIGHT_PURPLE + "➥ " + Option.OptionValue.FRIENDS.getFormatName()
                        + "§7 avec Clic Molette");
            }
            itemBuilder.addLoreLine(ChatColor.RED + "➥ " + Option.OptionValue.OFF.getFormatName() + "§7 avec Clic Droit");

            contents.set(option.getCategory() * 2, option.getIndex(),
                    ClickableItem.of(itemBuilder.toItemStack(), event -> {
                        changeOption(event, option, vexiaPlayer);
                        guiManager.getSettingsMenu().open(player);
                    }));
        }
    }

    private void changeOption(InventoryClickEvent event, Option option, VexiaPlayer vexiaPlayer) {
        Option.OptionValue value = event.getClick() == ClickType.LEFT ? Option.OptionValue.ON :
                (event.getClick() == ClickType.MIDDLE ? Option.OptionValue.FRIENDS : Option.OptionValue.OFF);
        if ((value == Option.OptionValue.FRIENDS && !option.isAllowFriends()) || vexiaPlayer.getOption(option) == value) {
            return;
        }

        vexiaPlayer.getOptions().put(option, value);
        PlayerManager.save(vexiaPlayer);
        ((Player) event.getWhoClicked()).updateInventory();
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
