package br.com.fatex.backend_fatex.entities;

public class CaronaPontos {

    private Long id;
    private String address;
    private String name;
    private double lat;
    private double lng;

    // Constructors, getters, and setters
    public CaronaPontos() {}

    public CaronaPontos(String address, String name, double lat, double lng) {
        this.address = address;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
