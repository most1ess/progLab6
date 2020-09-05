package command;

import person.Person;
import server.Processor;

import java.util.TreeMap;

public class Clear extends Command {
    public TreeMap<String, Person> collection;

    public Clear(Processor processor) {
        collection = processor.getCollection().get();
    }

    @Override
    public String execute() {
        if (collection.isEmpty()) {
            return "Невозможно очистить коллекцию. Коллекция уже пуста.\n";
        } else {
            collection.clear();
            return "Коллекция успешно очищена.\n";
        }
    }
}
