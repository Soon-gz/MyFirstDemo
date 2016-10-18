package com.abings.baby.data.model;

import java.io.Serializable;

public class WaterFallGridHeaderItem implements Serializable {
    private int sectionFirstPosition;
    public boolean isHeader;

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "WaterFallGridHeaderItem{" +
                "sectionFirstPosition=" + sectionFirstPosition +
                ", isHeader=" + isHeader +
                ", header='" + header + '\'' +
                ", item=" + item +
                '}';
    }

    public String getHeader() {
        return header;
    }

    public String header;
    private WaterFallGridItem item;

    public WaterFallGridHeaderItem() {
        super();
    }

    public WaterFallGridHeaderItem(String header, boolean flag, int sectionFirstPosition) {
        super();
        this.header = header;
        this.isHeader = flag;
        this.sectionFirstPosition = sectionFirstPosition;
    }


    public WaterFallGridItem getItem() {
        return item;
    }

    public void setItem(WaterFallGridItem item) {
        this.item = item;
    }

    public int getSectionFirstPosition() {
        return sectionFirstPosition;
    }

    public void setSectionFirstPosition(int sectionFirstPosition) {
        this.sectionFirstPosition = sectionFirstPosition;
    }


    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }


}
