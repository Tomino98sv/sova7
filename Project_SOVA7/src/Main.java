import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Database db = new Database("root","");
//        Car skoda1 = new Car("Citroen","orange",'D',"KE 720IO",4100);
//        db.addCar(skoda1);

        db.changeSPZ("KE 999AA","KE 111II");
        List<Car> priceCARS = db.getCarsByRegion("KE");
        for (Car car:priceCARS){
            System.out.println(car.getBrand()+"|"+car.getColor()+"|"+car.getFuel()+"|"+car.getSpz()+"|"+car.getPrice());
        }

        db.generateXML();

    }
}
