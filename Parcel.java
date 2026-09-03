/**
 * Parcel - represents a courier parcel, its description and addresses,
 * and the sender/reserver that own it.
 */
public class Parcel {
	private int id;
	private String trackingNumber;
	private String name;
	private double weight;
	private double size;
	private String senderAddress;
	private String receiverAddress;
	private String description;
	private int senderId;
	private int reserverId;
	private String createdAt;

	public Parcel() {
	}

	public Parcel(String trackingNumber, String name, double weight, double size,
			String senderAddress, String receiverAddress, String description,
			int senderId, int reserverId) {
		this.trackingNumber = trackingNumber;
		this.name = name;
		this.weight = weight;
		this.size = size;
		this.senderAddress = senderAddress;
		this.receiverAddress = receiverAddress;
		this.description = description;
		this.senderId = senderId;
		this.reserverId = reserverId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getReserverId() {
		return reserverId;
	}

	public void setReserverId(int reserverId) {
		this.reserverId = reserverId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
