package io.papermc.paper.gui;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface GuiProvider {

    Gui.Builder createGuiBuilder();

    PaginatedGui.Builder createPaginatedGuiBuilder();

    static GuiProvider provider() {
        return Holder.INSTANCE;
    }

    @ApiStatus.Internal
    final class Holder {
        static GuiProvider INSTANCE;

        private Holder() {}
    }
}
