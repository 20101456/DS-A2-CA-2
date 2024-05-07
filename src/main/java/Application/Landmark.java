package Application;

public class Landmark {
    private String name;
    private String street;
    private double latitude;
    private double longitude;

    public Landmark(String name, String street, double latitude, double longitude) {
        this.name = name;
        this.street = street;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getName() { return name; }
    public String getStreet() { return street; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }


    @Override
    public String toString() {
        return "Landmark{" +
                "name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
