package dev.authorises.cavelet.deepmarket;
import com.mitchtalmadge.asciidata.graph.ASCIIGraph;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.gui.DeepMarketBuyGUI;
import dev.authorises.cavelet.gui.DeepMarketGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DeepMarketManager {
    public HashMap<String, DeepMarketItem> marketItems;
    private Boolean open;
    private Long timeOpen;
    private Long timeClose;
    public Long updateDelay;
    public ArrayList<DeepMarketGUI> openGuis;
    public ArrayList<DeepMarketBuyGUI> openBuyGuis;


    private void readFromApi(){
        JSONParser parser = new JSONParser();
        try {
            URL oracle = new URL("http://38.242.136.220:8080/items"); // URL to Parse
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            this.open = true;
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                JSONArray a = (JSONArray) parser.parse(inputLine);
                for (Object o : a) {
                    JSONObject item = (JSONObject) o;
                    String itemid = (String) item.get("itemId");
                    if(!marketItems.containsKey(itemid)){
                        marketItems.put(itemid, new DeepMarketItem(
                                Cavelet.customItemsManager.getItemById((String) item.get("itemId")),
                                (Double) item.get("minimumPrice"),
                                (Double) item.get("maximumPrice"),
                                (Double) item.get("startPrice"),
                                DeepMarketCategory.valueOf((String) item.get("category")),
                                Material.valueOf((String) item.get("material"))
                        ));
                    }else{
                        marketItems.get(itemid).setPrice((Double) item.get("price"));
                    }
                }
            }
            in.close();
        } catch (ConnectException e) {
            this.open = false;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public DeepMarketManager(){
        this.marketItems = new HashMap<>();
        this.openGuis = new ArrayList<>();
        this.openBuyGuis = new ArrayList<>();
        this.updateDelay = 200L;
        this.open = true;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Cavelet.getPlugin(Cavelet.class), new Runnable() {
            @Override
            public void run() {
                readFromApi();
                if(open){
                    for(DeepMarketGUI gui: openGuis){
                        try {
                            gui.update(gui.gui);
                        } catch (InvalidItemIdException | ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    for(DeepMarketBuyGUI gui: openBuyGuis){
                        try {
                            gui.update(gui.gui);
                        } catch (InvalidItemIdException | ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        },0L, updateDelay);
    }

    public Boolean getOpen(){
        return this.open;
    }

    public void closeMarket(){
        this.open = false;
        this.timeClose = System.currentTimeMillis();
        this.timeOpen = this.timeClose+600000;
    }

    public void openMarket(){
        this.open = true;
        this.timeOpen = System.currentTimeMillis();
        this.timeClose = this.timeOpen+600000;
    }

    public HashMap<String, DeepMarketItem> getMarketItems() {
        return marketItems;
    }

}
