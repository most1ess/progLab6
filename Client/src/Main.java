import client.Processor;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n\n----------------------------------------");
            System.out.println("\nРабота программы была завершена.\n");
        }));
        Processor client = new Processor();
        client.execute();
    }
}
