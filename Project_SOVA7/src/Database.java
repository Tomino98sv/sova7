import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Database implements carmethods {

    private String user;
    private String pass;
    private String url;
    private final String driver="com.mysql.jdbc.Driver";

    public Database(String user,String pass, String url){
        this.user=user;
        this.pass=pass;
        this.url=url;
    }

    public Database(String user,String pass){
        this.user=user;
        this.pass=pass;
        this.url="jdbc:mysql://localhost:3306/sova7";
    }

    private Connection getConnection(){
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url,user,pass);
            System.out.println("Driver is running");
            return conn;
        }catch (SQLException|ClassNotFoundException e){
            System.out.println("Driver is not running");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addCar(Car car) {
        Connection conn = getConnection();
        try {
            PreparedStatement statm = conn.prepareStatement("INSERT INTO cars(brand,color,fuel,spz,price) values(?,?,?,?,?)");
            statm.setString(1,car.getBrand());
            statm.setString(2,car.getColor());
            statm.setString(3, String.valueOf(car.getFuel()));
            statm.setString(4,car.getSpz());
            statm.setInt(5,car.getPrice());
            statm.executeUpdate();
            conn.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Car> getCarsByPrice(int maxPrice) {
        Connection conn = getConnection();
        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("Select * from cars where price = ?");
            statement.setInt(1,maxPrice);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                cars.add(
                        new Car(
                                resultSet.getString("brand"),
                                resultSet.getString("color"),
                                resultSet.getString("fuel").charAt(0),
                                resultSet.getString("spz"),
                                resultSet.getInt("price")
                                )
                );
            }

            return cars;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Car> getCarsByBrand(String brand) {
        Connection conn = getConnection();
        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("Select * from cars where brand like ?");
            stmt.setString(1,brand);
            ResultSet result = stmt.executeQuery();
            while (result.next()){
                cars.add(
                        new Car(
                                result.getString("brand"),
                                result.getString("color"),
                                result.getString("fuel").charAt(0),
                                result.getString("spz"),
                                result.getInt("price")
                        )
                );
            }
            return cars;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Car> getCarsByFuel(char fuel) {
        Connection conn = getConnection();
        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("Select * from cars where fuel like ?");
            statement.setString(1,String.valueOf(fuel));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                cars.add(
                        new Car(
                                resultSet.getString("brand"),
                                resultSet.getString("color"),
                                resultSet.getString("fuel").charAt(0),
                                resultSet.getString("spz"),
                                resultSet.getInt("price")
                        )
                );
            }
            return cars;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Car> getCarsByRegion(String spz) {
        Connection connection = getConnection();
        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("Select * from cars where spz like ?");
            statement.setString(1,spz+"%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                cars.add(
                        new Car(
                                resultSet.getString("brand"),
                                resultSet.getString("color"),
                                resultSet.getString("fuel").charAt(0),
                                resultSet.getString("spz"),
                                resultSet.getInt("price")
                        )
                );
            }
            return cars;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void changeSPZ(String oldSPZ, String newSPZ) {
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE cars set spz = ? where spz = ?");
            statement.setString(1,newSPZ);
            statement.setString(2,oldSPZ);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Car> getAllCars(){
        Connection connection = getConnection();
        List<Car> cars = new ArrayList<Car>();

        try {
         PreparedStatement statement = connection.prepareStatement("Select * from cars");
         ResultSet resultSet = statement.executeQuery();
         while (resultSet.next()){
             cars.add(
               new Car(
                       resultSet.getString("brand"),
                       resultSet.getString("color"),
                       resultSet.getString("fuel").charAt(0),
                       resultSet.getString("spz"),
                       resultSet.getInt("price")
               )
             );
         }
         return cars;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void generateXML(){
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

                Document doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("cars");
                doc.appendChild(rootElement);

                Database database = new Database("root", "", "jdbc:mysql://localhost:3306/sova7");
                List<Car> allCars = database.getAllCars();

                for (Car auto : allCars) {
                    Element car = doc.createElement("car");
                    rootElement.appendChild(car);

                    Element brand = doc.createElement("brand");
                    brand.appendChild(doc.createTextNode(auto.getBrand()));
                    car.appendChild(brand);

                    Element color = doc.createElement("color");
                    color.appendChild(doc.createTextNode(auto.getColor()));
                    car.appendChild(color);

                    Element fuel = doc.createElement("fuel");
                    fuel.appendChild(doc.createTextNode(String.valueOf(auto.getFuel())));
                    car.appendChild(fuel);

                    Element spz = doc.createElement("spz");
                    spz.appendChild(doc.createTextNode(auto.getSpz()));
                    car.appendChild(spz);

                    Element price = doc.createElement("price");
                    price.appendChild(doc.createTextNode(String.valueOf(auto.getPrice())));
                    car.appendChild(price);

                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("C:\\Users\\Tomas\\IdeaProjects\\Project_SOVA7\\src\\XMLcar.xml"));
                transformer.transform(source, result);

                StreamResult consoleResult = new StreamResult(System.out);
                transformer.transform(source, consoleResult);
            }catch (ParserConfigurationException | TransformerException  e){
                System.out.println("Wrong something");
            }
        }

}
