package mage.client.table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Resolves AI deck inputs. A regular file is used directly; a directory means
 * choose one .dck file from that directory tree.
 */
public final class RandomDeckSelector {

    private RandomDeckSelector() {
    }

    public static String resolveDeckPath(String deckPath) throws IOException {
        return resolveDeckPath(deckPath, new HashSet<>());
    }

    public static String resolveDeckPath(String deckPath, Set<String> usedDeckPaths) throws IOException {
        if (deckPath == null || deckPath.trim().isEmpty()) {
            return deckPath;
        }

        File deckFile = new File(deckPath);
        if (!deckFile.isDirectory()) {
            rememberDeck(deckFile, usedDeckPaths);
            return deckPath;
        }

        List<Path> deckFiles;
        try (Stream<Path> paths = Files.walk(deckFile.toPath())) {
            deckFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(RandomDeckSelector::isDeckFile)
                    .sorted()
                    .collect(Collectors.toList());
        }

        if (deckFiles.isEmpty()) {
            throw new FileNotFoundException("No .dck files found in random AI deck folder: " + deckFile.getPath());
        }

        List<Path> availableDeckFiles = new ArrayList<>();
        for (Path path : deckFiles) {
            if (!usedDeckPaths.contains(toDeckKey(path.toFile()))) {
                availableDeckFiles.add(path);
            }
        }

        List<Path> selectableDeckFiles = availableDeckFiles.isEmpty() ? deckFiles : availableDeckFiles;
        Path selectedDeck = selectableDeckFiles.get(ThreadLocalRandom.current().nextInt(selectableDeckFiles.size()));
        rememberDeck(selectedDeck.toFile(), usedDeckPaths);
        return selectedDeck.toString();
    }

    private static boolean isDeckFile(Path path) {
        String filename = path.getFileName().toString().toLowerCase(Locale.ENGLISH);
        return filename.endsWith(".dck");
    }

    private static void rememberDeck(File deckFile, Set<String> usedDeckPaths) throws IOException {
        usedDeckPaths.add(toDeckKey(deckFile));
    }

    private static String toDeckKey(File deckFile) throws IOException {
        return deckFile.getCanonicalPath().toLowerCase(Locale.ENGLISH);
    }
}
