import java.util.*;

public class Graph {
    static class Edge {
        int src;
        int dest;
        int weight;

        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }
    
    class DistComparator implements Comparator<int[]> {
        public int compare(int[] arr1, int[] arr2) {
            return Integer.compare(arr1[1], arr2[1]); // Smaller distances have higher priority
        }
    }


    public ArrayList<Edge> generateGraph(int n) {
        ArrayList<Edge> edges = new ArrayList<>();
        Random rand = new Random();
        Set<String> edgeSet = new HashSet<>();

        for (int i = 0; i < n - 1; i++) {
            Edge e = new Edge(i, i + 1, rand.nextInt(10)+1);
            edges.add(e);
            edgeSet.add(i + "-" + (i + 1));
        }

        for (int i = 0; i < n - 1; i++) {
            int extraEdges = rand.nextInt(3);
            for (int j = 0; j < extraEdges; j++) {
                int dest = rand.nextInt(n);
                String edgeKey = i + "-" + dest;
                if (dest != i && dest != i + 1 && !edgeSet.contains(edgeKey)) {
                    edges.add(new Edge(i, dest, rand.nextInt(100) + 1));
                    edgeSet.add(edgeKey);
                }
            }
        }

        return edges;
    }
    
    public ArrayList<ArrayList<Edge>> findNeighbours(ArrayList<Edge> edges, int n) {
        ArrayList<ArrayList<Edge>> neighbors = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ArrayList<Edge> list = new ArrayList<>(); // Create a new list for each node
            for (Edge e : edges) {
                if (e.src == i) {
                    list.add(e);
                }
            }
            neighbors.add(list);
        }
        return neighbors;
    }

    
    public boolean isFound(PriorityQueue<int[]> q, int target) {
    	
    	for(int[] arr: q) {
    		if(arr[0] == target) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public int[] Dijkstra(ArrayList<ArrayList<Edge>> list, int n, int start_index) {
    	int[] dist =  new int[n];
    	
    	for(int i = 0; i < dist.length; i++) {
    		dist[i] =  Integer.MAX_VALUE;
    	}
    	
    	dist[start_index] = 0;
    	
    	// Priority queue will contain the node and its dist value in the form of an array
    	PriorityQueue<int[]> pq = new PriorityQueue<>(n, new DistComparator());
    	pq.add(new int[]{start_index, 0}); // Add start node with distance 0

    	
    	while(!pq.isEmpty()) {
    		int[] arr = pq.poll();
    		int node = arr[0];
    		int distance = arr[1];
    		
    		ArrayList<Edge> neighbors  = list.get(node);
    		
    		for(Edge neighbor: neighbors) {
    			if(dist[node] != Integer.MAX_VALUE && dist[neighbor.dest] > dist[node] + neighbor.weight) {
    				dist[neighbor.dest] = dist[node] + neighbor.weight;
    				
    				if(!isFound(pq, neighbor.dest)) {
    					int[] a = {neighbor.dest, dist[neighbor.dest]};
    					pq.add(a);
    				}
    			}
    		}
    		
    	}
    	
    	return dist;
    	
    	
    }

    public int[] bellmanFord(ArrayList<Edge> edges, int start_index, int n) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start_index] = 0;

        for (int i = 0; i < n - 1; i++) {
            for (Edge e : edges) {
                if (dist[e.src] != Integer.MAX_VALUE && dist[e.src] + e.weight < dist[e.dest]) {
                    dist[e.dest] = dist[e.src] + e.weight;
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        Graph g = new Graph();

        long startTime, endTime;
        int[] dist;

        int[] number_of_nodes = {5, 10, 20, 50, 100, 150, 200, 300};

        for (int n : number_of_nodes) {
            ArrayList<Edge> edges = g.generateGraph(n);

            // Measure Bellman-Ford runtime
            startTime = System.nanoTime();
            dist = g.bellmanFord(edges, 0, n);
            endTime = System.nanoTime();
            System.out.println("Bellman-Ford Time for " + n + " nodes: " + (endTime - startTime) + " ns");
            
            ArrayList<ArrayList<Edge>> list = g.findNeighbours(edges, n);
            
          
            startTime = System.nanoTime();
            dist = g.Dijkstra(list, n, 0);
            endTime = System.nanoTime();
            System.out.println("Dijkstra Time for " + n + " nodes: " + (endTime - startTime) + " ns");
            System.out.println();
            	 
            // Print distances
//            for (int j = 0; j < dist.length; j++) {
//                System.out.println(j + " -> " + (dist[j] == Integer.MAX_VALUE ? "Infinity" : dist[j]));
//            }
//            System.out.println("-------------------------------------");
        }
    }
}
