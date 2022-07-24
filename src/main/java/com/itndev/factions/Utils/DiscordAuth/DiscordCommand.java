package com.itndev.factions.Utils.DiscordAuth;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(label.equalsIgnoreCase("연동")) {
                if(args.length >= 1) {
                    DiscordAuth.VerifyAuthInfo(p.getUniqueId().toString(), args[0]);
                } else {
                    SystemUtils.sendmessage(p, "&c&l[주의] &7사용법 -> /연동 <ID>");
                    SystemUtils.sendmessage(p, "&c&l[주의] &7공식 디스코드 서버 " + Config.DiscordLink + " 에 접속하여 디스코드 계정을 연동할수 있습니다");
                }
            } else if(label.equalsIgnoreCase("연동해제")) {
                DiscordAuth.RemoveVerify(p, p.getUniqueId().toString());
            } else if(label.equalsIgnoreCase("연동정보")) {
                DiscordAuth.CheckAuthInfo(p.getUniqueId().toString());
            }


        }
        return false;
    }
}
