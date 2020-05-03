package fr.vexia.hub.gui.main;

import fr.vexia.api.servers.GameType;
import fr.vexia.core.items.ItemBuilder;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum GameItems {

    RTF(Material.WOOL, GameType.RTF, "Vous êtes téléporté sur votre base dans une partie continue." +
            "Votre mission est de capturer le drapeau de l'équipe adverse afin de le déposer en dessous du votre. \n§a");

    private final Material material;
    private final GameType gameType;
    private final List<String> lores;

    GameItems(Material material, GameType gameType, String lores) {
        this.material = material;
        this.gameType = gameType;
        this.lores = Arrays.stream(String.join("\n", lores)
                .split("(?<=\\G.{30,}\\s)|\n"))
                .map(s -> "§7" + s).collect(Collectors.toList());
    }

    public Material getMaterial() {
        return material;
    }

    public GameType getGameType() {
        return gameType;
    }

    public List<String> getLores() {
        return lores;
    }

    public ItemBuilder getItem() {
        return new ItemBuilder(material).setName("§e" + gameType.getName()).setLore(lores);
    }

}
