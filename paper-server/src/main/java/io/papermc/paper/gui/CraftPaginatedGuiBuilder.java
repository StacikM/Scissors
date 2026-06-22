package io.papermc.paper.gui;

import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

@DefaultQualifier(NonNull.class)
final class CraftPaginatedGuiBuilder implements PaginatedGui.Builder {

    private Component title = Component.empty();
    private int rows = 4;
    private @Nullable Consumer<HumanEntity> onClose;
    private int prevSlot = -1; // resolved in build()
    private int nextSlot = -1; // resolved in build()
    private @Nullable ItemStack prevItem;
    private @Nullable ItemStack nextItem;
    private @Nullable ItemStack fillItem;

    @Override
    public PaginatedGui.Builder title(final Component title) {
        this.title = title;
        return this;
    }

    @Override
    public PaginatedGui.Builder rows(final int rows) {
        if (rows < 2 || rows > 6) throw new IllegalArgumentException("rows must be 2–6, got " + rows);
        this.rows = rows;
        return this;
    }

    @Override
    public PaginatedGui.Builder onClose(final Consumer<HumanEntity> onClose) {
        this.onClose = onClose;
        return this;
    }

    @Override
    public PaginatedGui.Builder previousPageSlot(final int slot) {
        this.prevSlot = slot;
        return this;
    }

    @Override
    public PaginatedGui.Builder nextPageSlot(final int slot) {
        this.nextSlot = slot;
        return this;
    }

    @Override
    public PaginatedGui.Builder previousPageItem(final ItemStack item) {
        this.prevItem = item;
        return this;
    }

    @Override
    public PaginatedGui.Builder nextPageItem(final ItemStack item) {
        this.nextItem = item;
        return this;
    }

    @Override
    public PaginatedGui.Builder fillItem(final @Nullable ItemStack item) {
        this.fillItem = item;
        return this;
    }

    @Override
    public PaginatedGui build() {
        final int chromeStart = (this.rows - 1) * 9;
        final int resolvedPrev = this.prevSlot == -1 ? chromeStart : this.prevSlot;
        final int resolvedNext = this.nextSlot == -1 ? chromeStart + 8 : this.nextSlot;
        final ItemStack resolvedPrevItem = this.prevItem != null ? this.prevItem : defaultArrow("Previous Page");
        final ItemStack resolvedNextItem = this.nextItem != null ? this.nextItem : defaultArrow("Next Page");
        return new CraftPaginatedGui(
            this.title, this.rows, this.onClose,
            resolvedPrev, resolvedNext,
            resolvedPrevItem, resolvedNextItem,
            this.fillItem
        );
    }

    private static ItemStack defaultArrow(final String name) {
        final ItemStack item = new ItemStack(Material.ARROW);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(text(name, GRAY));
        item.setItemMeta(meta);
        return item;
    }
}
