package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Utils.BackendIO;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

public class FactionLeave {

    public static void FactionLeave(Player sender, String UUID, String[] args) {
        BackendIO.SendCMD_BACKEND(UUID, args, "LEAVE_FACTION");
    }
}
