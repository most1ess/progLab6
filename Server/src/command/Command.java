package command;

public abstract class Command {
    /**
     * Исполнения команды.
     * @return результат, отправляемый клитенту.
     */
    public String execute() {
        return "Результат метода execute(), вызванного из абстрактного класса Command.";
    }
}
