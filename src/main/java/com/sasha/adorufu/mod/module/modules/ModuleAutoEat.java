/*
 * Copyright (c) Sasha Stevens 2018.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.sasha.adorufu.mod.module.modules;

import com.sasha.adorufu.mod.AdorufuMod;
import com.sasha.adorufu.mod.module.AdorufuCategory;
import com.sasha.adorufu.mod.module.AdorufuModule;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;

public class ModuleAutoEat extends AdorufuModule {

    private static boolean eating = false;
    private static boolean checked = false;

    public ModuleAutoEat() {
        super("AutoEat", AdorufuCategory.MISC, false, true, true);
        this.addOption("priority gapple", false);
        this.addOption("conserve gapple", true);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTick() {
        if (!this.isEnabled()) return;
        if (AdorufuMod.minecraft.player.getFoodStats().getFoodLevel() <= 8) {
            // we need to eat
            for (int s = 36; s <= 44; s++) {
                if (AdorufuMod.minecraft.player.inventory.getStackInSlot(s).getItemUseAction() == EnumAction.EAT) {
                    // we can eat this item
                    if (this.getOption("conserve gapple")) {
                        if (AdorufuMod.minecraft.player.inventory.getStackInSlot(s).getItem() == Items.GOLDEN_APPLE) {
                            continue;
                        }
                    }
                    if (this.getOption("priority gapple") && !checked) {
                        if (AdorufuMod.minecraft.player.inventory.getStackInSlot(s).getItem() != Items.GOLDEN_APPLE) {
                            continue;
                        }
                    }
                    eating = true;
                    checked = false;
                    AdorufuMod.minecraft.player.inventory.currentItem = s;
                    AdorufuMod.setPressed(AdorufuMod.minecraft.gameSettings.keyBindUseItem, true);
                    return;
                }
            }
            if (this.getOption("priority gapple")) {
                checked = true;
            }
            return;
        }
        if (eating) {
            AdorufuMod.setPressed(AdorufuMod.minecraft.gameSettings.keyBindUseItem, false);
            eating = false;
        }
    }
}
