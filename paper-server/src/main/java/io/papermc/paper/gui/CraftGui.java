package io.papermc.paper.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class CraftGui implements Gui, GuiHolder {

    private final Component title;
    private final int rows;
    private final @Nullable Consumer<HumanEntity> onClose;
    private final Map<Integer, GuiItem> slots = new HashMap<>();
    private final Inventory inventory;

    CraftGui(final Component title, final int rows, final @Nullable Consumer<HumanEntity> onClose) {
        this.title = title;
        this.rows = rows;
        this.onClose = onClose;
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
    }

    @Override
    public void slot(final int slot, final ItemStack item) {
        this.slot(slot, GuiItem.of(item));
    }

    @Override
    public void slot(final int slot, final ItemStack item, final Consumer<GuiClick> onClick) {
        this.slot(slot, GuiItem.of(item, onClick));
    }

    @Override
    public void slot(final int slot, final GuiItem item) {
        this.slots.put(slot, item);
        this.inventory.setItem(slot, item.item());
    }

    @Override
    public void clearSlot(final int slot) {
        this.slots.remove(slot);
        this.inventory.clear(slot);
    }

    @Override
    public @Nullable GuiItem getSlot(final int slot) {
        return this.slots.get(slot);
    }

    @Override
    public void fill(final ItemStack item) {
        this.fill(item, null);
    }

    @Override
    public void fill(final ItemStack item, final @Nullable Consumer<GuiClick> onClick) {
        final GuiItem guiItem = onClick != null ? GuiItem.of(item, onClick) : GuiItem.of(item);
        for (int i = 0; i < this.size(); i++) {
            if (!this.slots.containsKey(i)) {
                this.slot(i, guiItem);
            }
        }
    }

    @Override
    public void open(final HumanEntity player) {
        player.openInventory(this.inventory);
    }

    @Override
    public void close(final HumanEntity player) {
        if (this.inventory.equals(player.getOpenInventory().getTopInventory())) {
            player.closeInventory();
        }
    }

    @Override
    public Component title() {
        return this.title;
    }

    @Override
    public int rows() {
        return this.rows;
    }

    @Override
    public int size() {
        return this.rows * 9;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void handleClick(final HumanEntity player, final int slot, final ClickType clickType) {
        final @Nullable GuiItem item = this.slots.get(slot);
        if (item == null || item.onClick() == null) return;
        item.onClick().accept(new CraftGuiClick(player, slot, clickType, this));
    }

    @Override
    public void handleClose(final HumanEntity player) {
        if (this.onClose != null) {
            this.onClose.accept(player);
        }
    }

    record CraftGuiClick(
        HumanEntity player,
        int slot,
        ClickType clickType,
        Gui gui
    ) implements GuiClick {}
}
