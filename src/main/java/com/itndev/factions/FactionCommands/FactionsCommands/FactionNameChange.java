package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.BackendIO;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.ValidChecker;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FactionNameChange {

    public static void FactionNameChange(Player sender, String UUID, String[] args) {

        if(args.length < 2) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 이름 &7(국가이름)");
            return;
        }
        if(args[1].length() > 12) {
            SystemUtils.sendfactionmessage(sender, "&r&f국가 이름의 길이는 12자를 초과할수 없습니다");
            return;
        }
        CompletableFuture<Boolean> FutureValidCheck = ValidChecker.ValidCheck(args[1]);
        if(!FactionUtils.isInFaction(UUID)) {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }
        if(!FactionUtils.HigherThenorSameRank(UUID, Config.Leader)) {
            SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.Leader_Lang + " &r&f만이 사용이 가능합니다");
            return;
        }
        try {
            if(!FutureValidCheck.get()) {
                SystemUtils.sendfactionmessage(sender, "&r&f해당 문자/단어는 국가 이름에 들어갈수 없습니다");
                return;
            }
            //do something
        } catch (InterruptedException | ExecutionException e) {
            sender.sendMessage(Component.text("[ERROR] Please Report this Error To Admins ASAP -> NAME VALIDATION ERROR"));
            throw new RuntimeException(e);
        }

        // stuff
    }
}
