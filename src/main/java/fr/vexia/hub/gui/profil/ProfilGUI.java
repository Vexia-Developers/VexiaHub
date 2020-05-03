package fr.vexia.hub.gui.profil;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.hub.gui.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ProfilGUI implements InventoryProvider {

    private GUIManager guiManager;

    public ProfilGUI(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        contents.set(1, 2, ClickableItem.of(new ItemBuilder(Material.RAW_FISH, 1, (byte) 3)
                .setName("§6Ami(s)")
                .setLore("§7Gestion de vos ami(s)")
                .toItemStack(), event -> openFriends(player)));

        contents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.PAINTING, 1)
                .setName("§6Statistiques")
                .setLore("§7Voir vos statistiques", "§7dans les différents jeux")
                .toItemStack(), event -> openStats(player)));

        contents.set(1, 6, ClickableItem.of(new ItemBuilder(Material.NAME_TAG, 1)
                .setName("§6Team")
                .setLore("§7Gérer votre team")
                .toItemStack(), this::openTeam));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }

    private void openFriends(Player player) {
        guiManager.getFriendsMenu().open(player);
    }

    private void openStats(Player player) {
        guiManager.getStatsMenu().open(player);
    }

    private void openTeam(InventoryClickEvent event) {

    }
}
