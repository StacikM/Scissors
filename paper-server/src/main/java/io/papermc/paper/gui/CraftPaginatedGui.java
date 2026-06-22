package io.papermc.paper.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public final class CraftPaginatedGui implements PaginatedGui, GuiHolder {

    private final Component title;
    private final int rows;
    private final @Nullable Consumer<HumanEntity> onClose;
    private final int prevSlot;
    private final int nextSlot;
    private final @Nullable ItemStack prevItem;
    private final @Nullable ItemStack nextItem;
    private final @Nullable ItemStack fillItem;

    private final List<GuiItem> items = new ArrayList<>();
    // Slots explicitly set by the caller (e.g. back buttons) — survive page re-renders
    private final Map<Integer, GuiItem> staticSlots = new HashMap<>();
    private final Inventory inventory;
    private int currentPage = 0;

    CraftPaginatedGui(
        final Component title,
        final int rows,
        final @Nullable Consumer<HumanEntity> onClose,
        final int prevSlot,
        final int nextSlot,
        final @Nullable ItemStack prevItem,
        final @Nullable ItemStack nextItem,
        final @Nullable ItemStack fillItem
    ) {
        this.title = title;
        this.rows = rows;
        this.onClose = onClose;
        this.prevSlot = prevSlot;
        this.nextSlot = nextSlot;
        this.prevItem = prevItem;
        this.nextItem = nextItem;
        this.fillItem = fillItem;
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        this.renderChrome();
    }

    private int itemsPerPage() {
        return (this.rows - 1) * 9;
    }

    private int chromeStart() {
        return (this.rows - 1) * 9;
    }

    // --- PaginatedGui ---

    @Override
    public void addItem(final ItemStack item) {
        this.addItem(GuiItem.of(item));
    }

    @Override
    public void addItem(final ItemStack item, final Consumer<GuiClick> onClick) {
        this.addItem(GuiItem.of(item, onClick));
    }

    @Override
    public void addItem(final GuiItem item) {
        this.items.add(item);
        final int start = this.currentPage * this.itemsPerPage();
        if (this.items.size() > start && this.items.size() <= start + this.itemsPerPage()) {
            this.renderContent();
        }
        this.renderChrome();
    }

    @Override
    public void clearItems() {
        this.items.clear();
        this.currentPage = 0;
        this.renderPage();
    }

    @Override
    public int page() {
        return this.currentPage;
    }

    @Override
    public int pages() {
        if (this.items.isEmpty()) return 1;
        return (int) Math.ceil((double) this.items.size() / this.itemsPerPage());
    }

    @Override
    public void page(final int page) {
        if (page < 0 || page >= this.pages()) throw new IllegalArgumentException("page out of range: " + page);
        this.currentPage = page;
        this.renderPage();
    }

    // --- Gui (static slots and chrome) ---

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
        this.staticSlots.put(slot, item);
        this.inventory.setItem(slot, item.item());
    }

    @Override
    public void clearSlot(final int slot) {
        this.staticSlots.remove(slot);
        this.inventory.clear(slot);
    }

    @Override
    public @Nullable GuiItem getSlot(final int slot) {
        return this.staticSlots.get(slot);
    }

    @Override
    public void fill(final ItemStack item) {
        for (int i = this.chromeStart(); i < this.size(); i++) {
            if (!this.staticSlots.containsKey(i)) {
                this.inventory.setItem(i, item);
            }
        }
    }

    @Override
    public void fill(final ItemStack item, final Consumer<GuiClick> onClick) {
        this.fill(item);
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
    public Component title() { return this.title; }

    @Override
    public int rows() { return this.rows; }

    @Override
    public int size() { return this.rows * 9; }

    @Override
    public Inventory getInventory() { return this.inventory; }

    // --- GuiHolder ---

    @Override
    public void handleClick(final HumanEntity player, final int slot, final ClickType clickType) {
        // Static slots (back button etc.) take priority
        final @Nullable GuiItem staticItem = this.staticSlots.get(slot);
        if (staticItem != null) {
            if (staticItem.onClick() != null) {
                staticItem.onClick().accept(new CraftGui.CraftGuiClick(player, slot, clickType, this));
            }
            return;
        }

        if (slot == this.prevSlot) {
            if (this.currentPage > 0) { this.currentPage--; this.renderPage(); }
            return;
        }
        if (slot == this.nextSlot) {
            if (this.currentPage < this.pages() - 1) { this.currentPage++; this.renderPage(); }
            return;
        }

        // Content slot
        if (slot < this.itemsPerPage()) {
            final int index = this.currentPage * this.itemsPerPage() + slot;
            if (index < this.items.size()) {
                final GuiItem item = this.items.get(index);
                if (item.onClick() != null) {
                    item.onClick().accept(new CraftGui.CraftGuiClick(player, slot, clickType, this));
                }
            }
        }
    }

    @Override
    public void handleClose(final HumanEntity player) {
        if (this.onClose != null) this.onClose.accept(player);
    }

    // --- Rendering ---

    private void renderPage() {
        this.renderContent();
        this.renderChrome();
    }

    private void renderContent() {
        final int start = this.currentPage * this.itemsPerPage();
        for (int i = 0; i < this.itemsPerPage(); i++) {
            final int index = start + i;
            this.inventory.setItem(i, index < this.items.size() ? this.items.get(index).item() : null);
        }
    }

    private void renderChrome() {
        for (int i = this.chromeStart(); i < this.size(); i++) {
            if (!this.staticSlots.containsKey(i)) {
                this.inventory.setItem(i, this.fillItem);
            }
        }
        if (!this.staticSlots.containsKey(this.prevSlot)) {
            this.inventory.setItem(this.prevSlot,
                this.prevItem != null && this.currentPage > 0 ? this.prevItem : this.fillItem);
        }
        if (!this.staticSlots.containsKey(this.nextSlot)) {
            this.inventory.setItem(this.nextSlot,
                this.nextItem != null && this.currentPage < this.pages() - 1 ? this.nextItem : this.fillItem);
        }
    }
}
