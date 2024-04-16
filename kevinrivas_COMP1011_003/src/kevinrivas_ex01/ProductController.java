package kevinrivas_ex01;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;



public class ProductController implements Initializable {

    @FXML
    private TableView<Product> resultsOutput;

    @FXML
    private Button clearBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField productName;
    
    @FXML
    private TextField productID;
    
    @FXML
    private TextArea avgOutput;

    @FXML
    private Button displayAvgBtn;
    
    @FXML
    private TextArea minProdOutput;

    @FXML
    private Button MinProductBtn;

    
    private Connection connection;
    private PreparedStatement selectAllStatement;
    private PreparedStatement searchStatement;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/productsdb", "root", "Welcome1");

            selectAllStatement = connection.prepareStatement("SELECT * FROM products");

            TableColumn<Product, Integer> productIDCol = new TableColumn<>("Product ID");
            productIDCol.setCellValueFactory(new PropertyValueFactory<>("productID"));
            productIDCol.setPrefWidth(70); // Set the width to 100 pixels

            TableColumn<Product, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            nameCol.setPrefWidth(160); // Set the width to 200 pixels

            TableColumn<Product, String> manufacturerNameCol = new TableColumn<>("Manufacturer Name");
            manufacturerNameCol.setCellValueFactory(new PropertyValueFactory<>("manufacturerName"));
            manufacturerNameCol.setPrefWidth(130); // Set the width to 200 pixels

            TableColumn<Product, Double> pricePerUnitCol = new TableColumn<>("Price Per Unit");
            pricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));
            pricePerUnitCol.setPrefWidth(100); // Set the width to 100 pixels

            resultsOutput.getColumns().addAll(productIDCol, nameCol, manufacturerNameCol, pricePerUnitCol);

            clearBtn.setOnAction(this::clearAll);
            searchBtn.setOnAction(this::searchProduct);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void displayAll(ActionEvent event) {
        try {
            resultsOutput.getItems().clear();
            ResultSet resultSet = selectAllStatement.executeQuery();
            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String name = resultSet.getString("Name");
                String manufacturerName = resultSet.getString("ManufacturerName");
                double pricePerUnit = resultSet.getDouble("PricePerUnit");
                resultsOutput.getItems().add(new Product(productID, name, manufacturerName, pricePerUnit));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearAll(ActionEvent event) {
        resultsOutput.getItems().clear();
        avgOutput.clear();
        minProdOutput.clear();
    }

    @FXML
    private void searchProduct(ActionEvent event) {
        try {
            String searchName = productName.getText().trim();
            String searchID = productID.getText().trim();
            String query = "SELECT * FROM products WHERE Name = ? OR ProductID = ?";
            PreparedStatement searchStatement = connection.prepareStatement(query);
            searchStatement.setString(1, searchName);
            searchStatement.setString(2, searchID);
            ResultSet resultSet = searchStatement.executeQuery();
            resultsOutput.getItems().clear();
            boolean found = false;
            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String name = resultSet.getString("Name");
                String manufacturerName = resultSet.getString("ManufacturerName");
                double pricePerUnit = resultSet.getDouble("PricePerUnit");
                resultsOutput.getItems().add(new Product(productID, name, manufacturerName, pricePerUnit));
                found = true;
            }
            searchStatement.close();
            if (!found) {
                // Display an error message here
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Search Result");
                alert.setHeaderText(null);
                alert.setContentText("No results found.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void displayAveragePrice(ActionEvent event) {
        try {
            String query = "SELECT AVG(PricePerUnit) FROM products";
            PreparedStatement avgStatement = connection.prepareStatement(query);
            ResultSet resultSet = avgStatement.executeQuery();
            if (resultSet.next()) {
                double avgPrice = resultSet.getDouble(1);
                double roundedAvgPrice = Math.round(avgPrice * 100.0) / 100.0;
                avgOutput.setText("$" + String.valueOf(roundedAvgPrice));
            }
            avgStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void displayMinProduct(ActionEvent event) {
        try {
            String query = "SELECT MIN(PricePerUnit) FROM products";
            PreparedStatement minStatement = connection.prepareStatement(query);
            ResultSet resultSet = minStatement.executeQuery();
            if (resultSet.next()) {
                double minPrice = resultSet.getDouble(1);
                double roundedMinPrice = Math.round(minPrice * 100.0) / 100.0;
                minProdOutput.setText("$" + String.valueOf(roundedMinPrice));
            }
            minStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Don't forget to close the connection when done
    public void close() {
        try {
            if (selectAllStatement != null) {
                selectAllStatement.close();
            }
            if (searchStatement != null) {
                searchStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
