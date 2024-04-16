package kevinrivas_ex01;

public class Product {

    private int productID;
    private String name;
    private String manufacturerName;
    private double pricePerUnit;

    public Product(int productID, String name, String manufacturerName, double pricePerUnit) {
        this.productID = productID;
        this.name = name;
        this.manufacturerName = manufacturerName;
        this.pricePerUnit = pricePerUnit;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}