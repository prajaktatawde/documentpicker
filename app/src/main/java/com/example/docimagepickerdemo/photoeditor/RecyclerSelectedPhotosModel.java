package com.example.docimagepickerdemo.photoeditor;

import java.io.Serializable;

public class RecyclerSelectedPhotosModel implements Serializable {
    String doc_link;
    String doc_type;
    String doc_description;
    boolean isSelected;

    public RecyclerSelectedPhotosModel(String doc_link, String doc_type, String doc_description) {
        this.doc_link = doc_link;
        this.doc_type = doc_type;
        this.doc_description = doc_description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDoc_link() {
        return doc_link;
    }

    public void setDoc_link(String doc_link) {
        this.doc_link = doc_link;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getDoc_description() {
        return doc_description;
    }

    public void setDoc_description(String doc_description) {
        this.doc_description = doc_description;
    }
}
