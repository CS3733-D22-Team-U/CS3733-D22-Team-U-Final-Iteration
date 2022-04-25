package edu.wpi.cs3733.D22.teamU;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class Main {
  public static Firestore db;

  public static void main(String[] args)
      throws SQLException, IOException, ExecutionException, InterruptedException {
    Firebase();
    DBController.main(new String[] {"admin", "admin"});
    UIController.main(args);
  }

  public static void Firebase() throws IOException, ExecutionException, InterruptedException {
    GoogleCredentials credentials =
        GoogleCredentials.fromStream(
            Main.class
                .getClassLoader()
                .getResourceAsStream("edu/wpi/cs3733/D22/teamU/credentials.json"));
    FirebaseOptions options =
        new FirebaseOptions.Builder()
            .setCredentials(credentials)
            .setProjectId("ultra-unicorn")
            .build();
    FirebaseApp.initializeApp(options);
  }
}
