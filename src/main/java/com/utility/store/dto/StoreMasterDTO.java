package com.utility.store.dto;

public class StoreMasterDTO {

    private String storeId;
    private String bocId;
    private String storeName;
    private String whenAddedDate;

    public StoreMasterDTO() {
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getBocId() {
        return bocId;
    }

    public void setBocId(String bocId) {
        this.bocId = bocId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getWhenAddedDate() {
        return whenAddedDate;
    }

    public void setWhenAddedDate(String whenAddedDate) {
        this.whenAddedDate = whenAddedDate;
    }
}
