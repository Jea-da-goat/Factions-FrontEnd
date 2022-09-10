package com.itndev.factions.MySQL;

import com.itndev.factions.Main;
import com.itndev.factions.Utils.CacheUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class wtfDatabase {

    public static void CacheFactionDTR(String FactionUUID) {
        new Thread( () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT " +
                        "FactionDTR FROM FactionDTR WHERE FactionUUID='" + FactionUUID + "'");
                ResultSet rs = ps.executeQuery();
                double DTR = 0;
                if(rs.next()) {
                    DTR = Double.parseDouble(rs.getString("FactionDTR"));
                    CacheUtils.UpdateCachedDTR(FactionUUID, DTR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void CacheFactionBank(String FactionUUID) {
        new Thread( () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT " +
                        "FactionBank FROM FactionBank WHERE FactionUUID='" + FactionUUID + "'");
                ResultSet rs = ps.executeQuery();
                double Bank = 0;
                if(rs.next()) {
                    Bank = Double.parseDouble(rs.getString("FactionBank"));
                    CacheUtils.UpdateCachedBank(FactionUUID, Bank);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public CompletableFuture<Double> GetFactionDTR(String FactionUUID) {
        CompletableFuture<Double> FutureDTR = new CompletableFuture();
        new Thread( () -> {
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT " +
                        "FactionDTR FROM FactionDTR WHERE FactionUUID='" + FactionUUID + "'");
                ResultSet rs = ps.executeQuery();
                double DTR = 0;
                if(rs.next()) {
                    DTR = Double.parseDouble(rs.getString("FactionDTR"));
                    FutureDTR.complete(DTR);
                    CacheUtils.UpdateCachedDTR(FactionUUID, DTR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return FutureDTR;
    }

    public CompletableFuture<Double> GetFactionBank(String FactionUUID) {
        CompletableFuture<Double> FutureBank = new CompletableFuture();
        new Thread( () -> {
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT " +
                        "FactionBank FROM FactionBank WHERE FactionUUID='" + FactionUUID + "'");
                ResultSet rs = ps.executeQuery();
                double Bank = 0;
                if(rs.next()) {
                    Bank = Double.parseDouble(rs.getString("FactionBank"));
                    FutureBank.complete(Bank);
                    CacheUtils.UpdateCachedBank(FactionUUID, Bank);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return FutureBank;
    }

    public CompletableFuture<Double> AddFactionBank(String FactionUUID, double Bank) {
        CompletableFuture<Double> futureBank = new CompletableFuture<>();
        new Thread( () ->{
            try {

                String cmd = "Call UPDATEBANK(0," + String.valueOf(Bank) + ",0,'" + FactionUUID + "')";
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement(cmd);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    double Bankf = rs.getDouble("FINALBANK");
                    CacheUtils.UpdateCachedBank(FactionUUID, Bankf);
                    futureBank.complete(Bankf);
                } else {
                    futureBank.complete(-420D);
                }
                CacheFactionBank(FactionUUID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return futureBank;
    }

}