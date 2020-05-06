package fr.vexia.hub.gui.main;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.vexia.api.data.manager.ServerManager;
import fr.vexia.api.servers.GameType;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.hub.VexiaHub;
import fr.vexia.hub.gui.GUIManager;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainGUI implements InventoryProvider {

    private final GUIManager guiManager;

    public MainGUI(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRow(5, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDyeColor(DyeColor.ORANGE)
                .toItemStack(), event -> event.setCancelled(true)));

        contents.set(4, 0, ClickableItem.of(new ItemBuilder(Material.BED)
                .setName("§6§lSpawn")
                .toItemStack(), event -> player.teleport(VexiaHub.getInstance().getWorldManager().getSpawnLocation())));

        contents.set(4, 4, ClickableItem.of(new ItemBuilder(Material.COMMAND)
                .setName("§6Hosts")
                .toItemStack(), event -> openHostMenu(player)));

        contents.set(4, 8, ClickableItem.of(new ItemBuilder(Material.FEATHER)
                .setName("§6Jump")
                .toItemStack(), event -> player.teleport(VexiaHub.getInstance().getWorldManager().getJumpLocation())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        contents.set(1, 4, ClickableItem.of(getGameItem(GameItems.ISLANDFLAG), event -> openGameMenu(player, GameType.ISLANDFLAG)));
        contents.set(1, 4, ClickableItem.of(getGameItem(GameItems.ISLANDFLAG), event -> openGameMenu(player, GameType.ISLANDFLAG)));
    }

    private void openGameMenu(Player player, GameType gameType) {
        guiManager.getGameInventories().get(gameType).open(player);
    }

    private void openHostMenu(Player player) {
        player.sendMessage("§cCette fonctionnalité n'est pas disponible pour le moment..");
    }

    private ItemStack getGameItem(GameItems gameItems) {
        ItemBuilder itemBuilder = gameItems.getItem();
        int playersPlaying = ServerManager.getOnlines(gameItems.getGameType());
        itemBuilder.addLoreLine("§eIl y a §6" + playersPlaying + " §ejoueur" + (playersPlaying > 1 ? "s" : "") + " dans ce mode.");
        return itemBuilder.toItemStack();
    }
}