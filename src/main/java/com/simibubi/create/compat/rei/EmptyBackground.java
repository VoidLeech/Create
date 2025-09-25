package com.simibubi.create.compat.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import net.minecraft.client.gui.GuiGraphics;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EmptyBackground implements Renderer {

	private int width;
	private int height;

	public EmptyBackground(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void render(GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
	}
}
