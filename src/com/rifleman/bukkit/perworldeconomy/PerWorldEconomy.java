

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rifleman.bukkit.perworldeconomy;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author sovadi
 */
public class PerWorldEconomy  extends JavaPlugin {
    public static final Logger log = Logger.getLogger("Minecraft");
    public static PluginManager pluginManager;
    public static PluginDescriptionFile pdfFile;
    public static BukkitScheduler bs;
    public static Server server;
    
    
    @Override
    public void onDisable() {
        log.info(PluginData.MESSAGE_DISABLE);
    }
    
    @Override
    public void onEnable() {
        pluginManager = this.getServer().getPluginManager();
        pdfFile = this.getDescription();
        server = this.getServer();
        bs = server.getScheduler();
        
        log.info(PluginData.MESSAGE_ENABLE);
    }
    
}
