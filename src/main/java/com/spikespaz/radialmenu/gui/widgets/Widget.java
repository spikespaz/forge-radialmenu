package com.spikespaz.radialmenu.gui.widgets;

import com.google.common.collect.Lists;
import com.spikespaz.radialmenu.Utilities;
import com.spikespaz.radialmenu.gui.RenderHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class Widget {
    @Getter
    private static List<Class> widgetTypes = Lists.newArrayList();
    @Getter @Setter
    protected float zLevel;
    @Getter @Setter
    protected double x, y, width, height;
    @Getter @Setter
    protected boolean visible;

    Widget() {
        if (!widgetTypes.contains(this.getClass()))
            widgetTypes.add(this.getClass());

        this.zLevel = 0;
        this.x = 0;
        this.y = 0;
        this.width = 20;
        this.height = 20;
        this.visible = true;
    }

    public double getTop() {
        return this.y;
    }

    public double getBottom() {
        return this.y + this.height;
    }

    public double getLeft() {
        return this.x;
    }

    public double getRight() {
        return this.x + this.width;
    }

    public void setBottom(double y) {
        this.y = y - this.height;
    }

    public void setRight(double x) {
        this.x = x - this.width;
    }

    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(double w, double h) {
        this.width = w;
        this.height = h;
    }

    public void setBox(double w, double h, double x, double y) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public void draw(double mouseX, double mouseY, float partialTicks) {
        if (!this.visible)
            return;

        this.update();
    }

    public void update() {
    }

    public void drawDebug() {
        if (!this.visible)
            return;

        RenderHelper.drawGradientRect(this.x, this.y, this.width, this.height, this.zLevel, this.getDebugColor(), this.getDebugColor());
    }

    private int getDebugColor() {
        return Utilities.shiftHue(0x66FF0000, widgetTypes.size() / (widgetTypes.indexOf(this.getClass()) + 1f));
    }

    static abstract class Builder<W extends Widget, B extends Builder<W, B>> {
        protected W widget;

        @SuppressWarnings("unchecked")
        protected final B self() {
            return (B) this;
        }

        public Builder(W widget) {
            this.widget = widget;
        }

        public W done() {
            return this.widget;
        }

        public B zLevel(float z) {
            this.widget.setZLevel(z);
            return this.self();
        }

        public B visible(boolean v) {
            this.widget.setVisible(v);
            return this.self();
        }

        public B top(double y) {
            this.widget.setY(y);
            return this.self();
        }

        public B bottom(double y) {
            this.widget.setBottom(y);
            return this.self();
        }

        public B left(double x) {
            this.widget.setX(x);
            return this.self();
        }

        public B right(double x) {
            this.widget.setRight(x);
            return this.self();
        }

        public B width(double w) {
            this.widget.setWidth(w);
            return this.self();
        }

        public B height(double h) {
            this.widget.setHeight(h);
            return this.self();
        }

        public B pos(double x, double y) {
            this.widget.setPos(x, y);
            return this.self();
        }

        public B size(double w, double h) {
            this.widget.setSize(w, h);
            return this.self();
        }

        public B box(double w, double h, double x, double y) {
            this.widget.setBox(w, h, x, y);
            return this.self();
        }
    }

    public enum Align {
        CV, CH, T, B, L, R
    }
}
