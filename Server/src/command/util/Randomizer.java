package command.util;

public class Randomizer {
    private Integer idMax = 0;

    /**
     * Генерация уникального id.
     * @return - новый наибольший id
     */
    public Integer generateId() {
        idMax++;
        return idMax;
    }

    public Integer getIdMax() {
        return idMax;
    }

    public void setIdMax(Integer idMax) {
        this.idMax = idMax;
    }
}
