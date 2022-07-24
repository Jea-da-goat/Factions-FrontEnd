package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Utils.BackendIO;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import org.bukkit.entity.Player;

import java.util.Locale;

public class FactionKick {
    public static void FactionKick(Player sender, String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 추방 &7(이름)");
            return;
        }
        if (FactionUtils.isInFaction(UUID)) {
            if(FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
                SystemUtils.sendfactionmessage(sender, "&r&f전쟁 중에는 양도를 할수 없습니다");
                return;
            }
            BackendIO.SendCMD_BACKEND(UUID, args, "FACTION_KICK_PLAYER");
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
        }
    }
}
