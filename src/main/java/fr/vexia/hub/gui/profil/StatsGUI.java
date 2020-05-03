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
import org.bukkit.event.inventory.InventoryClickEvent;

public class StatsGUI implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRow(1, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(DyeColor.ORANGE).setName(" ")
                .toItemStack(), event -> event.setCancelled(true)));

        GameItems firstGame = GameItems.values()[0];
        contents.set(0, 0, ClickableItem.of(new ItemBuilder(firstGame.getMaterial())
                .setName("§6" + firstGame.getGameType().getName())
                .setLore(firstGame.getLores())
                .addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1)
                .toItemStack(), this::changeGame));

        Statistic statistic = StatsManager.getStatistic(player.getUniqueId(), firstGame.getGameType());
        StatsType[] statisticTypes = statistic.getGameType().getStatsTypes();
        for (int i = 0; i < statisticTypes.length; i++) {
            StatsType statisticType = statisticTypes[i];
            contents.set(2, i, getItem(statisticType, statistic.get(statisticType)));
        }
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

    private void changeGame(InventoryClickEvent event) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
