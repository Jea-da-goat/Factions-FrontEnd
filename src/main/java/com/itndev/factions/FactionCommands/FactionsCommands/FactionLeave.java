package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Utils.BackendIO;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

public class FactionLeave {

    public static void FactionLeave(Player sender, String UUID, String[] args) {
        if(!FactionUtils.isInFaction(UUID)) {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }
        BackendIO.SendCMD_BACKEND(UUID, args, "LEAVE_FACTION");
    }
}
