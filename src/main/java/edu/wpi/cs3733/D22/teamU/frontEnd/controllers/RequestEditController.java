package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RequestEditController {
	private Request request;
	private ArrayList<Request> requests;
	private ArrayList<String> fields;

	@FXML
	TextField ID;
	@FXML
	TextField name;
	@FXML
	TextField patientName;
	@FXML
	TextField status;
	@FXML
	ComboBox<Location> locations;
	@FXML
	ComboBox<Employee> employees;

	@FXML
	TextField service;
	@FXML
	TextField notes;
	@FXML
	DatePicker pickUp;
	@FXML
	DatePicker dropOff;
	@FXML
	TextField amount;
	@FXML
	TextField typeOfRequest;
	@FXML
	TextField priority;
	@FXML
	TextField labType;
	@FXML
	TextField descript;
	@FXML
	TextField lethal;
	@FXML
	TextField typeOfMaintenance;
	@FXML
	TextField description;
	@FXML
	TextField message;
	@FXML
	TextField device;
	@FXML
	TextField dietRest;
	@FXML
	TextField addNotes;
	@FXML
	TextField gifts;
	@FXML
	TextField religion;
	@FXML
	TextField toLang;

	public void setUp(Request request) {
		this.request = request;
		this.requests = new ArrayList<>(); // initialize this to something diff in future
		this.fields = new ArrayList<>();
		try {
			locations.setTooltip(new Tooltip());
			locations.getItems().addAll(Udb.getInstance().locationImpl.locations);
			new ComboBoxAutoComplete<Location>(locations, 650, 290);
			employees.setTooltip(new Tooltip());
			employees.getItems().addAll(Udb.getInstance().EmployeeImpl.hList().values());
			new ComboBoxAutoComplete<Employee>(employees, 675, 380);
		} catch (Exception e) {
			e.printStackTrace();
		}

		switch (request.getClass().getSimpleName()) {
			case "LaundryRequest":
				fields.add("patientName");
				fields.add("ID");
				fields.add("employee");
				fields.add("status");
				fields.add("destination");
				fields.add("pickUpDate");
				fields.add("dropOffDate");
				fields.add("services");
				fields.add("time");
				fields.add("notes");
				break;
		}

		updateFields();
	}

	// removes request from database
	public void removeRequest() {
		requests.remove(request);
		try {
			Udb.getInstance().remove(request);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// close pane
	}

	// updates the request
	public void updateRequest() {
		Request newRequest = request;
		for (String field : fields) {
			switch (field) {
				case "patientName" -> newRequest.setPatientName(patientName.getText().trim());
				case "employee" -> newRequest.setEmployee(employees.getValue());
				case "status" -> newRequest.setStatus(status.getText().trim());
				case "destination" -> newRequest.setDestination(locations.getValue().getNodeID());
				case "pickUpDate" -> newRequest.setPickUpDate(pickUp.getValue().toString());
				case "dropOffDate" -> newRequest.setDropOffDate(dropOff.getValue().toString());
				case "services" -> newRequest.setServices(service.getText().trim());
				case "notes" -> newRequest.setNotes(notes.getText().trim());
				case "name" -> newRequest.setName(name.getText().trim());
				case "location" -> newRequest.setLocation(locations.getValue());
				case "amount" -> newRequest.setAmount(Integer.parseInt(amount.getText().trim()));
				case "typeOfRequest" -> newRequest.setTypeOfRequest(typeOfRequest.getText().trim());
				case "priority" -> newRequest.setPriority(Integer.parseInt(priority.getText().trim()));
				case "labType" -> newRequest.setLabType(labType.getText().trim());
				case "descript" -> newRequest.setDescript(descript.getText().trim());
				case "lethal" -> newRequest.setLethal(lethal.getText().trim());
				case "typeOfMaintenance" -> newRequest.setTypeOfMaintenance(typeOfMaintenance.getText().trim());
				case "description" -> newRequest.setDescription(description.getText().trim());
				case "message" -> newRequest.setMessage(message.getText().trim());
				case "device" -> newRequest.setDevice(device.getText().trim());
				case "dietRest" -> newRequest.setDietRest(dietRest.getText().trim());
				case "addNotes" -> newRequest.setAddNotes(addNotes.getText().trim());
				case "gifts" -> newRequest.setGifts(gifts.getText().trim());
				case "religion" -> newRequest.setReligion(religion.getText().trim());
				case "toLang" -> newRequest.setToLang(toLang.getText().trim());
				default -> {
				}
			}
		}
		System.out.println(request);

		// close pane?
	}

	// Set fields to edit or remove
	public void updateFields() {
		for (String field : fields) {
			switch (field) {
				case "ID" -> ID.setText(request.getID());
				case "patientName" -> patientName.setText(request.getPatientName());
				case "employee" -> employees.setValue(request.getEmployee());
				case "status" -> status.setText(request.getStatus());
				case "destination" -> locations.setValue(request.getLocation());
				case "pickUpDate" -> pickUp.setValue(LocalDate.parse(request.getPickUpDate()));
				case "dropOffDate" -> dropOff.setValue(LocalDate.parse(request.getDropOffDate()));
				case "services" -> service.setText(request.getServices());
				case "notes" -> notes.setText(request.getNotes());
				case "amount" -> amount.setText(String.valueOf(request.getAmount()));
				case "typeOfRequest" -> typeOfRequest.setText(request.getTypeOfRequest());
				case "priority" -> priority.setText(String.valueOf(request.getPriority()));
				case "labType" -> labType.setText(request.getLabType());
				case "descript" -> descript.setText(request.getDescript());
				case "lethal" -> lethal.setText(request.getLethal());
				case "typeOfMaintenance" -> typeOfMaintenance.setText(request.getTypeOfMaintenance());
				case "description" -> description.setText(request.getDescription());
				case "message" -> message.setText(request.getMessage());
				case "device" -> device.setText(request.getDevice());
				case "dietRest" -> dietRest.setText(request.getDietRest());
				case "addNotes" -> addNotes.setText(request.getAddNotes());
				case "gifts" -> gifts.setText(request.getGifts());
				case "religion" -> religion.setText(request.getReligion());
				case "toLang" -> toLang.setText(request.getToLang());
				default -> {
				}
			}
		}
	}

	public Request getRequest() {
		return request;
	}
}
