package com.itndev.factions.Listener;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Storage.TempStorage;
import com.itndev.factions.Storage.UserInfoStorage;
import com.itndev.factions.Utils.BackendIO;
import com.itndev.factions.Utils.DiscordAuth.DiscordAuth;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerListener implements Listener {

    public static HashMap<String, Location> onJoinWarp = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent e) {
        String UUID = e.getPlayer().getUniqueId().toString();
        if(!DiscordAuth.isAuth(UUID)) {
            e.setCancelled(true);
        }
        new Thread( () -> {
            Location from = e.getFrom();
            Location to = e.getTo();
            if(from.getZ() != to.getZ()  ||  from.getX() != to.getX()  ||  from.getY() != to.getY()) {
                TempStorage.TeleportLocation.remove(e.getPlayer().getUniqueId().toString());
                if(!from.getChunk().equals(to.getChunk())) {
                    String fromname = FactionUtils.GetClaimFaction(from);
                    String toname = FactionUtils.GetClaimFaction(to);
                    if(!fromname.equals(toname)) {
                        SystemUtils.sendfactionmessage(e.getPlayer(), "&r&f이동 &7: &r" + fromname + " &8->&r&f " + toname);
                    }
                }
            }
        }).start();
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String UUID = e.getPlayer().getUniqueId().toString();
        if(!DiscordAuth.isAuth(UUID)) {
            if(!e.getMessage().contains("/연동")) {
                Player p = e.getPlayer();
                SystemUtils.sendmessage(p, "&c&l[주의] &r&7공식 디스코드 서버 " + Config.DiscordLink + " 에서 당신의 계정을 연동시키세요");
                e.setCancelled(true);
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(EntityExplodeEvent e) {
        Location loc = e.getLocation();
        if(FactionUtils.isClaimed(loc)) {
            if(!FactionUtils.isInWar(FactionUtils.GetClaimFaction(loc))) {
                e.setCancelled(true);
            }
        } else if(FactionUtils.isOutPost(loc)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Location loc = e.getInteractionPoint();
        Action ac = e.getAction();
        if(!ac.equals(Action.RIGHT_CLICK_AIR) && !ac.equals(Action.LEFT_CLICK_AIR) && !ac.equals(Action.LEFT_CLICK_BLOCK) && !ac.equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if(loc != null && FactionUtils.isOutPost(loc) && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(!FactionUtils.GetOutPostOwner(loc).equals(FactionUtils.getPlayerFactionUUID(p.getUniqueId().toString()))) {
                String FactionUUID = FactionUtils.GetOutPostOwner(loc);
                if(!FactionUtils.isInWar(FactionUUID)) {
                    SystemUtils.sendmessage(p, "&4&l⚠ &r&c전쟁이 없는 도중에는 상대국가 전초기지에서 해당 엑션을 취하실수 없습니다");
                    e.setCancelled(true);
                }
            }
        }
    }

    @Deprecated
    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {
        String UUID = e.getPlayer().getUniqueId().toString();
        if(!DiscordAuth.isAuth(UUID)) {
            e.setCancelled(true);
        }
        Player p = e.getPlayer();
        if(!p.getGameMode().equals(GameMode.CREATIVE)) {
            Location loc = e.getBlock().getLocation();
            ItemStack holding = p.getInventory().getItemInMainHand();
            Boolean tempwefjw = FactionUtils.isOutPost(loc);
            if (holding.getType() == Material.BEACON) {
                if(FactionUtils.isInFaction(UUID) && !FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID)) && holding.hasItemMeta() && holding.getItemMeta().hasCustomModelData() && holding.getItemMeta().getCustomModelData() == Config.Faction_Main_Beacon_Data) {
                    if(!FactionUtils.HigherThenorSameRank(UUID, Config.Leader)) {
                        SystemUtils.sendfactionmessage(p, "&r&f국가의 &c" + Config.Leader_Lang + "&r 만 국가 신호기를 설치할수 있습니다");
                        e.setCancelled(true);
                        return;
                    }
                    if(FactionUtils.hasMainBeacon(FactionUtils.getPlayerFactionUUID(UUID))) {
                        e.setCancelled(true);
                        SystemUtils.sendfactionmessage(p, "&r&f이미 국가의 신호기가 설치되어 있습니다. &7(/국가 정보)&r 에서 국가의 신호기 위치를 확인할수 있습니다");
                        return;
                    }
                    if(!FactionUtils.isFactionsClaim(loc, FactionUtils.getPlayerFactionUUID(UUID))) {
                        SystemUtils.sendfactionmessage(p, "&r&f자신의 국가 영토 안에만 국가의 신호기를 설치할수 있습니다");
                        e.setCancelled(true);
                        return;
                    }
                    FactionUtils.setMainBeaconStringLocation(FactionUtils.getPlayerFactionUUID(UUID), loc);
                    SystemUtils.sendfactionmessage(p, "&r&f성공적으로 신호기를 설치했습니다. 중립국에서 해제됩니다. &7(/국가 전쟁)&r 이 사용 가능합니다\n 앞으로 &7(/국가 정보)&r 로 신호기의 위치를 확인할수 있습니다");
                    SystemUtils.sendfactionmessage(p, "&c신호기 좌표 &8&l: &7(월드:" + loc.getWorld().getName() + "&r&7 " +"X:" + loc.getX() + " Y:" + loc.getY() + " Z:" + loc.getZ() + ")");
                } else {
                    e.setCancelled(true);
                }
                return;
            }
            if(FactionUtils.isClaimed(loc)) {
                if(FactionUtils.isInWar(FactionUtils.AsyncWhosClaim(loc))) {
                    //SystemUtils.sendfactionmessage(p, "&r&f전쟁중에는 영토에 블럭을 설치하실수 없습니다");
                    //e.setCancelled(true);
                    if(!FactionUtils.getPlayerFactionUUID(UUID).equalsIgnoreCase(FactionUtils.AsyncWhosClaim(loc)) && e.getBlockPlaced().getType() == Material.LAVA) {
                        SystemUtils.sendfactionmessage(p, "&r&f용암은 설치가 불가능합니다");
                        e.setCancelled(true);
                    }
                    return;
                }
                if(!FactionUtils.isInFaction(UUID) || !FactionUtils.getPlayerFactionUUID(UUID).equalsIgnoreCase(FactionUtils.AsyncWhosClaim(loc))) {
                    e.setCancelled(true);
                }
            } else if(tempwefjw) {
                SystemUtils.sendfactionmessage(p, "&r&f전초기지에서는 해당 액션을 취하실수 없습니다");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        String UUID = e.getPlayer().getUniqueId().toString();
        if(!DiscordAuth.isAuth(UUID)) {
            e.setCancelled(true);
        }
        Player p = e.getPlayer();
        if(!p.getGameMode().equals(GameMode.CREATIVE)) {
            Location loc = e.getBlock().getLocation();
            Material m = e.getBlock().getType();
            Boolean isOutPost = FactionUtils.isOutPost(loc);
            String LocalFactionUUID = FactionUtils.AsyncWhosClaim(loc);
            String PlayerFactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            if(LocalFactionUUID != null) {
                if(PlayerFactionUUID != null && LocalFactionUUID.equals(PlayerFactionUUID) && loc.getBlock().getType().equals(Material.BEACON)) {
                    if(FactionUtils.hasMainBeacon(LocalFactionUUID) && FactionUtils.isMainBeaconLocation(LocalFactionUUID, loc)) {
                        SystemUtils.sendfactionmessage(p, "&r&f자신의 국가의 신호기는 파괴가 불가능합니다");
                        //자기 국가 신호기 파괴 시도
                        e.setCancelled(true);
                    }
                    return;
                }
                if(FactionUtils.isInWar(LocalFactionUUID)) {
                    if(loc.getBlock().getType().equals(Material.BEACON)) {
                        if(FactionUtils.isMainBeaconLocation(LocalFactionUUID, loc)) {
                            if(FactionUtils.isInFaction(UUID) && FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID)) && FactionUtils.getOPPWar(LocalFactionUUID).equalsIgnoreCase(FactionUtils.getPlayerFactionUUID(UUID))) {
                                //real faction beacon down
                                //국가
                                String TEMP_FactionUUID = LocalFactionUUID;
                                ArrayList<String> LAND_CHUNKKEY = FactionStorage.FactionToLand.get(TEMP_FactionUUID);
                                JedisTempStorage.AddCommandToQueue_INNER("update:=:DESTROYED_FactionUUIDToFactionName:=:add:=:" + TEMP_FactionUUID + ":=:add:=:" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(TEMP_FactionUUID))) ;
                                for(String key : LAND_CHUNKKEY) {
                                    JedisTempStorage.AddCommandToQueue_INNER("update:=:DESTORYED_LandToFaction:=:add:=:" + key + ":=:add:=:" + TEMP_FactionUUID);
                                    JedisTempStorage.AddCommandToQueue_INNER("update:=:DESTORYED_FactionToLand:=:add:=:" + TEMP_FactionUUID + ":=:add:=:" + key);
                                }
                                String FactionName = FactionUtils.getCappedFactionName(FactionUtils.getFactionName(TEMP_FactionUUID));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        JedisTempStorage.AddCommandToQueue_INNER("update:=:DESTORYED_FactionToLand:=:remove:=:" + FactionName + ":=:add:=:" + "key");
                                        for(String key : LAND_CHUNKKEY) {
                                            JedisTempStorage.AddCommandToQueue_INNER("update:=:DESTORYED_LandToFaction:=:remove:=:" + key + ":=:add:=:" + FactionName);
                                        }
                                    }
                                }.runTaskLater(Main.getInstance(), 60*60*20L);
                                String[] args = new String[2];
                                args[0] = "국가멸망";
                                args[1] = FactionUtils.getPlayerFactionUUID(UUID);
                                BackendIO.SendCMD_BACKEND(FactionUtils.getFactionLeader(TEMP_FactionUUID), args, "FACTION_DESTROY");
                                //전쟁종료메세지
                                //전쟁해제
                                String Coords = "(월드:" + loc.getWorld().getName() + " X:" + loc.getX() + " Y:" + loc.getY() + " Z:" + loc.getZ() + ")";
                                JedisTempStorage.AddCommandToQueue_INNER("notify:=:" + UUID + ":=:" + "all" + ":=:" + "&a&o&l[ &r&f국가 &a&o&l] &r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID))) + "&r의 " + UserInfoUtils.getPlayerUUIDOriginName(UUID) + " (이)가 국가 &7" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(TEMP_FactionUUID)) + "&r의 &c국가 신호기&r를 파괴했습니다:=:" + "true");
                                JedisTempStorage.AddCommandToQueue_INNER("notify:=:" + UUID + ":=:" + "all" + ":=:" + "&a&o&l[ &r&f국가 &a&o&l] &r&f국가 &c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID))) + "&r (이)가 국가 &7" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(TEMP_FactionUUID)) + "&r와의 전쟁에서 승리했습니다:=:" + "true");
                                JedisTempStorage.AddCommandToQueue_INNER("notify:=:" + UUID + ":=:" + "all" + ":=:" + "&a&o&l[ &r&f국가 &a&o&l] &r&f국가 &c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(TEMP_FactionUUID)) + "&r (이)의 영토는 앞으로 1시간 동안 점령이 불가능합니다.\n&r&7좌표:" + Coords + ":=:true");
                            } else {
                                SystemUtils.sendfactionmessage(p, "&r&f오직 전쟁 상대 국가의 신호기만 파괴할수 있습니다");
                            }
                        } else {
                            if(FactionUtils.isInFaction(UUID) && FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID)) && FactionUtils.getOPPWar(LocalFactionUUID).equalsIgnoreCase(FactionUtils.getPlayerFactionUUID(UUID))) {
                                SystemUtils.sendfactionmessage(p, "&r&f가짜 신호기를 파괴했습니다");
                            } else {
                                SystemUtils.sendfactionmessage(p, "&r&f오직 전쟁 상대 국가의 신호기만 파괴할수 있습니다");
                            }

                        }
                    } else {
                        // do nothing
                    }
                } else {
                    if(PlayerFactionUUID == null || !PlayerFactionUUID.equalsIgnoreCase(LocalFactionUUID)) {
                        e.setCancelled(true);
                        if(loc.getBlock().getType().equals(Material.BEACON)) {
                            SystemUtils.sendfactionmessage(p, "&r&f전쟁 중인 국가의 신호기만 파괴가 가능합니다");
                        }
                        return;
                    }
                }
                /*
                * 서버 1 -> 맵 1 -> 점령시 -> 그 서버에만 저장
                * 서버 2 -> 맵 2 ^ <- 점령불가
                * 갯수 -> 서버 2 -> 서버1의 영토갯수 알고싶다 -> 서버 1 물어보기
                * 해당 맵에 점령한 영토갯수 -> 좀 이상하니까
                * 채팅 중복 ->
                * 벌크 업데이트 -> 업데이트 -> 순서대로 작동 틱 단위로 함 50ms
                * 중복이 별로 일어나지 않거나 일어나도 위험하지 않은 정보 -> 레디스 캐시 -> 모든 서버에게 데이터 복사본 나눠줌
                * ---> MYSQL 데이터베이스 갯수만 저장 <- 데이터 중복 방지 (이름 , 금고 금액, DTR 등등) <- 리얼타임
                * 국가 전용 UUID
                * */
            } else if(isOutPost) {
                e.setCancelled(true);
                new Thread(() -> {
                    String OutPostFactionUUID = FactionUtils.GetOutPostOwner(loc);
                    if(PlayerFactionUUID != null && PlayerFactionUUID.equals(OutPostFactionUUID)) {
                        SystemUtils.sendfactionmessage(p, "&r&f전초기지에서는 해당 액션을 취하실수 없습니다");
                    } else if(PlayerFactionUUID != null) {
                        if(m == Material.BEACON) {
                            String Chunkkey = FactionUtils.getChunkKey(loc);
                            String FactionOutPostName = FactionUtils.GetFactionOutPostName(Chunkkey);
                            if(FactionUtils.GetBeaconLocation(OutPostFactionUUID, FactionOutPostName) == SystemUtils.loc2string(loc)) {
                                SystemUtils.sendfactionmessage(p, "&r&c진짜&r&f 신호기를 파괴했습니다 >.<");
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        FactionUtils.UnClaimOutPost(OutPostFactionUUID, Chunkkey);
                                        JedisTempStorage.AddCommandToQueue_INNER("notify:=:" + FactionUtils.getFactionLeader(OutPostFactionUUID) + ":=:" + "SIBAL" + ":=:" + "&r&f" + UserInfoUtils.getPlayerUUIDOriginName(UUID) + " 이가 당신의 전초기지 " + FactionOutPostName + " 의 신호기를 파괴했습니다" + ":=:" + "true");
                                        if(FactionUtils.isInFaction(UUID)) {
                                            JedisTempStorage.AddCommandToQueue_INNER("notify:=:" + FactionUtils.getFactionLeader(FactionUtils.getPlayerFactionUUID(UUID)) + ":=:" + "SIBAL" + ":=:" + "&r&f" + UserInfoUtils.getPlayerUUIDOriginName(UUID) + " 이가 " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(OutPostFactionUUID)) + " 의 전초기지의 신호기를 파괴했습니다" + ":=:" + "true");
                                        }
                                        loc.getBlock().setType(Material.DIAMOND_BLOCK);
                                    }
                                }.runTask(Main.getInstance());
                            } else {
                                SystemUtils.sendfactionmessage(p, "&r&c가짜&r&f 신호기를 파괴했습니다 >.<");
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        loc.getBlock().setType(Material.AIR);
                                    }
                                }.runTask(Main.getInstance());
                            }
                        }
                    } else {

                    }


                }).start();
            }

        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(!DiscordAuth.isAuth(p.getUniqueId().toString())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPVP(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player victem = (Player) e.getEntity();
            if(FactionUtils.isSameFaction(attacker.getUniqueId().toString(), victem.getUniqueId().toString())) {
                e.setCancelled(true);
                SystemUtils.sendmessage(attacker, "&4&l⚠ &r&c국가원들끼리의 전투는 금지되어 있습니다");
                return;
            } else {
                TempStorage.TeleportLocation.remove(attacker.getUniqueId().toString());
                TempStorage.TeleportLocation.remove(victem.getUniqueId().toString());
            }
        }
    }

    @EventHandler
    public void OnJoinWarp(PlayerJoinEvent e) {
        UserInfoStorage.onPlayerJoinEvent(e);
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString();
        if(!DiscordAuth.isAuth(uuid)) {
            DiscordAuth.FetchAuthInfo(p, uuid);
        }
        if(p.hasPermission("faxcore.bypass.discordauth")) {
            DiscordAuth.DISCORD_AUTH_INFO.put(uuid, "ADMIN_BYPASS");
        }
        if(onJoinWarp.containsKey(uuid)) {
            p.teleportAsync(onJoinWarp.get(uuid));
            onJoinWarp.remove(uuid);
            SystemUtils.sendfactionmessage(p, "&r&f국가 스폰으로 이동합니다");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpenInv(InventoryOpenEvent e) {
        String UUID = e.getPlayer().getUniqueId().toString();
        if(!DiscordAuth.isAuth(UUID)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInvClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            String UUID = e.getWhoClicked().getUniqueId().toString();
            if (!DiscordAuth.isAuth(UUID)) {
                e.setCancelled(true);
            }
        }
    }


    @Deprecated
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        String UUID = e.getPlayer().getUniqueId().toString();
        if(!DiscordAuth.isAuth(UUID)) {
            e.setCancelled(true);
            return;
        }
        String k = e.getMessage();
        Player p = e.getPlayer();
        e.setCancelled(true);
        if(ChatColor.stripColor(e.getMessage()).isBlank()) {
            return;
        }
        k = k.stripTrailing();
        k = k.stripTrailing();

        Main.sysutils.MainChatProcced(p , UUID, k);
            /*try {
                if (main.getInstance().ess.getUser(p.getUniqueId()).isMuted()) {
                    utils.sendmsg(p, "&4&l(!) &f&l당신은 뮤트됬습니다");
                    e.setCancelled(true);
                    return;
                }
            } catch (NullPointerException nulle) {
                System.out.println(nulle.getMessage());
            }*/


    }

    //@EventHandler
    /*public void onTeleport(PlayerTeleportEvent e) {
        TeleportInvisFix.onTeleport(e);
    }*/
    @Deprecated
    @EventHandler(ignoreCancelled = true)
    public void onclick(InventoryClickEvent e) {
        if(e.getView().getTitle().contains(SystemUtils.colorize("&3&l[ &r&f국가 워프메뉴 &3&l]"))) {
            //main.sendmsg((Player) e.getWhoClicked(), "&c&l(!) &7상대방의 정보를 보고 있는 도중에는 해당 엑션을 취하실수 없습니다");
            ItemStack item = e.getCurrentItem().clone();
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
            Player sender = (Player) e.getWhoClicked();
            String UUID = sender.getUniqueId().toString();

            new Thread( () -> {
                String OutPostName = item.getItemMeta().getDisplayName().split(" ")[1];
                String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                String[] temp222 = FactionUtils.GetFactionOutPostWarpLocation(FactionUUID, OutPostName).split("===");
                String TargetServerName = temp222[0];
                Location loc = SystemUtils.string2loc(temp222[1]);

                if(TargetServerName.equalsIgnoreCase(Main.ServerName)) {

                    //wait and teleport to location
                    SystemUtils.sendfactionmessage(sender, "&r&c5초&r&f후 전초기지 " + OutPostName + "으로 이동합니다");
                    Long time = System.currentTimeMillis();
                    TempStorage.TeleportLocation.put(sender.getUniqueId().toString(), time);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(TempStorage.TeleportLocation.containsKey(UUID)) {
                                if(TempStorage.TeleportLocation.get(UUID).equals(time)) {
                                    SystemUtils.sendfactionmessage(sender, "&r&f전초기지 " + OutPostName + "으로 이동합니다");
                                    sender.teleport(loc);
                                    return;
                                }
                            }
                            SystemUtils.sendfactionmessage(sender, "&r&f이동이 취소되었습니다");
                        }
                    }.runTaskLater(Main.getInstance(), 100L);
                } else {
                    SystemUtils.sendfactionmessage(sender, "&r&c5초&r&f후 전초기지 " + OutPostName + "으로 이동합니다");
                    Long time = System.currentTimeMillis();
                    TempStorage.TeleportLocation.put(sender.getUniqueId().toString(), time);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(TempStorage.TeleportLocation.containsKey(UUID)) {
                                if(TempStorage.TeleportLocation.get(UUID).equals(time)) {
                                    SystemUtils.SendtoServer(sender, TargetServerName);
                                    FactionUtils.WarpLocation(UUID, TargetServerName, temp222[1], false);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            FactionUtils.WarpLocation(UUID, TargetServerName, temp222[1], true);
                                        }
                                    }.runTaskLaterAsynchronously(Main.getInstance(), 400L);
                                    return;
                                }
                            }
                            SystemUtils.sendfactionmessage(sender, "&r&f이동이 취소되었습니다");
                        }
                    }.runTaskLater(Main.getInstance(), 100L);

                    //
                    //서버로 이동
                    //그다음에 텔레포트
                }
            }).start();

        }
    }

}
