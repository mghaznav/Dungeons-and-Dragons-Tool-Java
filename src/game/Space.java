/***************************\
*   Muhammad Ali Ghaznavi   *
*   mghaznav@uoguelph.ca    *
*        Space.java         *
*         1068753           *
*         26/11/19          *
\***************************/

package game;

import java.io.Serializable;

/**
* This is an abstract class for Chamber and Passage.
*/
public abstract class Space implements Serializable {

    /**
    * This method gets the description of the space.
    * @return A String with the description of the space.
    */
    public abstract String getDescription();

    /**
    * This method attaches a door to the space.
    * @param theDoor - The Door to be attached to the space.
    */
    public abstract void setDoor(Door theDoor);

}
