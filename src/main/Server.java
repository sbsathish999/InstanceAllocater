package main;

public class Server {
    String region = null;
    String type = null;
    Float price = null;
    Integer CPUs = null;

    public Server() {}
    public Server(String region, String type, Float price, Integer CPUs) {
        this.region = region;
        this.type = type;
        this.price = price;
        this.CPUs = CPUs;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getCPUs() {return CPUs; }

    public void setCPUs(Integer CPUs) {
        this.CPUs = CPUs;
    }
}
