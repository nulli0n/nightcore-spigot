package su.nightexpress.nightcore.util.text.decoration;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GradientDecorator implements Decorator {

    private final Color color;
    private final Color colorEnd;

    public GradientDecorator(@NotNull Color color, @NotNull Color colorEnd) {
        this.color = color;
        this.colorEnd = colorEnd;
    }

    public boolean isSimilar(@NotNull GradientDecorator other) {
        return this.getColor().equals(other.getColor()) && this.getColorEnd().equals(other.getColorEnd());
    }

    public Color getColor() {
        return color;
    }

    @NotNull
    public Color getColorEnd() {
        return colorEnd;
    }

    public Color[] createGradient(int length) {
        Color[] colors = new Color[length];
        for (int index = 0; index < length; index++) {
            double percent = (double) index / (double) length;

            int red = (int) (color.getRed() + percent * (colorEnd.getRed() - color.getRed()));
            int green = (int) (color.getGreen() + percent * (colorEnd.getGreen() - color.getGreen()));
            int blue = (int) (color.getBlue() + percent * (colorEnd.getBlue() - color.getBlue()));

            java.awt.Color color = new java.awt.Color(red, green, blue);
            colors[index] = color;
        }
        return colors;
    }

    /*@NotNull
    public List<Pair<String, Color>> gradient(@NotNull String string) {
        List<Pair<String, Color>> list = new ArrayList<>();

        Color[] colors = createGradient(string.length());
        char[] characters = string.toCharArray();

        for (int index = 0; index < characters.length; index++) {
            list.add(Pair.of(String.valueOf(characters[index]), colors[index]));
        }

        return list;
    }*/

    @Override
    public void decorate(@NotNull BaseComponent component) {
        if (!(component instanceof TextComponent textComponent)) return;

        String text = textComponent.getText();

        Color[] colors = createGradient(text.length());
        char[] characters = text.toCharArray();

        for (int index = 0; index < characters.length; index++) {
            TextComponent extra = new TextComponent(String.valueOf(characters[index]));
            extra.copyFormatting(textComponent);
            extra.setColor(ChatColor.of(colors[index]));
            textComponent.addExtra(extra);
        }
        textComponent.setText("");
    }
}
