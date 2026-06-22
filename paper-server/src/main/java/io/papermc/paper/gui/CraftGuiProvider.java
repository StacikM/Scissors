package io.papermc.paper.gui;

public final class CraftGuiProvider implements GuiProvider {

    @Override
    public Gui.Builder createGuiBuilder() {
        return new CraftGuiBuilder();
    }

    @Override
    public PaginatedGui.Builder createPaginatedGuiBuilder() {
        return new CraftPaginatedGuiBuilder();
    }
}
