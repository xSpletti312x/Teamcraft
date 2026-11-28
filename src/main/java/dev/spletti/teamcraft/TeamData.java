package dev.spletti.teamcraft;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public class TeamData extends SavedData {
    private final List<Team> teams = new ArrayList<>();

    private static final Codec<TeamData> CODEC = Team.CODEC.listOf().xmap(
        TeamData::new,
        data -> data.teams
    );

    private static final SavedDataType<TeamData> TYPE = new SavedDataType<>(
        Identifier.fromNamespaceAndPath("teamcraft", "teams"),
        TeamData::new,
        CODEC,
        null
    );

    public TeamData() {

    }

    private TeamData(List<Team> teams) {
        this.teams.addAll(teams);
    }

    public static TeamData get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public boolean hasTeam(UUID palyerId) {
        for (Team team : teams) {
            if (team.getMembers().contains(palyerId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasTeamName(String name) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean addTeam(Team team) {
        if (hasTeam(team.getOwner())) {
            return false;
        }
        if (hasTeamName(team.getName())) {
            return false;
        }
        
        teams.add(team);
        setDirty();
        return true;
    }

    public boolean requestToJoin(UUID playerId, Team team) {
        if (hasTeam(playerId)) {
            return false;
        }

        if (!team.addJoinRequest(playerId)) {
            return false;
        }

        setDirty();
        return true;
    }

    public boolean addMemberToJoin(UUID playerId, Team team) {
        if (hasTeam(playerId)) {
            return false;
        }

        if (!team.addMembers(playerId)) {
            return false;
        }

        team.removeJoinRequest(playerId);
        setDirty();
        return true;
    }

    public boolean approveJoinRequest(UUID approveId, UUID requesterId) {
        Optional<Team> foundTeam = findTeam(approveId);

        if(foundTeam.isEmpty()) {
            return false;
        }

        Team team = foundTeam.get();

        if (!team.getJoinRequests().contains(requesterId)) {
            return false;
        }

        return addMemberToJoin(requesterId, team);
    }

    public Optional<Team> findTeam(UUID palyerId) {
        for (Team team : teams) {
            if (team.getMembers().contains(palyerId)) {
                return Optional.of(team);
            }
        }
        return Optional.empty();
    }

    public boolean leaveTeam(UUID playerId) {
        Optional<Team> foundTeam = findTeam(playerId);

        if (foundTeam.isEmpty()) {
            return false;
        }

        Team team = foundTeam.get();

        if (!team.removeMember(playerId)) {
            return false;
        }

        if (team.getMembers().isEmpty()) {
            teams.remove(team);
        }

        setDirty();
        return true;
    }

    public List<String> getTeamNames() {
        List<String> teamNames = new ArrayList<>();

        for (Team team : teams) {
            teamNames.add(team.getName());
        }

        return List.copyOf(teamNames);
    }

    public boolean renameTeam(Team currentTeam, String newName) {
        for (Team team : teams) {
            if (team != currentTeam && team.getName().equalsIgnoreCase(newName)) {
                return false;
            }
        }

        currentTeam.setName(newName);
        setDirty();
        return true;
    }

    public void setTeamColor(Team team, String color) {
        team.setColor(color);
        setDirty();
    }

    public Optional<Team> findTeamByName(String name) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                return Optional.of(team);
            }
        }
        return Optional.empty();
    }
}
