public class IDGenerator {
	private AVL retired;
	private AVL in_use;
	private int next_int;
	
	public IDGenerator() {
		retired = new AVL();
		in_use = new AVL();
	}
	
	public int generate_new_id() throws Exception {
		if (retired.get_size() > 0) {
			return retired.pop_smallest();
		} else {
			int newest = next_int;
			next_int += 1;
			in_use.add_node(newest);
		}
		return 0;
	}
	
	public void retire_old_id(int num) {
		boolean removed = in_use.remove_node(num);
		if (removed) {
			retired.add_node(num);
		}
	}
}