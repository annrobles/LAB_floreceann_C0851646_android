package com.example.myapplication.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplication.helper.Converters;

import java.io.Serializable;
import java.util.Date;

@TypeConverters({Converters.class})
// this is our entity in Room db
@Entity(
        tableName = "place_table"
)
public class Place implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "address")
    @NonNull
    private String address;

    @ColumnInfo(name = "latitude")
    @NonNull
    private Double latitude;

    @ColumnInfo(name = "longitude")
    @NonNull
    private Double longitude;

    @ColumnInfo(name = "created_date")
    @NonNull
    private Date createdDate;

    @Ignore
    public Place() {
    }

    public Place(@NonNull String address, Double latitude, Double longitude, Date createdDate) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdDate = createdDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NonNull Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull Double longitude) {
        this.longitude = longitude;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(@NonNull Date createdDate) {
        this.createdDate = createdDate;
    }
}
