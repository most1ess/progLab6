package server;

import person.Person;

import java.io.*;
import java.nio.ByteBuffer;

public class Receiver {
    /**
     * Получение и десериализация данных от пользователя.
     * @param processor процессор сервера.
     * @param <T> тип получаемых данных.
     * @return полученный объект.
     * @throws IOException ошибка ввода-вывода.
     * @throws ClassNotFoundException ошибка ненахождения класса.
     */
    public static <T> T receive(Processor processor) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(5000);
        processor.setSocketAddress(processor.getDatagramChannel().receive(buffer));
        byte[] bufArray = buffer.array();
        ObjectInputStream serialize = new ObjectInputStream(new ByteArrayInputStream(bufArray));
        T obj = (T) serialize.readObject();
        serialize.close();
        buffer.clear();
        return obj;
    }
}
