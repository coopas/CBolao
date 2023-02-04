package com.coopas.cbolao;

import com.coopas.cbolao.Comandos.BolaoC;
import com.coopas.cbolao.Utils.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {


    public static JavaPlugin plugin;
    public static Economy econ = null;

    public static Config config;
    public static Config msg;

    @Override
    public void onEnable() {

        plugin = this;

        Bukkit.getConsoleSender().sendMessage("   ");
        comandos();
        Bukkit.getConsoleSender().sendMessage("     §a[CBOLAO] COMANDOS INICIADOS");
        arquivos();
        Bukkit.getConsoleSender().sendMessage("     §a[CBOLAO] ARQUIVOS INICIADOS");
        setupEconomy();
        Bukkit.getConsoleSender().sendMessage("     §a[CBOLAO] VAULT INICIADO");

        Bukkit.getConsoleSender().sendMessage("   ");
        Bukkit.getConsoleSender().sendMessage("     §a - PLUGIN CBOLAO INICIADO!");
        Bukkit.getConsoleSender().sendMessage("     §a - MUITO OBRIGADO POR ME APOIAR!");
        Bukkit.getConsoleSender().sendMessage("   ");

    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("     §c - PLUGIN CBOLAO FINALIZADO!  ");
        Bukkit.getConsoleSender().sendMessage(" ");


    }

    public void comandos() {

        getCommand("bolao").setExecutor(new BolaoC());
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null)
            econ = (Economy)economyProvider.getProvider();
        return (econ != null);
    }

    public void arquivos() {
        config = new Config(this, "config.yml");
        config.saveDefaultConfig();

        msg = new Config(this, "mensagens.yml");
        msg.saveDefaultConfig();
    }

    public void createFile(Main main, String fileName, boolean isFile) {
        try {
            File file = new File(main.getDataFolder() + File.separator + fileName);
            if (isFile) file.createNewFile();
            else if (!file.exists()) file.mkdirs();
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

    }
}
