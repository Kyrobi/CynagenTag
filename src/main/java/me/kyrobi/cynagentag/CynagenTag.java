package me.kyrobi.cynagentag;

import org.bukkit.plugin.java.JavaPlugin;

public final class CynagenTag extends JavaPlugin {

    @Override
    public void onEnable() {
        new TagHandler(this);
        this.getCommand("tagg").setExecutor(new TagCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
