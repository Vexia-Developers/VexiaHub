package fr.vexia.hub.world;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldManager {

    private static final double[] SPAWN_LOCATION = new double[]{-862.5, 22.0, 885.5, 0, 0};
    private static final double[] JUMP_LOCATION = new double[]{-862.5, 90.0, 885.5, 0, 0};

    private final Location spawnLocation;
    private final Location jumpLocation;

    public WorldManager() {
        World world = Bukkit.getWorlds().get(0);
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doWeatherCycle", "false");

        this.spawnLocation = new Location(world, SPAWN_LOCATION[0], SPAWN_LOCATION[1], SPAWN_LOCATION[2], (float) SPAWN_LOCATION[3], (float) SPAWN_LOCATION[4]);
        this.jumpLocation = new Location(world, JUMP_LOCATION[0], JUMP_LOCATION[1], JUMP_LOCATION[2]);
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getJumpLocation() {
        return jumpLocation;
    }
}

