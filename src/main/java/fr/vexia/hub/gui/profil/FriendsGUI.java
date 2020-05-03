package fr.vexia.hub.gui.profil;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import fr.vexia.api.data.manager.FriendManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.core.player.PlayerUtils;
import fr.vexia.hub.VexiaHub;
import fr.vexia.hub.gui.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class FriendsGUI implements InventoryProvider {

    private final GUIManager guiManager;

    public FriendsGUI(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        List<VexiaPlayer> friends = FriendManager.getFriends(player.getUniqueId())
                .stream()
                .sorted((o1, o2) -> o1.getServer() != null ? 1 : 0)
                .collect(Collectors.toList());

        ClickableItem[] items = new ClickableItem[friends.size()];

        for (int i = 0; i < items.length; i++) {
            VexiaPlayer rushyPlayer = friends.get(i);
            ItemBuilder itemBuilder = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                    .setName("§6" + rushyPlayer.getName())
                    .setSkullOwner(rushyPlayer.getName());
            if (rushyPlayer.getServer() != null) {
                itemBuilder.setLore("§aConnecté §7(" + rushyPlayer.getServer() + ")", "§fCliques pour te connecter à son server");
                items[i] = ClickableItem.of(itemBuilder.toItemStack(),
                        event -> PlayerUtils.teleportServer(VexiaHub.getInstance(), player, rushyPlayer.getServer()));
            } else {
                itemBuilder.setLore("§cDéconnecté");
                items[i] = ClickableItem.empty(itemBuilder.toItemStack());
            }
        }


        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§6Page suivante").toItemStack(),
                e -> guiManager.getFriendsMenu().open(player, pagination.previous().getPage())));
        contents.set(2, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§6Page précédente").toItemStack(),
                e -> guiManager.getFriendsMenu().open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
