import server.Processor;

import java.util.logging.Level;

public class Main {
    public static void main(String[] args){
        Processor server = new Processor();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println('\n');
            server.getCollection().getParser().parseTo(server.getCollection().get());
            server.getLogger().log(Level.INFO, "Программа завершена.");
            System.out.println("\n----------------------------------------");
            System.out.println("\nРабота программы была завершена.\n");
        }));
        server.execute();
    }
}
