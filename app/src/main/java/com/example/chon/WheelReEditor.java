package com.example.chon;

public class WheelReEditor extends WheelEditor {

    public WheelReEditor(WheelData wd) {
        super();
        thisWheel = wd;
        wheelItemsUI.removeAllViews();
        for (WheelDataItem i : thisWheel.getHashMap().values()) {
            LoadUIElement(i);
        }
        UpdateWheelUI();
    }
}
