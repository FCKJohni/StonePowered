package dev.teamhelios.stonepowered.console;

import org.fusesource.jansi.Ansi;

import java.awt.*;
import java.util.regex.Pattern;

public enum ConsoleColor {

    BLACK("black", '0', Ansi.ansi().reset().fg(Ansi.Color.BLACK).toString()),
    DARK_BLUE("dark_blue", '1', Ansi.ansi().reset().fg(Ansi.Color.BLUE).toString()),
    GREEN("green", '2', Ansi.ansi().reset().fg(Ansi.Color.GREEN).toString()),
    CYAN("cyan", '3', Ansi.ansi().reset().fg(Ansi.Color.CYAN).toString()),
    DARK_RED("dark_red", '4', Ansi.ansi().reset().fg(Ansi.Color.RED).toString()),
    PURPLE("purple", '5', Ansi.ansi().reset().fg(Ansi.Color.MAGENTA).toString()),
    ORANGE("orange", '6', Ansi.ansi().reset().fg(Ansi.Color.YELLOW).toString()),
    GRAY("gray", '7', Ansi.ansi().reset().fg(Ansi.Color.WHITE).toString()),
    DARK_GRAY("dark_gray", '8', Ansi.ansi().reset().fg(Ansi.Color.BLACK).bold().toString()),
    BLUE("blue", '9', Ansi.ansi().reset().fg(Ansi.Color.BLUE).bold().toString()),
    LIGHT_GREEN("light_green", 'a', Ansi.ansi().reset().fg(Ansi.Color.GREEN).bold().toString()),
    AQUA("aqua", 'b', Ansi.ansi().reset().fg(Ansi.Color.CYAN).bold().toString()),
    RED("red", 'c', Ansi.ansi().reset().fg(Ansi.Color.RED).bold().toString()),
    PINK("pink", 'd', Ansi.ansi().reset().fg(Ansi.Color.MAGENTA).bold().toString()),
    YELLOW("yellow", 'e', Ansi.ansi().reset().fg(Ansi.Color.YELLOW).bold().toString()),
    WHITE("white", 'f', Ansi.ansi().reset().fg(Ansi.Color.WHITE).bold().toString()),
    OBFUSCATED("obfuscated", 'k', Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString()),
    BOLD("bold", 'l', Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString()),
    STRIKETHROUGH("strikethrough", 'm', Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString()),
    UNDERLINE("underline", 'n', Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString()),
    ITALIC("italic", 'o', Ansi.ansi().a(Ansi.Attribute.ITALIC).toString()),
    DEFAULT("default", 'r', Ansi.ansi().reset().toString());
    private static final ConsoleColor[] VALUES = values();
    private static final String LOOKUP = "0123456789abcdefklmnor";
    private static final String RGB_ANSI = "\u001B[38;2;%d;%d;%dm";
    private final String name;
    private final String ansiCode;
    private final char index;

    ConsoleColor(String name, char index, String ansiCode) {
        this.name = name;
        this.index = index;
        this.ansiCode = ansiCode;
    }

    public static String toColouredString(char triggerChar, String text) {
        var content = convertRGBColors(triggerChar, text);

        var breakIndex = content.length() - 1;
        for (var i = 0; i < breakIndex; i++) {
            if (content.charAt(i) == triggerChar) {
                var format = LOOKUP.indexOf(content.charAt(i + 1));
                if (format != -1) {
                    var ansiCode = VALUES[format].ansiCode();

                    content.delete(i, i + 2).insert(i, ansiCode);
                    breakIndex += ansiCode.length() - 2;
                }
            }
        }

        return content.toString();
    }

    private static StringBuffer convertRGBColors(char triggerChar, String input) {
        var matcher = Pattern.compile(triggerChar + "#([\\da-fA-F]){6}").matcher(input);
        var stringBuffer = new StringBuffer();

        while (matcher.find()) {
            var temp = matcher.group().replace(String.valueOf(triggerChar), "");
            var color = Color.decode(temp);

            matcher.appendReplacement(stringBuffer,
                    String.format(RGB_ANSI, color.getRed(), color.getGreen(), color.getBlue()));
        }

        matcher.appendTail(stringBuffer);
        return stringBuffer;
    }

    @Override
    public String toString() {
        return this.ansiCode;
    }

    public String displayName() {
        return this.name;
    }

    public String ansiCode() {
        return this.ansiCode;
    }
}
