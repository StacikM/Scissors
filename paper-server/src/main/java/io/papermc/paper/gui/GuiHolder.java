package io.papermc.paper.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;

interface GuiHolder extends InventoryHolder {
    void handleClick(HumanEntity player, int slot, ClickType clickType);
    void handleClose(HumanEntity player);
}
