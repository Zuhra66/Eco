package com.example.eco.database.entity;

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
    private LocalDateTime date;

    public EcoTrackLog() {
    }

    public EcoTrackLog(String transportation, String energy, String dietary) {
        Transportation = transportation;
        Energy = energy;
        Dietary = dietary;
        date = LocalDateTime.now();
    }

    @NonNull
    @Override
    public String toString() {
        return "EcoTrackLog" + '\n'+
                ", Transportation='" + Transportation + '\'' +
                ", Energy='" + Energy + '\'' +
                ", Dietary='" + Dietary + '\'' +
                ", date=" + date.toString()+'\n'+
                "=-=-=-=-=-=-=-=-=-=\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EcoTrackLog that = (EcoTrackLog) o;
        return id == that.id && Objects.equals(Transportation, that.Transportation) && Objects.equals(Energy, that.Energy) && Objects.equals(Dietary, that.Dietary) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Transportation, Energy, Dietary, date);
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
}
