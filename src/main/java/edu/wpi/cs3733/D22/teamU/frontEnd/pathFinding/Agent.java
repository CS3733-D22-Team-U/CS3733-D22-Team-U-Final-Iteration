package edu.wpi.cs3733.D22.teamU.frontEnd.pathFinding;

import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import java.util.ArrayList;

public class Agent implements Comparable<Agent>, Cloneable {
  ArrayList<Edge> edges;
  Location current, goal;
  double distance;

  public Agent(Location current, Location goal) {
    this.current = current;
    this.goal = goal;
    this.distance = 0;
    edges = new ArrayList<>();
  }

  private Agent(Location current, Location goal, ArrayList<Edge> edges, double distance) {
    this.current = current;
    this.goal = goal;
    this.distance = distance;
    this.edges = edges;
  }

  public void move(Edge moved, Location current) {
    edges.add(moved);
    boolean sameFloor = current.getFloor().equals(this.current.getFloor());
    this.current = current;
    if (sameFloor) {
      double a = Math.abs(moved.loc1.getXcoord() - moved.loc2.getXcoord());
      double b = Math.abs(moved.loc1.getYcoord() - moved.loc2.getYcoord());
      distance += Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    } else distance += 500;
  }

  @Override
  public int compareTo(Agent o) {
    if (o.heuristic() < heuristic()) return 1;
    else if (o.heuristic() > heuristic()) return -1;
    else return 0;
  }

  /**
   * Calculates a heuristic using the straight line distance towards the goal
   *
   * @return
   */
  public double heuristic() {
    double a = Math.abs(current.getXcoord() - goal.getXcoord());
    double b = Math.abs(current.getYcoord() - goal.getYcoord());
    double d = distance + Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    if (!current.getFloor().equals(goal.getFloor())) d += 500;
    return d;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return new Agent(current, goal, (ArrayList<Edge>) edges.clone(), distance);
  }
}
