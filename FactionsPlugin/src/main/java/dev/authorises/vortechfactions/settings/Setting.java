package dev.authorises.vortechfactions.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Setting {
    private Set<SettingsCategory> categories;

    /**
     * @param category category to add to the list of categories
     */
    public void addCategory(SettingsCategory category){
        this.categories.add(category);
    }

    /**
     * @param category to remove from the list of categories
     */
    public void removeCategory(SettingsCategory category){
        this.categories.remove(category);
    }

    /**
     * @return the categories of this setting
     */
    public ArrayList<SettingsCategory> getCategories(){
        ArrayList<SettingsCategory> cats = new ArrayList<>();
        cats.add(SettingsCategory.ALL);
        return cats;
    }

    public Setting(){
        this.categories = new HashSet<>();
    }

}
