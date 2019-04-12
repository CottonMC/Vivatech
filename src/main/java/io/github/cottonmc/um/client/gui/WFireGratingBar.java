package io.github.cottonmc.um.client.gui;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import io.github.cottonmc.gui.client.ScreenDrawing;
import io.github.cottonmc.gui.widget.WBar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class WFireGratingBar extends WBar {
	public static Identifier BACKGROUND = new Identifier("united-manufacturing", "textures/gui/coal_generator_grating_bg.png");
	public static Identifier MASK = new Identifier("united-manufacturing", "textures/gui/coal_generator_grating_mask.png");
	public static Identifier FIRE = new Identifier("united-manufacturing", "textures/gui/coal_generator_grating_flames.png");
	private boolean firstFrame = true;
	private long lastFrame = 0L;
	private long frameTime = 0L;
	private static final long FRAME_LENGTH = 17L; //17L == 60fps; 33L == 30fps
	private static final double THETA_PER_FRAME = Math.PI / 64.0;
	private static final double THETA_PER_SEGMENT = Math.PI / 8.0;
	
	private double theta = 0.0;
	
	public WFireGratingBar(int field, int maxfield) {
		super(null, null, field, maxfield);
	}
	
	@Override
	public void paintBackground(int x, int y) {
		ScreenDrawing.rect(BACKGROUND, x, y, getWidth(), getHeight(), 0xFFFFFFFF);
		
		float percent = properties.get(field) / (float) properties.get(max);
		if (percent < 0) percent = 0f;
		if (percent > 1) percent = 1f;
		
		int barMax = getHeight();
		percent = ((int) (percent * barMax)) / (float) barMax; //Quantize to bar size
		
		int barSize = (int) (barMax * percent);
		if (barSize <= 0) return;
		
		
		//int left = x;
		//int top = y + getHeight();
		//top -= barSize;
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		GL11.glDepthRange(0.0, 1.0);
		GL11.glClearDepth(1.0);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glDepthMask(true);
		
		//GlStateManager.colorMask(false, false, false, false);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0f, 0.0f, 0.5f);
		rect(MASK, x, y, getWidth(), getHeight(), 0f, 0f, 1f, 1f, 0.01);
		
		//GL11.glDepthMask(false);
		//GlStateManager.colorMask(true, true, true, true);
		
		int numSegments = 18*2;
		int segmentHeight = getHeight() / numSegments;
		GL11.glDepthFunc(GL11.GL_EQUAL);
		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		for(int i=0; i<numSegments; i++) {
			int wavey = (int)(Math.cos(theta + i*THETA_PER_SEGMENT)*8);
			float uHeight = 1f/numSegments;
			rect(FIRE, x+wavey, y + (i*segmentHeight), getWidth(), segmentHeight, 0f, uHeight*i, 1f, uHeight*i + uHeight, 0.01);
		}
		GL11.glPopMatrix();
		/*
		float[] pixelData = new float[getWidth()*getHeight()];
		GL11.glReadPixels(x, y, getWidth(), getHeight(), GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, pixelData);
		//System.out.println(Arrays.toString(pixelData));
		boolean depthFound = false;
		for(float f : pixelData) {
			if (f!=1.0f) {
				System.out.println("DEPTH WRITE FOUND");
				depthFound = true;
				break;
			}
		}
		if (!depthFound) System.out.println("DEPTH MISSING.");*/
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glDepthMask(false);
		
		long thisFrame = System.nanoTime() / 1_000_000L;
		if (firstFrame) {
			lastFrame = thisFrame;
			firstFrame = false;
		} else {
			frameTime += (thisFrame - lastFrame);
			while(frameTime>FRAME_LENGTH) {
				frameTime-=FRAME_LENGTH;
				
				//TODO: Animate waveys
				theta += THETA_PER_FRAME;
			}
			
			lastFrame = thisFrame;
		}
	}
	
	private static void rect(Identifier texture, int left, int top, int width, int height, float u1, float v1, float u2, float v2, double z) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(texture);
		
		if (width <= 0) width = 1;
		if (height <= 0) height = 1;
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBufferBuilder();
		
		GL11.glAlphaFunc ( GL11.GL_GREATER, 0.1f ) ;
		GL11.glEnable ( GL11.GL_ALPHA_TEST ) ;
		GL11.glDisable(GL11.GL_BLEND);
		
		//GlStateManager.enableBlend();
		//GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		
		GL11.glColor3f(1f,1f,1f);
		buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV); //I thought GL_QUADS was deprecated but okay, sure.
		buffer.vertex(left,         top + height, z).texture(u1, v2).next();
		buffer.vertex(left + width, top + height, z).texture(u2, v2).next();
		buffer.vertex(left + width, top,          z).texture(u2, v1).next();
		buffer.vertex(left,         top,          z).texture(u1, v1).next();
		tessellator.draw();
		GL11.glEnable(GL11.GL_BLEND);
		//GlStateManager.disableBlend();
	}
}
