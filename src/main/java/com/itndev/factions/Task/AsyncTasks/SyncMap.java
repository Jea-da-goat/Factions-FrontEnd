package com.itndev.factions.Task.AsyncTasks;

import com.itndev.factions.Main;
import com.itndev.factions.Storage.Faction.FactionStorage;
import com.itndev.factions.Utils.FactionList.FactionList;

import java.util.HashMap;

public class SyncMap {

    public void SyncStorageMap() {
        new Thread(() -> {
            while(true) {
                HashMap<String, String> templands = (HashMap<String, String>) FactionStorage.LandToFaction.clone();
                synchronized (FactionStorage.AsyncLandToFaction) {
                    FactionStorage.AsyncLandToFaction.clear();
                    FactionStorage.AsyncLandToFaction.putAll(templands);
                }

                HashMap<String, String> tempoutposts = (HashMap<String, String>) FactionStorage.OutPostToFaction.clone();
                synchronized (FactionStorage.AsyncOutPostToFaction) {
                    FactionStorage.AsyncOutPostToFaction.clear();
                    FactionStorage.AsyncOutPostToFaction.putAll(tempoutposts);
                }
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(Main.ShutDown) {
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            while(true) {
                FactionList.FactionTopExecute(10L);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignored) {
                }
                FactionList.BuildFactionTop();
                if(Main.ShutDown) {
                    break;
                }
            }
        }).start();
    }
}
