package com.comandante.creeper.npc;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.CoolDown;
import com.comandante.creeper.player.CoolDownType;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.world.Area;
import com.comandante.creeper.world.Room;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class NpcMover {

    private final GameManager gameManager;
    private final Random random = new Random();
    private static final Logger log = Logger.getLogger(NpcMover.class);

    public NpcMover(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void roam(String npcId) {
        final Npc npcEntity = gameManager.getEntityManager().getNpcEntity(npcId);
        if (npcEntity == null) {
            return;
        }
        if (!gameManager.getRoomManager().getNpcCurrentRoom(npcEntity).isPresent()) {
            return;
        }
        Room npcCurrentRoom = gameManager.getRoomManager().getNpcCurrentRoom(npcEntity).get();
        Set<Integer> possibleExits = getPossibleExits(npcCurrentRoom);
        Predicate<Integer> roamableByArea = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer roomId) {
                Room room = gameManager.getRoomManager().getRoom(roomId);
                for (Area roomArea : room.getAreas()) {
                    if (npcEntity.getRoamAreas().contains(roomArea)) {
                        if (doesRoomHaveEmptyNpcsSpots(room, npcEntity, roomArea)){
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        List<Integer> canRoam = Lists.newArrayList(Iterables.filter(possibleExits, roamableByArea));
        if (canRoam.size() <= 0) {
            return;
        }
        Integer destinationRoomId = canRoam.get(random.nextInt(canRoam.size()));
        String exitMessage = getExitMessage(npcCurrentRoom, destinationRoomId);
        npcCurrentRoom.getNpcIds().remove(npcId);
        gameManager.roomSay(npcCurrentRoom.getRoomId(), npcEntity.getColorName() + " " + exitMessage, "");
        Room destinationRoom = gameManager.getRoomManager().getRoom(destinationRoomId);
        npcEntity.setCurrentRoom(destinationRoom);
        destinationRoom.getNpcIds().add(npcId);
        npcEntity.addCoolDown(new CoolDown(CoolDownType.NPC_ROAM));
        gameManager.roomSay(destinationRoomId, npcEntity.getColorName() + " has arrived.", "");
    }

    private boolean doesRoomHaveEmptyNpcsSpots(Room room, Npc npc, Area area) {
        Set<Area> roamAreas = npc.getRoamAreas();
        for (Area ar : roamAreas) {
            if (ar.equals(area)) {
                Optional<SpawnRule> spawnRuleByArea = npc.getSpawnRuleByArea(area);
                if (spawnRuleByArea.isPresent()) {
                    int maxPerRoom = spawnRuleByArea.get().getMaxPerRoom();
                    int numberOfNpcInRoom = numberOfNpcInRoom(npc, room);
                    if (numberOfNpcInRoom < maxPerRoom) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int numberOfNpcInRoom(Npc npc, Room room) {
        int count = 0;
        for (String npcId : room.getNpcIds()) {
            Npc npcEntity = gameManager.getEntityManager().getNpcEntity(npcId);
            if (npcEntity == null) {
                continue;
            }
            if (npc.getName().equals(npcEntity.getName())) {
                count++;
            }
        }
        return count;
    }

    public String getExitMessage(Room room, Integer exitRoomId) {
        if (room.getUpId().isPresent() && room.getUpId().get().equals(exitRoomId)) {
            return "exited up.";
        }
        if (room.getDownId().isPresent() && room.getDownId().get().equals(exitRoomId)) {
            return "exited down.";
        }
        if (room.getNorthId().isPresent() && room.getNorthId().get().equals(exitRoomId)) {
            return "exited to the north.";
        }
        if (room.getSouthId().isPresent() && room.getSouthId().get().equals(exitRoomId)) {
            return "exited to the south.";
        }
        if (room.getEastId().isPresent() && room.getEastId().get().equals(exitRoomId)) {
            return "exited to the east.";
        }
        if (room.getWestId().isPresent() && room.getWestId().get().equals(exitRoomId)) {
            return "exited to the west..";
        }
        return "";
    }

    public Set<Integer> getPossibleExits(Room room) {
        List<Optional<Integer>> opts = Lists.newArrayList();
        opts.add(room.getDownId());
        opts.add(room.getUpId());
        opts.add(room.getNorthId());
        opts.add(room.getSouthId());
        opts.add(room.getEastId());
        opts.add(room.getWestId());

        Set<Integer> exits = Sets.newHashSet();
        for (Optional<Integer> opt : opts) {
            if (opt.isPresent()) {
                exits.add(opt.get());
            }
        }
        return exits;
    }

}
