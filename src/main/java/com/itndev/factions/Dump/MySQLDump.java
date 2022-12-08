package com.itndev.factions.Dump;

import com.itndev.FaxLib.Utils.Data.MapFixer;
import com.itndev.factions.Main;
import com.itndev.factions.MySQL.HikariCP;
import com.itndev.factions.Storage.Faction.FactionStorage;
import com.itndev.factions.Utils.SystemUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MySQLDump {

    public static void LoadFromMySQL() throws SQLException {
        try {
            FactionStorage.FactionInfo = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionInfo")); //(ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionInfo");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionMember = MapFixer.get().Fixer_3_1(LoadHASHMAP("FactionMember"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionNameToFactionName = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionNameToFactionName"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionInfoList = MapFixer.get().Fixer_3_1(LoadHASHMAP("FactionInfoList"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionNameToFactionUUID = LoadHASHMAP("FactionNameToFactionUUID");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionOutPost = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionOutPost"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionOutPostList = MapFixer.get().Fixer_3_1(LoadHASHMAP("FactionOutPostList"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionRank = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionRank"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionToLand = MapFixer.get().Fixer_2_1(LoadHASHMAP("FactionToLand"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionToOutPost = MapFixer.get().Fixer_2_1(LoadHASHMAP("FactionToLand"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionUUIDToFactionName = LoadHASHMAP("FactionUUIDToFactionName");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionWarpLocations = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionWarpLocations"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.OutPostToFaction = LoadHASHMAP("OutPostToFaction");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.PlayerFaction = LoadHASHMAP("PlayerFaction");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        /*try {
            UserInfoStorage.namename = MapFixer.get().Fixer_1_2(LoadHASHMAP("namename"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            UserInfoStorage.uuidname = MapFixer.get().Fixer_1_2(LoadHASHMAP("uuidname"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            UserInfoStorage.nameuuid = MapFixer.get().Fixer_1_2(LoadHASHMAP("nameuuid"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            AuthStorage.DISCORDID_TO_UUID = LoadHASHMAP("DISCORDID_TO_UUID");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }

        try {
            AuthStorage.UUID_TO_DISCORDID = LoadHASHMAP("UUID_TO_DISCORDID");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
        }*/
    }

    private static HashMap<String, String> LoadHASHMAP(String MapName) throws SQLException {
        HashMap<String, String> finalmap = new HashMap<>();
        //Connection connection = SQL.getConnection().getHikariConnection();
        PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT * FROM FactionBackup WHERE MAP_NAME=?");
        ps.setString(1, MapName);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            finalmap.put(
                    rs.getString("MAP_KEY"),
                    rs.getString("MAP_VALUE")
            );
        }
        HikariCP.closeConnections(null, ps, rs);
        return finalmap;
    }
}
