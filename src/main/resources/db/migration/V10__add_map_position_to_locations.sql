-- V10__add_map_position_to_locations.sql
-- Add map position columns for interactive floor plan markers

ALTER TABLE locations
    ADD COLUMN map_x DECIMAL(5,2) NULL COMMENT 'Position X as percentage (0-100) on floor plan image',
    ADD COLUMN map_y DECIMAL(5,2) NULL COMMENT 'Position Y as percentage (0-100) on floor plan image',
    ADD COLUMN map_image VARCHAR(500) NULL COMMENT 'URL of the floor plan map image';
