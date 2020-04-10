package com.spikespaz.radialmenu.gui;

import com.spikespaz.radialmenu.MathHelper;
import com.spikespaz.radialmenu.Utilities;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.ParametersAreNonnullByDefault;

public class GuiRadialButton extends GuiButton {
    private static final Minecraft mc = Minecraft.getMinecraft();
    protected final RenderItem itemRender;
    private final int showDuration;
    private final int hoverDuration;
    private final FadeMode showFadeMode;
    private final FadeMode hoverFadeMode;
    public int radius;
    public int deadRadius;
    public int thickness;
    public int sliceCount;
    public int sliceNum;
    public int normalColor;
    public int hoverColor;
    public int currentColor;
    public double[] centroid;
    protected ItemStack itemIcon;
    protected ResourceLocation imageIcon;
    public KeyBinding keyBinding;
    private SoundEvent pressSound;
    private float pressSoundPitch;
    private long firstShownTime;
    private long lastHoverChange;
    private int hoverFadeTime;

    public enum FadeMode {
        FADE_IN, FADE_OUT, FADE_BOTH, FADE_NONE;
    }

    public GuiRadialButton(int buttonId, int radius, int deadRadius, int thickness, int normalColor, int hoverColor, int showDuration, int hoverDuration, FadeMode showFadeMode, FadeMode hoverFadeMode) {
        super(buttonId, 0, 0, "");
        this.itemRender = mc.getRenderItem();
        this.id = buttonId;
        this.radius = radius;
        this.deadRadius = deadRadius;
        this.thickness = thickness;
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.showDuration = showDuration;
        this.hoverDuration = hoverDuration;
        this.showFadeMode = showFadeMode;
        this.hoverFadeMode = hoverFadeMode;
    }

    public void onMouseOver() {
        this.hovered = true;
        this.lastHoverChange = Minecraft.getSystemTime();
    }

    public void onMouseAway() {
        this.hovered = false;
        this.lastHoverChange = Minecraft.getSystemTime();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) return;

        final boolean hovered;

        if (this.firstShownTime == 0) {
            hovered = false;
            this.firstShownTime = Minecraft.getSystemTime();
        } else
            hovered = this.isMouseOver(mc, mouseX, mouseY);

        if (!this.hovered && hovered)
            this.onMouseOver();
        else if (this.hovered && !hovered)
            this.onMouseAway();

        final double timeNow = Minecraft.getSystemTime();

//        final double showFadeFactor = Math.max(0, Math.min(1, (timeNow - this.firstShownTime) / this.showDuration));
        final double hoverFadeFactor = Math.min((timeNow - this.lastHoverChange) / this.hoverDuration, 1);

        if (this.id == 0) {
            System.out.println("Time since last hover change: " + (timeNow - this.lastHoverChange));
            System.out.println("Hover fade factor: " + hoverFadeFactor);
        }

        final int normalColor = this.normalColor & 0xFFFFFF;
        final int normalAlpha = this.normalColor >> 24 & 0xFF;
        final int hoverColor = this.hoverColor & 0xFFFFFF;
        final int hoverAlpha = this.hoverColor >> 24 & 0xFF;
        final int currentColor = this.currentColor & 0xFFFFFF;
        final int currentAlpha = this.currentColor >> 24 & 0xFF;

        int alpha, color;

        if (this.hovered)
            if (this.hoverFadeMode == FadeMode.FADE_BOTH || this.hoverFadeMode == FadeMode.FADE_IN) {
                alpha = Utilities.interpolateValue(currentAlpha, hoverAlpha, hoverFadeFactor);
                color = Utilities.interpolateColor(currentColor, hoverColor, hoverFadeFactor);
            } else {
                alpha = hoverAlpha;
                color = hoverColor;
            }
        else
            if (this.hoverFadeMode == FadeMode.FADE_BOTH || this.hoverFadeMode == FadeMode.FADE_OUT) {
                alpha = Utilities.interpolateValue(currentAlpha, normalAlpha, hoverFadeFactor);
                color = Utilities.interpolateColor(currentColor, normalColor, hoverFadeFactor);
            } else {
                alpha = normalAlpha;
                color = normalColor;
            }

        this.currentColor = (alpha << 24) + color;

//        alpha = (int) Utilities.interpolateValue(0, alpha, showFadeFactor);

