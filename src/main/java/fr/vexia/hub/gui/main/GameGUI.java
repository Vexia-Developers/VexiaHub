package fr.vexia.hub.gui.main;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.vexia.api.data.manager.ServerManager;
import fr.vexia.api.servers.GameType;
import fr.vexia.api.servers.VexiaServer;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.core.player.PlayerUtils;
import fr.vexia.hub.VexiaHub;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GameGUI implements InventoryProvider {

    private final GameType gameType;
    public GameGUI(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 8, ClickableItem.of(new ItemBuilder(Material.ARROW)
                .setName("§cQuitter").toItemStack(), event -> player.closeInventory()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        List<VexiaServer> servers = ServerManager.getServers(gameType);
        GameItems gameItems = GameItems.valueOf(gameType.name());
        for (int i = 0; i < servers.size(); i++) {
            VexiaServer server = servers.get(i);
            contents.set(0, i, ClickableItem.of(getItem(gameItems.getMaterial(), server),
                    event -> teleportServer(player, server)));
        }
    }

    private void teleportServer(Player player, VexiaServer server) {
        PlayerUtils.teleportServer(VexiaHub.getInstance(), player, server.getName());
    }

    private ItemStack getItem(Material material, VexiaServer server) {
        return new ItemBuilder(material).setName("§6" + server.getName()).setLore("§7Joueurs: " + server.getOnline() + "/" + server.getMax()).toItemStack();
    }
}
