package io.papermc.paper.gui;

import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public interface Gui {

    static Builder builder() {
        return GuiProvider.provider().createGuiBuilder();
    }

    void slot(int slot, ItemStack item);

    void slot(int slot, ItemStack item, Consumer<GuiClick> onClick);

    void slot(int slot, GuiItem item);

    void clearSlot(int slot);

    @Nullable GuiItem getSlot(int slot);

    // Fills all currently empty slots with a decoration item (e.g. gray glass pane)
    void fill(ItemStack item);

    void fill(ItemStack item, Consumer<GuiClick> onClick);

    void open(HumanEntity player);

    void close(HumanEntity player);

    Component title();

    int rows();

    int size();

    interface Builder {
        Builder title(Component title);

        // 1–6 rows
        Builder rows(int rows);

        Builder onClose(Consumer<HumanEntity> onClose);

        Gui build();
    }
}
