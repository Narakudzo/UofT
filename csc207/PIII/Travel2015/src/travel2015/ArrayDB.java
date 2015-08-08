package travel2015;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * The generic class of the User of the program
 */

public class ArrayDB<T> implements Serializable {
	private static final long serialVersionUID = -9025935656832745598L;
	private List<T> arrayObject;
	public ArrayDB() {
             /** The ArrayDB contents. */
            this.arrayObject = new ArrayList<T>();
        }
        /**
        * Adds the specified object to this ArrayDB.
        * @param newObject the new object to be added to this ArrayDB
        */
        public void addObject(T newObject) {
            this.arrayObject.add(newObject);
        }
        /**
        * remove the specified object to this ArrayDB.
        * @param index the index of the object to be removed from the ArrayDB
        */
        public void removeObject(int index) {
            this.arrayObject.remove(index);
        }
        /**
        * @param newObject the new object to be added to this ArrayDB
        * @return the list of the objects in the ArrayDB
        */
        public List<T> getDB() {
            return this.arrayObject;
        }
     
}