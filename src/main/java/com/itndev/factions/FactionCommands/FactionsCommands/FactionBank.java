package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.BackendIO;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.ValidChecker;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class FactionBank {
    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void FactionBank(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            if (FactionUtils.isInFaction(UUID)) {
                if (!FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                    SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                    return;
                }

                if (args.length < 3) {
                    SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n");
                    return;
                }

                Boolean Take = null;

                if (args[1].equalsIgnoreCase("입금")) {
                    Take = false;
                } else if (args[1].equalsIgnoreCase("출금")) {
                    Take = true;
                } else {
                    SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n");
                    return;
                }

                if (args[2].length() > 20) {
                    SystemUtils.sendfactionmessage(sender, "&r&c오류 ! &7명령어가 너무 깁니다... 확인 불가\n");
                    return;
                }

                if (!ValidChecker.instanceofNumber(args[2])) {
                    SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n" +
                            "&7> &f금액에는 숫자를 입력해야 합니다.");
                    return;
                }
                double bal = Main.getEconomy().getBalance(Bukkit.getOfflinePlayer(sender.getUniqueId()));
                double amount = Double.parseDouble(args[2]);
                if (!Take) {

                    if (bal <= amount) {
                        SystemUtils.sendfactionmessage(sender, "&r&f국가 금고에 해당 금액을 넣기에는 돈이 부족합니다.\n");
                        return;
                    }
                    amount = amount * -1;
                } else {
                    if (bal + amount > 9000000000000D) {
                        SystemUtils.sendfactionmessage(sender, "&r&f출금시 최대 금액 한도를 초과하므로 자동으로 해당 출금요청을 취소합니다\n");
                        return;
                    }
                }

                if (!Take) {
                    //Main.econ.depositPlayer(Bukkit.getOfflinePlayer(java.util.UUID.fromString(UUID)), amount);
                    Main.econ.withdrawPlayer(Bukkit.getOfflinePlayer(java.util.UUID.fromString(UUID)), amount*-1);
                }
                BackendIO.SendCMD_BACKEND(UUID, args, df.format(bal));
            } else {
                SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
            }
        }).start();

    }
}
