package fr.vexia.hub.gui.host;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.hub.gui.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class HostGUI implements InventoryProvider {

    private GUIManager guiManager;

    private static SmartInventory createHostGUI;
    private static SmartInventory listHostConfigGUI;

    public static final Material[] iconHostType = {Material.GOLDEN_APPLE, Material.LEATHER, Material.IRON_PICKAXE};

    public HostGUI(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        VexiaPlayer account = PlayerManager.get(player.getUniqueId());
        Pagination pagination = contents.pagination();
        //TODO AFFICHER LA LIST DES HOSTS

        contents.set(4, 0, ClickableItem.of(new ItemBuilder(Material.PAPER).setName("§aCréer un serveur host")
                .setLore("§7Vous pouvez créer un serveur" + "§7host gratuitement en personnalisant", "§7les règles de jeu").toItemStack(), e -> openHostCreateMenu(player, e)));
        contents.set(4, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§6Page suivante").toItemStack(),
                e -> guiManager.getHostMenu().open(player, pagination.previous().getPage())));
        contents.set(4, 4, ClickableItem.of(new ItemBuilder(Material.PAPER).setName("§6Mes configurations")
                .setLore("§7Vous pouvez editer et créé des", "§7configurations").toItemStack(), e -> openHostConfigMenu(player, e)));
        contents.set(4, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§6Page précédente").toItemStack(),
                e -> guiManager.getFriendsMenu().open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void openHostCreateMenu(Player player, InventoryClickEvent event){
        createHostGUI.open(player);
    }

    public void openHostConfigMenu(Player player, InventoryClickEvent event){
        listHostConfigGUI.open(player);
    }

    public static SmartInventory registerSmartInventorys(GUIManager manager, InventoryManager inventoryManager){
        createHostGUI = CreateHostGUI.getSmartInventory(manager, inventoryManager);
        listHostConfigGUI = ListHostConfigGUI.getSmartInventory(manager, inventoryManager);
        return SmartInventory.builder()
                .id("host_menu")
                .provider(new HostGUI(manager))
                .manager(inventoryManager)
                .size(4,9)
                .title("Hosts")
                .build();
    }
}
