/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greedy;

/**
 *
 * @author ulakbim
 */
public class AircraftGreedy implements Comparable<AircraftGreedy>{
    
    private int type;
    private int order;
    private int count;
    private int latestLandingTime;
    private boolean landed;
    private int actualLandingTime;

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the latestLandingTime
     */
    public int getLatestLandingTime() {
        return latestLandingTime;
    }

    /**
     * @param latestLandingTime the latestLandingTime to set
     */
    public void setLatestLandingTime(int latestLandingTime) {
        this.latestLandingTime = latestLandingTime;
    }

    /**
     * @return the landed
     */
    public boolean isLanded() {
        return landed;
    }

    /**
     * @param landed the landed to set
     */
    public void setLanded(boolean landed) {
        this.landed = landed;
    }

    @Override
    public int compareTo(AircraftGreedy o) {
        int cmp = this.order > o.order ? +1 : this.order < o.order ? -1 : 0;
        return cmp;
    }
    
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof AircraftGreedy)) return false;

        AircraftGreedy that = (AircraftGreedy)obj;
        
        return this.order == that.order;
    }

    @Override
    public int hashCode(){
        return this.hashCode();
    }

    /**
     * @return the actualLandingTime
     */
    public int getActualLandingTime() {
        return actualLandingTime;
    }

    /**
     * @param actualLandingTime the actualLandingTime to set
     */
    public void setActualLandingTime(int actualLandingTime) {
        this.actualLandingTime = actualLandingTime;
    }
    
}
