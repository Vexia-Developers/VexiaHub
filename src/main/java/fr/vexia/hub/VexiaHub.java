package fr.vexia.hub;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.servers.ServerType;
import fr.vexia.api.servers.VexiaServer;
import fr.vexia.core.VexiaPlugin;
import fr.vexia.core.scoreboard.FastBoard;
import fr.vexia.core.scoreboard.TeamsTagsManager;
import fr.vexia.hub.gui.GUIManager;
import fr.vexia.hub.listeners.FlyListener;
import fr.vexia.hub.listeners.PlayerListener;
import fr.vexia.hub.listeners.ProtectionListener;
import fr.vexia.hub.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.UUID;

public class VexiaHub extends VexiaPlugin {

    private static VexiaHub instance;

    private WorldManager worldManager;

    private final HashMap<UUID, FastBoard> boards = new HashMap<>();
    private final HashMap<UUID, TeamsTagsManager> tags = new HashMap<>();

    private int totalOnline = 0;

    @Override
    public VexiaServer onStart() {
        instance = this;

        this.worldManager = new WorldManager();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new FlyListener(), this);
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new ProtectionListener(), this);
        pluginManager.registerEvents(new GUIManager(), this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            totalOnline = PlayerManager.getOnlines().size();
            for (FastBoard fastBoard : boards.values()) {
                fastBoard.updateLine(9, "§6Joueurs: §a" + totalOnline);
            }
        }, 10 * 20L, 10 * 20L);

        return new VexiaServer(Bukkit.getMotd(), ServerType.LOBBY, null, null, Bukkit.getMaxPlayers(), Bukkit.getPort());
    }

    @Override
    public void onStop() {

    }

    public HashMap<UUID, FastBoard> getBoards() {
        return boards;
    }

    public HashMap<UUID, TeamsTagsManager> getTags() {
        return tags;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public int getTotalOnline() {
        return totalOnline;
    }

    public static VexiaHub getInstance() {
        return instance;
    }
}
