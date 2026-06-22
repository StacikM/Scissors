package io.papermc.paper.command;

import io.papermc.paper.gui.Gui;
import io.papermc.paper.gui.GuiItem;
import io.papermc.paper.gui.PaginatedGui;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

@DefaultQualifier(NonNull.class)
public final class ControlPanelCommand extends Command {

    public ControlPanelCommand(final String name) {
        super(name);
        this.description = "Open the server control panel";
        this.usageMessage = "/controlpanel";
        this.setPermission("bukkit.command.controlpanel");
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!testPermission(sender)) return true;
        if (!(sender instanceof final Player player)) {
            sender.sendMessage(text("Only players can use this command.", RED));
            return true;
        }
        openMainMenu(player);
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args, final Location location) {
        return Collections.emptyList();
    }

    // -------------------------------------------------------------------------
    // Main menu
    // -------------------------------------------------------------------------

    private static void openMainMenu(final Player player) {
        final Gui gui = Gui.builder()
            .title(text("Control Panel", DARK_GRAY))
            .rows(3)
            .build();

        gui.fill(pane(Material.GRAY_STAINED_GLASS_PANE));

        gui.slot(11, named(Material.RED_CONCRETE, text("Power Options", RED),
            text("Shutdown / Restart", GRAY)),
            click -> openPowerMenu((Player) click.player()));

        gui.slot(13, named(Material.IRON_SWORD, text("Moderation", GOLD),
            text("Manage online players", GRAY)),
            click -> openPlayerList((Player) click.player()));

        gui.slot(15, named(Material.GRASS_BLOCK, text("World Controls", GREEN),
            text("Time, weather & more", GRAY)),
            click -> openWorldControls((Player) click.player()));

        gui.open(player);
    }

    // -------------------------------------------------------------------------
    // Power menu
    // -------------------------------------------------------------------------

    private static void openPowerMenu(final Player player) {
        final Gui gui = Gui.builder()
            .title(text("Power Options", RED))
            .rows(3)
            .build();

        gui.fill(pane(Material.RED_STAINED_GLASS_PANE));

        gui.slot(11, named(Material.BARRIER, text("Shutdown", RED),
            text("Stop the server immediately.", GRAY),
            text("Click to confirm.", DARK_RED)),
            click -> {
                click.gui().close(click.player());
                Bukkit.getServer().shutdown();
            });

        gui.slot(15, named(Material.ORANGE_CONCRETE, text("Restart", GOLD),
            text("Restart the server.", GRAY),
            text("Click to confirm.", DARK_GREEN)),
            click -> {
                click.gui().close(click.player());
                Bukkit.getServer().spigot().restart();
            });

        gui.slot(22, back(), click -> openMainMenu((Player) click.player()));

        gui.open(player);
    }

    // -------------------------------------------------------------------------
    // Player list (paginated)
    // -------------------------------------------------------------------------

    private static void openPlayerList(final Player viewer) {
        final PaginatedGui gui = PaginatedGui.builder()
            .title(text("Moderation", DARK_RED))
            .rows(4)
            .fillItem(pane(Material.GRAY_STAINED_GLASS_PANE))
            .build();

        for (final Player target : Bukkit.getOnlinePlayers()) {
            gui.addItem(playerHead(target), click -> openPlayerActions((Player) click.player(), target));
        }

        // Back button in the centre of the chrome row (slot 31 in a 4-row GUI)
        gui.slot((4 - 1) * 9 + 4, back(), click -> openMainMenu((Player) click.player()));

        gui.open(viewer);
    }

    // -------------------------------------------------------------------------
    // Per-player action menu
    // -------------------------------------------------------------------------

    private static void openPlayerActions(final Player viewer, final Player target) {
        final Gui gui = Gui.builder()
            .title(text("Actions: " + target.getName(), DARK_RED))
            .rows(3)
            .build();

        gui.fill(pane(Material.RED_STAINED_GLASS_PANE));

        gui.slot(10, named(Material.LEATHER_BOOTS, text("Kick", GOLD),
            text("Kick " + target.getName() + " from the server.", GRAY)),
            click -> {
                if (!target.isOnline()) {
                    ((Player) click.player()).sendMessage(text(target.getName() + " is no longer online.", RED));
                    openPlayerList((Player) click.player());
                    return;
                }
                target.kick(text("You have been kicked by an operator.", YELLOW));
                ((Player) click.player()).sendMessage(text("Kicked " + target.getName() + ".", YELLOW));
                openPlayerList((Player) click.player());
            });

        gui.slot(12, named(Material.BARRIER, text("Ban", RED),
            text("Permanently ban " + target.getName() + ".", GRAY),
            text("This cannot be undone here!", DARK_RED)),
            click -> {
                if (!target.isOnline()) {
                    ((Player) click.player()).sendMessage(text(target.getName() + " is no longer online.", RED));
                    openPlayerList((Player) click.player());
                    return;
                }
                Bukkit.getBanList(BanList.Type.NAME)
                    .addBan(target.getName(), "Banned by an operator.", null, null);
                target.kick(text("You have been banned.", RED));
                ((Player) click.player()).sendMessage(text("Banned " + target.getName() + ".", RED));
                openPlayerList((Player) click.player());
            });

        gui.slot(14, named(Material.CHEST, text("Clear Inventory", YELLOW),
            text("Clear " + target.getName() + "'s inventory.", GRAY)),
            click -> {
                target.getInventory().clear();
                ((Player) click.player()).sendMessage(text("Cleared " + target.getName() + "'s inventory.", YELLOW));
            });

        gui.slot(16, named(Material.ENDER_CHEST, text("View Inventory", AQUA),
            text("Open " + target.getName() + "'s inventory.", GRAY)),
            click -> {
                click.gui().close(click.player());
                ((Player) click.player()).openInventory(target.getInventory());
            });

        gui.slot(22, back(), click -> openPlayerList((Player) click.player()));

        gui.open(viewer);
    }

    // -------------------------------------------------------------------------
    // World controls
    // -------------------------------------------------------------------------

    private static void openWorldControls(final Player player) {
        final World world = player.getWorld();

        final Gui gui = Gui.builder()
            .title(text("World: " + world.getName(), DARK_GREEN))
            .rows(3)
            .build();

        gui.fill(pane(Material.CYAN_STAINED_GLASS_PANE));

        gui.slot(10, named(Material.GLOWSTONE, text("Set Day", YELLOW),
            text("Set time to day.", GRAY)),
            click -> {
                world.setTime(1000);
                ((Player) click.player()).sendMessage(text("Time set to day.", YELLOW));
            });

        gui.slot(11, named(Material.BLUE_CONCRETE, text("Set Night", BLUE),
            text("Set time to night.", GRAY)),
            click -> {
                world.setTime(13000);
                ((Player) click.player()).sendMessage(text("Time set to night.", BLUE));
            });

        gui.slot(13, named(Material.WHITE_CONCRETE, text("Clear Weather", WHITE),
            text("Stop rain and thunder.", GRAY)),
            click -> {
                world.setStorm(false);
                world.setThundering(false);
                ((Player) click.player()).sendMessage(text("Weather cleared.", WHITE));
            });

        gui.slot(14, named(Material.WATER_BUCKET, text("Rain", AQUA),
            text("Start rain.", GRAY)),
            click -> {
                world.setStorm(true);
                world.setThundering(false);
                ((Player) click.player()).sendMessage(text("Rain started.", AQUA));
            });

        gui.slot(15, named(Material.LIGHTNING_ROD, text("Thunder", GRAY),
            text("Start thunderstorm.", GRAY)),
            click -> {
                world.setStorm(true);
                world.setThundering(true);
                ((Player) click.player()).sendMessage(text("Thunderstorm started.", GRAY));
            });

        gui.slot(17, named(Material.NETHER_STAR, text("Toggle Keep Inventory", LIGHT_PURPLE),
            text("Toggle keepInventory gamerule.", GRAY),
            text("Currently: " + (world.getGameRuleValue(org.bukkit.GameRule.KEEP_INVENTORY) == Boolean.TRUE ? "ON" : "OFF"), GRAY)),
            click -> {
                final boolean current = world.getGameRuleValue(org.bukkit.GameRule.KEEP_INVENTORY) == Boolean.TRUE;
                world.setGameRule(org.bukkit.GameRule.KEEP_INVENTORY, !current);
                ((Player) click.player()).sendMessage(
                    text("Keep Inventory: ", GRAY).append(text(!current ? "ON" : "OFF", !current ? GREEN : RED))
                );
                // Reopen so the lore updates
                openWorldControls((Player) click.player());
            });

        gui.slot(22, back(), click -> openMainMenu((Player) click.player()));

        gui.open(player);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static ItemStack pane(final Material material) {
        return named(material, Component.empty());
    }

    private static ItemStack back() {
        return named(Material.ARROW, text("Back", GRAY));
    }

    private static ItemStack playerHead(final Player player) {
        final ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(player);
        meta.displayName(text(player.getName(), YELLOW));
        meta.lore(List.of(
            text("Ping: " + player.getPing() + "ms", GRAY),
            text("Click to manage", DARK_GRAY)
        ));
        skull.setItemMeta(meta);
        return skull;
    }

    private static ItemStack named(final Material material, final Component name, final Component... lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        if (lore.length > 0) meta.lore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
}
