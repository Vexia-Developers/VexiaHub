package fr.vexia.hub.gui.profil;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.vexia.api.data.manager.StatsManager;
import fr.vexia.api.stats.Statistic;
import fr.vexia.api.stats.StatsType;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.hub.gui.main.GameItems;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StatsGUI implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRow(1, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(DyeColor.ORANGE).setName(" ")
                .toItemStack(), event -> event.setCancelled(true)));

        GameItems[] gameItems = GameItems.values();

        for (int i = 0; i < gameItems.length; i++) {
            GameItems gameItem = gameItems[i];
            ItemBuilder itemBuilder = new ItemBuilder(gameItem.getMaterial())
                    .setName("§6" + gameItem.getGameType().getName())
                    .setLore(gameItem.getLores());
            if (i == 0) {
                itemBuilder.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
            }
            contents.set(0, i, ClickableItem.of(itemBuilder.toItemStack(), event -> changeGame(gameItem, player, contents)));
        }

        changeGame(gameItems[0], player, contents);
    }

    private ClickableItem getItem(StatsType statsType, int value) {
        ItemBuilder itemBuilder;
        switch (statsType) {
            case WINS:
                itemBuilder = new ItemBuilder(Material.SLIME_BALL);
                break;
            case LOOSES:
                itemBuilder = new ItemBuilder(Material.INK_SACK, 1, (byte) 1);
                break;
            case KILLS:
                itemBuilder = new ItemBuilder(Material.IRON_SWORD);
                break;
            case DEATHS:
                itemBuilder = new ItemBuilder(Material.BONE);
                break;
            default:
                itemBuilder = new ItemBuilder(Material.BOOK);
                break;
        }

        itemBuilder.setName("§6" + statsType.getName() + ": §e" + value);
        return ClickableItem.of(itemBuilder.toItemStack(), event -> event.setCancelled(true));
    }

    private void changeGame(GameItems gameItems, Player player, InventoryContents contents) {
        contents.fillRow(2, ClickableItem.empty(new ItemStack(Material.AIR)));
        
        for (ClickableItem[] row : contents.all()) {
            for (ClickableItem column : row) {
                if (column == null) {
                    continue;
                }

                ItemStack item = column.getItem();
                if (item.containsEnchantment(Enchantment.ARROW_INFINITE) && item.getType() != gameItems.getMaterial()) {
                    item.removeEnchantment(Enchantment.ARROW_INFINITE);
                } else if (item.getType() == gameItems.getMaterial()) {
                    item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                }
            }
        }

        Statistic statistic = StatsManager.getStatistic(player.getUniqueId(), gameItems.getGameType());
        StatsType[] statisticTypes = statistic.getGameType().getStatsTypes();
        for (int i = 0; i < statisticTypes.length; i++) {
            StatsType statisticType = statisticTypes[i];
            contents.set(2, i, getItem(statisticType, statistic.get(statisticType)));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
