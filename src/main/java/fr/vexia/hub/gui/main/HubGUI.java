package fr.vexia.hub.gui.main;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.vexia.core.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class HubGUI implements InventoryProvider {
    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 8, ClickableItem.of(new ItemBuilder(Material.ARROW)
                .setName("§cQuitter").toItemStack(), event -> player.closeInventory()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

}
