package main;

import java.util.List;

public class Instance {
    String region = null;
    Float total_cost = null;
    List<Tuple> serverTypes = null;

    Instance() { }

    Instance(String region, Float total_cost, List<Tuple> serverTypes) {
        this.region = region;
        this.total_cost = total_cost;
        this.serverTypes = this.serverTypes;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Float getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(Float total_cost) {
        this.total_cost = total_cost;
    }

    public List<Tuple> getServerTypes() {
        return serverTypes;
    }

    public void setServerTypes(List<Tuple> serverTypes) {
        this.serverTypes = serverTypes;
    }
}
