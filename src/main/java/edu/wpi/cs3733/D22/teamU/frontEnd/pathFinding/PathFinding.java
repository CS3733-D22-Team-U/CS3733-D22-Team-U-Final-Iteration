package edu.wpi.cs3733.D22.teamU.frontEnd.pathFinding;

import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class PathFinding {
  private ArrayList<Edge> edges;

  public PathFinding(ArrayList<Edge> edges) throws SQLException, IOException {
    this.edges = edges;
  }

  public ArrayList<Edge> getEdges() {
    return edges;
  }

  public void setEdges(ArrayList<Edge> edges) {
    this.edges = edges;
  }

  public ArrayList<Edge> findPath(Location l1, Location l2) throws CloneNotSupportedException {
    PriorityQueue<Agent> agents = new PriorityQueue<>();
    agents.add(new Agent(l1, l2));
    Agent best = new Agent(l1, l2);
    while (!agents.isEmpty()) {
      Agent check = agents.remove();
      if (check.current.equals(l2)) return check.edges;
      else {
        for (Edge e : check.current.getConnected()) {
          Location temp;
          Agent next;
          if (e.loc1 == check.current) temp = e.loc2;
          else temp = e.loc1;
          next = (Agent) check.clone();
          next.move(e, temp);
          agents.add(next);
        }
      }
    }
    return best.edges;
  }
}
