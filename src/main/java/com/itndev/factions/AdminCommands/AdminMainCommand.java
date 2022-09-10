package com.itndev.factions.AdminCommands;

import com.itndev.factions.Dump.YamlDump;
import com.itndev.factions.Faction.FactionOutpost;
import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.RedisStreams.RedisConnection;
import com.itndev.factions.Storage.Faction.FactionStorage;
import com.itndev.factions.Storage.StorageIO.FactionStorageIOManager;
import com.itndev.factions.Storage.StorageIO.UserInfoStorageIOManager;
import com.itndev.factions.Utils.FactionList.FactionList;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AdminMainCommand implements CommandExecutor {

    private static Location loc = new Location(Bukkit.getWorld("spawn"), 480, 42, 436);

    public static Location getCopyLocation() {
        return loc;
    }

    public static void setCopyLocation(Location newloc) {
        loc = newloc;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if(!sender.hasPermission("itndevfactions.admin")) {
            sender.sendMessage(SystemUtils.colorize("&c&lERROR &r&7권한이 없습니다"));
            return false;
        }
        if(label.equalsIgnoreCase("factionadmin")) {
            RunAdminCommand((Player) sender, strings);
        }
        return false;
    }

    public void RunAdminCommand(Player p, String[] args) {
        if(args.length < 1) {
            SystemUtils.sendmessage(p, SystemUtils.colorize("&c&lERROR &r&f존재하지 않는 명령어"));
            return;
        }
        if(args[0].equalsIgnoreCase("reloadfactionbaltop")) {
            FactionList.FactionTopExecute(100L);
            SystemUtils.sendmessage(p, "&c&lSUCESS &r&7국가 금고 정보 로컬 데이터 리프레시 중...");
        } else if(args[0].equalsIgnoreCase("buildfactionbaltop")) {
            FactionList.BuildFactionTop();
            SystemUtils.sendmessage(p, "&c&lSUCESS &r&7국가 목록 빌드 중...");
        } else if(args[0].equalsIgnoreCase("setlocation")) {
            loc = p.getLocation();
            SystemUtils.sendmessage(p, "&c&lSUCESS &r&7복사 위치 설정완료");
        } else if(args[0].equalsIgnoreCase("pastechunk")) {
            if(loc != null) {
                Location loc2 = SystemUtils.ReplaceChunk(loc, p.getLocation());
                SystemUtils.sendmessage(p, "&c&lSUCESS &r&7청크 붙여넣는중");
                if(loc2 == null) {
                    SystemUtils.sendmessage(p, "&c&lINFO &r&7BEACON 위치 확인 불가");
                } else {
                    String k = "X:" + loc2.getBlockX() + " Y:" + loc2.getBlockY() + " Z:" + loc2.getBlockZ();
                    SystemUtils.sendfactionmessage(p, "&r&f참 신호기 위치 &r&7: &r&c" + k);
                }
            } else {
                SystemUtils.sendmessage(p, "&c&lERROR &r&7설정된 위치가 존재하지 않음");
            }
        } else if(args[0].equalsIgnoreCase("tryclaimoutpost")) {
            Location loc = p.getLocation();
            FactionOutpost.TryClaimOutPost(p, loc, UUID.randomUUID().toString());
        } else if(args[0].equalsIgnoreCase("radiusclaimed")) {
            new Thread(() -> {
                try {
                    if(FactionUtils.AsyncNearByChunksAreOwned5(p.getLocation()).get()) {
                        p.sendMessage("yes");
                    } else {
                        p.sendMessage("no");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }).start();
        } else if(args[0].equalsIgnoreCase("checkfactionoutposthashmap")) {
            new Thread( () -> {
                p.sendMessage("==============");
                for(String key : FactionStorage.FactionOutPost.keySet()) {
                    p.sendMessage(key + " - " + FactionStorage.FactionOutPost.get(key));
                }
                p.sendMessage("==============");
                for(String key : FactionStorage.FactionOutPostList.keySet()) {
                    p.sendMessage(key + " - " + FactionStorage.FactionOutPostList.get(key).toString());
                }
                p.sendMessage("==============");
            }).start();

        } else if(args[0].equalsIgnoreCase("getclock")) {
            SystemUtils.sendmessage(p, "&c&lINFO &7JEDIS SYNC CLOCK TIME : " + JedisManager.JEDISSYNCCLOCK);
        } else if(args[0].equalsIgnoreCase("updatehashmap")) {
            String updatecommand = SystemUtils.Args2String(args, 1);
            JedisTempStorage.AddCommandToQueue_INNER(updatecommand);
            SystemUtils.sendmessage(p, "&c&lINFO &r&7Added Command To Queue\n" +
                    "&r&7-> &r" + updatecommand);
        } else if(args[0].equalsIgnoreCase("uploadstorage")) {
            if(args.length < 2) {
                RedisConnection.UploadStorageToRedis(null);
                SystemUtils.sendmessage(p, "&c&lINFO &r&7uploaded to redis without a key");
                return;
            }
            RedisConnection.UploadStorageToRedis(args[1]);
            SystemUtils.sendmessage(p, "&c&lINFO &r&7uploaded to redis with key " + args[1]);
        } else if(args[0].equalsIgnoreCase("loadfromstorage")) {
            if(args.length < 2) {
                SystemUtils.sendmessage(p, "&c&lERROR &7/factionadmin reloadstorage [keyname]");
                return;
            }
            RedisConnection.ReloadStorageFromRemoteServer(args[1]);
            SystemUtils.sendmessage(p, "&c&lINFO &r&7loaded from redis with key " + args[1]);
        } else if(args[0].equalsIgnoreCase("deletestorage")) {
            if(args.length < 2) {
                RedisConnection.RemoveUploadedStorage(null);
                SystemUtils.sendmessage(p, "&c&lINFO &r&7deleted storage from redis without key");
                return;
            }
            RedisConnection.RemoveUploadedStorage(args[1]);
            SystemUtils.sendmessage(p, "&c&lINFO &r&7deleted storage from redis with key " + args[1]);
        } else if(args[0].equalsIgnoreCase("savestorage")) {
            SystemUtils.sendmessage(p, "&c&lINFO &r&7saving storage to flatfile ");
            FactionStorageIOManager.SaveFactionInfo();
            UserInfoStorageIOManager.SaveUserInfo();
        } else if(args[0].equalsIgnoreCase("syncandclose")) {
            JedisTempStorage.AddCommandToQueue_INNER("syncandclose");
            SystemUtils.sendmessage(p, "&c&lINFO &r&7Added Command To Queue\n" +
                    "&r&7-> &r" + "syncandclose");
        } else if(args[0].equalsIgnoreCase("dumpyaml")) {
            YamlDump.CreateFile("dumpyaml");
            YamlDump.TryDumpHashMaps_Factions();
            YamlDump.TryDumpHashMaps_Factions_LISTTYPE();
            YamlDump.TryDumpHashMaps_UUIDINFO();
            SystemUtils.sendmessage(p, "&c&lINFO &r&7Dumping FactionData to Yaml File");
        } else {
            SystemUtils.sendmessage(p, "&7&m-----------&r&c&l[HELP]&7&m-----------\n" +
                    "&r/factionadmin reloadfactionbaltop &8&l: &r&7reload faction balance top info (need to /factionadmin buildfactionbaltop after)\n" +
                    "&r/factionadmin buildfactionbaltop &8&l: &r&7build faction balance top info based on cached faction info\n" +
                    "&r/factionadmin setlocation &8&l: &r&7set location for outpost paste chunk location\n" +
                    "&r/factionadmin pastechunk &8&l: &r&7paste chunk based on the location posted\n" +
                    "&r/factionadmin tryclaimoutpost &8&l: &r&7test command for outpost claim fuction (outdated)\n" +
                    "&r/factionadmin radiusclaimed &8&l: &r&7check chunk raidus of 5 chunks if any chunks are claimed\n" +
                    "&r/factionadmin checkfactionoutposthashmap &8&l: &r&7check faction outpost storage data\n" +
                    "&r/factionadmin getclock &8&l: &r&7get clock of the I/O thread\n" +
                    "&r/factionadmin updatehashmap &8&l: &r&7manualy sends a command throught the INTERNAL I/O pipline\n" +
                    "&r/factionadmin uploadstorage [keyname] &8&l: &r&7upload storage to database based on [keyname]\n" +
                    "&r/factionadmin loadfromstorage [keyname] &8&l: &r&7load storage from database based on [keyname]\n" +
                    "&r/factionadmin deletestorage [keyname]\n &8&l: &r&7deleted storage from database based on [keyname]\n" +
                    "&r/factionadmin savestorage &8&l: &r&7save storage to local yaml file in plugin directory using Bukkit FileConfig\n" +
                    "&r/factionadmin syncandclose &8&l: &r&7safely close FactionCore System\n" +
                    "&r/factionadmin dumpyaml &8&l: &r&7dump FactionData in Directory 'dump' using SnakeYaml\n" +
                    "&r/factionadmin help &8&l: &r&7display this help section\n" +
                    "&r&7&m-----------&r&c&l[HELP]&7&m-----------");
        }
     }

}
