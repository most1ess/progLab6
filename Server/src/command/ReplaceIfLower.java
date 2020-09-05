package command;

import person.Person;
import server.Processor;

import java.util.TreeMap;

public class ReplaceIfLower extends Command {
    private TreeMap<String, Person> collection;
    private String key;
    private String value;

    public ReplaceIfLower(Processor processor) {
        collection = processor.getCollection().get();
        key = processor.getCommandData().getParam1();
        value = processor.getCommandData().getParam2();
    }

    @Override
    public String execute() {
        if(collection.isEmpty()) {
            return ("Опа! А коллекция то пуста!\n");
        } else {
            int valueInt;
            try {
                valueInt = Integer.parseInt(value);
            } catch(NumberFormatException e) {
                return "Введённый вами аргумент должен быть целым положительным числом!\n";
            }
            if(valueInt <= 0) {
                return "Введённый вами аргумент должен быть целым положительным числом!\n";
            }
            if (collection.get(key).getHeight() > valueInt) {
                collection.get(key).setHeight(Integer.parseInt(value));
                return ("Рост успешно изменен.\n");
            } else {
                return ("Опа! А введённый рост то не меньше текущего!\n");
            }
        }
    }
}
