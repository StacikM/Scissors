package io.papermc.paper.gui;

// Single public entry point for initialising the GUI subsystem.
// Called once during server startup from PaperCommands.
public final class GuiApi {

    private GuiApi() {}

    public static void init() {
        GuiProvider.Holder.INSTANCE = new CraftGuiProvider();
        GuiListener.register();
    }
}
