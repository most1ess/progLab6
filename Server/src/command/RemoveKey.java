package command;

import person.Person;
import server.Processor;

import java.util.TreeMap;

public class RemoveKey extends Command {
    private TreeMap<String, Person> collection;
    private String key;

    public RemoveKey(Processor processor) {
        collection = processor.getCollection().get();
        key = processor.getCommandData().getParam1();
    }

    @Override
    public String execute() {
        if (collection.isEmpty())
            return "Невозможно удалить элемент с заданным ключом. Коллекция уже пуста.\n";
        else if (!collection.containsKey(key))
            return "Элемента с таким ключом нет в коллекции! Попробуйте ввести другой ключ.\n";
        else {
            collection.remove(key);
            return ("Элемент с ключом " + key + " успешно удален!\n");
        }
    }
}
