package io.papermc.paper.gui;

import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
final class CraftGuiBuilder implements Gui.Builder {

    private Component title = Component.empty();
    private int rows = 3;
    private @Nullable Consumer<HumanEntity> onClose;

    @Override
    public Gui.Builder title(final Component title) {
        this.title = title;
        return this;
    }

    @Override
    public Gui.Builder rows(final int rows) {
        if (rows < 1 || rows > 6) throw new IllegalArgumentException("rows must be 1–6, got " + rows);
        this.rows = rows;
        return this;
    }

    @Override
    public Gui.Builder onClose(final Consumer<HumanEntity> onClose) {
        this.onClose = onClose;
        return this;
    }

    @Override
    public Gui build() {
        return new CraftGui(this.title, this.rows, this.onClose);
    }
}
