package ch.sniffdatel.presentation.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Interface {

	private final IntegerProperty number;
	private final StringProperty name;
	private final StringProperty ipAdress;
	private final StringProperty macAdress;

	public Interface(int number, String name, String ipAdress, String macAdress) {
		this.number = new SimpleIntegerProperty(number);
		this.name = new SimpleStringProperty(name);
		this.ipAdress = new SimpleStringProperty(ipAdress);
		this.macAdress = new SimpleStringProperty(macAdress);
	}

	public IntegerProperty getNumber() {
		return number;
	}

	public StringProperty getName() {
		return name;
	}

	public StringProperty getIpAdress() {
		return ipAdress;
	}

	public StringProperty getMacAdress() {
		return macAdress;
	}
}
