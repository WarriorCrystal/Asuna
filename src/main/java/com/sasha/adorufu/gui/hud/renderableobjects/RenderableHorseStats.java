package com.sasha.adorufu.gui.hud.renderableobjects;


import com.sasha.adorufu.AdorufuMod;
import com.sasha.adorufu.gui.hud.RenderableObject;
import com.sasha.adorufu.gui.hud.ScreenCornerPos;
import com.sasha.adorufu.gui.hud.AdorufuHUD;
import com.sasha.adorufu.gui.fonts.FontManager;
import com.sasha.adorufu.module.ModuleManager;
import net.minecraft.entity.passive.EntityHorse;

import java.io.IOException;

import static com.sasha.adorufu.misc.AdorufuMath.dround;
import static com.sasha.adorufu.AdorufuMod.minecraft;

public class RenderableHorseStats extends RenderableObject {
    public RenderableHorseStats() {
        super("HorseStats", ScreenCornerPos.RIGHTTOP);
        try {
            this.setPos(AdorufuMod.DATA_MANAGER.getHudPositionState(this));
        } catch (IOException e) {
            e.printStackTrace();
            this.setPos(this.getDefaultPos());
        }
    }

    @Override
    public void renderObjectLT(int yyy) {
        if (ModuleManager.getModule("HorseStats").isEnabled()) {
            String s = "\247" + "3Horse Stats: " + "\247" + "7Jump Height: " + "\247" + "f@JH" + "\247" + "7 Health: " + "\247" + "f@<3" + "\247" + "7 Max Speed: " + "\247" + "f@S";
            if (minecraft.player.isRidingHorse() && minecraft.player.getRidingEntity() instanceof EntityHorse) {
                EntityHorse e = ((EntityHorse) minecraft.player.getRidingEntity());
                AdorufuMod.FONT_MANAGER.segoe_36.drawStringWithShadow(s.replace("@JH", "" + dround(e.getHorseJumpStrength(), 2)).replace("@<3", "" + e.getMaxHealth() / 2).replace("@S", "" + dround((e.getAIMoveSpeed() * 20) * 3, 3)), 4, yyy, 0xffffff);
                return;
            }
            AdorufuMod.FONT_MANAGER.segoe_36.drawStringWithShadow("\247" + "3Horse stats: " + "\247" + "4None.", 4, yyy, 0xffffff);
        }
    }
    @Override
    public void renderObjectLB(int yyy) {
        if (ModuleManager.getModule("HorseStats").isEnabled()) {
            String s = "\247" + "3Horse Stats: " + "\247" + "7Jump Height: " + "\247" + "f@JH" + "\247" + "7 Health: " + "\247" + "f@<3" + "\247" + "7 Max Speed: " + "\247" + "f@S";
            if (minecraft.player.isRidingHorse() && minecraft.player.getRidingEntity() instanceof EntityHorse) {
                EntityHorse e = ((EntityHorse) minecraft.player.getRidingEntity());
                AdorufuMod.FONT_MANAGER.segoe_36.drawStringWithShadow(s.replace("@JH", "" + dround(e.getHorseJumpStrength(), 2)).replace("@<3", "" + e.getMaxHealth() / 2).replace("@S", "" + dround((e.getAIMoveSpeed() * 20) * 3, 3)), 4, yyy, 0xffffff);
                return;
            }
            AdorufuMod.FONT_MANAGER.segoe_36.drawStringWithShadow("\247" + "3Horse stats: " + "\247" + "4None.", 4, yyy, 0xffffff);
        }
    }
    @Override
    public void renderObjectRT(int yyy) {
        if (ModuleManager.getModule("HorseStats").isEnabled()) {
            String s = "\247" + "3Horse Stats: " + "\247" + "7Jump Height: " + "\247" + "f@JH" + "\247" + "7 Health: " + "\247" + "f@<3" + "\247" + "7 Max Speed: " + "\247" + "f@S";
            if (minecraft.player.isRidingHorse() && minecraft.player.getRidingEntity() instanceof EntityHorse) {
                EntityHorse e = ((EntityHorse) minecraft.player.getRidingEntity());
                AdorufuMod.FONT_MANAGER.segoe_36.drawStringWithShadow(s.replace("@JH", "" + dround(e.getHorseJumpStrength(), 2)).replace("@<3", "" + e.getMaxHealth() / 2).replace("@S", "" + dround((e.getAIMoveSpeed() * 20) * 3, 3)), (AdorufuHUD.sWidth - AdorufuMod.FONT_MANAGER.segoe_36.getStringWidth(s.replace("@JH", "" + dround(e.getHorseJumpStrength(), 2)).replace("@<3", "" + e.getMaxHealth() / 2).replace("@S", "" + dround((e.getAIMoveSpeed() * 20) * 3, 3))) - 2), yyy, 0xffffff);
                return;
            }
            AdorufuMod.FONT_MANAGER.segoe_36.drawStringWithShadow("\247" + "3Horse stats: " + "\247" + "4None.", (AdorufuHUD.sWidth - AdorufuMod.FONT_MANAGER.segoe_36.getStringWidth("Horse stats: None.")) - 2, yyy, 0xffffff);
        }
    }
    @Override
    public void renderObjectRB(int yyy) {
        if (ModuleManager.getModule("HorseStats").isEnabled()) {
            String s = "\247" + "3Horse Stats: " + "\247" + "7Jump Height: " + "\247" + "f@JH" + "\247" + "7 Health: " + "\247" + "f@<3" + "\247" + "7 Max Speed: " + "\247" + "f@S";
            if (minecraft.player.isRidingHorse() && minecraft.player.getRidingEntity() instanceof EntityHorse) {
                EntityHorse e = ((EntityHorse) minecraft.player.getRidingEntity());
                AdorufuMod.FONT_MANAGER.segoe_36.drawStringWithShadow(s.replace("@JH", "" + dround(e.getHorseJumpStrength(), 2)).replace("@<3", "" + e.getMaxHealth() / 2).replace("@S", "" + dround((e.getAIMoveSpeed() * 20) * 3, 3)), (AdorufuHUD.sWidth - AdorufuMod.FONT_MANAGER.segoe_36.getStringWidth(s.replace("@JH", "" + dround(e.getHorseJumpStrength(), 2)).replace("@<3", "" + e.getMaxHealth() / 2).replace("@S", "" + dround((e.getAIMoveSpeed() * 20) * 3, 3))) - 2), yyy, 0xffffff);
                return;
            }
            AdorufuMod.FONT_MANAGER.segoe_36.drawStringWithShadow("\247" + "3Horse stats: " + "\247" + "4None.", (AdorufuHUD.sWidth - AdorufuMod.FONT_MANAGER.segoe_36.getStringWidth("Horse stats: None.")) - 2, yyy, 0xffffff);
        }
    }
}
