package com.itndev.factions.Storage;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.Faction.FactionStorage;
import com.itndev.factions.Utils.CommonUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class UserInfoStorage {

    public static ConcurrentHashMap<String, String> uuidname = new ConcurrentHashMap<>(); // uuid -> lowercase name
    public static ConcurrentHashMap<String, String> nameuuid = new ConcurrentHashMap<>(); // lowercase name -> uuid
    public static ConcurrentHashMap<String, String> namename = new ConcurrentHashMap<>();

    public static void onPlayerJoinEvent(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        //p.sendMessage("반갑습니다");
        String uuid = p.getUniqueId().toString();
        //p.setGameMode(GameMode.ADVENTURE);
        /*if(storage.proxyonline.containsKey(uuid)) {
            String cmd = "update the player location";
        } else {
            String cmd = "update the player location";
            jedis.RedisChatSyncQ.put("", "");
            String cmd2 = "notifiy team members that the player has joined the network";
            jedis.RedisChatSyncQ.put("notify:=:" + p.getUniqueId().toString() + ":=:" + p.getUniqueId().toString() + ":=:" + p.getName() + "님이 서버에 접속하셨습니다" + ":=:" + "true", "notify:=:" + p.getUniqueId().toString() + ":=:" + p.getUniqueId().toString() + ":=:" + p.getName() + "님이 서버에 접속하셨습니다" + ":=:" + "true");

        }*/
        if(UserInfoStorage.uuidname.isEmpty()) {
            System.out.println("====UUIDNAME IS EMPTY====");
        }
        if(UserInfoStorage.nameuuid.isEmpty()) {
            System.out.println("====NAMEUUID IS EMPTY====");
        }
        if(UserInfoStorage.namename.isEmpty()) {
            System.out.println("====NAMENAME IS EMPTY====");
        }
        if(Main.ServerName.equalsIgnoreCase("client1")) { //main.getInstance().clientname.equalsIgnoreCase("client1")
            if (!FactionStorage.PlayerFaction.containsKey(uuid)) {
                //storage.teamrank.put(e.getPlayer().getUniqueId().toString(), "nomad");
                JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionRank:=:add:=:" + CommonUtils.String2Byte(uuid) + ":=:add:=:" + CommonUtils.String2Byte("nomad"));
            }
            if(FactionStorage.FactionRank.containsKey(uuid) && FactionStorage.FactionRank.get(uuid).equalsIgnoreCase(Config.Nomad)) {
                if(FactionStorage.PlayerFaction.containsKey(uuid)) {
                    String teamname = FactionStorage.PlayerFaction.get(uuid);
                    JedisTempStorage.AddCommandToQueue_INNER("update:=:PlayerFaction:=:remove:=:" + CommonUtils.String2Byte(uuid) + ":=:add:=:" + CommonUtils.String2Byte("nomad"));
                    JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionRank:=:add:=:" + CommonUtils.String2Byte(uuid) + ":=:add:=:" + CommonUtils.String2Byte("nomad"));
                    //jedis.RedisUpdateQ.put("update:=:teammember:=:remove:=:" + uuid + ":=:add:=:" + "nomad", "update:=:teammember:=:remove:=:" + uuid + ":=:add:=:" + "nomad");

                }

            }
            if (uuidname.containsKey(uuid) /*e.getPlayer().hasPlayedBefore()*/) {
                if (!FactionStorage.FactionRank.containsKey(uuid)) {
                    //storage.teamrank.put(e.getPlayer().getUniqueId().toString(), "nomad");
                    JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionRank:=:add:=:" + CommonUtils.String2Byte(uuid) + ":=:add:=:" + CommonUtils.String2Byte("nomad"));
                }
                if (uuidname.containsKey(uuid)) {
                    String originname = uuidname.get(uuid);
                    if (!originname.equals(e.getPlayer().getName().toLowerCase(Locale.ROOT))) {


                        //uuidname.put(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName().toLowerCase(Locale.ROOT));
                        JedisTempStorage.AddCommandToQueue_INNER("update:=:uuidname:=:add:=:" + CommonUtils.String2Byte(uuid) + ":=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName().toLowerCase(Locale.ROOT)));

                        //nameuuid.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getUniqueId().toString());
                        JedisTempStorage.AddCommandToQueue_INNER("update:=:nameuuid:=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName().toLowerCase(Locale.ROOT)) + ":=:add:=:" + CommonUtils.String2Byte(uuid));

                        //nameuuid.remove(originname);
                        JedisTempStorage.AddCommandToQueue_INNER("update:=:nameuuid:=:remove:=:" + CommonUtils.String2Byte(originname) + ":=:add:=:" + CommonUtils.String2Byte("nomad"));

                        //namename.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getName());
                        JedisTempStorage.AddCommandToQueue_INNER("update:=:namename:=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName().toLowerCase(Locale.ROOT)) + ":=:add:=:" + e.getPlayer().getName());

                    }
                } else {
                    //uuidname.put(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName().toLowerCase(Locale.ROOT));
                    JedisTempStorage.AddCommandToQueue_INNER("update:=:uuidname:=:add:=:" + CommonUtils.String2Byte(uuid) + ":=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName().toLowerCase(Locale.ROOT)));

                    //nameuuid.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getUniqueId().toString());
                    JedisTempStorage.AddCommandToQueue_INNER("update:=:nameuuid:=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName().toLowerCase(Locale.ROOT)) + ":=:add:=:" + CommonUtils.String2Byte(uuid));

                    //namename.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getName());
                    JedisTempStorage.AddCommandToQueue_INNER("update:=:namename:=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName().toLowerCase(Locale.ROOT)) + ":=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName()));
                }
            } else {
                //storage.teamrank.put(e.getPlayer().getUniqueId().toString(), "nomad");
                JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionRank:=:add:=:" + CommonUtils.String2Byte(uuid) + ":=:add:=:" + CommonUtils.String2Byte("nomad"));

                //uuidname.put(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName().toLowerCase(Locale.ROOT));
                JedisTempStorage.AddCommandToQueue_INNER("update:=:uuidname:=:add:=:" + CommonUtils.String2Byte(uuid) + ":=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName().toLowerCase(Locale.ROOT)));

                //nameuuid.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getUniqueId().toString());
                JedisTempStorage.AddCommandToQueue_INNER("update:=:nameuuid:=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName().toLowerCase(Locale.ROOT)) + ":=:add:=:" + CommonUtils.String2Byte(uuid));

                //namename.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getName());
                JedisTempStorage.AddCommandToQueue_INNER("update:=:namename:=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName().toLowerCase(Locale.ROOT)) + ":=:add:=:" + CommonUtils.String2Byte(e.getPlayer().getName()));
            }
        }
    }

    public static void UserInfoStorageUpdateHandler(String[] args) {
        if(args[1].equalsIgnoreCase("namename")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    UserInfoStorage.namename.remove(key);
                    UserInfoStorage.namename.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    UserInfoStorage.namename.remove(key);
                }

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                UserInfoStorage.namename.remove(key);
            }

        } else if(args[1].equalsIgnoreCase("nameuuid")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {
                    UserInfoStorage.nameuuid.remove(key);
                    UserInfoStorage.nameuuid.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    UserInfoStorage.nameuuid.remove(key);
                }

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                UserInfoStorage.nameuuid.remove(key);
            }

        } else if(args[1].equalsIgnoreCase("uuidname")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {
                    UserInfoStorage.uuidname.remove(key);
                    UserInfoStorage.uuidname.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    UserInfoStorage.uuidname.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                UserInfoStorage.uuidname.remove(key);
            }
        }
    }
}
