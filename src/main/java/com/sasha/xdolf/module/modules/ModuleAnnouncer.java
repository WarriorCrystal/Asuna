package com.sasha.xdolf.module.modules;

import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleListener;
import com.sasha.xdolf.XdolfMod;
import com.sasha.xdolf.events.PlayerBlockBreakEvent;
import com.sasha.xdolf.events.PlayerBlockPlaceEvent;
import com.sasha.xdolf.module.ModuleInfo;
import com.sasha.xdolf.module.XdolfCategory;
import com.sasha.xdolf.module.XdolfModule;
import net.minecraft.util.text.TextComponentString;

import java.util.*;

import static com.sasha.xdolf.module.modules.ModuleAnnouncer.blocksBrokenMap;
import static com.sasha.xdolf.module.modules.ModuleAnnouncer.blocksPlacedMap;
import static com.sasha.xdolf.module.modules.ModuleAnnouncer.swap;

/**
 * Created by Sasha on 11/08/2018 at 4:31 PM
 **/
@ModuleInfo(description = "Sends a message in chat every 30 seconds about what you're doing in the world.")
public class ModuleAnnouncer extends XdolfModule implements SimpleListener {

    private Timer announcerTimer = new Timer();
    static boolean swap = false;

    public static LinkedHashMap<String, Integer> blocksBrokenMap = new LinkedHashMap<>();
    public static LinkedHashMap<String, Integer> blocksPlacedMap = new LinkedHashMap<>();

    public ModuleAnnouncer() {
        super("Announcer", XdolfCategory.CHAT, false);
    }

    @Override
    public void onEnable(){
        blocksBrokenMap.clear();
        blocksPlacedMap.clear();
        announcerTimer.scheduleAtFixedRate(new AnnouncerTask(), 3000, 3000);
    }

    @Override
    public void onDisable() {
        announcerTimer.cancel();
    }

    @Override
    public void onTick() {

    }
    @SimpleEventHandler
    public void onBlockBreak(PlayerBlockBreakEvent e){
        if (this.isEnabled()){
            if (blocksBrokenMap.containsKey(e.getBlock().getLocalizedName())){
                blocksBrokenMap.put(e.getBlock().getLocalizedName(), (blocksBrokenMap.get(e.getBlock().getLocalizedName()))+1);
                return;
            }
            blocksBrokenMap.put(e.getBlock().getLocalizedName(), 1);
        }
    }
    @SimpleEventHandler
    public void onBlockBreak(PlayerBlockPlaceEvent e){
        if (this.isEnabled()){
            if (blocksPlacedMap.containsKey(e.getBlock().getLocalizedName())){
                blocksPlacedMap.put(e.getBlock().getLocalizedName(), (blocksBrokenMap.get(e.getBlock().getLocalizedName()))+1);
                return;
            }
            blocksPlacedMap.put(e.getBlock().getLocalizedName(), 1);
        }
    }
}
class AnnouncerTask extends TimerTask{
    @Override
    public void run() {
        XdolfMod.logMsg(true, "Refreshing announcer");
        if (XdolfMod.minecraft.world == null) return; //not logged in to a world
        Random rand = new Random();
        if (ModuleAnnouncer.swap && !blocksBrokenMap.isEmpty()){
            ArrayList<String> blockNames= new ArrayList<>(blocksBrokenMap.keySet());
            String blockname= (blockNames.get(rand.nextInt(blockNames.size())));
            int amt = blocksBrokenMap.get(blockname);
            XdolfMod.minecraft.player.sendMessage(new TextComponentString("> I just mined " + amt + " " + blockname));
            blocksBrokenMap.remove(blockname);
            ModuleAnnouncer.swap=!swap;
            return;
        }
        if (!ModuleAnnouncer.swap && !blocksPlacedMap.isEmpty()){
            ArrayList<String> blockNames= new ArrayList<>(blocksPlacedMap.keySet());
            String blockname= (blockNames.get(rand.nextInt(blockNames.size())));
            int amt = blocksPlacedMap.get(blockname);
            XdolfMod.minecraft.player.sendMessage(new TextComponentString("> I just placed " + amt + " " + blockname));
            blocksPlacedMap.remove(blockname);
            ModuleAnnouncer.swap=!swap;
            return;
        }
    }
}