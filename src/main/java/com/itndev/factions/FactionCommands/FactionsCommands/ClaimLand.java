package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClaimLand {

    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void ClaimLand(Player sender, String UUID, String[] args) {
        if (args.length < 2) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 영토 &7(구매/해제)\n" +
                    "&7(영토는 청크단위로 구매하며 청크당 가격이 " + Config.LandClaimPrice + "원 입니다. 또한 한 청크의 경계는 F3 + G 로 확인 가능합니다)");
            return;
        }

        if (FactionUtils.isInFaction(UUID)) {

            if (!FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                return;
            }
            Boolean Buy = null;
            if (args[1].equalsIgnoreCase("구매")) {
                Buy = true;
            } else if (args[1].equalsIgnoreCase("해제")) {
                Buy = false;
            } else {
                SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 영토 &7(구매/해제)\n" +
                        "&7(영토는 청크단위로 구매하며 청크당 가격이 " + Config.LandClaimPrice + "원 입니다. 또한 한 청크의 경계는 F3 + G 로 확인 가능합니다)");
                return;
            }
            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            Location loc = sender.getLocation();
            String Chunkkey = FactionUtils.getChunkKey(loc);
            if (Buy) {
                if(FactionUtils.isInWar(FactionUUID)) {
                    SystemUtils.sendfactionmessage(sender, "&r&f전쟁 도중에는 영토를 해제할수 없습니다");
                    return;
                }
                if (FactionUtils.isClaimed(loc)) {
                    if (FactionUtils.WhosClaim(loc).equalsIgnoreCase(FactionUUID)) {
                        SystemUtils.sendfactionmessage(sender, "&r&f해당 영토는 이미 당신의 국가가 점령을 했습니다");
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f해당 영토는 다른 국가에 의해 점령되있습니다");
                    }
                } else if(FactionUtils.isOutPost(loc)) {
                    if (FactionUtils.GetOutPostOwner(loc).equalsIgnoreCase(FactionUUID)) {
                        SystemUtils.sendfactionmessage(sender, "&r&f해당 영토는 이미 당신의 국가의 전초기지입니다");
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f해당 영토는 다른 국가의 전초기지입니다");
                    }
                } else if(FactionUtils.isICEDLand(Chunkkey)) {
                    SystemUtils.sendfactionmessage(sender, "&r&f해당 영토는 국가가 멸망한후 남은 황무지입니다. 국가 멸망후 1시간 동안은 점령이 불가능하고 약탈이 가능합니다");
                } else {
                    FactionUtils.ClaimLand(FactionUUID, Chunkkey);
                    new Thread( () -> {
                        try {
                            CompletableFuture<Double> futurebank = Main.database.AddFactionBank(FactionUUID, Config.LandClaimPrice);
                            double FinalBank = 0;
                            FinalBank = futurebank.get(30, TimeUnit.MILLISECONDS);
                            if (FinalBank >= 0) {
                                //FactionUtils.ClaimLand(FactionUUID, Chunkkey);
                                SystemUtils.sendfactionmessage(sender, "&r&f성공적으로 해당 영토&r&7(청크)&r&f를 구매했습니다. \n" +
                                        "&r&7(남은금액 : " + df.format(FinalBank) + "원)");

                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&f영토를 구매하려면 적어도 국가 금고에 " + Config.LandClaimPrice + "원 초과가 있어야 합니다");
                                FactionUtils.UnClaimLand(FactionUUID, Chunkkey);
                            }
                        } catch (ExecutionException | InterruptedException | TimeoutException e) {
                            SystemUtils.sendmessage(sender, "&c&lERROR &7오류 발생 : 오류코드 DB-D01 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
                        }
                    }).start();
                }
            } else {
                if (!FactionUtils.WhosClaim(loc).equalsIgnoreCase(FactionUUID)) {
                    SystemUtils.sendfactionmessage(sender, "&r&f해당 영토는 당신의 국가의 소유가 아닙니다");
                    return;
                }
                if(FactionUtils.isInWar(FactionUUID)) {
                    SystemUtils.sendfactionmessage(sender, "&r&f전쟁 도중에는 영토를 해제할수 없습니다");
                    return;
                }
                if(FactionUtils.hasMainBeacon(FactionUUID) && FactionUtils.isMainBeaconChunk(FactionUUID, loc)) {
                    SystemUtils.sendfactionmessage(sender, "&r&f국가의 신호기가 설치된 영토는 해제가 불가능합니다");
                    return;
                }
                FactionUtils.UnClaimLand(FactionUUID, Chunkkey);
                SystemUtils.sendfactionmessage(sender, "&r&f성공적으로 해당 영토&r&7(청크)&r&f에 대한 점령을 해제했습니다. \n");
            }
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
        }
    }

}
