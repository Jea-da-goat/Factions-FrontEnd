package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Utils.BackendIO;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class DeleteFaction {
    public static void DeleteFactionQueue(Player sender, String UUID, String[] args) {
        if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
            BackendIO.SendCMD_BACKEND(UUID, args, "FACTION_DELETE_QUEUE");
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
        }
    }

    public static void DeleteFaction(Player sender, String UUID, String[] args) {
        if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
            BackendIO.SendCMD_BACKEND(UUID, args, "FACTION_DELETE");
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
        }
    }
}
