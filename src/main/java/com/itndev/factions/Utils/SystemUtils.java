package com.itndev.factions.Utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.itndev.factions.Config.Config;
import com.itndev.factions.FactionCommands.FactionsCommands.FactionChatToggle;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Package.WrappedInfo;
import io.papermc.paper.text.PaperComponents;
import me.clip.placeholderapi.PlaceholderAPI;
import me.leoko.advancedban.Universal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.Style;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Unmodifiable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemUtils {

    public static SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public static void warning(String Message) {
        Bukkit.broadcast(Component.text(colorize("&c&lERROR &7" + Message)));
    }

    public static void sendmessage(Player p, String Message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
    }

    public static void sendfactionmessage(Player p, String Message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f" + Message));
    }

    public static void sendrawfactionmessage(Player p, String Message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f") + Message);
    }

    public static UUID Convert2UUID(String UUID2) {
        return UUID.fromString(UUID2);
    }

    @Deprecated
    public static void sendUUIDfactionmessage(String UUID, String Message) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(UserInfoUtils.getPlayerName(UUID));
        if(op.isOnline()) {
            Player p = (Player) op;
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f" + Message));
        }
    }

    public static void sendUUIDmessage(String uuid, String Message) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(java.util.UUID.fromString(uuid));
        if(op.isOnline()) {
            Player p = (Player) op;
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
        }
    }

    public static String loc2string(Location loc) {
        String a = String.valueOf(Math.round(loc.getPitch()));
        String b = String.valueOf(Math.round(loc.getYaw()));
        String c = String.valueOf(loc.getWorld().getName());
        String d = String.valueOf(loc.getX());
        String e = String.valueOf(loc.getY());
        String f = String.valueOf(loc.getZ());
        return a + "=" + b + "=" + c + "=" + d + "=" + e + "=" + f;
    }

    public static Location string2loc(String coords) {
        if(coords.contains("=")) {
            String[] skadi = coords.split("=");
            if(skadi.length == 6) {
                int a = Integer.valueOf(skadi[0]);
                int b = Integer.valueOf(skadi[1]);
                String c = skadi[2];
                double d = Double.valueOf(skadi[3]);
                double e = Double.valueOf(skadi[4]);
                double f = Double.valueOf(skadi[5]);
                Location loc = new Location(Bukkit.getWorld(c), d, e, f);
                loc.setPitch(a);
                loc.setYaw(b);
                return loc;
            }
        }
        return null;
    }

    public static String FactionUUIDToDate(String FactionUUID) {
        Long time = Long.valueOf(FactionUUID.split("=")[0]);
        return timeformat.format(new Date(time));
    }

    public static String getDate(Long timedata) {
        return timeformat.format(new Date(timedata));
    }

    public static String Args2String(String[] args, int Start) {
        String FinalString = "";
        for(int k = Start; k < args.length; k++) {
            if(args[k] == null) {
                break;
            }
            if(k == args.length - 1) {
                FinalString = FinalString + args[k];
            } else {
                FinalString = FinalString + args[k] + " ";
            }

        }
        return FinalString;
    }

    public static void SendtoServer(Player p, String ServerName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(ServerName);
        p.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String uncolorize(String message, String defaultcolorcode) {
        return message.replace("&", defaultcolorcode);
    }

    public static String stripcolor(String message) {
        return ChatColor.stripColor(message);
    }

    public static void SendErrorMessage(String Error, Player p) {
        if(p != null) {
            SystemUtils.sendmessage(p, "&c&lERROR &r&7오류가 발생한듯 합니다. 커뮤니티에 오류를 제보해주시기 바랍니다\n" +
                    "&r&c&lINFO " + "&f&l오류 ID &r&8&l: &r&7" + Error + "\n" +
                    "&r&c&lINFO " + "&f&l디스코드 &r&8&l: &r&7" + Config.DiscordLink + "\n");
        }
        System.out.println("[ERROR] 오류 발생 (대상유저:" + getPlayerName(p) + "),(오류ID:" + Error + ")");
    }

    public static String getPlayerName(Player p) {
        if(p == null) {
            return "null";
        } else {
            return p.getName();
        }
    }

    public static BaseComponent Convert(String message) {
        TextComponent textComponent = new TextComponent(message);
        BaseComponent baseComponent = new TextComponent("");
        baseComponent.addExtra(textComponent);
        return baseComponent;
    }

    public static Location ReplaceChunk(Location From, Location To) {
        ChunkSnapshot SnapShot = From.getChunk().getChunkSnapshot();

        int height = 0;
        To.getChunk().getBlock(8, height, 8).getLocation();
        Random random = new Random();
        int c = random.nextInt(8) + 1;
        int amount = 0;
        Location loc1 = null;
        for(int x = 0; x <= 15; x++) {
            for(int z = 0; z <= 15; z++) {
                for(int y = 42; y <= 118; y++) {
                    BlockData block = SnapShot.getBlockData(x, y, z);
                    Boolean cu = false;
                    if(block.getMaterial() == Material.BEACON) {
                       amount++;
                        if(amount == c) {
                            cu = true;
                        }
                    }
                    To.getChunk().getBlock(x, y, z).setBlockData(block);
                    if(cu) {
                        loc1 = To.getChunk().getBlock(x, y, z).getLocation();
                    }
                }
            }
        }
        return loc1;
    }

    public static void RemoveHeldItem(Player p, int Amount) {
        ItemStack item = p.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - Amount);
    }

    public static HashMap<String, Long> chatevent = new HashMap<>();

    public static Boolean areachat = false;

    public static void PROCCED_INNER2_CHAT(String CMD, String clientname) {
        String SPLITTER_INNER2 = " ";//"</SPLIT:=:1C4FD2F>";
        System.out.println(CMD);
        if(CMD.contains(SPLITTER_INNER2)) {
            String[] TEMP = CMD.split(SPLITTER_INNER2);
            if(TEMP.length == 2) {
                String UUID = CommonUtils.Byte2String(TEMP[0]);
                String Message = CommonUtils.Byte2String(TEMP[1]);
                Message = Message.stripLeading();
                Message = Message.stripTrailing();
                String finalMessage = Message;
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Component.text(colorize(finalMessage))));
            }
        }
    }

    public static void error_logger(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void logger(String message) {
        System.out.println(message);
    }

    public void MainChatProcced(Player p, String UUID, String k) {
        new Thread( () -> {
            if(!p.hasPermission("faxcore.chatbypass")) {
                if (FactionChatToggle.FactionChatToggled.containsKey(p.getName())) {
                    int cooldownTime = 2;
                    long secondsLeft = ((Long) chatevent.get(p.getName())).longValue() / 1000L + cooldownTime - System.currentTimeMillis() / 1000L;
                    if (secondsLeft > 0L) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(!) &r&f채팅은 2초마다 한번 칠수 있습니다."));
                        return;
                    }
                }
                chatevent.put(p.getName(), Long.valueOf(System.currentTimeMillis()));
            }
            if(k.startsWith("@")) {
                String message = uncolorize(stripcolor(k).replaceFirst("@", ""), "&7");
                String finalmessage = "&7";
                for(String temp : message.split("")) {
                    finalmessage = finalmessage + temp + "&7";
                }
                if(FactionUtils.isInFaction(UUID)) {
                    FactionUtils.SendFactionChat(p.getUniqueId().toString(), finalmessage);
                    //JedisTempStorage.AddCommandToQueue_INNER("chat" + ":=:" + p.getUniqueId().toString() + ":=:" + finalmessage);
                } else {
                    SystemUtils.sendmessage(p, "&c&l(!) &7소속된 팀이 없습니다");
                }
            } else if(FactionChatToggle.FactionChatToggled.containsKey(p)) {
                String message = uncolorize(stripcolor(k), "&7");
                String finalmessage = "&7";
                for(String temp : message.split("")) {
                    finalmessage = finalmessage + temp + "&7";
                }
                if(FactionUtils.isInFaction(UUID)) {
                    FactionUtils.SendFactionChat(p.getUniqueId().toString(), finalmessage);
                    //JedisTempStorage.AddCommandToQueue_INNER("chat" + ":=:" + p.getUniqueId().toString() + ":=:" + finalmessage);
                } else {
                    SystemUtils.sendmessage(p, "&c&l(!) &7소속된 팀이 없어서 팀 채팅에서 강제적으로 퇴장당했습니다");
                    FactionChatToggle.FactionChatToggled.remove(p);
                }
            } else if(!p.hasPermission("faxcore.chatbypass") && k.length() > 64){
                SystemUtils.sendmessage(p, "&c&l(!) &f&l메시지가 너무 깁니다. 64자 미만으로 줄여주세요");
            } else {
                try {
                    if (Universal.get().getMethods().callChat(p)) {
                        return;
                    }
                } catch (NullPointerException nulle) {
                    System.out.println(nulle.getMessage());
                }
                if(Main.chattoggle.equals(true) && !p.hasPermission("faxcore.chatmutebypass")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(!) &f현재 채팅이 잠겨있습니다"));
                    return;
                }
                Player player = p;
                String group = loadUser(player).getPrimaryGroup();
                String message = stripcolor(k).stripTrailing().stripLeading();
                String finalmessage = "&f";
                for(String temp : message.split("")) {
                    finalmessage = finalmessage + temp + "&f";
                }
                String format = (String) Objects.<String>requireNonNull(/*"&a[&fLv.0&a]*/"&r%itndevfactions_formatfactionname%%itndevfaction_formatrank%{prefix}{name} {suffix}&8:: &r{message}".replace("{world}", player.getWorld().getName()).replace("{prefix}", getPrefix(player)).replace("{prefixes}", getPrefixes(player)).replace("{name}", player.getName()).replace("{suffix}", getSuffix(player)).replace("{suffixes}", getSuffixes(player)).replace("{username-color}", (playerMeta(player).getMetaValue("username-color") != null) ? playerMeta(player).getMetaValue("username-color") : ((groupMeta(group).getMetaValue("username-color") != null) ? groupMeta(group).getMetaValue("username-color") : "")).replace("{message-color}", (playerMeta(player).getMetaValue("message-color") != null) ?
                        playerMeta(player).getMetaValue("message-color") : ((groupMeta(group).getMetaValue("message-color") != null) ?
                        groupMeta(group).getMetaValue("message-color") : "")));
                if(areachat) {
                    ArrayList<Player> closeplayer = new ArrayList<>();
                    for(Player online : Bukkit.getOnlinePlayers()) {
                        if(online.getLocation().getWorld().getName().equals(p.getLocation().getWorld().getName()) && online.getLocation().distanceSquared(p.getLocation()) <= 300 * 300) {
                            closeplayer.add(online);
                        }
                    }
                    String tagonfront = "&7[ " + String.valueOf(closeplayer.size()) + "명 ]&r";
                    String Dformat = colorize(tagonfront) + colorize(format.replace("{message}", finalmessage).replace("%itndevfactions_formatfactionname%",  FactionUtils.getFormattedFaction2(UUID)).replace("%itndevfaction_formatrank%", FactionUtils.getFormattedRank2(UUID)));
                    Dformat = PlaceHolderReplace(p, Dformat);
                    for(Player finalonlinecloseplayers : closeplayer) {
                        finalonlinecloseplayers.sendMessage(Dformat);
                    }
                } else if(false){
                    /*ArrayList<Player> closeplayer = new ArrayList<>();
                    for(Player online : Bukkit.getOnlinePlayers()) {
                        if(online.getLocation().getWorld().getName().equals(p.getLocation().getWorld().getName()) && online.getLocation().distanceSquared(p.getLocation()) <= 300 * 300) {
                            closeplayer.add(online);
                        }
                    }
                    String tagonfront = "&7[ " + String.valueOf(closeplayer.size()) + "명 ]&r";*/
                    String Dformat = colorize(format.replace("{message}", (player.hasPermission("lpc.colorcodes") && player.hasPermission("lpc.rgbcodes")) ?
                            translateHexColorCodes(colorize(finalmessage)) : (player.hasPermission("lpc.colorcodes") ? colorize(finalmessage) : (player.hasPermission("lpc.rgbcodes") ?
                            translateHexColorCodes(finalmessage) : finalmessage))).replace("%itndevfactions_formatfactionname%"," " +  FactionUtils.getFormattedFaction2(UUID)).replace("%itndevfaction_formatrank%", FactionUtils.getFormattedRank2(UUID)));
                    Dformat = PlaceHolderReplace(p, Dformat);
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(Dformat);
                    }
                } else {
                    String Dformat = format.replace("{message}", (player.hasPermission("lpc.colorcodes") && player.hasPermission("lpc.rgbcodes")) ?
                            translateHexColorCodes(colorize(finalmessage)) : (player.hasPermission("lpc.colorcodes") ? colorize(finalmessage) : (player.hasPermission("lpc.rgbcodes") ?
                            translateHexColorCodes(finalmessage) : finalmessage))).replace("%itndevfactions_formatfactionname%","" +  FactionUtils.getFormattedFaction2(UUID)).replace("%itndevfaction_formatrank%", FactionUtils.getFormattedRank2(UUID));
                    Dformat = PlaceHolderReplace(p, Dformat);
                    sendMainChat(p.getUniqueId().toString(), Dformat);
                    //String SPLITTER_INNER2 = "</SPLIT:=:1C4FD2F>";
                    //JedisTempStorage.AddCommandToQueue_INNER2(p.getUniqueId() + SPLITTER_INNER2 + Dformat);
                }

            }
        }).start();
    }

    public static void sendMainChat(String UUID, String Dformat) {
        //System.out.println(CommonUtils.String2Byte(UUID) + " " + CommonUtils.String2Byte(Dformat));
        JedisTempStorage.AddCommandToQueue_INNER2(CommonUtils.String2Byte(UUID) + " " + CommonUtils.String2Byte(Dformat));
    }

    //private static String list_splitter = "<%&LISTSPLITTER&%>";

    public static ArrayList string2list(String k) {
        if(k.contains(" ")) {
            String[] parts = k.split(" ");
            ArrayList<String> memlist = new ArrayList<>();
            for(String d : parts) {
                memlist.add(CommonUtils.Byte2String(d));
            }
            return memlist;
        } else {
            ArrayList<String> memlist = new ArrayList<>();
            memlist.add(CommonUtils.Byte2String(k));
            return memlist;
        }
    }
    public static String list2string(ArrayList<String> list) {
        String k = "";
        int i = 0;
        for(String c : list) {
            i = i + 1;
            if(list.size() > i) {
                k = k + CommonUtils.String2Byte(c) + " ";
            } else {
                k = k + CommonUtils.String2Byte(c);
            }


        }
        return k;
    }

    private String translateHexColorCodes(String message) {
        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        char colorChar = '§';
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 32);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, "§x§" + group
                    .charAt(0) + '§' + group.charAt(1) + '§' + group
                    .charAt(2) + '§' + group.charAt(3) + '§' + group
                    .charAt(4) + '§' + group.charAt(5));
        }
        return matcher.appendTail(buffer).toString();
    }

    private String getPrefix(Player player) {
        String prefix = playerMeta(player).getPrefix();
        return (prefix != null) ? prefix : "";
    }

    private String getSuffix(Player player) {
        String suffix = playerMeta(player).getSuffix();
        return (suffix != null) ? suffix : "";
    }

    private String getPrefixes(Player player) {
        SortedMap<Integer, String> map = playerMeta(player).getPrefixes();
        StringBuilder prefixes = new StringBuilder();
        for (String prefix : map.values())
            prefixes.append(prefix);
        return prefixes.toString();
    }

    private String getSuffixes(Player player) {
        SortedMap<Integer, String> map = playerMeta(player).getSuffixes();
        StringBuilder suffixes = new StringBuilder();
        for (String prefix : map.values())
            suffixes.append(prefix);
        return suffixes.toString();
    }

    private CachedMetaData playerMeta(Player player) {
        return loadUser(player).getCachedData().getMetaData(getApi().getContextManager().getQueryOptions(player));
    }

    private CachedMetaData groupMeta(String group) {
        return loadGroup(group).getCachedData().getMetaData(getApi().getContextManager().getStaticQueryOptions());
    }

    private User loadUser(Player player) {
        if (!player.isOnline())
            throw new IllegalStateException("Player is offline!");
        return getApi().getUserManager().getUser(player.getUniqueId());
    }

    private Group loadGroup(String group) {
        return getApi().getGroupManager().getGroup(group);
    }

    private LuckPerms getApi() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServer().getServicesManager().getRegistration(LuckPerms.class);
        Validate.notNull(provider);
        return (LuckPerms)provider.getProvider();
    }

    private String PlaceHolderReplace(Player p, String format) {
        if(isPlaceholderAPIEnabled()) {
            return PlaceholderAPI.setPlaceholders(p, format);
        }
        return format;
    }

    private boolean isPlaceholderAPIEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    private static DecimalFormat df = new DecimalFormat("0.00");

    public static String getPlayerBalance(Player p) {
        return df.format(Main.getEconomy().getBalance((OfflinePlayer) p));
    }
}
