package com.itndev.factions.Utils;

import com.itndev.factions.AdminCommands.AdminMainCommand;
import com.itndev.factions.FactionCommands.FactionCommandTabComplete;
import com.itndev.factions.FactionCommands.FactionMainCommand;
import com.itndev.factions.Listener.PlayerListener;
import com.itndev.factions.Main;
import com.itndev.factions.PlaceHolder.PlaceHolderManager;
import com.itndev.factions.Storage.StorageIO.FactionStorageIOManager;
import com.itndev.factions.Storage.StorageIO.UserInfoStorageIOManager;
import com.itndev.factions.Task.AsyncTasks.SyncMap;
import com.itndev.factions.Utils.DiscordAuth.DiscordAuth;
import com.itndev.factions.Utils.DiscordAuth.DiscordCommand;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class RegisterStuff {


    public static void onStartup() {
        FactionStorageIOManager.restoreFactionInfo();
        UserInfoStorageIOManager.restoreUserInfo();
        ValidChecker.setvalid();
        try {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                System.out.println("Hooking into PlaceHolderAPI...");
                PlaceHolderManager papi = new PlaceHolderManager();
                papi.register();
            } else {
                System.out.println("PlaceHolderAPI not Found...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //new SyncMap().SyncStorageMap();
        DiscordAuth.LoopCheckAuth();
    }

    public static void onShutdown() {
        FactionStorageIOManager.SaveFactionInfo();
        UserInfoStorageIOManager.SaveUserInfo();
    }

    public static void RegisterFactionCommands() {
        ((PluginCommand) Objects.<PluginCommand>requireNonNull(Main.getInstance().getCommand("국가"))).setExecutor(new FactionMainCommand());
        ((PluginCommand) Objects.<PluginCommand>requireNonNull(Main.getInstance().getCommand("국가"))).setTabCompleter(new FactionCommandTabComplete());
        ((PluginCommand) Objects.<PluginCommand>requireNonNull(Main.getInstance().getCommand("factionadmin"))).setExecutor(new AdminMainCommand());
        ((PluginCommand) Objects.<PluginCommand>requireNonNull(Main.getInstance().getCommand("fa"))).setExecutor(new AdminMainCommand());
        ((PluginCommand) Objects.<PluginCommand>requireNonNull(Main.getInstance().getCommand("연동"))).setExecutor(new DiscordCommand());
        ((PluginCommand) Objects.<PluginCommand>requireNonNull(Main.getInstance().getCommand("연동정보"))).setExecutor(new DiscordCommand());
        ((PluginCommand) Objects.<PluginCommand>requireNonNull(Main.getInstance().getCommand("연동해제"))).setExecutor(new DiscordCommand());
    }

    public static void RegisterListener() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), (Plugin)Main.getInstance());
    }
}
