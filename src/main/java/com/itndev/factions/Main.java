package com.itndev.factions;

import com.itndev.factions.Config.*;
import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Listener.BungeeListener;
import com.itndev.factions.MySQL.*;
import com.itndev.factions.RedisStreams.BungeeAPI.BungeeStreamReader;
import com.itndev.factions.RedisStreams.RedisConnection;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.RegisterStuff;

import com.itndev.factions.Utils.ValidChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Main extends JavaPlugin {


    public static String ServerName = "client1";

    public static Main instance;

    public static Economy econ = null;

    public static HikariCP hikariCP = new HikariCP();

    public static wtfDatabase database = new wtfDatabase();

    public static Boolean chattoggle = false;

    public static SystemUtils sysutils = new SystemUtils();

    public static Boolean ShutDown = false;

    FileConfiguration config = getConfig();
    public final Logger logger = Logger.getLogger("Minecraft");

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        hikariCP.setupHikariInfo();
        hikariCP.ConnectHikari();
        hikariCP.createHikariTable();

        //JedisManager.jedisTest();
        setupEconomy();
        RegisterStuff.RegisterFactionCommands();
        RegisterStuff.RegisterListener();
        RegisterStuff.onStartup();

        StorageDir.SetupStorage();
        ConfigIO.read();

        //RedisConnection.RedisConnect();
        /*RedisConnection.RedisStreamWriter();
        RedisConnection.RedisStreamReader();*/
        new Thread(() -> {
            try {
                com.itndev.factions.SocketConnection.Main.launch();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        //BungeeStreamReader.RedisStreamReader();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());
        PingSQL();
    }

    private static void PingSQL() {
        new Thread(() -> {
            while(true) {
                try {
                    hikariCP.getHikariConnection().prepareStatement("SELECT 1").executeQuery();
                    Thread.sleep(1000*1000);
                } catch (SQLException | InterruptedException throwables) {
                    throwables.printStackTrace();
                    hikariCP.setupHikariInfo();
                    hikariCP.ConnectHikari();
                    hikariCP.createHikariTable();
                }
            }
        }).start();
    }


    @Override
    public void onDisable() {
        ConfigIO.save();
        RegisterStuff.onShutdown();
        ShutDown = true;
        //RedisConnection.RedisDisConnect();
        try {
            hikariCP.getHikariConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

        instance = null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEconomy() {
        return econ;
    }
}
