package collection.parser;


import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.io.IOException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import collection.People;
import person.Coordinates;
import person.Country;
import person.Location;
import person.Person;
import command.util.Randomizer;
import error.ErrorCheck;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.NodeList;

public class Parser {
    private Randomizer randomizer = new Randomizer();
    private Logger logger;

    /**
     * Парсинг из файла.
     *
     * @param collection коллекция для парсинга
     */
    public void parseFrom(People collection) {
        try {
           if(System.getenv("FILE_PATH") == null) {
                System.out.println("Переменная окружения отсутствует! Коллекция не может быть загружена.");
                logger.log(Level.WARNING, "Переменная окружения отсутствует. Коллекция не может быть загружена.");
                return;
            }
            String fileName = System.getenv("FILE_PATH");
            if(fileName.equals("")) {
                System.out.println("Переменная окружения пуста! Коллекция не может быть загружена.");
                logger.log(Level.WARNING, "Переменная окружения пуста! Коллекция не может быть загружена.");
                return;
            }
            File file = new File(fileName);
            TreeMap<String, Person> treeMap = new TreeMap<>();
            String key = "";
            randomizer.setIdMax(0);
            if (file.canRead()) {
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                ErrorCheck error = new ErrorCheck();
                documentBuilder.setErrorHandler(error);
                String readFile = readFile(file);
                StringReader stringReader = new StringReader(readFile);
                InputSource inputSource = new InputSource(stringReader);
                Document document = documentBuilder.parse(inputSource);
                Node root = document.getDocumentElement();

                String textContent;

                NodeList people = root.getChildNodes();

                Integer[] ids = new Integer[people.getLength()];
                String[] keys = new String[people.getLength()];
                int currenIdAmount = 0;
                int currentKeyAmount = 0;
                for (int i = 0; i < people.getLength(); i++) {
                    Person e = new Person();
                    Coordinates coordinates = new Coordinates();
                    Location location = new Location();

                    Node person = people.item(i);
                    NodeList personProps = person.getChildNodes();
                    if (personProps.getLength() == 10 || personProps.getLength() == 12) {
                        for (byte peopleCounter = 0; peopleCounter < personProps.getLength(); peopleCounter++) {
                            Node personProp = personProps.item(peopleCounter);
                            textContent = personProp.getTextContent();
                            String currentTagName = personProp.getNodeName().toUpperCase();
                            switch (currentTagName) {
                                case "KEY":
                                    if (textContent.equals("")) {
                                        System.out.println("Ключ одного из элементов имеет недопустимое значение! Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Ключ одного из элементов имеет недопустимое значение! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    boolean isKeyGood = true;
                                    for (int keyCounter = 0; keyCounter < currenIdAmount; keyCounter++) {
                                        if (textContent.equals(keys[keyCounter])) {
                                            System.out.println("Ключи нескольких элементов совпадают! Коллекция не может быть загружена.");
                                            logger.log(Level.WARNING, "Ключи нескольких элементов совпадают! Коллекция не может быть загружена.");
                                            isKeyGood = false;
                                        }
                                    }
                                    if (isKeyGood) {
                                        key = textContent;
                                        keys[currentKeyAmount] = key;
                                        currentKeyAmount++;
                                    } else {
                                        return;
                                    }

                                    break;
                                case "ID":
                                    if (textContent.equals("") || Integer.parseInt(textContent) <= 0) {
                                        System.out.println("Поле id одного из элементов имеет недопустимое значение.");
                                        logger.log(Level.WARNING, "Поле id одного из элементов имеет недопустимое значение.");
                                        return;
                                    }
                                    boolean isIdGood = true;
                                    for (int idCounter = 0; idCounter < currenIdAmount; idCounter++) {
                                        if (Integer.parseInt(textContent) == ids[idCounter]) {
                                            System.out.println("Id нескольких элементов совпадают! Коллекция не может быть загружена.");
                                            logger.log(Level.WARNING, "Id нескольких элементов совпадают! Коллекция не может быть загружена.");
                                            isIdGood = false;
                                        }
                                    }
                                    if (isIdGood) {
                                        e.setId(Integer.valueOf(textContent));
                                        ids[currenIdAmount] = e.getId();
                                        currenIdAmount++;
                                        if (randomizer.getIdMax() < e.getId()) {
                                            randomizer.setIdMax(e.getId());
                                        }
                                    } else {
                                        return;
                                    }

                                    break;
                                case "NAME":
                                    if (textContent.equals("")) {
                                        System.out.println("Имя одного из элементов пустое! Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Имя одного из элементов пустое! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    e.setName(textContent);
                                    break;
                                case "COORDINATES":
                                    NodeList coordinatesField = personProp.getChildNodes();

                                    Node coordinateXField = coordinatesField.item(0);
                                    textContent = coordinateXField.getTextContent();
                                    if (textContent.equals("")) {
                                        System.out.println("Координата х одного из элементов отсутствует. Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Координата х одного из элементов отсутствует. Коллекция не может быть загружена.");
                                        return;
                                    }
                                    coordinates.setX(Double.parseDouble(textContent));

                                    Node coordinateYField = coordinatesField.item(1);
                                    textContent = coordinateYField.getTextContent();
                                    if (textContent.equals("")) {
                                        System.out.println("Координата у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Координата у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    if (Double.parseDouble(textContent) > 355) {
                                        System.out.println("Координата у одного из элементов больше 355! Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Координата у одного из элементов больше 355! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    coordinates.setY(Double.parseDouble(textContent));

                                    e.setCoordinates(coordinates);
                                    break;
                                case "CREATIONDATE":
                                    if (textContent.equals("")) {
                                        System.out.println("Дата создания у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Дата создания у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    e.setCreationDate(textContent);
                                    break;
                                case "HEIGHT":
                                    if (textContent.equals("")) {
                                        System.out.println("Рост у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Рост у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    if (Integer.parseInt(textContent) <= 0) {
                                        System.out.println("Рост у одного из элементов неположителен! Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Рост у одного из элементов неположителен! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    e.setHeight(Integer.parseInt(textContent));
                                    break;
                                case "BIRTHDAY":
                                    if (textContent.equals("")) {
                                        textContent = null;
                                    }
                                    e.setBirthday(textContent);
                                    break;
                                case "WEIGHT":
                                    if (textContent.equals("")) {
                                        System.out.println("Вес у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Вес у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    if (Long.parseLong(textContent) <= 0) {
                                        System.out.println("Вес у одного из элементов неположителен! Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Вес у одного из элементов неположителен! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    e.setWeight(Long.parseLong(textContent));
                                    break;
                                case "NATIONALITY":
                                    if (textContent.equals("")) {
                                        System.out.println("Национальность у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        logger.log(Level.WARNING, "Национальность у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    e.setNationality(Country.valueOf(textContent));
                                    break;
                                case "LOCATION":
                                    NodeList locationField = personProp.getChildNodes();

                                    if (locationField.getLength() > 1) {
                                        Node locationXField = locationField.item(0);
                                        textContent = locationXField.getTextContent();
                                        if (textContent.equals("")) {
                                            System.out.println("Местоположение по х у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                            logger.log(Level.WARNING, "Местоположение по х у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                            return;
                                        }
                                        location.setX(Long.valueOf(textContent));

                                        Node locationYField = locationField.item(1);
                                        textContent = locationYField.getTextContent();
                                        if (textContent.equals("")) {
                                            System.out.println("Местоположение по у у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                            logger.log(Level.WARNING, "Местоположение по у у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                            return;
                                        }
                                        location.setY(Float.parseFloat(textContent));

                                        Node locationZField = locationField.item(2);
                                        textContent = locationZField.getTextContent();
                                        if (textContent.equals("")) {
                                            System.out.println("Местоположение по z у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                            logger.log(Level.WARNING, "Местоположение по z у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                            return;
                                        }
                                        location.setZ(Double.parseDouble(textContent));

                                        e.setLocation(location);
                                    } else {
                                        e.setLocation(null);
                                    }
                            }
                        }
                        treeMap.put(key, e);
                    } else {
                        System.out.println("Некоторые элементы из файла не были записаны в коллекцию, так как представлены в файле не верно.");
                        logger.log(Level.WARNING, "Некоторые элементы из файла не были записаны в коллекцию, так как представлены в файле не верно.");
                    }
                }
                collection.set(treeMap);
                collection.setCreationDate();
            } else {
                System.out.println("Невозможно считать данные из файла, так как файл недоступен для чтения.");
                logger.log(Level.WARNING, "Невозможно считать данные из файла, так как файл недоступен для чтения.");
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("Файл поврежден или не найден!");
            logger.log(Level.WARNING, "Файл поврежден или не найден!");
        } catch (IllegalArgumentException e) {
            System.out.println("Одно или несколько полей имеют недопустимые по формату значения! Коллекция не может быть загружена.");
            logger.log(Level.WARNING, "Одно или несколько полей имеют недопустимые по формату значения! Коллекция не может быть загружена.");
        }
        System.out.println("Коллекция успешно загружена.");
        logger.log(Level.INFO, "Коллекция успешно загружена.");
    }

    /**
     * Метод для чтения из файла в строку.
     * @param file файл.
     * @return строка с содержимым файла.
     */
    private String readFile(File file) {
        String fileInString = "";

        StringBuilder stringBuilder = new StringBuilder(fileInString);

        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            do {
                line = br.readLine();
                if (line != null) {
                    stringBuilder.append(line);
                }
            } while (line != null);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        } catch (IOException e) {
            System.out.println("Во время чтения файла произошла ошибка!");
        }
        return stringBuilder.toString();
    }

    /**
     * Парсинг в файл.
     *
     * @param treeMap - коллекция
     */
    public void parseTo(TreeMap<String, Person> treeMap) {
        try {
            if(System.getenv("FILE_PATH") == null) {
                System.out.println("Переменная окружения отсутствует! Коллекция не может быть загружена.");
                return;
            }
            String fileName = System.getenv("FILE_PATH");
            if(fileName.equals("")) {
                System.out.println("Переменная окружения пуста! Коллекция не может быть загружена.");
                return;
            }
            //String fileName = "data.xml";
            File file = new File(fileName);
            if (file.canWrite()) {
                try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><ROOT></ROOT>");
                }


                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                ErrorCheck error = new ErrorCheck();
                documentBuilder.setErrorHandler(error);
                Document document = documentBuilder.parse(fileName);

                Node root = document.getDocumentElement();

                if (treeMap.size() > 0) {
                    Iterator<Entry<String, Person>> iterator = treeMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Person> entry = iterator.next();

                        Element person = document.createElement("PERSON");

                        Element key = document.createElement("KEY");
                        key.setTextContent(entry.getKey());
                        person.appendChild(key);

                        Element id = document.createElement("ID");
                        id.setTextContent(entry.getValue().getId().toString());
                        person.appendChild(id);

                        Element name = document.createElement("NAME");
                        name.setTextContent(entry.getValue().getName());
                        person.appendChild(name);

                        Element coordinates = document.createElement("COORDINATES");
                        Element xCoordinate = document.createElement("X");
                        xCoordinate.setTextContent(Double.toString(entry.getValue().getCoordinates().getX()));
                        coordinates.appendChild(xCoordinate);
                        Element yCoordinate = document.createElement("Y");
                        yCoordinate.setTextContent(Double.toString(entry.getValue().getCoordinates().getY()));
                        coordinates.appendChild(yCoordinate);
                        person.appendChild(coordinates);

                        Element creationDate = document.createElement("CREATIONDATE");
                        creationDate.setTextContent(entry.getValue().getCreationDate().toString().replaceAll("T", " "));
                        person.appendChild(creationDate);

                        Element height = document.createElement("HEIGHT");
                        height.setTextContent(Integer.toString(entry.getValue().getHeight()));
                        person.appendChild(height);

                        Element birthday = document.createElement("BIRTHDAY");
                        if (entry.getValue().getBirthday() == null) {
                            birthday.setTextContent("");
                        } else {
                            birthday.setTextContent(entry.getValue().getBirthday().toString().replaceAll("T", " "));
                        }
                        person.appendChild(birthday);

                        Element weight = document.createElement("WEIGHT");
                        weight.setTextContent(Long.toString(entry.getValue().getWeight()));
                        person.appendChild(weight);

                        Element nationality = document.createElement("NATIONALITY");
                        nationality.setTextContent(entry.getValue().getNationality().toString());
                        person.appendChild(nationality);

                        Element location = document.createElement("LOCATION");
                        if (entry.getValue().getLocation() == null) {
                            location.setTextContent("null");
                        } else {
                            Element xLocation = document.createElement("X");
                            xLocation.setTextContent(entry.getValue().getLocation().getX().toString());
                            location.appendChild(xLocation);
                            Element yLocation = document.createElement("Y");
                            yLocation.setTextContent(Float.toString(entry.getValue().getLocation().getY()));
                            location.appendChild(yLocation);
                            Element zLocation = document.createElement("Z");
                            zLocation.setTextContent(Double.toString(entry.getValue().getLocation().getZ()));
                            location.appendChild(zLocation);
                        }
                        person.appendChild(location);

                        root.appendChild(person);
                    }
                    try {
                        Transformer tr = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(document);
                        try (FileOutputStream fos = new FileOutputStream("temp.xml")) {
                            StreamResult result = new StreamResult(fos);
                            tr.transform(source, result);
                        }
                        if (writeFile(fileName)) {
                            System.out.println("Во время чтения файла произошла ошибка или файл не найден!");
                        } else {
                            System.out.println("Коллекция успешно сохранена в файл!");
                            return;
                        }
                    } catch (TransformerException | IOException e) {
                        System.out.println("Во время записи в файл произошла ошибка.");
                    }
                    System.out.println("Коллекция успешно сохранена в файл!");

                } else {
                    System.out.println("Коллекция пуста, поэтому в файл был записан только корневой тег!");
                }
            } else {
                System.out.println("Операция сохранения невозможна, потому что файл недоступен для записи.");
            }
        } catch (ParserConfigurationException | SAXException ex) {
            System.out.println("Файл поврежден!");
        } catch (IOException e) {
            System.out.println("Во время записи в файл произошла ошибка.");
        }
    }

    /**
     * Метод для записи в вайл.
     * @param fileName имя файла.
     * @return успешность записи в файл.
     */
    private static boolean writeFile(String fileName) {
        File file = new File(fileName);
        String fileInString = "";
        StringBuilder stringBuilder = new StringBuilder(fileInString);
        try (Scanner scanner = new Scanner(new File("temp.xml"))) {
            String line;
            do {
                line = scanner.nextLine();
                if (line != null) {
                    stringBuilder.append(line);
                }
            } while (scanner.hasNextLine());
        } catch (IOException e) {
            return true;
        }

        try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(stringBuilder.toString());
        } catch (IOException e) {
            return true;
        }
        return false;
    }

    public Randomizer getRandomizer() {
        return randomizer;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}