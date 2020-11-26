package cloud.l0cky.radanalysetools.models;

import cloud.l0cky.radanalysetools.services.Coordinate;

public class BicycleParking implements Coordinate {

	private boolean covered;
	private int capacity;
	private double latitude;
	private double longitude;
	private String operator;
	private String bicycle_parking;
	private String access;


	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public boolean getCovered() {
		return covered;
	}

	public int getCapacity() {
		return capacity;
	}

//	public String getOperator() {
//		return operator;
//	}

	public String getBicycle_parking() {
		return bicycle_parking;
	}



	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setCovered(boolean covered) {
		this.covered = covered;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

//	public void setOperator(String operator) {
//		this.operator = operator;
//	}

	public void setBicycle_parking(String bicycle_parking) {
		this.bicycle_parking = bicycle_parking;
	}

	@Override
	public String toString() {
		return "BicycleParking{" +
//				"latitude:" + this.latitude +
//				", longitude:" + this.longitude +
//				", covered:" + this.covered +
//				", capacity:" + this.capacity +
//				", operator:" + this.operator +
////				", bicycle_parking:(\')" + this.bicycle_parking + "(\')" +
				"latitude=" + latitude +
				", longitude=" + longitude +
				", covered=" + covered +
				", capacity=" + capacity +
				", bicycle_parking'=" + bicycle_parking + '\'' +
				"}";
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}
}
