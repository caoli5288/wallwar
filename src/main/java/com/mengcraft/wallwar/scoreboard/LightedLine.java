package com.mengcraft.wallwar.scoreboard;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.getByChar;

/**
 * Created on 16-5-18.
 */
public class LightedLine extends ListedLine {

    protected LightedLine(String in, ChatColor lighted) {
        super(format(in, lighted));
    }

    private static List<String> format(String in, ChatColor lighted) {
        List<String> out = new ArrayList<>();
        Format format = new Format();
        StringBuilder b;
        ChatColor k;
        for (char i = 0, j; i < in.length(); ) {
            j = in.charAt(i);
            if (j == '§') {
                i++;
                k = getByChar(in.charAt(i++));
                if (k == null) {
                    throw new NullPointerException();
                }
                format.set(k);
            } else {
                b = new StringBuilder();
                if (i != 0) {
                    b.append(in.substring(0, i));
                }
                b.append(lighted);
                b.append(j);
                b.append(format.get());
                if (i++ < in.length()) {
                    b.append(in.substring(i));
                }
                out.add(b.toString());
            }
        }
        return out;
    }

    public static LightedLine of(String in, ChatColor lighted) {
        return new LightedLine(in, lighted);
    }

}
