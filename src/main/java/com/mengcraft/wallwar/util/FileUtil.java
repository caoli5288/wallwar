package com.mengcraft.wallwar.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created on 16-2-23.
 */
public final class FileUtil {

    public static void copy(File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (!target.isDirectory()) {
                target.mkdir();
            }
            for (File f : source.listFiles()) {
                copy(f, new File(target, f.getName()));
            }
        } else {
            Files.copy(source.toPath(), target.toPath());
        }
    }

    public static void delete(File file) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delete(f);
            }
            Files.delete(file.toPath());
        } else {
            Files.delete(file.toPath());
        }
    }

    public static void move(File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (!target.isDirectory()) {
                target.mkdir();
            }
            for (File f : source.listFiles()) {
                move(f, new File(target, f.getName()));
            }
            Files.delete(source.toPath());
        } else {
            Files.move(source.toPath(), target.toPath());
        }
    }

}
