package com.itndev.factions.Utils.DiscordAuth;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Config.Functions;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.BackendIO;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class DiscordAuth {
    public static HashMap<String, String> DISCORD_AUTH_INFO = new HashMap<>();

    public static void LoopCheckAuth() {
        if(Functions.DISCORD_AUTH_ENABLE) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!isAuth(p.getUniqueId().toString())) {
                            SystemUtils.sendmessage(p, "&c&l================================");
                            SystemUtils.sendmessage(p, "&e&l주의사항");
                            SystemUtils.sendmessage(p, "&f공식 디스코드 서버의 계정연동 채널에서");
                            SystemUtils.sendmessage(p, "!연동 <닉네임> 으로 연동을 해주시기 바랍니다");
                            SystemUtils.sendmessage(p, "&3&lDiscord &7: " + Config.DiscordLink);
                            SystemUtils.sendmessage(p, "&c&l================================");
                        }
                    }
                }
            }.runTaskTimerAsynchronously(Main.getInstance(), 60L, 60L);
        }
    }

    public static Boolean isAuth(String UUID) {
        return DISCORD_AUTH_INFO.containsKey(UUID);
    }

    public static void FetchAuthInfo(Player p, String UUID) {
        String[] args = new String[2];
        args[0] = "인증";
        args[1] = "정보";
        BackendIO.SendCMD_BACKEND(UUID, args, "DISCORD_FETCH_AUTH");
        SystemUtils.sendmessage(p, "&3&m-------------------------------------");
        SystemUtils.sendmessage(p, "&9&l디스코드 연동을 확인중입니다.");
        SystemUtils.sendmessage(p, "&3&m-------------------------------------");
    }

    public static void VerifyAuthInfo(String UUID, String ID) {
        String[] args = new String[3];
        args[0] = "인증";
        args[1] = "확인";
        args[2] = ID;
        BackendIO.SendCMD_BACKEND(UUID, args, "DISCORD_VERIFY_AUTH");
    }

    public static void AcceptAuthInfo(String UUID, String DiscordTagE) {
        String DiscordTag = DiscordTagE;
        if(!Functions.DISCORD_AUTH_ENABLE) {
            DiscordTag = "FORCE_DISABLE=TRUE";
        }
        if(!DiscordTag.equalsIgnoreCase("NULL")) {
            DISCORD_AUTH_INFO.put(UUID, DiscordTag);
            if(Functions.DISCORD_AUTH_ENABLE) {
                OfflinePlayer op = Bukkit.getOfflinePlayer(java.util.UUID.fromString(UUID));
                if (op.isOnline()) {
                    Player p = (Player) op;
                    SystemUtils.sendmessage(p, "&3&m-------------------------------------");
                    SystemUtils.sendmessage(p, "&9&l디스코드 연동인증이 확인되었습니다.");
                    SystemUtils.sendmessage(p, "&7현재 당신의 계정은 " + DiscordTag + " 와 연동되어 있습니다");
                    SystemUtils.sendmessage(p, "&3&m-------------------------------------");
                }
            }
        } else {
            DISCORD_AUTH_INFO.remove(UUID);
            OfflinePlayer op = Bukkit.getOfflinePlayer(java.util.UUID.fromString(UUID));
            if(op.isOnline()) {
                Player p = (Player) op;
                SystemUtils.sendmessage(p, "&3&m-------------------------------------");
                SystemUtils.sendmessage(p, "&9&l디스코드 연동인증이 확인되지 않았습니다");
                SystemUtils.sendmessage(p, "&7공식 디스코드 서버 " + Config.DiscordLink + " 에서 !연동 <닉네임> 으로 연동해주시기 바랍니다");
                SystemUtils.sendmessage(p, "&3&m-------------------------------------");
            }
        }

    }

    public static void CheckAuthInfo(String UUID) {
        String[] args = new String[2];
        args[0] = "인증";
        args[1] = "정보요청";
        BackendIO.SendCMD_BACKEND(UUID, args, "DISCORD_CHECK_AUTH_INFO");
    }



    public static void RemoveVerify(Player p, String UUID) {
        String[] args = new String[2];
        args[0] = "인증";
        args[1] = "해제";
        BackendIO.SendCMD_BACKEND(UUID, args, "DISCORD_REMOVE_AUTH");
        SystemUtils.sendmessage(p, "&3&m-------------------------------------------");
        SystemUtils.sendmessage(p, "&9&l디스코드 연동을 제거입니다.");
        SystemUtils.sendmessage(p, "&3&m-------------------------------------------");
    }
}
