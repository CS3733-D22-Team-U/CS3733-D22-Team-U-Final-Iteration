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
      if (document.getId().equals("Dr.Mike")) {
        drMikeTime.setText(document.getDouble("Time").toString());
      }
    }
  }
}
