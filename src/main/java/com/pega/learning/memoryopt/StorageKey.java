package com.pega.learning.memoryopt;

/**
 * This class represents a composite storage key in some distributed system.
 * The size of this object is about 24 bytes.
 */
final class StorageKey {
    private final int regionId;
    private final int shardId;

    public StorageKey(int regionId, int shardId) {
        this.regionId = regionId;
        this.shardId = shardId;
    }

    public int getRegionId() {
        return regionId;
    }

    public int getShardId() {
        return shardId;
    }
}