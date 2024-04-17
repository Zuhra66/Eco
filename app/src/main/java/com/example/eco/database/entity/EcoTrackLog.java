package com.example.eco.database.entity;
import com.example.eco.database.EcoTrackRepository;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.eco.database.EcoTrackDatabase;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = EcoTrackDatabase.ecoTrackLogTable)
public class EcoTrackLog {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Transportation;
    private String Energy;
    private String Dietary;
    private String totalEmissions;
    private LocalDateTime date;
    private int userId;
    private String username;

    public EcoTrackLog() {
    }

    public EcoTrackLog(String transportation, String energy, String dietary, String totalEmissions, int userId) {
        this.Transportation = transportation;
        this.Energy = energy;
        this.Dietary = dietary;
        this.totalEmissions = totalEmissions;
        this.userId = userId;
        date = LocalDateTime.now();
        // Set username
        this.username = getUsername(userId);
    }

    @NonNull
    @Override
    public String toString() {
        return "EcoTrackLog" + '\n' +
                " Transportation= " + Transportation + '\n' +
                " Energy= " + Energy + '\n' +
                " Dietary= " + Dietary + '\n' +
                " Total Emissions: " + totalEmissions + '\n' +
                " Date=" + date.toString() + '\n' +
                " Username=" + username + '\n' +
                "=-=-=-=-=-=-=-=-=-=\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EcoTrackLog log = (EcoTrackLog) o;
        return id == log.id && userId == log.userId && Objects.equals(Transportation, log.Transportation) && Objects.equals(Energy, log.Energy) && Objects.equals(Dietary, log.Dietary) && Objects.equals(totalEmissions, log.totalEmissions) && Objects.equals(date, log.date) && Objects.equals(username, log.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Transportation, Energy, Dietary, totalEmissions, date, userId, username);
    }

    // Method to get the username associated with the userId
    private String getUsername(int userId) {
        // Retrieve the username from the repository or database
        // For example, you can use EcoTrackRepository to get the username
        // Replace the null value with the actual username retrieval logic
        return EcoTrackRepository.getUsernameById(userId);
    }

    public String getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(String totalEmissions) {
        this.totalEmissions = totalEmissions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransportation() {
        return Transportation;
    }

    public void setTransportation(String transportation) {
        Transportation = transportation;
    }

    public String getEnergy() {
        return Energy;
    }

    public void setEnergy(String energy) {
        Energy = energy;
    }

    public String getDietary() {
        return Dietary;
    }

    public void setDietary(String dietary) {
        Dietary = dietary;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
