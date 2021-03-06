package com.spikespaz.radialmenu;

import lombok.Getter;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.config.Config;

@Config(modid = RadialMenu.MOD_ID, name = "RadialMenu")
public final class ConfigHandler {
    @Config.LangKey("config.radialmenu.category.general")
    public static final GeneralOptions GENERAL = GeneralOptions.INSTANCE;
    @Config.LangKey("config.radialmenu.category.label")
    public static final LabelOptions LABEL = LabelOptions.INSTANCE;
    @Config.LangKey("config.radialmenu.category.button")
    public static final ButtonOptions BUTTON = ButtonOptions.INSTANCE;
    @Config.LangKey("config.radialmenu.category.sound")
    public static final SoundOptions SOUND = SoundOptions.INSTANCE;

    public static final class GeneralOptions {
        @Config.Ignore
        public static final GeneralOptions INSTANCE = new GeneralOptions();

        @Getter
        @Config.LangKey("config.radialmenu.circle_radius")
        @Config.Comment("The radius of the inside of the radial menu.")
        @Config.RangeInt(min = 0)
        public int circleRadius = 70;

        @Getter
        @Config.LangKey("config.radialmenu.dead_zone_radius")
        @Config.Comment("The radius of the dead zone that the mouse must pass to highlight a radial button.")
        @Config.RangeInt(min = 0)
        public int deadZoneRadius = 30;

        @Getter
        @Config.LangKey("config.radialmenu.toggle_mode_enabled")
        @Config.Comment({
                "Whether or not the key binding will toggle the menu, or if the key has to be held.",
                "If toggle mode is off, the selection will be confirmed where the mouse is over when released."
        })
        public boolean toggleModeEnabled = false;
    }

    public static final class LabelOptions {
        @Config.Ignore
        public static final LabelOptions INSTANCE = new LabelOptions();

        @Config.LangKey("config.radialmenu.label_bg_color")
        @Config.Comment({
                "Background color of the label in the center of the radial menu.",
                "RGB in hexadecimal format (RRGGBB)."
        })
        public String bgColor = "000000";

        @Config.LangKey("config.radialmenu.label_bg_opacity")
        @Config.Comment("Background opacity of the label in the center of the radial menu.")
        @Config.RangeDouble(min = 0, max = 1)
        public double bgOpacity = 0.75;

        @Config.LangKey("config.radialmenu.label_text_color")
        @Config.Comment({
                "Text color of the label in the center of the radial menu.",
                "RGB in hexadecimal format (RRGGBB)."
        })
        public String textColor = "FFFFFF";

        @Config.LangKey("config.radialmenu.label_text_opacity")
        @Config.Comment("Text opacity of the label in the center of the radial menu.")
        @Config.RangeDouble(min = 0, max = 1)
        public double textOpacity = 1.0;

        @Config.LangKey("config.radialmenu.label_text_empty_color")
        @Config.Comment({
                "Text color of the label in the center of the radial menu when it is empty (button unassigned).",
                "RGB in hexadecimal format (RRGGBB)."
        })
        public String textEmptyColor = "FE3F3F";

        @Getter
        @Config.LangKey("config.radialmenu.label_padding_x")
        @Config.Comment("Horizontal padding on the left and right of the text for the label in the center of the radial menu.")
        @Config.RangeInt(min = 0)
        public int paddingX = 4;

        @Getter
        @Config.LangKey("config.radialmenu.label_padding_y")
        @Config.Comment("Vertical padding on the top and bottom of the text for the label in the center of the radial menu.")
        @Config.RangeInt(min = 0)
        public int paddingY = 4;

        public int getBgColor() {
            return (int) (this.bgOpacity * 0xFF) << 24 | (int) Long.parseLong(this.bgColor, 16);
        }

        public int getTextColor() {
            return (int) (this.textOpacity * 0xFF) << 24 | (int) Long.parseLong(this.textColor, 16);
        }

        public int getTextEmptyColor() {
            return (int) (this.textOpacity * 0xFF) << 24 | (int) Long.parseLong(this.textEmptyColor, 16);
        }
    }

    public static final class ButtonOptions {
        @Config.Ignore
        public static final ButtonOptions INSTANCE = new ButtonOptions();

        @Config.LangKey("config.radialmenu.button_bg_color")
        @Config.Comment({
                "Background color of each radial button.",
                "RGB in hexadecimal format (RRGGBB)."
        })
        public String bgColor = "000000";

        @Config.LangKey("config.radialmenu.button_bg_opacity")
        @Config.Comment("Background opacity of each radial button.")
        @Config.RangeDouble(min = 0, max = 1)
        public double bgOpacity = 0.75;

        @Config.LangKey("config.radialmenu.button_bg_hover_color")
        @Config.Comment({
                "Background color of each radial button when it is hovered or highlighted.",
                "RGB in hexadecimal format (RRGGBB)."
        })
        public String bgHoverColor = "CC0000";

        @Config.LangKey("config.radialmenu.button_bg_hover_opacity")
        @Config.Comment("Background opacity of each radial button when it is hovered or highlighted.")
        @Config.RangeDouble(min = 0, max = 1)
        public double bgHoverOpacity = 1.0;

        @Getter
        @Config.LangKey("config.radialmenu.button_icon_opacity")
        @Config.Comment("Opacity of each radial button icon.")
        @Config.RangeDouble(min = 0, max = 1)
        public double iconOpacity = 1.0;

        @Getter
        @Config.LangKey("config.radialmenu.button_icon_hover_opacity")
        @Config.Comment("Opacity of each radial button icon when it is hovered or highlighted.")
        @Config.RangeDouble(min = 0, max = 1)
        public double iconHoverOpacity = 1.0;

        @Getter
        @Config.LangKey("config.radialmenu.button_thickness")
        @Config.Comment("Thickness or width of each radial button's trapezoid.")
        @Config.RangeInt(min = 0)
        public int thickness = 30;

        public int getBgColor() {
            return (int) (this.bgOpacity * 0xFF) << 24 | (int) Long.parseLong(this.bgColor, 16);
        }

        public int getBgHoverColor() {
            return (int) (this.bgHoverOpacity * 0xFF) << 24 | (int) Long.parseLong(this.bgHoverColor, 16);
        }
    }

    public static final class SoundOptions {
        @Config.Ignore
        public static final SoundOptions INSTANCE = new SoundOptions();

        @Config.LangKey("config.radialmenu.button_sound_event")
        @Config.Comment("The sound for a radial button when it is pressed.")
        public String buttonSoundEvent = "UI_BUTTON_CLICK";

        @Getter
        @Config.LangKey("config.radialmenu.button_sound_pitch")
        @Config.Comment("The pitch of the sound for a radial button when it is pressed.")
        @Config.RangeDouble(min = 0.5, max = 2.0)
        public double buttonSoundPitch = 2.0;

        @Getter
        @Config.LangKey("config.radialmenu.button_sound_enabled")
        @Config.Comment("Enable button press sound for radial button.")
        public boolean buttonSoundEnabled = true;

        public SoundEvent getButtonSoundEvent() {
            try {
                SoundEvents.class.getDeclaredField(this.buttonSoundEvent);
                return (SoundEvent) SoundEvents.class.getDeclaredField(this.buttonSoundEvent).get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
