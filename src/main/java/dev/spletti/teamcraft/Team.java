package dev.spletti.teamcraft;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

public class Team {
    private String name;
    private UUID owner;
    private final List<UUID> members = new ArrayList<>();
    private final List<UUID> joinRequests = new ArrayList<>();

    public Team(String name, UUID owner) {
        this.name = name;
        this.owner = owner;
        this.members.add(owner);
        this.color = "white";
    }

    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getMembers() {
        return List.copyOf(members);
    }

    public boolean addMembers(UUID playerId) {
        if (members.contains(playerId)) {
            return false;
        }

        members.add(playerId);
        return true;
    }

    public boolean addJoinRequest(UUID playerId) {
        if (members.contains(playerId) || joinRequests.contains(playerId)) {
            return false;
        }

        joinRequests.add(playerId);
        return true;
    }

    public boolean removeJoinRequest(UUID playerId) {
        return joinRequests.remove(playerId);
    }

    public static final Codec<Team> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
        Codec.STRING.fieldOf("name").forGetter(Team::getName),
        UUIDUtil.CODEC.fieldOf("owner").forGetter(Team::getOwner),
        UUIDUtil.CODEC.listOf().fieldOf("members").forGetter(Team::getMembers),
        Codec.STRING.optionalFieldOf("color", "white").forGetter(Team::getColor),
        UUIDUtil.CODEC.listOf().optionalFieldOf("teamAdmins", List.of()).forGetter(Team::getTeamAdmins),
        UUIDUtil.CODEC.listOf().optionalFieldOf("joinRequests", List.of()).forGetter(Team::getJoinRequests)
        ).apply(instance, Team::new)
    );

    private Team(String name, UUID owner, List<UUID> members, String color, List<UUID> teamAdmins, List<UUID> joinRequests) {
        this.name = name;
        this.owner = owner;
        this.members.addAll(members);
        this.color = color;
        this.teamAdmins.addAll(teamAdmins);
        this.joinRequests.addAll(joinRequests);
    }

    public boolean removeMember(UUID playerId) {
        if (!members.remove(playerId)) {
            return false;
        }

        if (owner.equals(playerId) && !members.isEmpty()) {
            owner = members.get(0);
        }

        return true;
    }

    private String color;
    private final List<UUID> teamAdmins = new ArrayList<>();

    public String getColor() {
        return color;
    }

    public List<UUID> getTeamAdmins() {
        return List.copyOf(teamAdmins);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<UUID> getJoinRequests() {
        return List.copyOf(joinRequests);
    }
}