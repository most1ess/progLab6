package client;

import command.*;
import command.util.InsertUtil;
import command.util.UpdateUtil;
import person.Person;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.HashSet;
import java.util.Scanner;

public class Processor {
    private SocketAddress socketAddress;
    private DatagramChannel datagramChannel;
    private HashSet<String> scriptFileNames = new HashSet<>();
    private boolean execStatus = true;
    private boolean scriptMode;
    private DatagramSocket datagramSocket;
    private byte[] buf = new byte[65536];

    /**
     * Основной метод, организующий работу клиента.
     */
    public void execute() {
        System.out.println("\n>>>>>>>>>> ДОБРО ПОЖАЛОВАТЬ! <<<<<<<<<<\n\n");
        System.out.println("Лабораторная работа №6, Клиентское приложение\n");
        System.out.println("Студент: Максим Беляков\n");
        System.out.println("Группа: Р3131\n\n");
        System.out.println("----------------------------------------\n");

        try (DatagramChannel datagramChannel = DatagramChannel.open()) {
            this.setDatagramChannel(datagramChannel);
            Connector.connect(this);
            loop();
        } catch (IOException e) {
            System.out.println("!!!---ОШИБКА---!!!\n");
            System.out.println("Канал обмена датаграммами не может быть открыт.");
            System.out.println("Дальнейшая работа программы не может быть продолжена.\n");
            System.exit(1);
        }
    }

    /**
     * Метод, организующий цикл ввода команд.
     */
    private void loop() {
        String input;
        String[] splitInput;
        Scanner sc = new Scanner(System.in);
        System.out.println("Для начала работы введите нужную вам команду. Список доступных команд можно узнать, использовав команду 'help'.");
        while (execStatus) {
            do {
                System.out.print(">");
                input = sc.nextLine();
            } while (input.equals(""));
            System.out.println();
            scriptFileNames.clear();
            splitInput = input.trim().split(" ");
            scriptMode = splitInput[0].equals("execute_script");
            handle(splitInput);
        }
    }

    /**
     * Метод, осуществляющий обработку введённых пользователем данных.
     * @param splitInput введённые данные в виде массива слов.
     * @return булево значение, используемое во многих других классах.
     */
    public boolean handle(String[] splitInput) {
        try {
            switch (splitInput[0]) {
                case "exit":
                    execStatus = false;
                    return false;
                case "help":
                case "info":
                case "show":
                case "clear":
                case "group_counting_by_creation_date":
                    Sender.send(this, new CommandData(splitInput[0]));
                    break;
                case "remove_key":
                case "remove_greater":
                case "remove_greater_key":
                case "count_greater_than_location":
                case "filter_starts_with_name":
                    Sender.send(this, new CommandData(splitInput[0], splitInput[1]));
                    break;
                case "replace_if_lower":
                    Sender.send(this, new CommandData(splitInput[0], splitInput[1], splitInput[2]));
                    break;
                case "update":
                    Sender.send(this, new CommandData(splitInput[0], splitInput[1]));
                    if (Receiver.receive(this)) {
                        UpdateUtil updateUtil = new UpdateUtil();
                        Person person = updateUtil.genPerson();
                        Sender.send(this, person);
                    } else return false;
                    break;
                case "insert":
                    Sender.send(this, new CommandData(splitInput[0], splitInput[1]));
                    if (Receiver.receive(this)) {
                        Person person;
                        InsertUtil insertUtil = new InsertUtil();
                        person = insertUtil.genPerson();
                        Sender.send(this, person);
                    } else return false;
                    break;
                case "execute_script":
                    if (!scriptFileNames.add(splitInput[1])) return false;
                    ExecuteScript executeScript = new ExecuteScript(splitInput[1], this);
                    if (executeScript.execute()) {
                        System.out.println("Скрипт из файла '" + splitInput[1] + "' был успешно исполнен.\n");
                    } else {
                        System.out.println("Скрипт из файла '" + splitInput[1] + "' не был исполнен корректно.\n");
                    }
                    return true;
                default:
                    System.out.println("Введённой вами команды не существует!\n" +
                            "Для просмотра списка команд введите 'help'.");
                    return false;
            }
            Receiver.receive(this);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("У введённой вами команды недостаточно аргументов! Чтобы посмотреть, какие аргументы принимает команда," +
                    " используйте команду 'help'.\n");
            return false;
        }
        return true;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public DatagramChannel getDatagramChannel() {
        return datagramChannel;
    }

    public void setDatagramChannel(DatagramChannel datagramChannel) {
        this.datagramChannel = datagramChannel;
    }

    public boolean isScriptMode() {
        return scriptMode;
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }
}
