import java.util.*;
import java.io.*;

public class Graph_M {
	public class Vertex {
		HashMap<String, Integer> nbrs = new HashMap<>();
	}

	static HashMap<String, Vertex> vtces;

	public Graph_M() {
		vtces = new HashMap<>();
	}

	public boolean containsVertex(String vname) {
		return vtces.containsKey(vname);
	}

	public void addVertex(String vname) {
		Vertex vtx = new Vertex();
		vtces.put(vname, vtx);
	}

	public void addEdge(String vname1, String vname2, int value) {
		Vertex vtx1 = vtces.get(vname1);
		Vertex vtx2 = vtces.get(vname2);
		if (vtx1 == null || vtx2 == null || vtx1.nbrs.containsKey(vname2)) {
			return;
		}
		vtx1.nbrs.put(vname2, value);
		vtx2.nbrs.put(vname1, value);
	}

	// ✅ FIXED: containsEdge() Method
	public boolean containsEdge(String vname1, String vname2) {
		Vertex vtx1 = vtces.get(vname1);
		Vertex vtx2 = vtces.get(vname2);
		if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
			return false;
		}
		return true;
	}

	public void display_Stations() {
		System.out.println("\n************ METRO STATIONS ************\n");
		int i = 1;
		for (String key : vtces.keySet()) {
			System.out.println(i + ". " + key);
			i++;
		}
		System.out.println("\n***************************************\n");
	}

	public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> processed) {
		if (containsEdge(vname1, vname2)) { // ✅ FIXED: Now it works correctly
			return true;
		}
		processed.put(vname1, true);
		for (String nbr : vtces.get(vname1).nbrs.keySet()) {
			if (!processed.containsKey(nbr) && hasPath(nbr, vname2, processed)) {
				return true;
			}
		}
		return false;
	}

	private class DijkstraPair implements Comparable<DijkstraPair> {
		String vname;
		String psf;
		int cost;

		@Override
		public int compareTo(DijkstraPair o) {
			return Integer.compare(this.cost, o.cost); // ✅ FIXED: Correct Sorting Order
		}
	}

	public int dijkstra(String src, String des) {
		PriorityQueue<DijkstraPair> heap = new PriorityQueue<>();
		HashMap<String, DijkstraPair> map = new HashMap<>();

		for (String key : vtces.keySet()) {
			DijkstraPair np = new DijkstraPair();
			np.vname = key;
			np.psf = key.equals(src) ? key : "";
			np.cost = key.equals(src) ? 0 : Integer.MAX_VALUE;
			heap.add(np);
			map.put(key, np);
		}

		while (!heap.isEmpty()) {
			DijkstraPair rp = heap.poll();
			if (rp.vname.equals(des)) {
				return rp.cost;
			}
			map.remove(rp.vname);

			for (String nbr : vtces.get(rp.vname).nbrs.keySet()) {
				if (map.containsKey(nbr)) {
					int oc = map.get(nbr).cost;
					int nc = rp.cost + vtces.get(rp.vname).nbrs.get(nbr);
					if (nc < oc) {
						DijkstraPair gp = map.get(nbr);
						gp.psf = rp.psf + " -> " + nbr;
						gp.cost = nc;
						heap.remove(gp);
						heap.add(gp);
					}
				}
			}
		}
		return -1;
	}

	public static void createMetroMap(Graph_M g) {
		g.addVertex("Noida Sector 62");
		g.addVertex("Botanical Garden");
		g.addVertex("Yamuna Bank");
		g.addVertex("Rajiv Chowk");
		g.addVertex("Vaishali");
		g.addVertex("Moti Nagar");
		g.addVertex("Janak Puri West");
		g.addVertex("Dwarka Sector 21");

		g.addEdge("Noida Sector 62", "Botanical Garden", 8);
		g.addEdge("Botanical Garden", "Yamuna Bank", 10);
		g.addEdge("Yamuna Bank", "Vaishali", 8);
		g.addEdge("Yamuna Bank", "Rajiv Chowk", 6);
		g.addEdge("Rajiv Chowk", "Moti Nagar", 9);
		g.addEdge("Moti Nagar", "Janak Puri West", 7);
		g.addEdge("Janak Puri West", "Dwarka Sector 21", 6);
	}

	public static void main(String[] args) throws IOException {
		Graph_M g = new Graph_M();
		createMetroMap(g);
		BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println("\n********** METRO SYSTEM MENU **********");
			System.out.println("1. List All Metro Stations");
			System.out.println("2. Find Shortest Distance Between Stations");
			System.out.println("3. Exit");
			System.out.print("Enter Your Choice (1-3): ");

			int choice = -1;
			try {
				choice = Integer.parseInt(inp.readLine());
			} catch (Exception e) {
				System.out.println("Invalid Input! Please enter a number.");
				continue;
			}

			switch (choice) {
				case 1:
					g.display_Stations();
					break;

				case 2:
					System.out.print("Enter Source Station: ");
					String src = inp.readLine();
					System.out.print("Enter Destination Station: ");
					String dest = inp.readLine();

					if (!g.containsVertex(src) || !g.containsVertex(dest)) {
						System.out.println("Invalid Stations! Please enter a valid metro station.");
						break;
					}

					HashMap<String, Boolean> processed = new HashMap<>();
					if (!g.hasPath(src, dest, processed)) {
						System.out.println("No direct path exists between these stations.");
						break;
					}

					int distance = g.dijkstra(src, dest);
					System.out.println("Shortest Distance from " + src + " to " + dest + " is: " + distance + " KM");
					break;

				case 3:
					System.out.println("Exiting Metro System. Have a safe journey!");
					System.exit(0);

				default:
					System.out.println("Invalid Choice! Please select an option between 1-3.");
			}
		}
	}
}
