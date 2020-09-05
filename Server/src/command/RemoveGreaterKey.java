package command;

import person.Person;
import server.Processor;

import java.util.TreeMap;

public class RemoveGreaterKey extends Command {
    private TreeMap<String, Person> collection;
    private String key;

    public RemoveGreaterKey(Processor processor) {
        collection = processor.getCollection().get();
        key = processor.getCommandData().getParam1();
    }

    @Override
    public String execute() {
        if (collection.isEmpty()) {
            return "Опа! А коллекция то пуста!\n";
        } else {
            if(collection.keySet().removeIf(k -> k.compareTo(key) > 0))
                return ("Все объекты, ключ которых превышает " + key + ", успешно удалены.\n");
            else return "Опа! А элементов таких и нет, оказывается!\n";
        }
    }
}
