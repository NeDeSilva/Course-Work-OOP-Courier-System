/**
 * Session - the tracking lifecycle record for a parcel.
 * Holds the current status and the expected delivery / collection dates,
 * plus the driver currently assigned to the parcel.
 */
public class Session {
	private int id;
	private String trackingNumber;
	private String status;
	private String deliveryDate;
	private String collectionDate;
	private int driverId;

	public Session() {
	}

	public Session(String trackingNumber, String status, String deliveryDate,
			String collectionDate, int driverId) {
		this.trackingNumber = trackingNumber;
		this.status = status;
		this.deliveryDate = deliveryDate;
		this.collectionDate = collectionDate;
		this.driverId = driverId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
	}

	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}
}
