package su.nightexpress.nightcore.util.text.tag.decorator;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GradientColorDecorator implements ColorDecorator {

    private final Color color;
    private final Color colorEnd;

    private Color[] colors;
    private int colorIndex;

    public GradientColorDecorator(@NotNull Color color, @NotNull Color colorEnd) {
        this.color = color;
        this.colorEnd = colorEnd;
    }

    @NotNull
    public Color getColor() {
        return color;
    }

    @NotNull
    public Color getColorEnd() {
        return colorEnd;
    }

    public boolean isCreated() {
        return this.colors != null && this.colors.length > 0;
    }

    public boolean hasNextColor() {
        return this.isCreated() && this.colorIndex < this.colors.length;
    }

    public Color[] createGradient(int length) {
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
    }

    public Color[] getColors() {
        return colors;
    }

    public Color nextColor() {
        return this.colors[this.colorIndex++];
    }

    public Color getLatestcColor() {
        return this.colors[this.colors.length - 1];
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        if (component instanceof TextComponent textComponent) {
            int length = 0;

            //if (textComponent.getText().equals("\n")) return; // Do not 'spend' colors on line breaks.

            for (char letter : textComponent.getText().toCharArray()) {
                if (!Character.isWhitespace(letter)) length++;

                textComponent.addExtra(new TextComponent(String.valueOf(letter)));
            }

            if (!this.isCreated()) {
                this.createGradient(length);
            }

            textComponent.setText("");
            textComponent.getExtra().forEach(extra -> {
                if (extra instanceof TextComponent extraText && extraText.getText().isBlank()) return;

                if (this.hasNextColor()) {
                    extra.setColor(ChatColor.of(this.nextColor()));
                }
            });

            return;
        }

        if (!this.isCreated()) this.createGradient(1);

        if (this.hasNextColor()) {
            component.setColor(ChatColor.of(this.nextColor()));
        }
    }
}
