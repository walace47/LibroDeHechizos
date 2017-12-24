package com.joan.librohechizos.utiles;

import android.widget.ImageView;

/**
 * Created by Joan on 29/09/2017.
 */

public class MenuItem {
    private String name;
    private int iconId;

    public MenuItem(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
