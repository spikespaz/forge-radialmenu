package com.spikespaz.radialmenu.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class LabeledButtonWidget extends ContainerWidget {
    protected final Minecraft mc;
    protected LabelWidget labelWidget;
    protected ButtonWidget buttonWidget;

    public LabeledButtonWidget(Minecraft mc, FontRenderer fontRenderer) {
        super();
        this.mc = mc;
        this.labelWidget = (LabelWidget) this.addChild(new LabelWidget(mc.fontRenderer));
        this.buttonWidget = (ButtonWidget) this.addChild(new ButtonWidget(mc, mc.fontRenderer));
    }
}
