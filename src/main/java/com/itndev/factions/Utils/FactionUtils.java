package com.itndev.factions.Utils;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Config.Lang;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.Faction.FactionStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FactionUtils {

    public static void SetPlayerRank(String UUID, String Rank) {
        if(Rank == null) {
            //JedisTempStorage.AddCommandToQueue("update:=:FactionRank:=:remove:=:" + UUID + ":=:add:=:" + Rank.toLowerCase(Locale.ROOT));
        } else {
            //JedisTempStorage.AddCommandToQueue("update:=:FactionRank:=:add:=:" + UUID + ":=:add:=:" + Rank.toLowerCase(Locale.ROOT));
        }
    }

    public static String getFormattedFaction(String UUID) {
        if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
            return "";
        } else {
            return "&f[&r&a" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID))) + " &r&f] &r";
        }
    }

    public static String getFormattedFaction2(String UUID) {
        if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
            return "";
        } else {
            return "&6[ &r&f" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID))) + " &r&6] &r";
        }
    }

    public static String getFormattedRank(String UUID) {
        if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
            return "";
        } else {
            return "&r&7" + FactionUtils.getPlayerLangRank(UUID);
        }
    }

    public static String getFormattedRank2(String UUID) {
        if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
            return "";
        } else {
            return "&r&7" + FactionUtils.getPlayerLangRank(UUID) + "&r ";
        }
    }

    public static String getFactionLeader(String FactionUUID) {
        //ArrayList<String> members = FactionStorage.FactionMember.get(FactionUUID);
        for(String UUID : FactionStorage.FactionMember.get(FactionUUID)) {
            if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
                return UUID;
            }
        }
        return null;
    }
    public static Boolean isExistingFaction(String FactionName) {
        return FactionStorage.FactionNameToFactionUUID.containsKey(FactionName.toLowerCase(Locale.ROOT));
    }

    public static String Build_PERMLVLINFO(int PERMLVL, int FINALPERMLVL, String SUCCESSID) {
        return ":=:" + Config.PERM_LEVEL_INDICATOR + PERMLVL + ":=:" + Config.PERM_FINAL_LEVEL_INDICATOR + FINALPERMLVL + ":=:" + Config.SUCCESS_ID_INDICATOR + SUCCESSID;
    }

    public static CompletableFuture<Boolean> AsyncNearByChunksAreOwned5(Location loc) {
        CompletableFuture<Boolean> Finalbool = new CompletableFuture<>();
        new Thread( () -> {
            ArrayList<Location> checklist = new ArrayList<>();
            ArrayList<CompletableFuture<Boolean>> checkfinishlist = new ArrayList<>();
            int x = loc.clone().getBlockX();
            int z = loc.clone().getBlockZ();
            int amount = 48;
            Location temploc1 = loc.clone();
            temploc1.setX(x + amount);
            temploc1.setZ(z + amount);
            checklist.add(temploc1);
            Location temploc2 = loc.clone();
            temploc2.setX(x - amount);
            temploc2.setZ(z - amount);
            checklist.add(temploc2);
            Location temploc3 = loc.clone();
            temploc3.setX(x + amount);
            temploc3.setZ(z - amount);
            checklist.add(temploc3);
            Location temploc4 = loc.clone();
            temploc4.setX(x - amount);
            temploc4.setZ(z + amount);
            checklist.add(temploc4);
            Location temploc5 = loc.clone();
            temploc5.setX(x + amount);
            checklist.add(temploc5);
            Location temploc6 = loc.clone();
            temploc6.setX(x - amount);
            checklist.add(temploc6);
            Location temploc7 = loc.clone();
            temploc7.setZ(z + amount);
            checklist.add(temploc7);
            Location temploc8 = loc.clone();
            temploc8.setZ(z - amount);
            checklist.add(temploc8);
            Location temploc9 = loc.clone();
            checklist.add(temploc9);
            Location temploc10 = loc.clone();
            temploc10.setX(x + 16);
            checklist.add(temploc10);
            Location temploc11 = loc.clone();
            temploc11.setZ(z + 16);
            checklist.add(temploc11);
            Location temploc12 = loc.clone();
            temploc12.setX(x - 16);
            checklist.add(temploc12);
            Location temploc13 = loc.clone();
            temploc13.setZ(z - 16);
            checklist.add(temploc13);
            for(Location temploc : checklist) {
                checkfinishlist.add(AsyncNearByChunksAreOwned(temploc));
            }
            Boolean isfinished = false;
            for(CompletableFuture<Boolean> finalboolean : checkfinishlist) {
                try {
                    if(finalboolean.get()) {
                        Finalbool.complete(true);
                        isfinished = true;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            if(!isfinished) {
                Finalbool.complete(false);
            }

        }).start();
        return Finalbool;
    }

    public static Boolean AreNearByPlayersEnemies(Player p, double Radius) {
        for(Player near : p.getLocation().getNearbyPlayers(Radius)) {
            if(!isSameFaction(p.getUniqueId().toString(), near.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isExistingOutPost(String FactionUUID, String OutPostName) {
        if(FactionStorage.FactionOutPostList.containsKey(FactionUUID)) {
            return FactionStorage.FactionOutPostList.get(FactionUUID).contains(OutPostName.toLowerCase(Locale.ROOT));
        } else {
            return false;
        }
    }

    public static Boolean NearByChunksAreOwned(Location loc) {
        Location temploc1 = loc.clone();
        Location temploc2 = loc.clone();
        Location temploc3 = loc.clone();
        Location temploc4 = loc.clone();
        Location templocC1 = loc.clone();
        Location templocC2 = loc.clone();
        Location templocC3 = loc.clone();
        Location templocC4 = loc.clone();

        temploc1.setX(loc.getX() + 16);

        temploc2.setZ(loc.getZ() + 16);

        temploc3.setX(loc.getX() - 16);

        temploc2.setZ(loc.getZ() - 16);

        templocC1.setX(loc.getX() + 16);
        templocC1.setZ(loc.getZ() + 16);

        templocC2.setX(loc.getX() - 16);
        templocC2.setZ(loc.getZ() + 16);

        templocC3.setX(loc.getX() + 16);
        templocC3.setZ(loc.getZ() - 16);

        templocC4.setX(loc.getX() - 16);
        templocC4.setZ(loc.getZ() - 16);

        if(FactionUtils.isClaimed(temploc1)) {
            return false;
        } else if(FactionUtils.isClaimed(temploc2)) {
            return false;
        } else if(FactionUtils.isClaimed(temploc3)) {
            return false;
        } else if(FactionUtils.isClaimed(temploc4)) {
            return false;
        } else if(FactionUtils.isClaimed(templocC1)) {
            return false;
        } else if(FactionUtils.isClaimed(templocC2)) {
            return false;
        } else if(FactionUtils.isClaimed(templocC3)) {
            return false;
        } else if(FactionUtils.isClaimed(templocC4)) {
            return false;
        } else {
            return true;
        }

    }

    public static CompletableFuture<Boolean> AsyncNearByChunksAreOwned(Location loc) {
        CompletableFuture<Boolean> Finalbool = new CompletableFuture<>();
        new Thread( () -> {
            Location temploc0 = loc.clone();
            Location temploc1 = loc.clone();
            Location temploc2 = loc.clone();
            Location temploc3 = loc.clone();
            Location temploc4 = loc.clone();
            Location templocC1 = loc.clone();
            Location templocC2 = loc.clone();
            Location templocC3 = loc.clone();
            Location templocC4 = loc.clone();

            temploc1.setX(loc.getX() + 16);

            temploc2.setZ(loc.getZ() + 16);

            temploc3.setX(loc.getX() - 16);

            temploc2.setZ(loc.getZ() - 16);

            templocC1.setX(loc.getX() + 16);
            templocC1.setZ(loc.getZ() + 16);

            templocC2.setX(loc.getX() - 16);
            templocC2.setZ(loc.getZ() + 16);

            templocC3.setX(loc.getX() + 16);
            templocC3.setZ(loc.getZ() - 16);

            templocC4.setX(loc.getX() - 16);
            templocC4.setZ(loc.getZ() - 16);

            Boolean isFinished = false;

            if(FactionUtils.isClaimed(temploc0)) {
                Finalbool.complete(true);
                isFinished = true;
            }
            if(FactionUtils.isClaimed(temploc1) && !isFinished) {
                Finalbool.complete(true);
                isFinished = true;
            }
            if(FactionUtils.isClaimed(temploc2) && !isFinished) {
                Finalbool.complete(true);
                isFinished = true;
            }
            if(FactionUtils.isClaimed(temploc3) && !isFinished) {
                Finalbool.complete(true);
                isFinished = true;
            }
            if(FactionUtils.isClaimed(temploc4) && !isFinished) {
                Finalbool.complete(true);
                isFinished = true;
            }
            if(FactionUtils.isClaimed(templocC1) && !isFinished) {
                Finalbool.complete(true);
                isFinished = true;
            }
            if(FactionUtils.isClaimed(templocC2) && !isFinished) {
                Finalbool.complete(true);
                isFinished = true;
            }
            if(FactionUtils.isClaimed(templocC3) && !isFinished) {
                Finalbool.complete(true);
                isFinished = true;
            }
            if(FactionUtils.isClaimed(templocC4) && !isFinished) {
                Finalbool.complete(true);
                isFinished = true;
            }
            if(!isFinished) {
                Finalbool.complete(false);
            }
        }).start();

        return Finalbool;

    }



    public static void SendFactionMessage(String playeruuid, String targetuuid, String type, String message) {
        if(type.equalsIgnoreCase("single")) {
            //type : SIBAL, TeamChat, all
            JedisTempStorage.AddCommandToQueue_INNER("notify:=:" + CommonUtils.String2Byte(playeruuid) + ":=:" + CommonUtils.String2Byte(targetuuid) + ":=:" + CommonUtils.String2Byte(message) + ":=:" + "false");
        } else {
            JedisTempStorage.AddCommandToQueue_INNER("notify:=:" + CommonUtils.String2Byte(playeruuid) + ":=:" + CommonUtils.String2Byte(type) + ":=:" + CommonUtils.String2Byte(message) + ":=:" + "true");
        }
    }

    public static void SendFactionMessage_GETRAWCMD(String playeruuid, String targetuuid, String type, String message) {
        if(type.equalsIgnoreCase("single")) {
            //type : SIBAL, TeamChat, all
            JedisTempStorage.AddCommandToQueue_INNER("notify:=:" + CommonUtils.String2Byte(playeruuid) + ":=:" + CommonUtils.String2Byte(targetuuid) + ":=:" + CommonUtils.String2Byte(message) + ":=:" + "false");
        } else {
            JedisTempStorage.AddCommandToQueue_INNER("notify:=:" + CommonUtils.String2Byte(playeruuid) + ":=:" + CommonUtils.String2Byte(type) + ":=:" + CommonUtils.String2Byte(message) + ":=:" + "true");
        }
    }

    public static void SendFactionChat(String senderUUID, String finalmessage) {
        JedisTempStorage.AddCommandToQueue_INNER("chat" + ":=:" + CommonUtils.String2Byte(senderUUID) + ":=:" + CommonUtils.String2Byte(finalmessage));
    }

    public static void ClaimLand(String FactionUUID, String Chunkkey) {
        FactionStorage.LandToFaction.put(Chunkkey, FactionUUID);
        JedisTempStorage.AddCommandToQueue_INNER("update:=:LandToFaction:=:add:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:" + Main.ServerName);
        ArrayList<String> updatelist = new ArrayList<>();
        if(FactionStorage.FactionToLand.containsKey(FactionUUID)) {
            updatelist = FactionStorage.FactionToLand.get(FactionUUID);
        }
        updatelist.add(Chunkkey);
        FactionStorage.FactionToLand.put(FactionUUID, updatelist);
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionToLand:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:" + Main.ServerName);
    }

    public static void UnClaimLand(String FactionUUID, String Chunkkey) {
        FactionStorage.LandToFaction.remove(Chunkkey);
        JedisTempStorage.AddCommandToQueue_INNER("update:=:LandToFaction:=:remove:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:" + Main.ServerName);
        ArrayList<String> updatelist = FactionStorage.FactionToLand.get(FactionUUID);
        updatelist.remove(Chunkkey);
        if(updatelist.isEmpty()) {
            FactionStorage.FactionToLand.remove(FactionUUID);
        } else {
            FactionStorage.FactionToLand.put(FactionUUID, updatelist);
        }

        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionToLand:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:" + Main.ServerName);
    }

    public static void ClaimOutPost(String FactionUUID, String Chunkkey) {
        FactionStorage.OutPostToFaction.put(Chunkkey, FactionUUID);
        JedisTempStorage.AddCommandToQueue_INNER("update:=:OutPostToFaction:=:add:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:" + Main.ServerName);
        ArrayList<String> updatelist = new ArrayList<>();
        if(FactionStorage.FactionToOutPost.containsKey(FactionUUID)) {
            updatelist = FactionStorage.FactionToOutPost.get(FactionUUID);
        }
        updatelist.add(Chunkkey);
        FactionStorage.FactionToOutPost.put(FactionUUID, updatelist);
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionToOutPost:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:" + Main.ServerName);
    }

    public static void UnClaimOutPost(String FactionUUID, String Chunkkey) {
        FactionStorage.OutPostToFaction.remove(Chunkkey);
        JedisTempStorage.AddCommandToQueue_INNER("update:=:OutPostToFaction:=:remove:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:" + Main.ServerName);
        ArrayList<String> updatelist = FactionStorage.FactionToOutPost.get(FactionUUID);
        updatelist.remove(Chunkkey);
        if(updatelist.isEmpty()) {
            FactionStorage.FactionToOutPost.remove(FactionUUID);
        } else {
            FactionStorage.FactionToOutPost.put(FactionUUID, updatelist);
        }
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionToOutPost:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:" + Main.ServerName);
    }

    public static Boolean isOutPost(Location loc) {
        String Chunkkey = FactionUtils.getChunkKey(loc);
        return FactionStorage.OutPostToFaction.containsKey(Chunkkey);
    }

    public static Boolean isMyOutPost(String FactionUUID, Location loc) {
        return FactionStorage.OutPostToFaction.get(FactionUtils.getChunkKey(loc)).equals(FactionUUID);
    }

    public static Boolean isFactionsClaim(Location loc, String FactionUUID) {
        if(!FactionUtils.isClaimed(loc)) {
            return false;
        }
        return FactionUtils.getFactionByClaim(FactionUtils.getChunkKey(loc)).equals(FactionUUID);
    }

    public static Boolean isSameClaimFaction(Location loc1, Location loc2) {
        if(!isClaimed(loc1) && !isClaimed(loc2)) {
            return true;
        } else if(Objects.equals(FactionUtils.AsyncWhosClaim(loc1), FactionUtils.AsyncWhosClaim(loc2))) {
            return true;
        } else {
            return false;
        }
    }

    public static String GetOutPostOwner(Location loc) {
        return FactionStorage.OutPostToFaction.get(FactionUtils.getChunkKey(loc));
    }

    public static String GetClaimFaction(Location loc) {
        if(!isClaimed(loc) && !isOutPost(loc) && !isICEDLand(FactionUtils.getChunkKey(loc))) {
            return "&2야생";
        } else if(isClaimed(loc)){
            return "&a" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.AsyncWhosClaim(loc))) + "&r";
        } else if(isOutPost(loc)) {
            return "&a" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.GetOutPostOwner(loc))) + "&7(전초기지)&r";
        } else if(isICEDLand(FactionUtils.getChunkKey(loc))) {
            return "&7(구)" + getICEDLandName(getICEDLand(FactionUtils.getChunkKey(loc))) + "(멸망후 황무지)&r";
        } else {
            return "NULL";
        }
    }

    public static Boolean isAExistingLangRank(String LangRank) {
        String lowcaserank = LangRank.toLowerCase(Locale.ROOT);
        if(lowcaserank.equalsIgnoreCase(Config.Leader_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.CoLeader_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.VipMember_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.Warrior_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.Member_Lang)) {
            return true;
        } else {
            return false;
        }
    }


    @Deprecated
    public static void FactionUUIDNotify(String FactionUUID, String Message) {
        for(String UUID : FactionUtils.getFactionMember(FactionUUID)) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(UserInfoUtils.getPlayerName(UUID));
            if(op.isOnline()) {
                Player p = (Player) op;
                SystemUtils.sendmessage(p, SystemUtils.colorize(Message));
            }
        }
    }

    public static void FactionNotify(String playeruuid, String targetuuid, String message, String trueorfalse) {
        if(trueorfalse.equalsIgnoreCase("true")) {
            if(targetuuid.equalsIgnoreCase("all")) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    SystemUtils.sendmessage(p, message);
                }
            } else if (targetuuid.equalsIgnoreCase("SIBAL")) {
                if(FactionUtils.isInFaction(playeruuid)) {
                    for (String playeruuids : FactionUtils.getFactionMember(FactionUtils.getPlayerFactionUUID(playeruuid))) {
                        if (playeruuids != targetuuid) {
                            SystemUtils.sendUUIDmessage(playeruuids, "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message);
                        }
                    }

                }
            } else if(targetuuid.equalsIgnoreCase("TeamChat")){
                if(FactionUtils.isInFaction(playeruuid)) {
                    for (String playeruuids : FactionUtils.getFactionMember(FactionUtils.getPlayerFactionUUID(playeruuid))) {
                        SystemUtils.sendUUIDmessage(playeruuids, "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message);
                    }
                }
            } else {
                if(FactionUtils.isInFaction(playeruuid)) {
                    for (String playeruuids : FactionUtils.getFactionMember(FactionUtils.getPlayerFactionUUID(playeruuid))) {
                        if (!playeruuids.equals(targetuuid)) {
                            SystemUtils.sendUUIDmessage(playeruuids, "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message);
                        }
                    }

                    SystemUtils.sendUUIDmessage(targetuuid, "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message);
                }
                //Bukkit.getPlayer(listener.uuid2name(targetuuid)).sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f팀 &a&o&l] &r&f" + message));
            }
        } else {

            OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(playeruuid));
            if(targetuuid.equalsIgnoreCase("puremessagesendoptiontrue")) {
                if(op.isOnline()) {
                    Player s = (Player) op;
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            } else
            if(op.isOnline()) {
                Player p = (Player) op;
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message));
            } else if(Bukkit.getOfflinePlayer(UUID.fromString(targetuuid)).isOnline()) {
                Player p = (Player) Bukkit.getOfflinePlayer(UUID.fromString(targetuuid));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message));
            }
            //SystemUtils.sendUUIDmessage(playeruuid, );
            //Bukkit.getPlayer(listener.uuid2name(targetuuid)).sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f팀 &a&o&l] &r&f" + message));
        }
    }

    public static void FactionChat(String UUID, String message) {
        for(String UUIDs : FactionUtils.getFactionMember(FactionUtils.getPlayerFactionUUID(UUID))) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(java.util.UUID.fromString(UUIDs));
            if(op.isOnline()) {
                Player p = (Player) op;
                p.sendRawMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &f국가채팅 &a&o&l] &r&7" + FactionUtils.getPlayerLangRank(UUID) + " &f" + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(UUID)) + " &7: &r&7") + SystemUtils.colorize(message));
            }
        }
    }

    public static String getFactionByClaim(String ChunkKey) {
        if(FactionStorage.LandToFaction.containsKey(ChunkKey)) {
            return FactionStorage.LandToFaction.get(ChunkKey);
        }
        return null;
    }


    //=====================================================
    //                 Get FactionUUID of Player
    public static String getPlayerFactionUUID(String uuid) {
        String finalUUID = null;
        if(FactionStorage.PlayerFaction.containsKey(uuid)) {
            finalUUID = FactionStorage.PlayerFaction.get(uuid);
        }
        return finalUUID;
    }
    public static String getPlayerFactionUUID(UUID uuid) {
        String finalUUID = null;
        if(FactionStorage.PlayerFaction.containsKey(uuid.toString())) {
            finalUUID = FactionStorage.PlayerFaction.get(uuid.toString());
        }
        return finalUUID;
    }
    //=====================================================

    public static String getFactionName(String FactionUUID) {
        String finalname = null;
        if(FactionStorage.FactionUUIDToFactionName.containsKey(FactionUUID)) {
            finalname = FactionStorage.FactionUUIDToFactionName.get(FactionUUID);
        }
        return finalname;
    }

    public static String getFactionUUID(String FactionName) {
        String finalUUID = null;
        if(FactionStorage.FactionNameToFactionUUID.containsKey(FactionName.toLowerCase(Locale.ROOT))) {
            finalUUID = FactionStorage.FactionNameToFactionUUID.get(FactionName.toLowerCase(Locale.ROOT));
        }
        return finalUUID;
    }

    public static String getCappedFactionName(String FactionName) {
        if(FactionStorage.FactionNameToFactionName.containsKey(FactionName.toLowerCase(Locale.ROOT))) {
            return FactionStorage.FactionNameToFactionName.get(FactionName.toLowerCase(Locale.ROOT));
        }
        return null;
    }

    public static ArrayList<String> getFactionMember(String FactionUUID) {
        ArrayList<String> finallist = new ArrayList<>();
        if(FactionStorage.FactionMember.containsKey(FactionUUID)) {
            finallist = FactionStorage.FactionMember.get(FactionUUID);
        }
        return finallist;
    }

    public static String getPlayerRank(String UUID) {
        if(FactionStorage.FactionRank.containsKey(UUID)) {
            return FactionStorage.FactionRank.get(UUID);
        } else {
            return Config.Nomad;
        }
    }

    public static String LangRankConvert(String Rank) {
        if(Rank.equalsIgnoreCase(Config.Nomad)) {
            return Config.Nomad_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.Member)) {
            return Config.Member_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.Warrior)) {
            return Config.Warrior_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.VipMember)) {
            return Config.VipMember_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.CoLeader)) {
            return Config.CoLeader_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.Leader)) {
            return Config.Leader_Lang;
        }
        return null;
    }

    public static String RankConvert(String Rank) {
        if(Rank.equalsIgnoreCase(Config.Nomad_Lang)) {
            return Config.Nomad;
        } else
        if(Rank.equalsIgnoreCase(Config.Member_Lang)) {
            return Config.Member;
        } else
        if(Rank.equalsIgnoreCase(Config.Warrior_Lang)) {
            return Config.Warrior;
        } else
        if(Rank.equalsIgnoreCase(Config.VipMember_Lang)) {
            return Config.VipMember;
        } else
        if(Rank.equalsIgnoreCase(Config.CoLeader_Lang)) {
            return Config.CoLeader;
        } else
        if(Rank.equalsIgnoreCase(Config.Leader_Lang)) {
            return Config.Leader;
        }
        return null;
    }

    public static String FactionStatusConvert(String Status) {
        if(Status.equalsIgnoreCase(Config.Ally_Lang)) {
            return Config.Ally;
        } else if(Status.equalsIgnoreCase(Config.Enemy_Lang)) {
            return Config.Enemy;
        } else if(Status.equalsIgnoreCase(Config.Neutral_Lang)) {
            return Config.Neutral;
        } else {
            return null;
        }
    }

    public static String FactionLangStatusConvert(String Status) {
        if(Status.equalsIgnoreCase(Config.Ally)) {
            return Config.Ally_Lang;
        } else if(Status.equalsIgnoreCase(Config.Enemy)) {
            return Config.Enemy_Lang;
        } else if(Status.equalsIgnoreCase(Config.Neutral)) {
            return Config.Neutral_Lang;
        } else {
            return null;
        }
    }

    public static String getPlayerLangRank(String UUID) {
        if(FactionStorage.FactionRank.containsKey(UUID)) {
            return LangRankConvert(FactionStorage.FactionRank.get(UUID));
        } else {
            return LangRankConvert(Config.Nomad);
        }
    }

    public static String getChunkKey(Location loc) {
        return Main.ServerName + "=" + loc.getWorld().getName() + "=" + loc.getChunk().getChunkKey();
    }

    public static Boolean isClaimed(Location loc) {
        return FactionStorage.LandToFaction.containsKey(getChunkKey(loc));
    }


    //=============================================================
    //                   IS IN FACTION
    public static Boolean isInFaction(String uuid) {
        return FactionStorage.PlayerFaction.containsKey(uuid);
    }
    public static Boolean isInFaction(UUID uuid) {
        return FactionStorage.PlayerFaction.containsKey(uuid.toString());
    }
    //=============================================================


    //=============================================================
    //                     IS SAME FACTION
    public static Boolean isSameFaction(String UUID, String UUID2) {
        if(isInFaction(UUID) && isInFaction(UUID2)) {
            return (getPlayerFactionUUID(UUID).equalsIgnoreCase(getPlayerFactionUUID(UUID2)));
        }
        return false;
    }
    public static Boolean isSameFaction(UUID uuid, UUID uuid2) {
        if(isInFaction(uuid) && isInFaction(uuid)) {
            return (getPlayerFactionUUID(uuid).equalsIgnoreCase(getPlayerFactionUUID(uuid2)));
        }
        return false;
    }
    //=============================================================

    public static String AsyncWhosClaim(Location loc) {
        if(FactionStorage.LandToFaction.containsKey(getChunkKey(loc))) {
            return FactionStorage.LandToFaction.get(getChunkKey(loc));
        }
        return null;
    }

    public static String WhosClaim(Location loc) {
        if(FactionStorage.LandToFaction.containsKey(getChunkKey(loc))) {
            return FactionStorage.LandToFaction.get(getChunkKey(loc));
        }
        return null;
    }

    public static Boolean HigherThenorSameRank(String UUID, String Rank) {
        String PlayerRank = FactionUtils.getPlayerRank(UUID);
        if(RankPrio(PlayerRank) >= RankPrio(Rank)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isICEDLand(String Chunkkey) {
        return FactionStorage.DESTORYED_LandToFaction.containsKey(Chunkkey);
    }

    public static String getICEDLand(String Chunkkey) {
        return FactionStorage.DESTORYED_LandToFaction.get(Chunkkey);
    }

    public static String getICEDLandName(String ICEDUUID) {
        return FactionStorage.DESTROYED_FactionUUIDToFactionName.get(ICEDUUID);
    }

    public static Boolean HigherThenRank(String UUID, String Rank) {
        String PlayerRank = FactionUtils.getPlayerRank(UUID);
        if(RankPrio(PlayerRank) > RankPrio(Rank)) {
            return true;
        } else {
            return false;
        }
    }

    public static Integer RankPrio(String Rank) {
        if(Rank.equalsIgnoreCase(Config.Leader)) {
            return 6;
        } else if(Rank.equalsIgnoreCase(Config.CoLeader)) {
            return 5;
        } else if(Rank.equalsIgnoreCase(Config.VipMember)) {
            return 4;
        } else if(Rank.equalsIgnoreCase(Config.Warrior)) {
            return 3;
        } else if(Rank.equalsIgnoreCase(Config.Member)) {
            return 2;
        } else {
            return 1;
        }
    }

    public static void SetFactionSpawn(String FactionUUID, String ConvertLoc) {
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=spawn") + ":=:add:=:" + CommonUtils.String2Byte(Main.ServerName + "===" + ConvertLoc));
        RegisterFactionInfo(FactionUUID, "spawn");
    }

    public static Boolean FactionSpawnExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=spawn");
    }

    public static String getFactionSpawn(String FactionUUID) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=spawn");
    }

    public static void ClearFactionInfo(String FactionUUID) {
        if(FactionStorage.FactionInfo.containsKey(FactionUUID)) {
            for (String key : FactionStorage.FactionInfoList.get(FactionUUID)) {
                JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:remove:=:" + CommonUtils.String2Byte(FactionUUID + "=" + key) + ":=:remove:=:" + CommonUtils.String2Byte(FactionUUID));
            }
            JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfoList:=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte(FactionUUID));
        }
    }

    public static void RemoveFactionSpawn(String FactionUUID) {
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:remove:=:" + CommonUtils.String2Byte(FactionUUID + "=spawn") + ":=:add:=:" + CommonUtils.String2Byte(Main.ServerName));
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfoList:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte("spawn"));
    }

    public static void SetFactionNotice(String FactionUUID, String factionnotice) {
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=notice") + ":=:add:=:" + CommonUtils.String2Byte(factionnotice));
        RegisterFactionInfo(FactionUUID, "notice");
    }

    public static Boolean FactionNoticeExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=notice");
    }

    public static String GetFactionNotice(String FactionUUID) {
        if(!FactionStorage.FactionInfo.containsKey(FactionUUID + "=notice")) {
            return Lang.FACTION_DEFAULT_NOTICE;
        } else {
            return FactionStorage.FactionInfo.get(FactionUUID + "=notice");
        }
    }

    public static String GetFactionOutPostWarpLocation(String FactionUUID, String OutPostName) {
        String key = FactionUUID + "=warplocation=" + OutPostName;
        return FactionStorage.FactionInfo.get(key);
    }

    public static String GetBeaconLocation(String FactionUUID, String OutPostName) {
        String key = FactionUUID + "=beacon=" + OutPostName;
        return FactionStorage.FactionInfo.get(key);
    }

    public static String GetFactionOutPostName(String Chunkkey) {
        if(FactionStorage.OutPostToFaction.containsKey(Chunkkey)) {
            String FactionUUID = FactionStorage.OutPostToFaction.get(Chunkkey);
            String key = FactionUUID + "=outpost=" + Chunkkey;
            return FactionStorage.FactionInfo.get(key);
        } else {
            return null;
        }
    }

    public static String GetFactionOutPostChunkkey(String FactionUUID, String OutPostName) {
        String key = FactionUUID + "=" + OutPostName;
        return FactionStorage.FactionOutPost.get(key);
    }

    public static void SetBeaconLocation(String FactionUUID, String OutPostName, Location BeaconLocation) {
        String location = SystemUtils.loc2string(BeaconLocation);
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=beacon=" + OutPostName) + ":=:add:=:" + CommonUtils.String2Byte(location));
    }

    public static void SetFactionOutPostWarpLocation(String FactionUUID, String Chunkkey, Location loc, String OutPostName) {
        String location = SystemUtils.loc2string(loc);
        SetFactionOutPostName(FactionUUID, Chunkkey, OutPostName);
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=warplocation=" + OutPostName) + ":=:add:=:" + CommonUtils.String2Byte(Main.ServerName + "===" + location));
    }

    public static void SetFactionOutPostName(String FactionUUID, String Chunkkey, String OutPostName) {
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=outpost=" + Chunkkey) + ":=:add:=:" + CommonUtils.String2Byte(OutPostName));
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionOutPost:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=" + OutPostName) + ":=:add:=:" + CommonUtils.String2Byte(Chunkkey));
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionOutPostList:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(OutPostName));
        RegisterFactionInfo(FactionUUID, Chunkkey);
    }

    public static void RemoveFactionNotice(String FactionUUID) {
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:remove:=:" + CommonUtils.String2Byte(FactionUUID + "=notice") + ":=:add:=:" + CommonUtils.String2Byte("D"));
    }

    public static void SetFactionDesc(String FactionUUID, String factionDesc) {
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=desc") + ":=:add:=:" + CommonUtils.String2Byte(factionDesc));
        RegisterFactionInfo(FactionUUID, "desc");
    }

    public static Boolean FactionDescExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=desc");
    }

    public static String GetFactionDesc(String FactionUUID) {
        if(!FactionStorage.FactionInfo.containsKey(FactionUUID + "=desc")) {
            return Lang.FACTION_DEFAULT_DESC;
        } else {
            return FactionStorage.FactionInfo.get(FactionUUID + "=desc");
        }
    }

    public static void RemoveFactionDesc(String FactionUUID) {
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:remove:=:" + CommonUtils.String2Byte(FactionUUID + "=desc") + ":=:add:=:" + CommonUtils.String2Byte("D"));
    }

    public static void WarpLocation(String UUID, String ServerName, String Location, Boolean isExpire) {
        if(!isExpire) {
            JedisTempStorage.AddCommandToQueue_INNER("warplocation:=:" + CommonUtils.String2Byte(UUID) + ":=:" + CommonUtils.String2Byte(ServerName) + ":=:" + CommonUtils.String2Byte(Location) + ":=:" + CommonUtils.String2Byte("notexpired"));
        } else {
            JedisTempStorage.AddCommandToQueue_INNER("warplocation:=:" + CommonUtils.String2Byte(UUID) + ":=:" + CommonUtils.String2Byte(ServerName) + ":=:" + CommonUtils.String2Byte(Location) + ":=:" + CommonUtils.String2Byte("expired"));
        }
    }

    public static void RegisterFactionInfo(String FactionUUID, String type) {
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfoList:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(type));
    }

    public static Boolean isInWar(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=war");
    }

    public static String getOPPWar(String FactionUUID) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=war");
    }

    public static Boolean hasMainBeacon(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=mainbeacon");
    }

    public static String getMainBeaconStringLocation(String FactionUUID) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=mainbeacon");
    }

    public static String getLocalLocation(Location loc) {
        return Main.ServerName + "===" + SystemUtils.loc2string(loc);
    }

    public static Boolean isMainBeaconChunk(String FactionUUID, Location loc) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=mainbeaconchunk").equals(getChunkKey(loc));
    }

    public static String getMainBeaconChunk(String FactionUUID) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=mainbeaconchunk");
    }

    public static Boolean isMainBeaconLocation(String FactionUUID, Location loc) {
        return getMainBeaconStringLocation(FactionUUID).equals(getLocalLocation(loc));
    }

    public static void setMainBeaconStringLocation(String FactionUUID, Location loc) {
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=mainbeacon") + ":=:add:=:" + CommonUtils.String2Byte(Main.ServerName + "===" + SystemUtils.loc2string(loc)));
        JedisTempStorage.AddCommandToQueue_INNER("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=mainbeaconchunk") + ":=:add:=:" + CommonUtils.String2Byte(getChunkKey(loc)));
        RegisterFactionInfo(FactionUUID, "mainbeacon");
        RegisterFactionInfo(FactionUUID, "mainbeaconchunk");
    }
}
