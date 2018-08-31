import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Date;

public class TaskList {
	private Hashtable<Integer, Task> table;
	private IDGenerator generator;
	private class Task {
		private int id;
		private String name;
		private String description;
		private String category;
		private Date assign_time;
		private Date due_time;
		
		
		public Task(int i, String nm, String desc, String cat, Date asn, Date due) {
			id = i;
			name = nm;
			description = desc;
			category = cat;
			assign_time = asn;
			due_time = due;
		}
	}

	public TaskList() {
		table = new Hashtable<Integer, Task>();
		generator = new IDGenerator();
	}
	
	public void add_task(String nm, String desc, String cat, Date asn, Date due) {
		int new_id;
		try {
			new_id = generator.generate_new_id();
			Task t = new Task(new_id, nm, desc, cat, asn, due);
			table.put(new_id, t);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//this should be impossible
			System.out.println("add task threw error");
		}
	}
	
	public void remove_task(int id) {
		table.remove(id);
		generator.retire_old_id(id);
	}
	
	public ArrayList list_by_deadline() {
		ArrayList<Task> arr = new ArrayList<Task>(table.values());
		Collections.sort(arr, new Comparator<Task>() {
			@Override
			public int compare(Task t1, Task t2) {
				if (t1.due_time.getTime() < t2.due_time.getTime()) {
					return -1;
				} else if (t1.due_time.getTime() == t2.due_time.getTime()) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		return arr;
	}
	
	public ArrayList list_by_category() {
		ArrayList<Task> arr = new ArrayList<Task>(table.values());
		Collections.sort(arr, new Comparator<Task>() {
			@Override
			public int compare(Task t1, Task t2) {
				int comp = t1.category.compareTo(t2.category);
				if (comp < 0) {
					return -1;
				} else if (comp == 0) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		return arr;
	}
	
	public ArrayList list_by_alpha() {
		ArrayList<Task> arr = new ArrayList<Task>(table.values());
		Collections.sort(arr, new Comparator<Task>() {
			@Override
			public int compare(Task t1, Task t2) {
				int comp = t1.name.compareTo(t2.name);
				if (comp < 0) {
					return -1;
				} else if (comp == 0) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		return arr;
	}
	
	public ArrayList list_by_assign_time() {
		ArrayList<Task> arr = new ArrayList<Task>(table.values());
		Collections.sort(arr, new Comparator<Task>() {
			@Override
			public int compare(Task t1, Task t2) {
				if (t1.assign_time.getTime() < t2.assign_time.getTime()) {
					return -1;
				} else if (t1.due_time.getTime() == t2.due_time.getTime()) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		return arr;
	}
	
	public ArrayList list_by_time_elapsed() {
		ArrayList<Task> arr = new ArrayList<Task>(table.values());
		Collections.sort(arr, new Comparator<Task>() {
			@Override
			public int compare(Task t1, Task t2) {
				long elapsed1 = t1.due_time.getTime() - t1.assign_time.getTime();
				long elapsed2 = t2.due_time.getTime() - t2.assign_time.getTime();
				if (elapsed1 < elapsed2) {
					return -1;
				} else if (elapsed1 == elapsed2) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		return arr;
	}
  }