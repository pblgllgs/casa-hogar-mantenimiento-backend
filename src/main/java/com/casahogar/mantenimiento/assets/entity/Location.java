package com.casahogar.mantenimiento.assets.entity;

import com.casahogar.mantenimiento.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private LocationType type;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Location parent;

    @Column(name = "floor", length = 20)
    private String floor;

    @Column(name = "wing", length = 50)
    private String wing;

    @Column(name = "room_number", length = 20)
    private String roomNumber;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "area_sqm", precision = 10, scale = 2)
    private java.math.BigDecimal areaSqm;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "map_x", precision = 5, scale = 2)
    private java.math.BigDecimal mapX;

    @Column(name = "map_y", precision = 5, scale = 2)
    private java.math.BigDecimal mapY;

    @Column(name = "map_image", length = 500)
    private String mapImage;

    public enum LocationType {
        BUILDING, FLOOR, WING, ROOM, COMMON_AREA, KITCHEN, BATHROOM, LAUNDRY,
        OFFICE, STORAGE, OUTDOOR, PARKING, GARDEN, PLAYGROUND, MEDICAL_ROOM,
        DINING_ROOM, CLASSROOM, LIBRARY, CHAPEL, OTHER, STUDY_ROOM, RECREATION
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocationType getType() { return type; }
    public void setType(LocationType type) { this.type = type; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public Location getParent() { return parent; }
    public void setParent(Location parent) { this.parent = parent; }

    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }

    public String getWing() { return wing; }
    public void setWing(String wing) { this.wing = wing; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public java.math.BigDecimal getAreaSqm() { return areaSqm; }
    public void setAreaSqm(java.math.BigDecimal areaSqm) { this.areaSqm = areaSqm; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public java.math.BigDecimal getMapX() { return mapX; }
    public void setMapX(java.math.BigDecimal mapX) { this.mapX = mapX; }

    public java.math.BigDecimal getMapY() { return mapY; }
    public void setMapY(java.math.BigDecimal mapY) { this.mapY = mapY; }

    public String getMapImage() { return mapImage; }
    public void setMapImage(String mapImage) { this.mapImage = mapImage; }
}
