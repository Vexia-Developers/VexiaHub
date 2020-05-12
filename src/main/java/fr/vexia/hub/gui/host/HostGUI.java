package fr.vexia.hub.gui.host;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.vexia.api.data.manager.HostManager;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.data.manager.ServerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.servers.GameType;
import fr.vexia.api.servers.VexiaServer;
import fr.vexia.api.servers.hosts.VexiaHostConfig;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.core.player.PlayerUtils;
import fr.vexia.hub.VexiaHub;
import fr.vexia.hub.gui.GUIManager;
import fr.vexia.hub.gui.main.GameItems;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HostGUI implements InventoryProvider {

    private GUIManager guiManager;

    public static SmartInventory createHostGUI;
    public static SmartInventory listHostConfigGUI;

    public static final Material[] iconHostType = {Material.GOLDEN_APPLE, Material.LEATHER, Material.IRON_PICKAXE};

    public HostGUI(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRow(3, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDyeColor(DyeColor.ORANGE)
                .toItemStack(), event -> event.setCancelled(true)));

        contents.set(3, 0, ClickableItem.of(new ItemBuilder(Material.COMMAND).setName("§aCréer un serveur host")
                .setLore("§7Vous pouvez créer un serveur", "§7host gratuitement en personnalisant", "§7les règles de jeu").toItemStack(), e -> openHostCreateMenu(player, e)));
        contents.set(3, 4, ClickableItem.of(new ItemBuilder(Material.PAPER).setName("§6Mes configurations")
                .setLore("§7Vous pouvez editer et créé des", "§7configurations").toItemStack(), e -> openHostConfigMenu(player, e)));
        contents.set(3, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§cRetour").toItemStack(),
                event -> guiManager.getMainMenu().open(player)));
        update(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        List<VexiaServer> servers = ServerManager.getServers(GameType.UHC);
        List<VexiaHostConfig> hosts = HostManager.getRedisHosts();
        contents.fillRect(0,0, 2,8, ClickableItem.empty(new ItemStack(Material.AIR)));
        for (int i = 0; i < Math.min(hosts.size(), 3 * 9); i++) {
            VexiaHostConfig config = hosts.get(i);
            VexiaServer server = servers.stream().filter(vexiaServer -> vexiaServer.getName().equals(config.getServerName())).findFirst().orElse(null);
            VexiaPlayer target = PlayerManager.get(config.getOwnerUUID());
            if(server != null){
                contents.add(ClickableItem.of(new ItemBuilder(iconHostType[config.getType().ordinal()]).setName("§6"+config.getType().getName()+ " #"+config.getId())
                                .setLore(config.buildConfig()).addLoreLine(" ").addLoreLine("§7Owner : §2" + target.getName()).addLoreLine("§3Il y a "+server.getOnline()+" joueur(s)").toItemStack(),
                        event -> teleportServer(player, server)));
            }
        }
    }

    private void teleportServer(Player player, VexiaServer server) {
        PlayerUtils.teleportServer(VexiaHub.getInstance(), player, server.getName());
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
