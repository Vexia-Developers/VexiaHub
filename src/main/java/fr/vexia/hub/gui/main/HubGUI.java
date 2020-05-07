package fr.vexia.hub.gui.main;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.vexia.api.data.manager.ServerManager;
import fr.vexia.api.servers.VexiaServer;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.core.player.PlayerUtils;
import fr.vexia.hub.VexiaHub;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HubGUI implements InventoryProvider {
    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 8, ClickableItem.of(new ItemBuilder(Material.ARROW)
                .setName("§cQuitter").toItemStack(), event -> player.closeInventory()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        VexiaServer hub01 = ServerManager.get("hub01");
        VexiaServer hub02 = ServerManager.get("hub02");
        contents.set(0, 0, ClickableItem.of(getItem(hub01, "hub01"), event -> PlayerUtils.teleportServer(VexiaHub.getInstance(), player, hub01.getName())));
        contents.set(0, 1, ClickableItem.of(getItem(hub02, "hub02"), event -> PlayerUtils.teleportServer(VexiaHub.getInstance(), player, hub02.getName())));
    }

    private ItemStack getItem(VexiaServer server, String name) {
        return new ItemBuilder(Material.STAINED_CLAY)
                .setDyeColor(server != null ? DyeColor.LIME : DyeColor.RED)
                .setName("§6" + name)
                .setLore(" ", server == null ? "§cEteint" : "§7Il y a §6" + server.getOnline() + " §7joueur(s) dans ce hub")
                .toItemStack();

    }

}
