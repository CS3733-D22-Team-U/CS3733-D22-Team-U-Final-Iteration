package edu.wpi.cs3733.D22.teamU.frontEnd.pathFinding;

import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import javafx.scene.shape.Line;

public class Edge extends Line {
  String edgeID;
  Location loc1, loc2;

  public Edge(String edgeID, Location loc1, Location loc2) {
    this.edgeID = edgeID;
    this.loc1 = loc1;
    this.loc2 = loc2;
    setStartX(loc1.getXcoord());
    setStartY(loc1.getYcoord());
    setEndX(loc2.getXcoord());
    setEndY(loc2.getYcoord());
  }

  public String getEdgeID() {
    return edgeID;
  }

  public Location getLoc1() {
    return loc1;
  }

  public Location getLoc2() {
    return loc2;
  }
}
