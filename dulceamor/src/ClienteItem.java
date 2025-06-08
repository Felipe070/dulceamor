package dulceamor;

class ClienteItem {
    private int id;
    private String display;

    public ClienteItem(int id, String display) {
        this.id = id;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return display;
    }
}
