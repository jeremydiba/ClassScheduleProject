package edu.gatech.scheduleproject.model;

public class Edge {
    private OfferedClass vertex1;
    private OfferedClass vertex2;
    public Edge(OfferedClass vertex1, OfferedClass vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }
    /**
     * Getter for the incident OfferedClass
     * @return the incident OfferedClass in the pair
     */
    public OfferedClass getSource() {
        return vertex1;
    }
    /**
     * Getter for the adjacent OfferedClass
     * @return the adjacent OfferedClass in the pair
     */
    public OfferedClass getAdjacent() {
        return vertex2;
    }
    /**
     * contains method analyzing whether the object is in the vertex pair
     * @param Object to be compared
     * @return A boolean value expressing whether the object is contained in the edge
     */
    public boolean contains(Object o) {
        if(o instanceof OfferedClass) {
            if (((OfferedClass)o).equals(vertex1) || ((OfferedClass)o).equals(vertex2)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public boolean equals(Object o) {
        if(o instanceof Edge) {
            if((((Edge)o).getSource()).equals(this.getSource()) && (((Edge)o).getAdjacent()).equals(this.getAdjacent())) {
                return true;
            } else if((((Edge)o).getSource()).equals(this.getAdjacent()) && (((Edge)o).getAdjacent()).equals(this.getSource())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}