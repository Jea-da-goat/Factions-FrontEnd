package com.itndev.factions.Storage.Faction;

import com.itndev.factions.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class FactionStorage {

    public static HashMap<String, ArrayList<String>> FactionToLand = new HashMap<>();

    public static HashMap<String, String> LandToFaction = new HashMap<>();

    public static HashMap<String, ArrayList<String>> DESTORYED_FactionToLand = new HashMap<>();

    public static HashMap<String, String> DESTORYED_LandToFaction = new HashMap<>();

    public static HashMap<String, String> DESTROYED_FactionUUIDToFactionName = new HashMap<>();

    public static ConcurrentHashMap<String, String> AsyncLandToFaction = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> PlayerFaction = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionUUIDToFactionName = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionNameToFactionUUID = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionNameToFactionName = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionRank = new ConcurrentHashMap<>();

    /*
    * Faction Rank Explain
    *
    * nomad = joined server before, not in a faction
    *
    * member = in a faction but just a normal member
    *
    * coleader = co-leader , manages members but lower then leader
    *
    * leader = the highest rank, owns the faction
    *
    */

    public static ConcurrentHashMap<String, ArrayList<String>> FactionMember = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionInfo = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ArrayList<String>> FactionInfoList = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ArrayList<String>> FactionInviteQueue = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionOutPost = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionWarpLocations = new ConcurrentHashMap<>();

    public static HashMap<String, String> OutPostToFaction = new HashMap<>();

    public static ConcurrentHashMap<String, String> AsyncOutPostToFaction = new ConcurrentHashMap<>();

    public static HashMap<String, ArrayList<String>> FactionToOutPost = new HashMap<>();

    public static ConcurrentHashMap<String, ArrayList<String>> FactionOutPostList = new ConcurrentHashMap<>();

    // key : FactionUUID + Type + id / FactionOutPostList
    // if Type is Spawn id is Spawn
    // -- example --
    // FactionUUID=Spawn=Spawn , Coords
    // FactionUUID=OutPost=ID , Coords

    public static void FactionStorageUpdateHandler(String[] args, String ServerName) {
        if(args[1].equalsIgnoreCase("FactionToLand")) {
            if(args.length > 6 && args[6].equalsIgnoreCase(Main.ServerName)) {
                return;
            }
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionToLand.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionToLand.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionToLand.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionToLand.put(key, updatelist);
                        } else {
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionToLand.put(key, updatelist2);
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionToLand.put(key, updatelist3);;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionToLand.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionToLand.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionToLand.get(key);
                            updatelist.remove(value);
                            if(updatelist.isEmpty()) {
                                FactionStorage.FactionToLand.remove(key);
                            } else {
                                FactionStorage.FactionToLand.put(key, updatelist);
                            }
                        }
                    }
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionToLand.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("LandToFaction")) {
            if(args.length > 6 && args[6].equalsIgnoreCase(Main.ServerName)) {
                if(args[2].equalsIgnoreCase("add")) {
                    String key = args[3]; //키
                    String value = args[5]; //추가하고 싶은 값
                    if(args[4].equalsIgnoreCase("add")) {
                        FactionStorage.AsyncLandToFaction.put(key, value);
                    } else if(args[4].equalsIgnoreCase("remove")) {
                        FactionStorage.AsyncLandToFaction.remove(key);
                    }
                } else if(args[2].equalsIgnoreCase("remove")) {
                    String key = args[3];
                    FactionStorage.AsyncLandToFaction.remove(key);
                }
                return;
            }
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {
                    FactionStorage.LandToFaction.put(key, value);
                    FactionStorage.AsyncLandToFaction.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    FactionStorage.LandToFaction.remove(key);
                    FactionStorage.AsyncLandToFaction.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.LandToFaction.remove(key);
                FactionStorage.AsyncLandToFaction.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionRank")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {
                    FactionStorage.FactionRank.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    FactionStorage.FactionRank.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionRank.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("PlayerFaction")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은
                if(args[4].equalsIgnoreCase("add")) {
                    FactionStorage.PlayerFaction.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    FactionStorage.PlayerFaction.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.PlayerFaction.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionMember")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionMember.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionMember.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionMember.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionMember.put(key, updatelist);
                        } else {
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionMember.put(key, updatelist2);
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionMember.put(key, updatelist3);;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionMember.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionMember.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionMember.get(key);
                            updatelist.remove(value);
                            FactionStorage.FactionMember.put(key, updatelist);
                        }
                    }
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionMember.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionNameToFactionName")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {
                    FactionStorage.FactionNameToFactionName.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    FactionStorage.FactionNameToFactionName.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionNameToFactionName.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionNameToFactionUUID")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {
                    FactionStorage.FactionNameToFactionUUID.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    FactionStorage.FactionNameToFactionUUID.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionNameToFactionUUID.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionUUIDToFactionName")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {
                    FactionStorage.FactionUUIDToFactionName.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    FactionStorage.FactionUUIDToFactionName.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionUUIDToFactionName.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionInfo")) { //no longer used
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {
                    FactionStorage.FactionInfo.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    FactionStorage.FactionInfo.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionInfo.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionInfoList")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionInfoList.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionInfoList.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionInfoList.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionInfoList.put(key, updatelist);
                        } else {
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionInfoList.put(key, updatelist2);
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionInfoList.put(key, updatelist3);;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionInfoList.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionInfoList.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionInfoList.get(key);
                            updatelist.remove(value);
                            FactionStorage.FactionInfoList.put(key, updatelist);
                        }
                    }
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionInfoList.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionInviteQueue")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionInviteQueue.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionInviteQueue.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionInviteQueue.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionInviteQueue.put(key, updatelist);
                        } else {
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionInviteQueue.put(key, updatelist2);
                        }
                    } else {
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionInviteQueue.put(key, updatelist3);;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionInviteQueue.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionInviteQueue.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionInviteQueue.get(key);
                            updatelist.remove(value);
                            FactionStorage.FactionInviteQueue.put(key, updatelist);
                        }
                    }
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionInviteQueue.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("Timeout2info")) {
        } else if(args[1].equalsIgnoreCase("Timeout2")) {
        } else if(args[1].equalsIgnoreCase("OutPostToFaction")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {
                    FactionStorage.OutPostToFaction.put(key, value);
                    FactionStorage.AsyncOutPostToFaction.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if(FactionStorage.OutPostToFaction.containsKey(key)) {
                        FactionStorage.OutPostToFaction.remove(key);
                        FactionStorage.AsyncOutPostToFaction.remove(key);
                    }
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.OutPostToFaction.remove(key);
                FactionStorage.AsyncOutPostToFaction.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionToOutPost")) {
            if(args.length > 6 && args[6].equalsIgnoreCase(Main.ServerName)) {
                return;
            }
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionToOutPost.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionToOutPost.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionToOutPost.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionToOutPost.put(key, updatelist);
                        } else {
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionToOutPost.put(key, updatelist2);
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionToOutPost.put(key, updatelist3);;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionToOutPost.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionToOutPost.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionToOutPost.get(key);
                            updatelist.remove(value);
                            if(updatelist.isEmpty()) {
                                FactionStorage.FactionToOutPost.remove(key);
                            } else {
                                FactionStorage.FactionToOutPost.put(key, updatelist);
                            }
                        }
                    }
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionToOutPost.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionOutPostList")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionOutPostList.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionOutPostList.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionOutPostList.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionOutPostList.put(key, updatelist);
                        } else {
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionOutPostList.put(key, updatelist2);
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionOutPostList.put(key, updatelist3);;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionOutPostList.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionOutPostList.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.FactionOutPostList.get(key);
                            updatelist.remove(value);
                            if(updatelist.isEmpty()) {
                                FactionStorage.FactionOutPostList.remove(key);
                            } else {
                                FactionStorage.FactionOutPostList.put(key, updatelist);
                            }
                        }
                    }
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionOutPostList.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("FactionOutPost")) { //no longer used
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {
                    FactionStorage.FactionOutPost.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    FactionStorage.FactionOutPost.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionOutPost.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("DESTORYED_FactionToLand")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.DESTORYED_FactionToLand.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.DESTORYED_FactionToLand.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.DESTORYED_FactionToLand.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.DESTORYED_FactionToLand.put(key, updatelist);
                        } else {
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.DESTORYED_FactionToLand.put(key, updatelist2);
                        }
                    } else {
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.DESTORYED_FactionToLand.put(key, updatelist3);;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.DESTORYED_FactionToLand.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.DESTORYED_FactionToLand.containsKey(key)) {
                            ArrayList<String> updatelist = FactionStorage.DESTORYED_FactionToLand.get(key);
                            updatelist.remove(value);
                            if(updatelist.isEmpty()) {
                                FactionStorage.DESTORYED_FactionToLand.remove(key);
                            } else {
                                FactionStorage.DESTORYED_FactionToLand.put(key, updatelist);
                            }
                        }
                    }
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.DESTORYED_FactionToLand.remove(key);
            }
