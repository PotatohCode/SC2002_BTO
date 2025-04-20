package BTO;

import java.util.ArrayList;
import java.util.List;

public class Project<T> {
	// declare
	private List<T> list;
	
	// constructor
	public Project() {
		this.list = new ArrayList<>();
	}
	
	// get
	public List<T> getItems() {
		return this.list;
	}
	
	public int getCount() {
		return this.list.size();
	}
	
	// add
	public void addItem(T item) {
		this.list.add(item);
	}
	
	// remove
	public void removeItem(T item) {
		this.list.remove(item);
	}

}
