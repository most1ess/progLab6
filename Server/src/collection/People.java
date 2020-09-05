package collection;

import collection.parser.Parser;
import person.Person;

import java.time.LocalDateTime;
import java.util.TreeMap;

public class People {
    private TreeMap<String, Person> collection = new TreeMap<>();
    private java.time.LocalDateTime creationDate;

    public People() {
        setCreationDate();
    }

    private Parser parser = new Parser();

    public void set(TreeMap<String, Person> collection) {
        this.collection = collection;
    }

    public void setCreationDate() {
        creationDate = LocalDateTime.now().withNano(0).withSecond(0);
    }

    public TreeMap<String, Person> get() {
        return collection;
    }

    public java.time.LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Parser getParser() {
        return parser;
    }
}
