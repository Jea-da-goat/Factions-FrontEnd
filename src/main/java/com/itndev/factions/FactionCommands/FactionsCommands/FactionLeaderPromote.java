package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Utils.BackendIO;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import org.bukkit.entity.Player;

import java.util.Locale;

public class FactionLeaderPromote {

    public static void FactionLeaderPromote(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            if(args.length < 2) {
                SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 소속 &7(이름)\n");
                return;
            }
            if(!FactionUtils.isInFaction(UUID)) {
                SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                return;
            }
            if(!FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
                SystemUtils.sendfactionmessage(sender, "&r&f국가의 " + Config.Leader_Lang + " 만 이 명령어를 사용할수 있습니다\n");
                return;
            }
            if(FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
                SystemUtils.sendfactionmessage(sender, "&r&f전쟁 중에는 양도를 할수 없습니다");
                return;
            }
            BackendIO.SendCMD_BACKEND(UUID, args, "LEADER_PROMOTE");
        }).start();
    }

}
