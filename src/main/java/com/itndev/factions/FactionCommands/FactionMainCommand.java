package com.itndev.factions.FactionCommands;

import com.itndev.factions.FactionCommands.FactionsCommands.*;
import com.itndev.factions.Config.Config;
import com.itndev.factions.Config.Lang;
import com.itndev.factions.Faction.Faction;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.SocketConnection.Socket;
import com.itndev.factions.Utils.*;
import com.itndev.factions.Utils.FactionList.FactionList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class FactionMainCommand implements CommandExecutor {

    //public MySQLManager sqlmanager = Main.getInstance().sqlmanager;

    //public MySQLUtils sqlUtils = Main.getInstance().sqlutils;
    private HashMap<String, Long> commandcooldown = new HashMap<>();

    private static DecimalFormat df = new DecimalFormat("0.00");

    //private static HashMap<UUID, Long> FactionCommandCoolDown = new HashMap<>();

    @Deprecated
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(label.equalsIgnoreCase("국가")) {
                if(!Socket.Connected) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f데이터베이스 서버와의 연결이 끊김으로써 명령어 사용에 제한이 걸립니다"));
                    return true;
                }
                if (commandcooldown.containsKey(sender.getName())) {
                    int cooldownTime = 2;
                    long secondsLeft = ((Long) commandcooldown.get(sender.getName())).longValue() / 1000L + cooldownTime - System.currentTimeMillis() / 1000L;
                    if (secondsLeft > 0L) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f해당 명령어는 &c" + secondsLeft + "&r&f초 후에 다시 사용 가능합니다"));
                        return true;
                    }
                }
                commandcooldown.put(sender.getName(), Long.valueOf(System.currentTimeMillis()));
                factioncommand(p, args);
            }
        }
        return false;
    }


    private void factioncommand(Player sender, String[] args) {
        if(args.length < 1) {
            FactionHelp.FactionHelp(sender);
            return;
        } else {
            try {
                String UUID = sender.getUniqueId().toString();
                if(args[0].equalsIgnoreCase("도움말")) {
                    FactionHelp.FactionHelp(sender);
                } else if(args[0].equalsIgnoreCase("생성")) {

                    //=================생성=================

                    CreateFaction.CreateFaction(sender, UUID, args);

                    //=================생성=================

                } else if(args[0].equalsIgnoreCase("해체")) {

                    //=================해체=================

                    DeleteFaction.DeleteFactionQueue(sender, UUID, args);

                    //=================해체=================

                } else if(args[0].equalsIgnoreCase("해체수락")) {

                    //=================해체수락=================

                    DeleteFaction.DeleteFaction(sender, UUID, args);

                    //=================해체수락=================

                } else if(args[0].equalsIgnoreCase("초대")) {

                    //=================초대=================

                    FactionInvite.FactionInvite(sender, UUID, args);

                    //=================초대=================

                } else if(args[0].equalsIgnoreCase("초대취소")) {

                    //=================초대취소=================

                    FactionInvite.FactionInviteCancel(sender, UUID, args);

                    //=================초대취소=================

                } else if(args[0].equalsIgnoreCase("수락")) {

                    //=================수락=================

                    FactionInvite.FactionInviteAccept(sender, UUID, args);

                    //=================수락=================

                } else if(args[0].equalsIgnoreCase("추방")) {

                    //=================추방=================

                    FactionKick.FactionKick(sender, UUID, args);

                    //=================추방=================

                } else if(args[0].equalsIgnoreCase("채팅")) {

                    //=================채팅=================

                    if(FactionUtils.isInFaction(UUID)) {
                        FactionChatToggle.FactionChatToggle(sender);
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================채팅=================

                } else if(args[0].equalsIgnoreCase("설정")) {

                    //=================설정=================

                    if (args.length < 2) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 설정 &7(설정)\n" +
                                "&a&o&l[ &r&f국가 &a&o&l] &r&f설정으로는 &7등급&8, &7설명&8, &7공지&8, &7스폰&8, &f(&7동맹&8, &7적대&8, &7중립&f) 이 있습니다.");
                        return;
                    }

                    if (FactionUtils.isInFaction(UUID)) {
                        if (args[1].equalsIgnoreCase("스폰")) {
                            FactionSpawn.SetFactionSpawn(sender);
                        } else {
                            BackendIO.SendCMD_BACKEND(UUID, args, "FACTION_SETTINGS");
                        }
                    }

                    //=================설정=================

                } else if(args[0].equalsIgnoreCase("전쟁")) {

                    //=================전쟁=================

                    BackendIO.SendCMD_BACKEND(UUID, args, "FACTION_WAR");

                    //=================전쟁=================

                } else if(args[0].equalsIgnoreCase("스폰")) {

                    //=================스폰=================

                    FactionSpawn.FactionSpawn(sender, UUID, args);

                    //=================스폰=================

                } else if(args[0].equalsIgnoreCase("영토")) {

                    //=================영토=================

                    ClaimLand.ClaimLand(sender, UUID, args);

                    //=================영토=================

                } else if(args[0].equalsIgnoreCase("금고")) {

                    //=================금고=================

                    FactionBank.FactionBank(sender, UUID, args);

                    //=================금고=================

                } else if(args[0].equalsIgnoreCase("정보")) {

                    //=================정보=================

                    FactionInfo.FactionInfo(sender, UUID, args);

                    //=================정보=================

                } else if(args[0].equalsIgnoreCase("소속")) {

                    //=================소속=================

                    PlayerFaction.PlayerFaction(sender, UUID, args);

                    //=================소속=================

                } else if(args[0].equalsIgnoreCase("나가기")) {

                    //=================나가기=================

                    FactionLeave.FactionLeave(sender, UUID, args);

                    //=================나가기=================

                } else if(args[0].equalsIgnoreCase("양도")) {

                    //=================양도=================

                    FactionLeaderPromote.FactionLeaderPromote(sender, UUID, args);

                    //=================양도=================

                } else if(args[0].equalsIgnoreCase("목록")) {

                    //=================목록=================

                    com.itndev.factions.FactionCommands.FactionsCommands.FactionList.FactionList(sender, UUID, args);

                    //=================목록=================

                } else if(args[0].equalsIgnoreCase("공지")) {

                    //=================공지=================

                    FactionAnnounce.FactionAnnounce(sender, UUID, args);

                    //=================공지=================

                } else if(args[0].equalsIgnoreCase("전초기지")) {

                    //=================전초기지점령=================

                    FactionOutPost.FactionOutPostMainCommand(sender, UUID, args);

                    //=================전초기지점령=================

                } else if(args[0].equalsIgnoreCase("워프메뉴")) {

                    //=================워프메뉴=================

                    FactionWarpMenu.SendFactionWarpMenu(sender, UUID, args);

                    //=================워프메뉴=================

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
