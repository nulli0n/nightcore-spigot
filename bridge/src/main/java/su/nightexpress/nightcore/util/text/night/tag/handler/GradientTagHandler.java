package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.entry.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GradientTagHandler extends ClassicTagHandler {

    private Color[] colorStops;

    private Color[] createGradient(Color[] colorStops, int length) {
        Color[] gradient = new Color[length];
        int segments = colorStops.length - 1;
        int colorsPerSegment = length / segments;
        int remainder = length % segments; // Handle rounding leftovers

        int index = 0;
        for (int i = 0; i < segments; i++) {
            Color start = colorStops[i];
            Color end = colorStops[i + 1];

            int segmentLength = colorsPerSegment + (i < remainder ? 1 : 0); // Distribute remainder

            for (int j = 0; j < segmentLength; j++) {
                double t = (double) j / segmentLength;

                int r = (int) (start.getRed() + t * (end.getRed() - start.getRed()));
                int g = (int) (start.getGreen() + t * (end.getGreen() - start.getGreen()));
                int b = (int) (start.getBlue() + t * (end.getBlue() - start.getBlue()));

                gradient[index++] = new Color(r, g, b);
            }
        }

        return gradient;
    }

    private static class Gradient {

        private final Color[] colors;
        private int           colorIndex;

        public Gradient(Color[] colors) {
            this.colors = colors;
            this.colorIndex = 0;
        }

        /*public Color[] getColors() {
            return this.colors;
        }

        public Color getLatestColor() {
            return this.colors[this.colors.length - 1];
        }*/

        public boolean hasNextColor() {
            return this.colorIndex < this.colors.length;
        }

        @NotNull
        public Color nextColor() {
            return this.colors[this.colorIndex++];
        }
    }

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;
        String[] split = tagContent.split(String.valueOf(ParserUtils.DELIMITER));

        int length = split.length;
        if (length < 2) return;

        List<Color> colors = new ArrayList<>();

        for (String string : split) {
            Color stop = ParserUtils.colorFromSchemeOrHex(string);
            if (stop != null) colors.add(stop);
        }

        this.colorStops = colors.toArray(new Color[0]);
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {
        AtomicInteger textLength = new AtomicInteger(0);
        List<ChildEntry> gradientEntries = new ArrayList<>();

        this.splitGroup(group, textLength, gradientEntries, true);

        Color[] gradientColors = this.createGradient(this.colorStops, textLength.get());
        if (gradientColors.length == 0) return;

        Gradient gradient = new Gradient(gradientColors);

        gradientEntries.forEach(childEntry -> this.decorate(childEntry, gradient));
    }

    private void splitGroup(@NotNull EntryGroup group, @NotNull AtomicInteger textLength, @NotNull List<ChildEntry> gradientEntries, boolean root) {
        List<Entry> childrens = new ArrayList<>();
        List<Entry> oldChildrens = new ArrayList<>(group.getChildrens());
        group.getChildrens().clear();

        oldChildrens.forEach(entry -> {
            if (entry instanceof EntryGroup other) {
                childrens.add(other);

                // Nested groups that overrides gradient color should not be affected by the gradient.
                if (root && other.style().color() == group.style().color() && !other.isStyleLocked()) {
                    this.splitGroup(other, textLength, gradientEntries, false);
                }
                return;
            }

            if (entry instanceof ChildEntry childEntry) {

                textLength.addAndGet(childEntry.textLength());

                if (childEntry instanceof TextEntry textEntry) {
                    for (char c : textEntry.text().toCharArray()) {
                        EntryGroup subGroup = group.downward(String.valueOf(c));
                        TextEntry subText = subGroup.appendTextEntry(String.valueOf(c));
                        childrens.add(subGroup);
                        if (!Character.isWhitespace(c)) {
                            gradientEntries.add(subText);
                        }
                    }
                }
                else if (childEntry instanceof LangEntry langEntry) {
                    EntryGroup subGroup = group.downward(langEntry.getKey());
                    LangEntry subTrans = subGroup.appendLangEntry(langEntry.getKey(), langEntry.getFallback());
                    childrens.add(subGroup);
                    gradientEntries.add(subTrans);
                }
            }
        });

        group.setChildrens(childrens);
    }

    private void decorate(@NotNull ChildEntry entry, @NotNull Gradient gradient) {
        if (!gradient.hasNextColor()) return;

        entry.getParent().setStyle(style -> style.color(gradient.nextColor()));
    }
}
