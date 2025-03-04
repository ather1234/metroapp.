import java.util.ArrayList;
import java.util.HashMap;

public class Heap<T extends Comparable<T>> {
	private ArrayList<T> data = new ArrayList<>();
	private HashMap<T, Integer> map = new HashMap<>();

	public void add(T item) {
		data.add(item);
		map.put(item, data.size() - 1);
		upheapify(data.size() - 1);
	}

	private void upheapify(int ci) {
		if (ci == 0)
			return; // Prevents infinite recursion
		int pi = (ci - 1) / 2;
		if (isLarger(data.get(ci), data.get(pi)) > 0) {
			swap(pi, ci);
			upheapify(pi);
		}
	}

	private void swap(int i, int j) {
		T ith = data.get(i);
		T jth = data.get(j);

		data.set(i, jth);
		data.set(j, ith);
		map.put(ith, j);
		map.put(jth, i);
	}

	public void display() {
		System.out.println(data);
	}

	public int size() {
		return data.size();
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public T remove() {
		if (data.isEmpty())
			return null; // Prevent crash on empty heap
		swap(0, data.size() - 1);
		T removed = data.remove(data.size() - 1);
		map.remove(removed);
		if (!data.isEmpty()) {
			downheapify(0);
		}
		return removed;
	}

	private void downheapify(int pi) {
		int lci = 2 * pi + 1;
		int rci = 2 * pi + 2;
		int minIndex = pi;

		if (lci < data.size() && isLarger(data.get(lci), data.get(minIndex)) > 0) {
			minIndex = lci;
		}
		if (rci < data.size() && isLarger(data.get(rci), data.get(minIndex)) > 0) {
			minIndex = rci;
		}
		if (minIndex != pi) {
			swap(minIndex, pi);
			downheapify(minIndex);
		}
	}

	public T get() {
		return data.isEmpty() ? null : data.get(0);
	}

	public int isLarger(T t, T o) {
		return t.compareTo(o);
	}

	public void updatePriority(T pair) {
		if (!map.containsKey(pair))
			return; // Prevent KeyNotFound Error
		int index = map.get(pair);
		upheapify(index);
		downheapify(index);
	}

	// Included `main` method inside the same class for testing
	public static void main(String[] args) {
		Heap<Integer> heap = new Heap<>();

		System.out.println(" Adding elements to heap...");
		heap.add(10);
		heap.add(5);
		heap.add(20);
		heap.add(1);

		System.out.println("\nHeap after adding elements:");
		heap.display();

		System.out.println("\n Removing the top element...");
		System.out.println("Removed: " + heap.remove());

		System.out.println("\nHeap after removal:");
		heap.display();

		System.out.println("\n Checking top element (peek): " + heap.get());

		System.out.println("\n Checking heap size: " + heap.size());

		System.out.println("\n Checking if heap is empty: " + heap.isEmpty());

		System.out.println("\n Updating priority of an element...");
		heap.updatePriority(5);

		System.out.println("\nHeap after updating priority:");
		heap.display();
	}
}
