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

package com.sasha.adorufu.mod.mixins.client;

import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import com.sasha.adorufu.mod.AdorufuMod;
import com.sasha.adorufu.mod.misc.Manager;
import com.sasha.adorufu.mod.module.modules.ModuleClientIgnore;
import com.sasha.adorufu.mod.module.modules.ModuleNameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.client.gui.Gui.drawRect;

@Mixin(value = GuiPlayerTabOverlay.class, priority = 999)
public abstract class MixinGuiPlayerTabOverlay {
    @Shadow @Final private Minecraft mc;

    @Shadow protected abstract void drawPing(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo networkPlayerInfoIn);

    @Shadow protected abstract void drawScoreboardValues(ScoreObjective objective, int p_175247_2_, String name, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo info);

    @Shadow public abstract String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn);

    @Shadow private ITextComponent footer;

    @Shadow private ITextComponent header;

    @Shadow @Final private static Ordering<NetworkPlayerInfo> ENTRY_ORDERING;

    /**
     * @author Sasha Stevens
     * @reason make this THICCer
     */
    @Overwrite
    public void renderPlayerlist(int width, Scoreboard scoreboardIn, @Nullable ScoreObjective scoreObjectiveIn)
    {
        boolean exTab = Manager.Module.getModule("ExtendedTablist").isEnabled();
        NetHandlerPlayClient nethandlerplayclient = this.mc.player.connection;
        List<NetworkPlayerInfo> list = ENTRY_ORDERING.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
        int i = 0;
        int j = 0;

        if (Manager.Module.getModule(ModuleNameProtect.class).isEnabled()) {
            for (NetworkPlayerInfo networkPlayerInfo : list) {
                if (networkPlayerInfo.getDisplayName().getUnformattedText().equalsIgnoreCase(AdorufuMod.minecraft.player.getName())) {
                    list.remove(networkPlayerInfo);
                    break;
                }
            }
        }

        for (NetworkPlayerInfo networkPlayerInfo : list) {
            if (ModuleClientIgnore.ignorelist.contains(networkPlayerInfo.getDisplayName().getUnformattedText())) {
                networkPlayerInfo.setDisplayName(new TextComponentString("\2478" + networkPlayerInfo.getDisplayName()));
                continue;
            }
            if (AdorufuMod.FRIEND_MANAGER.getFriendListString().contains(networkPlayerInfo.getDisplayName().getUnformattedText())) {
                networkPlayerInfo.setDisplayName(new TextComponentString("\247b" + networkPlayerInfo.getDisplayName()));
            }
        }

        for (NetworkPlayerInfo networkplayerinfo : list)
        {
            int k = this.mc.fontRenderer.getStringWidth(this.getPlayerName(networkplayerinfo));
            i = Math.max(i, k);

            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreCriteria.EnumRenderType.HEARTS)
            {
                k = this.mc.fontRenderer.getStringWidth(" " + scoreboardIn.getOrCreateScore(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                j = Math.max(j, k);
            }
        }

        list = list.subList(0, Math.min(list.size(), (exTab ? 256 : 80)));
        int l3 = list.size();
        int i4 = l3;
        int j4;

        for (j4 = 1; i4 > (exTab && list.size() >= 80 ? 25 : 20); i4 = (l3 + j4 - 1) / j4)
        {
            ++j4;
        }

        boolean flag = this.mc.isIntegratedServerRunning() || this.mc.getConnection().getNetworkManager().isEncrypted();
        int l;

        if (scoreObjectiveIn != null)
        {
            if (scoreObjectiveIn.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS)
            {
                l = 90;
            }
            else
            {
                l = j;
            }
        }
        else
        {
            l = 0;
        }

        int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
        int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
        int k1 = 10; // makes the individual columns hold more players.
        int l1 = i1 * j4 + (j4 - 1) * 5;
        List<String> list1 = null;

        if (this.header != null)
        {
            list1 = this.mc.fontRenderer.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);

            for (String s : list1)
            {
                l1 = Math.max(l1, this.mc.fontRenderer.getStringWidth(s));
            }
        }

        List<String> list2 = null;

        if (this.footer != null)
        {
            list2 = this.mc.fontRenderer.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);

            for (String s1 : list2)
            {
                l1 = Math.max(l1, this.mc.fontRenderer.getStringWidth(s1));
            }
        }

        if (list1 != null)
        {
            drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list1.size() * this.mc.fontRenderer.FONT_HEIGHT, Integer.MIN_VALUE);

            for (String s2 : list1)
            {
                int i2 = this.mc.fontRenderer.getStringWidth(s2);
                this.mc.fontRenderer.drawStringWithShadow(s2, (float)(width / 2 - i2 / 2), (float)k1, -1);
                k1 += this.mc.fontRenderer.FONT_HEIGHT;
            }

            ++k1;
        }

        drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);

        for (int k4 = 0; k4 < l3; ++k4)
        {
            int l4 = k4 / i4;
            int i5 = k4 % i4;
            int j2 = j1 + l4 * i1 + l4 * 5;
            int k2 = k1 + i5 * 9;
            drawRect(j2, k2, j2 + i1, k2 + 8, 553648127);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (k4 < list.size())
            {
                NetworkPlayerInfo networkplayerinfo1 = list.get(k4);
                GameProfile gameprofile = networkplayerinfo1.getGameProfile();

                if (flag)
                {
                    EntityPlayer entityplayer = this.mc.world.getPlayerEntityByUUID(gameprofile.getId());
                    boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && ("Dinnerbone".equals(gameprofile.getName()) || "Grumm".equals(gameprofile.getName()));
                    this.mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
                    int l2 = 8 + (flag1 ? 8 : 0);
                    int i3 = 8 * (flag1 ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, (float)l2, 8, i3, 8, 8, 64.0F, 64.0F);

                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT))
                    {
                        int j3 = 8 + (flag1 ? 8 : 0);
                        int k3 = 8 * (flag1 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, (float)j3, 8, k3, 8, 8, 64.0F, 64.0F);
                    }

                    j2 += 9;
                }

                String s4 = this.getPlayerName(networkplayerinfo1);

                if (networkplayerinfo1.getGameType() == GameType.SPECTATOR)
                {
                    this.mc.fontRenderer.drawStringWithShadow(TextFormatting.ITALIC + s4, (float)j2, (float)k2, -1862270977);
                }
                else
                {
                    this.mc.fontRenderer.drawStringWithShadow(s4, (float)j2, (float)k2, -1);
                }

                if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != GameType.SPECTATOR)
                {
                    int k5 = j2 + i + 1;
                    int l5 = k5 + l;

                    if (l5 - k5 > 5)
                    {
                        this.drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
                    }
                }

                this.drawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
            }
        }

        if (list2 != null)
        {
            k1 = k1 + i4 * 9 + 1;
            drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list2.size() * this.mc.fontRenderer.FONT_HEIGHT, Integer.MIN_VALUE);

            for (String s3 : list2)
            {
                int j5 = this.mc.fontRenderer.getStringWidth(s3);
                this.mc.fontRenderer.drawStringWithShadow(s3, (float)(width / 2 - j5 / 2), (float)k1, -1);
                k1 += this.mc.fontRenderer.FONT_HEIGHT;
            }
        }
    }
}
