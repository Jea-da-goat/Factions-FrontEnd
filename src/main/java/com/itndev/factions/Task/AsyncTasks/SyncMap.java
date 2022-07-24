package com.itndev.factions.Task.AsyncTasks;

import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Utils.FactionList.FactionList;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class SyncMap {

    public void SyncStorageMap() {
        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<String, String> templands = (HashMap<String, String>) FactionStorage.LandToFaction.clone();
                synchronized (FactionStorage.AsyncLandToFaction) {
                    FactionStorage.AsyncLandToFaction.clear();
                    for(String key : templands.keySet()) {
                        FactionStorage.AsyncLandToFaction.put(key, templands.get(key));
                    }
                }

                HashMap<String, String> tempoutposts = (HashMap<String, String>) FactionStorage.OutPostToFaction.clone();
                synchronized (FactionStorage.AsyncOutPostToFaction) {
                    FactionStorage.AsyncOutPostToFaction.clear();
                    for(String key : tempoutposts.keySet()) {
                        FactionStorage.AsyncOutPostToFaction.put(key, tempoutposts.get(key));
                    }
                }


            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 3L, 3L);
        new BukkitRunnable() {
            @Override
            public void run() {
                FactionList.FactionTopExecute(10L);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignored) {
                }
                FactionList.BuildFactionTop();
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 100L, 2*20*60L);
    }
}
