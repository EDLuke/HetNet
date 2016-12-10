package android_network.hetnet.data;

import android.location.Location;

import java.util.Comparator;

public class PolicyRuleVector implements Comparator<PolicyRuleVector> {
    private String applicationId;
    private Location location;
    private String networkSSID;
    private double bandwidth;
    private double signalStrength;
    private double signalFrequency;
    private double timeToConnect;
    private double cost;


    public PolicyRuleVector(String applicationId, Location location, String networkSSID, double bandwidth, double signalStrength, double signalFrequency, double timeToConnect, double cost) {
        this.applicationId = applicationId;
        this.location = location;
        this.networkSSID = networkSSID;
        this.bandwidth = bandwidth;
        this.signalStrength = signalStrength;
        this.signalFrequency = signalFrequency;
        this.timeToConnect = timeToConnect;
        this.cost = cost;
    }

    public PolicyRuleVector() {

    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getNetworkSSID() {
        return networkSSID;
    }

    public void setNetworkSSID(String networkSSID) {
        this.networkSSID = networkSSID;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public double getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(double signalStrength) {
        this.signalStrength = signalStrength;
    }

    public double getSignalFrequency() {
        return signalFrequency;
    }

    public void setSignalFrequency(double signalFrequency) {
        this.signalFrequency = signalFrequency;
    }

    public double getTimeToConnect() {
        return timeToConnect;
    }

    public void setTimeToConnect(double timeToConnect) {
        this.timeToConnect = timeToConnect;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


    @Override
    public int compare(PolicyRuleVector vector, PolicyRuleVector t1) {
        //TODO:Compare logic for decision
        return 0;
    }

    @Override
    public String toString() {
        return "PolicyRuleVector{" +
                "applicationId='" + applicationId + '\'' +
                ", location=" + location +
                ", networkSSID='" + networkSSID + '\'' +
                ", bandwidth=" + bandwidth +
                ", signalStrength=" + signalStrength +
                ", signalFrequency=" + signalFrequency +
                ", timeToConnect=" + timeToConnect +
                ", cost=" + cost +
                '}';
    }
}
