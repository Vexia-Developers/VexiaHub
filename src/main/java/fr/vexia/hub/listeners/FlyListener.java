package fr.vexia.hub.listeners;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.ArrayList;

public class FlyListener implements Listener {

    private ArrayList<Player> doubleJump = new ArrayList<>();

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        VexiaPlayer account = PlayerManager.get(player.getUniqueId());
        if (player.getGameMode() == GameMode.CREATIVE || player.isFlying())
            return;
        if (account.getRank().getId() >= Rank.VX_PLUS.getId()) {
            player.setFlying(false);
            player.setAllowFlight(false);
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 50, 50);
            player.setVelocity(player.getLocation().getDirection().multiply(3).setY(1.5));
            doubleJump.add(player);
        } else {
            player.setFlying(false);
            player.setAllowFlight(false);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().clone().add(0, -1, 0).getBlock();
        if (player.isFlying() || block == null || block.getType() == Material.AIR)
            return;
        if (doubleJump.contains(player)) {
            doubleJump.remove(player);
            player.setAllowFlight(true);
        }
    }


}
