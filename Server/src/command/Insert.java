package command;

import collection.parser.Parser;
import person.Person;
import command.util.InsertUtil;
import server.Processor;

import java.util.TreeMap;

public class Insert extends Command {
    private TreeMap<String, Person> collection;
    private String key;
    private Parser parser;
    private Person person;
    private Processor processor;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Insert(Processor processor) {
        collection = processor.getCollection().get();
        key = processor.getCommandData().getParam1();
        parser = processor.getCollection().getParser();
        this.processor = processor;
    }

    public boolean exists() {
        return collection.containsKey(key);
    }

    @Override
    public String execute() {
        if(person.getCreationDate() == null) {
            return "Элемент не был добвлен.\n";
        }
        person.setId(InsertUtil.id(parser));
        collection.put(key, person);
        processor.getCollection().set(collection);
        return "Элемент успешно добавлен.\n";
    }
}
