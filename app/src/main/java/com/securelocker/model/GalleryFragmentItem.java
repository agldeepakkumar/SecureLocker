package com.securelocker.model;


import java.io.Serializable;

public class GalleryFragmentItem implements Serializable{

    private String id,imagePath;
    private boolean isSelected;
    private int encryptionValue;  // 1 for encrypted 0 for decrypted

    public GalleryFragmentItem(String id, String imagePath, boolean isSelected, int encryptionValue) {
        this.id = id;
        this.imagePath = imagePath;
        this.isSelected = isSelected;
        this.encryptionValue = encryptionValue;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getEncryptionValue() {
        return encryptionValue;
    }

    public void setEncryptionValue(int encryptionValue) {
        this.encryptionValue = encryptionValue;
    }
}
