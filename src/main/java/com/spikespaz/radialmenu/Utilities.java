package com.spikespaz.radialmenu;

import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.lang.reflect.Field;

public final class Utilities {
    private static Minecraft mc = Minecraft.getMinecraft();

    public interface ICallback<T> {
        void resolve(T param);
    }

    @SneakyThrows
    public static void emitKeyBindEvent(KeyBinding binding) {
//        KeyBinding.setKeyBindState(binding.getKeyCode(), true);

        final Field pressTime = KeyBinding.class.getDeclaredField("pressTime");
        final Field pressed = KeyBinding.class.getDeclaredField("pressed");

        pressTime.setAccessible(true);
        pressed.setAccessible(true);

        pressTime.set(binding, 1);
        pressed.set(binding, true);

        FMLCommonHandler.instance().fireKeyInput();

        pressTime.set(binding, 0);
        pressed.set(binding, false);
    }

    public static KeyBinding getKeyBindByName(String name) {
        for (KeyBinding binding : mc.gameSettings.keyBindings)
            if (binding.getKeyDescription().equals(name))
                return binding;

        return null;
    }

    public static void focusGame() {
        mc.displayGuiScreen(null);

        if (mc.currentScreen == null)
            mc.setIngameFocus();
    }

    public static int[] intToRgb(int color) {
        return new int[] {color >> 16 & 255, color >> 8 & 255, color & 255};
    }

    public static int rgbToInt(int red, int green, int blue) {
        return red << 16 | green << 8 | blue;
    }

    public static int interpolateValue(int fromValue, int toValue, double factor) {
        return (int) (fromValue + (toValue - fromValue) * factor);
    }

    public static int interpolateColor(int fromColor, int toColor, double factor) {
        final int[] fromRgb = intToRgb(fromColor);
        final int[] toRgb = intToRgb(toColor);
        final int[] interRgb = new int[] {
                interpolateValue(fromRgb[0], toRgb[0], factor),
                interpolateValue(fromRgb[1], toRgb[1], factor),
                interpolateValue(fromRgb[2], toRgb[2], factor),
        };

        return rgbToInt(interRgb[0], interRgb[1], interRgb[2]);
    }
}
