package io.papermc.paper.gui;

import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * A {@link Gui} with built-in pagination. The last row is reserved for
 * navigation buttons and an optional fill item. All other rows display the
 * paginated item list.
 *
 * <p>Use {@link #slot} to place static items in the navigation row.
 * Use {@link #addItem} to add items to the paginated content.</p>
 */
@DefaultQualifier(NonNull.class)
public interface PaginatedGui extends Gui {

    static Builder builder() {
        return GuiProvider.provider().createPaginatedGuiBuilder();
    }

    void addItem(ItemStack item);

    void addItem(ItemStack item, Consumer<GuiClick> onClick);

    void addItem(GuiItem item);

    void clearItems();

    // 0-indexed current page
    int page();

    // Total number of pages given the current item list
    int pages();

    void page(int page);

    interface Builder {
        Builder title(Component title);

        // Minimum 2 rows (1 content + 1 chrome)
        Builder rows(int rows);

        Builder onClose(Consumer<HumanEntity> onClose);

        // Slot index within the last row for the previous-page button (default: first slot of last row)
        Builder previousPageSlot(int slot);

        // Slot index within the last row for the next-page button (default: last slot of last row)
        Builder nextPageSlot(int slot);

        // Item shown on the previous-page button; click handler is assigned automatically
        Builder previousPageItem(ItemStack item);

        // Item shown on the next-page button; click handler is assigned automatically
        Builder nextPageItem(ItemStack item);

        // Item used to fill empty chrome slots (e.g. gray glass pane). Null = leave empty.
        Builder fillItem(@Nullable ItemStack item);

        PaginatedGui build();
    }
}
