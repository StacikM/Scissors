package io.papermc.paper.gui;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.RegisteredListener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
final class GuiListener implements Listener {

    private GuiListener() {}

    static void register() {
        final GuiListener self = new GuiListener();

        InventoryClickEvent.getHandlerList().register(new RegisteredListener(
            self,
            (listener, event) -> ((GuiListener) listener).onClick((InventoryClickEvent) event),
            EventPriority.HIGH,
            ScissorsInternalPlugin.INSTANCE,
            false
        ));

        InventoryCloseEvent.getHandlerList().register(new RegisteredListener(
            self,
            (listener, event) -> ((GuiListener) listener).onClose((InventoryCloseEvent) event),
            EventPriority.NORMAL,
            ScissorsInternalPlugin.INSTANCE,
            false
        ));
    }

    private void onClick(final InventoryClickEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof final GuiHolder gui)) return;

        event.setCancelled(true);

        // Ignore clicks in the player's own inventory portion
        if (event.getClickedInventory() == null
            || !event.getClickedInventory().equals(event.getInventory())) {
            return;
        }

        gui.handleClick(event.getWhoClicked(), event.getSlot(), event.getClick());
    }

    private void onClose(final InventoryCloseEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof final GuiHolder gui)) return;
        gui.handleClose(event.getPlayer());
    }
}
