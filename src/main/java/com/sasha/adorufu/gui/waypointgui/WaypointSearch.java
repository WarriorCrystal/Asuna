package com.sasha.adorufu.gui.waypointgui;


import com.sasha.adorufu.AdorufuMod;
import com.sasha.adorufu.gui.fonts.Fonts;
import com.sasha.adorufu.misc.AdorufuMath;
import com.sasha.adorufu.waypoint.Waypoint;
import com.sasha.adorufu.waypoint.WaypointManager;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import org.lwjgl.input.Keyboard;

public class WaypointSearch {

	static Minecraft mc = Minecraft.getMinecraft();

	private WaypointWindow window;
	private String initText;
	private int x, y;
	boolean overButton;
	boolean selected;
	private String text;

	WaypointSearch(WaypointWindow window, String initText, int x, int y) {
		this.window = window;
		this.initText = "\247" + "7" + initText;
		this.x = x;
		this.y = y;
		//this.displayName = mod.getName();
		WaypointGUI.searchBoxes.add(this);
	}

	public void draw() {
		float var1 = 0f;
		float var2 = 0f;
		float var3 = 0f;
		float var4 = 0.5f;
		float varia = 0.0f;
		if (!selected && !overButton) {
			var1 = 0f;
			var2 = 0f;
			var3 = 0f;
			var4 = 0f;
		}if (overButton && !selected) {
			var1 = 0f;
			var2 = 0f;
			var3 = 1.5f;
		}
		 if (selected) {
			var1 = 0.5f;
			var2 = 0.5f;
			var3 = 1.5f;
			var4 = 0.5f;
			varia = 0.3f;
		}
		String currentText;
		//currentText=initText;
		if (text == null) {
			currentText = initText;
		} else {
			currentText = text;
		}
		//currentText=initText;
		AdorufuMath.drawBetterBorderedRect(x + window.getDragX(), y + window.getDragY(), x + 196 + window.getDragX(), y + 12 + window.getDragY(), 0.5F, var1, var2, var3, var4, 1f, 1f, 1f, varia);
		//ClientUtils.drawBetterBorderedRect(x + window.getDragX(), y + window.getDragY(), x + 96 + window.getDragX(), y + 11  + window.getDragY(), 0.5F, 0xF0F0F0FF, 0x00000000);
		Fonts.segoe_30.drawStringWithShadow(currentText, (x + 4 + window.getDragX()), (int) y + window.getDragY(), 0xFFFFFF);
		//Fonts.segoe_30.drawStringWithShadow(Command.SECTION_SIGN + "7XYZ " + Command.SECTION_SIGN + "r" + mod.getCoords()[0] + " " + mod.getCoords()[1] + " " + mod.getCoords()[2], (x + 4 + window.getDragX()), (y + window.getDragY())+10, 0xFFFFFF);
		text = currentText;
	}

	public void updateText(char s, int keyPress) {
		if (keyPress == Keyboard.KEY_ESCAPE) {
			if (this.selected) {
				selected = false;
				return;
			}
			mc.currentScreen = null;
		}
		if (this.selected) {
			if (keyPress == Keyboard.KEY_BACK) {
				if (text.length() == 0) {
					buttonUpdate("");
					text = initText;
					return;
				}
				text = text.substring(0, text.length() - 1);
				if (text.length() == 0) {
					buttonUpdate("");
					text = initText;
					return;
				}
				return;
			}
			if (keyPress == Keyboard.KEY_RETURN) {
				selected = false;
			}
			if (text.contains(String.valueOf("\247"))) {
				text = "";
				text += s;
				buttonUpdate(text);
				return;
			}
			text += s;
			buttonUpdate(text);
		}
	}

	public static void buttonUpdate(String term) {
		WaypointGUI.windowList.get(0).getButtonList().clear();
		for (Waypoint wp : AdorufuMod.WAYPOINT_MANAGER.getWaypoints()){
			if (wp.getName().toLowerCase().contains(term.toLowerCase())) {
				WaypointGUI.windowList.get(0).addButton(wp);
			}
		}
		if (term.equalsIgnoreCase("")) {
			WaypointGUI.windowList.get(0).getButtonList().clear();
			for (Waypoint wp : AdorufuMod.WAYPOINT_MANAGER.getWaypoints()){
				WaypointGUI.windowList.get(0).addButton(wp);
			}
		}
	}

	public void mouseClicked(int x, int y, int button) { //TODO: make sure only one button gets toggled even if the windows are overlapping
		if (x >= getX() + window.getDragX() && y >= getY() + window.getDragY() && x <= getX() + 196 + window.getDragX() && y <= getY() + 21 + window.getDragY() && window.isOpen() && button == 0) {
			WaypointGUI.sendPanelToFront(window);
			selected = !selected;
			mc.world.playSound(mc.player.posX, mc.player.posY, mc.player.posZ, SoundEvents.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1f, 1f, false);
		} else if (x >= getX() + window.getDragX() && y >= getY() + window.getDragY() && x <= getX() + 196 + window.getDragX() && y <= getY() + 11 + window.getDragY() && window.isOpen() && button == 1) {
			WaypointGUI.sendPanelToFront(window);
			mc.world.playSound(mc.player.posX, mc.player.posY, mc.player.posZ, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.MASTER, 1f, 1f, false);

			//Client.pushNotification("Under construction");
		}
	}

	private boolean isAnyWindowDragging() {
		for (WaypointWindow w : WaypointGUI.windowList) {
			if (w.dragging) {
				return true;
			}
		}
		return false;
	}

	public String getText() {
		return text;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setText(String displayName) {
		this.text = displayName;
	}

}