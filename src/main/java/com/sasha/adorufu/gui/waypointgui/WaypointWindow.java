package com.sasha.adorufu.gui.waypointgui;

import com.asunaclient.waypoints.Waypoint;
import com.asunaclient.waypoints.WaypointManager;
import com.asunaclient.main.Category;
import com.asunaclient.main.GlobalUtils;
import com.asunaclient.mcgui.clickgui.elements.WindowType;
import com.asunaclient.mcgui.fonts.Fonts;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;


public class WaypointWindow {
	private String title;
	private int x, y;
	public int dragX;
	public int dragY;
	private int lastDragX;
	private int lastDragY;
	private boolean isOpen;
	private boolean isShown;
	private WindowType type;
	protected boolean dragging;

	private boolean isEmpty = false;
	
	private ArrayList<WaypointButton> buttonList = new ArrayList<>();
	//private ArrayList<AsunaOptionButton> optionButtonList = new ArrayList<>();
	
	public void drag(int x, int y) {
		dragX = x - lastDragX;
		dragY = y - lastDragY;
	}
	
	public WaypointWindow(String title, int x, int y, boolean isShown, WindowType type) {
		this.title = title;
		this.x = x;
		this.y = y;
		this.isOpen = true;
		this.isShown = isShown;
		this.type = type;
		com.asunaclient.mcgui.waypointgui.WaypointGUI.windowList.add(this);
		com.asunaclient.mcgui.waypointgui.WaypointGUI.unFocusedWindows.add(this);
	}
	
	public void draw(int x, int y) {
		if (isShown) {
			GL11.glPushMatrix();
			GL11.glPushAttrib(8256);
			int toAdd = 0;
			if (dragging) {
				drag(x, y);
			}
			GlobalUtils.drawBetterBorderedRect(getXAndDrag(), getYAndDrag(), getXAndDrag() + 200, getYAndDrag() + 13 + (isEmpty ? 175 : (isOpen ? 12 + (24 * (buttonList.size()/* + optionButtonList.size()*/) + 0.5F) + (0 + (0 != 0 ? 2.5F : 0)) : 0) + toAdd), 0.5F, 0f, 0.5f, 1f, 1f, 0f, 0f, 0.5f, 0.3f);
			Fonts.segoe_30.drawCenteredString(title, getXAndDrag() + 100, getYAndDrag() + 1, 0xFFFFFF, true);
			if (Minecraft.getMinecraft().currentScreen instanceof WaypointGUI) {
				//ClientUtils.drawBetterBorderedRect(getXAndDrag() + 79, getYAndDrag() + 2, getXAndDrag() + 88, getYAndDrag() + 11, 0.5F, 0xFF000000, isPinned ? 0xFFFF0000 : 0xFF383b42);
				//ClientUtils.drawBetterBorderedRect(getXAndDrag() + 89, getYAndDrag() + 2, getXAndDrag() + 98, getYAndDrag() + 11, 0.5F, 0xFF000000, isOpen ? 0x2480dbFF : 0xFF383b42);
				if (isOpen) {
				} else {
					//ClientUtils.drawTex(arrowup, 32, 32, 8, 8, 1f);
				}
			}

			if (isOpen) {
				for (WaypointSearch b : WaypointGUI.searchBoxes) {
					b.draw();
					if (x >= b.getX() + dragX && y >= b.getY() + dragY && x <= b.getX() + 196 + dragX && y <= b.getY() + 11 + dragY) {
						b.overButton = true;
					} else {
						b.overButton = false;
					}
				}
				for (WaypointButton b : buttonList) {
					b.draw();
					if (x >= b.getX() + dragX && y >= b.getY() + dragY && x <= b.getX() + 196 + dragX && y <= b.getY() + 21 + dragY) {
						b.overButton = true;
					} else {
						b.overButton = false;
					}
				}
				if (buttonList.size()==0){
					Fonts.segoe_36.drawCenteredString("Well, you won't find anything useful here.",getXAndDrag() + 100, getYAndDrag()+75,0xFFFFFF, true);
					Fonts.segoe_30.drawCenteredString("Use the -waypoint command to set and delete waypoints.",getXAndDrag() + 100, getYAndDrag()+85,0xFFFFFF, true);
					isEmpty = true;
				}
				else {
					isEmpty = false;
				}
			}
			//GL11.glEnable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			GL11.glPopAttrib();
		}
	}
	
	public void mouseClicked(int x, int y, int button) throws IOException {
		for(WaypointButton b : buttonList) {
			b.mouseClicked(x, y, button);
		}
		for(WaypointSearch s : WaypointGUI.searchBoxes) {
			s.mouseClicked(x,y,button);
		}
		/*for(AsunaOptionButton b : optionButtonList) {
			b.mouseClicked(x, y, button);
		}*/
		
		if(x >= getXAndDrag() && y >= getYAndDrag() && x <= getXAndDrag() + 200 && y <= getYAndDrag() + 11) {
			WaypointGUI.sendPanelToFront(this);
			dragging = true;
			lastDragX = x - dragX;
			lastDragY = y - dragY;
		}
	}
	
	public void mouseReleased(int x, int y, int state) {
		if(state == 0) {
			dragging = false;
		}
	}
	
	public void addButton(Waypoint module) {
		buttonList.add(new WaypointButton(this, module, x + 2, y + 22 + (24 * buttonList.size())));
	}
	public void addOptionsButton(String name, String booleanToToggle) {
		//optionButtonList.add(new AsunaOptionButton(this, name, x+2, y+12 + (12 * optionButtonList.size()), booleanToToggle));
	}
	public void loadButtonsFromCategory(Category category) {
		for(Waypoint m : WaypointManager.getWaypoints()) {
			addButton(m);
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getDragX() {
		return dragX;
	}
	
	public int getDragY() {
		return dragY;
	}
	
	public int getXAndDrag() {
		return x + dragX;
	}
	
	public int getYAndDrag() {
		return y + dragY;
	}
	
	public void setTitle(String s) {
		this.title = s;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public void setXandY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	public boolean isShown() {
		return isShown;
	}

	public void setShown(boolean shownornot) {
		this.isShown = shownornot;
	}
	
	public void setOpen(boolean b) {
		this.isOpen = true;
	}
	
	public ArrayList<WaypointButton> getButtonList() {
		return buttonList;
	}

	public WindowType getType() {
		return type;
	}
}
