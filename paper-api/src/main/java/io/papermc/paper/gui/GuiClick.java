package io.papermc.paper.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public interface GuiClick {
    HumanEntity player();
    int slot();
    ClickType clickType();
    Gui gui();
}
