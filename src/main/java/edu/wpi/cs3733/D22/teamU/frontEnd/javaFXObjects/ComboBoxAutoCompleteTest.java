package edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ComboBoxAutoCompleteTest extends Application {

  private static final String[] LISTA = {
    "Abacate",
    "Abacaxi",
    "Ameixa",
    "Amora",
    "Alho descascado",
    "Batata baroa ou cenoura amarela",
    "Batata bolinha",
    "Batata doce",
    "Batata inglesa",
    "Batata yacon",
    "Berinjela",
    "Beterraba",
    "Cebola bolinha",
    "Cebola comum",
    "Cebola roxa",
    "Cenoura",
    "Cenoura baby",
    "Couve flor",
    "Ervilha",
    "Fava",
    "Gengibre",
    "Ovos brancos",
    "Ovos de codorna",
    "Ovos vermelhos"
  };

  @Override
  public void start(Stage stage) throws Exception {
    ComboBox<String> cmb = new ComboBox<>();
    cmb.setTooltip(new Tooltip());
    cmb.getItems().addAll(LISTA);
    stage.setScene(new Scene(new StackPane(cmb)));
    stage.show();
    stage.setTitle("Filtrando um ComboBox");
    stage.setWidth(300);
    stage.setHeight(300);
    new ComboBoxAutoComplete<String>(cmb, 300, 300);
  }
}