//DESTROYED_FactionUUIDToFactionName
        } else if(args[1].equalsIgnoreCase("DESTORYED_LandToFaction")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {
                    FactionStorage.DESTORYED_LandToFaction.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    FactionStorage.DESTORYED_LandToFaction.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.DESTORYED_LandToFaction.remove(key);
            }
        } else if(args[1].equalsIgnoreCase("DESTROYED_FactionUUIDToFactionName")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {
                     FactionStorage.DESTROYED_FactionUUIDToFactionName.put(key, value);
                } else if(args[4].equalsIgnoreCase("remove")) {
                    FactionStorage.DESTROYED_FactionUUIDToFactionName.remove(key);
                }
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.DESTROYED_FactionUUIDToFactionName.remove(key);
            }
        }
        /* else if(args[1].equalsIgnoreCase("LandToFaction")) { //====================== Land To Faction

                } else if(args[1].equalsIgnoreCase("FactionRank")) { //====================== FactionRank

                } else if(args[1].equalsIgnoreCase("PlayerFaction")) { //====================== PlayerFaction

                } else if(args[1].equalsIgnoreCase("FactionMember")) { //====================== FactionMember

                } else if(args[1].equalsIgnoreCase("FactionNameToFactionName")) { //====================== FactionName To FactionName

                } else if(args[1].equalsIgnoreCase("FactionNameToFactionUUID")) { //====================== FactionName To FactionUUID

                } else if(args[1].equalsIgnoreCase("FactionUUIDToFactionName")) { //====================== FactionUUID To FactionName

                } else if(args[1].equalsIgnoreCase("FactionInviteQueue")) { //====================== Faction Invite Queue

                }*/
    }

    public static void DTRUpdateEvent(String FactionUUID, String newDTR) {

    }

}