        final ScaledResolution scaledRes = new ScaledResolution(mc);

        final double cx = scaledRes.getScaledWidth_double() / 2;
        final double cy = scaledRes.getScaledHeight_double() / 2;

        final double sa = Math.PI * 2 / this.sliceCount; // Slice angle
        final double ssa = (this.sliceCount - this.sliceNum) * sa - sa / 2 + Math.PI; // Start slice angle
        final double esa = ssa + sa; // End slice angle
        final double ipr = this.radius / Math.cos(sa / 2); // Inner point radius
        final double opr = ipr + this.thickness; // Outer point radius

        final double x0 = cx + Math.sin(ssa) * ipr;
        final double y0 = cy + Math.cos(ssa) * ipr;
        final double x1 = cx + Math.sin(ssa) * opr;
        final double y1 = cy + Math.cos(ssa) * opr;
        final double x2 = cx + Math.sin(esa) * opr;
        final double y2 = cy + Math.cos(esa) * opr;
        final double x3 = cx + Math.sin(esa) * ipr;
        final double y3 = cy + Math.cos(esa) * ipr;

        double[][] vertices = new double[][]{{x0, y0}, {x1, y1}, {x2, y2}, {x3, y3}};

        this.centroid = MathHelper.centroid(vertices);
        RenderHelper.drawPoly(vertices, this.currentColor);

        // Uncomment to draw points in red
//        RenderHelper.drawCircle(x0, y0, 2D, 10, 0xFFFF0000);
//        RenderHelper.drawCircle(x1, y1, 2D, 10, 0xFFFF0000);
//        RenderHelper.drawCircle(x2, y2, 2D, 10, 0xFFFF0000);
//        RenderHelper.drawCircle(x3, y3, 2D, 10, 0xFFFF0000);

        // Uncomment to draw the a dot in the center of each button
//        RenderHelper.drawCircle(this.centroid[0], this.centroid[1], 5, 10, 0xFF00FFFF);

        if (this.imageIcon != null) {
            mc.getTextureManager().bindTexture(this.imageIcon);
            GlStateManager.color(1, 1, 1);
            Gui.drawModalRectWithCustomSizedTexture((int) this.centroid[0] - 8, (int) this.centroid[1] - 8, 0, 0, 16, 16, 16, 16);
        } else if (this.itemIcon != null) {
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            this.itemRender.renderItemIntoGUI(itemIcon, (int) this.centroid[0] - 8, (int) this.centroid[1] - 8);
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        }

        this.mouseDragged(mc, mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && this.isMouseOver(mc, mouseX, mouseY);
    }

    private boolean isMouseOver(Minecraft mc, int mouseX, int mouseY) {
        final ScaledResolution scaledRes = new ScaledResolution(mc);

        final double cx = scaledRes.getScaledWidth_double() / 2;
        final double cy = scaledRes.getScaledHeight_double() / 2;

        final double sa = Math.PI * 2 / this.sliceCount; // Slice angle
        final double ssa = (this.sliceCount - this.sliceNum) * sa - sa / 2 + Math.PI; // Start slice angle
        final double esa = ssa + sa; // End slice angle

        final double mr = Math.hypot(mouseX - cx, mouseY - cy); // Mouse radius
        final double ma = Math.atan2(mouseX - cx, mouseY - cy); // Mouse angle

        return MathHelper.isAngleBetween(ssa, esa, ma) && mr > this.deadRadius;
    }

    @Override
    public boolean isMouseOver() {
        return this.hovered;
    }

    public void setPressSound(SoundEvent sound, float pitch) {
        this.pressSound = sound;
        this.pressSoundPitch = pitch;
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(this.pressSound, this.pressSoundPitch));
    }

    public void setKeyBinding(KeyBinding binding) {
        if (binding == null) {
            this.displayString = "";
            this.keyBinding = null;
        } else {
            this.displayString = binding.getKeyDescription();
            this.keyBinding = binding;
        }
    }

    public void setIcon(Item item) {
        this.itemIcon = new ItemStack(item);
        this.imageIcon = null;
    }

    public void setIcon(ResourceLocation resource) {
        this.imageIcon = resource;
        this.itemIcon = null;
    }
}
