package command.util;

import collection.parser.Parser;

public class InsertUtil {
    /**
     * Генерация id.
     */
    public static int id(Parser parser) {
        Randomizer idRandomizer = parser.getRandomizer();
        return idRandomizer.generateId();
    }
}

