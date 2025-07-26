package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.impl.NightTextComponent;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.awt.*;
import java.nio.CharBuffer;

@Deprecated
public class GradientColorDecorator implements ColorDecorator {

    private final Color[] colorStops;

    private Color[] gradient;
    private int     colorIndex;

    public GradientColorDecorator(@NotNull Color color, @NotNull Color colorEnd) {
        this(new Color[]{color, colorEnd});
    }

    public GradientColorDecorator(@NotNull Color[] colorStops) {
        this.colorStops = colorStops;
    }

    public Color[] getColorStops() {
        return this.colorStops;
    }

    public boolean isCreated() {
        return this.gradient != null && this.gradient.length > 0;
    }

    public boolean hasNextColor() {
        return this.isCreated() && this.colorIndex < this.gradient.length;
    }

/*    public Color[] createGradient(int length) {
        this.colors = new Color[length];
        for (int index = 0; index < length; index++) {
            double percent = (double) index / (double) length;

            int red = (int) (color.getRed() + percent * (colorEnd.getRed() - color.getRed()));
            int green = (int) (color.getGreen() + percent * (colorEnd.getGreen() - color.getGreen()));
            int blue = (int) (color.getBlue() + percent * (colorEnd.getBlue() - color.getBlue()));

            java.awt.Color color = new java.awt.Color(red, green, blue);
            this.colors[index] = color;
        }
        return this.colors;
    }*/

    public Color[] createGradient(int length) {
        this.gradient = new Color[length];
        int segments = this.colorStops.length - 1;
        int colorsPerSegment = length / segments;
        int remainder = length % segments; // Handle rounding leftovers

        int index = 0;
        for (int i = 0; i < segments; i++) {
            Color start = this.colorStops[i];
            Color end = this.colorStops[i + 1];

            int segmentLength = colorsPerSegment + (i < remainder ? 1 : 0); // Distribute remainder

            for (int j = 0; j < segmentLength; j++) {
                double t = (double) j / segmentLength;

                int r = (int) (start.getRed() + t * (end.getRed() - start.getRed()));
                int g = (int) (start.getGreen() + t * (end.getGreen() - start.getGreen()));
                int b = (int) (start.getBlue() + t * (end.getBlue() - start.getBlue()));

                this.gradient[index++] = new Color(r, g, b);
            }
        }

        return this.gradient;
    }

    public Color[] getGradient() {
        return this.gradient;
    }

    public Color nextColor() {
        return this.gradient[this.colorIndex++];
    }

    public Color getLatestColor() {
        return this.gradient[this.gradient.length - 1];
    }

    @Override
    @NotNull
    public NightComponent decorate(@NotNull NightComponent component) {
        if (component instanceof NightTextComponent textComponent) {
            String content = textComponent.content();
            char[] letters = content.toCharArray();

            // Count gradient length and prepare colors.
            if (!this.isCreated()) {
                int length = Math.toIntExact(CharBuffer.wrap(content.toCharArray()).chars().filter(c -> !Character.isWhitespace(c)).count());
                this.createGradient(length);
            }

            NightComponent fresh = textComponent.content(""); // Inheritance from textComponent to retain Style.

            for (char letter : letters) {
                Color color = this.hasNextColor() && !Character.isWhitespace(letter) ? this.nextColor() : fresh.color();
                fresh = fresh.append(textComponent.content(String.valueOf(letter)).color(color)); // Inheritance from textComponent to retain Style.
            }

            return fresh;
        }

        // All other components can't have gradients due to unpredictable text.

        if (!this.isCreated()) this.createGradient(1);

        if (this.hasNextColor()) {
            return component.color(this.nextColor());
        }

        return component;
    }
}
