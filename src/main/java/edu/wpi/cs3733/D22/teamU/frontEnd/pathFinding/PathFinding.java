package edu.wpi.cs3733.D22.teamU.frontEnd.pathFinding;

import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class PathFinding {
    private Udb udb;
    public PathFinding(ArrayList<Edge> edges) throws SQLException, IOException {
        udb = Udb.getInstance();
        ArrayList<Location> locations = udb.locationImpl.locations;
        for(Edge e: edges){
            try {
                locations.get(locations.indexOf(e.loc1)).getConnected().add(e);
            }catch (IndexOutOfBoundsException e1){
            }
            try {
                locations.get(locations.indexOf(e.loc2)).getConnected().add(e);
            }catch (IndexOutOfBoundsException e2){
            }
        }
    }


    public ArrayList<Edge> findPath(Location l1, Location l2) throws CloneNotSupportedException {
        PriorityQueue<Agent> agents = new PriorityQueue<>();
        agents.add(new Agent(l1, l2));
        Agent best = new Agent(l1, l2);
        while(!agents.isEmpty()){
            Agent check = agents.remove();
            if(check.current.equals(l2))
                return check.edges;
            else{
                for(Edge e: check.current.getConnected()){
                    Location temp;
                    Agent next;
                    if(e.loc1 == check.current)
                        temp = e.loc2;
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
