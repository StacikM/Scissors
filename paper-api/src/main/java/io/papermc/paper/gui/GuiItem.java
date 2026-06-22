package io.papermc.paper.gui;

import java.util.function.Consumer;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public interface GuiItem {

    static GuiItem of(final ItemStack item) {
        return new GuiItemImpl(item, null);
    }

    static GuiItem of(final ItemStack item, final Consumer<GuiClick> onClick) {
        return new GuiItemImpl(item, onClick);
    }

    ItemStack item();

    GuiItem item(ItemStack item);

    @Nullable Consumer<GuiClick> onClick();

    GuiItem onClick(@Nullable Consumer<GuiClick> onClick);
}

// Package-private - no NMS dependencies so it lives in the API jar
record GuiItemImpl(ItemStack item, @Nullable Consumer<GuiClick> onClick) implements GuiItem {

    @Override
    public GuiItem item(final ItemStack item) {
        return new GuiItemImpl(item, this.onClick);
    }

    @Override
    public GuiItem onClick(final @Nullable Consumer<GuiClick> onClick) {
        return new GuiItemImpl(this.item, onClick);
    }
}
