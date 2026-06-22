package io.papermc.paper.gui;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

// Minimal Plugin stub used solely to register internal Bukkit listeners.
// Never loaded through the plugin system it exists only to satisfy RegisteredListener.
@DefaultQualifier(NonNull.class)
final class ScissorsInternalPlugin extends PluginBase {

    static final ScissorsInternalPlugin INSTANCE = new ScissorsInternalPlugin();

    private static final Logger LOGGER = Logger.getLogger("Scissors");
    private static final PluginDescriptionFile DESCRIPTION =
        new PluginDescriptionFile("Scissors", "internal", ScissorsInternalPlugin.class.getName());

    private ScissorsInternalPlugin() {}

    @Override public boolean isEnabled() { return true; }
    @Override public boolean isNaggable() { return false; }
    @Override public void setNaggable(final boolean canNag) {}
    @Override public Logger getLogger() { return LOGGER; }
    @Override public PluginDescriptionFile getDescription() { return DESCRIPTION; }
    @Override public Server getServer() { return org.bukkit.Bukkit.getServer(); }
    @Override public void onEnable() {}
    @Override public void onDisable() {}
    @Override public void onLoad() {}

    @Override
    public io.papermc.paper.plugin.configuration.PluginMeta getPluginMeta() {
        return DESCRIPTION;
    }

    @Override
    public io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager<org.bukkit.plugin.Plugin> getLifecycleManager() {
        throw new UnsupportedOperationException();
    }

    @Override public boolean onCommand(final CommandSender s, final Command c, final String l, final String[] a) { return false; }
    @Override public @Nullable List<String> onTabComplete(final CommandSender s, final Command c, final String l, final String[] a) { return null; }
    @Override public @Nullable InputStream getResource(final String filename) { return null; }
    @Override public FileConfiguration getConfig() { throw new UnsupportedOperationException(); }
    @Override public void saveConfig() {}
    @Override public void saveDefaultConfig() {}
    @Override public void saveResource(final String resourcePath, final boolean replace) {}
    @Override public void reloadConfig() {}
    @Override public PluginLoader getPluginLoader() { throw new UnsupportedOperationException(); }
    @Override public File getDataFolder() { throw new UnsupportedOperationException(); }
    @Override public @Nullable ChunkGenerator getDefaultWorldGenerator(final String worldName, final @Nullable String id) { return null; }
    @Override public @Nullable BiomeProvider getDefaultBiomeProvider(final String worldName, final @Nullable String id) { return null; }
}
