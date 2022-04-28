package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class LoginTimeController extends ServiceController {
  Firestore db = FirestoreClient.getFirestore();

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

  @FXML Text time;
  @FXML Text date;
  @FXML Text drMikeTime;
  @FXML Text drTimTime;
  @FXML Text drWillTime;
  @FXML Text drDeeptiTime;
  @FXML Text drHarshTime;
  @FXML Text drVillaTime;
  @FXML Text drJoselinTime;
  @FXML Text drKodyTime;
  @FXML Text drStaffTime;
  @FXML Text drAdminTime;
  @FXML Text drNickTime;
  @FXML Text drIainTime;
  @FXML Text drWilsonTime;
  @FXML Text drBelishaTime;

  @Override
  public void addRequest() {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ApiFuture<QuerySnapshot> future = db.collection("loginTimes").get();
    List<QueryDocumentSnapshot> documents = null;
    try {
      documents = future.get().getDocuments();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    for (QueryDocumentSnapshot document : documents) {
      switch (document.getId()) {
        case "Dr.Mike":
          drMikeTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Kody":
          drKodyTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Tim":
          drTimTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Harsh":
          drHarshTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Will":
          drWillTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Belisha":
          drBelishaTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Joselin":
          drJoselinTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Iain":
          drIainTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Marko":
          drVillaTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Deepti":
          drDeeptiTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Nick":
          drNickTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "Dr.Wilson":
          drWilsonTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "admin":
          drAdminTime.setText(document.getDouble("Time").toString() + " seconds");
          break;
        case "staff":
          drStaffTime.setText(document.getDouble("Time").toString() + " seconds");
          break;

      }
    }
  }
}
