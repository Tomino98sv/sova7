public class Car {

    private String brand;
    private String color;
    private char fuel;
    private String spz;
    private int price;

    public Car(String brand, String color, char fuel, String spz, int price){
        this.brand=brand;
        this.color=color;
        this.fuel=fuel;
        this.spz=spz;
        this.price=price;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public char getFuel() {
        return fuel;
    }

    public String getSpz() {
        return spz;
    }

    public int getPrice() {
        return price;
    }

}
