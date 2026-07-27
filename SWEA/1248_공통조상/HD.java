import java.io.*;
import java.util.*;

public class Solution {
	static int V, E, n1, n2;
	static int[] parent;
	static ArrayList<Integer>[] children;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int T = Integer.parseInt(br.readLine());
		StringTokenizer st;
		StringBuilder result = new StringBuilder();
		for (int tc = 1; tc <= T; tc++) {
			st = new StringTokenizer(br.readLine());

			V = Integer.parseInt(st.nextToken());
			E = Integer.parseInt(st.nextToken());
			n1 = Integer.parseInt(st.nextToken());
			n2 = Integer.parseInt(st.nextToken());

			parent = new int[V + 1];
			children = new ArrayList[V + 1];
			for (int i = 0; i <= V; i++) {
				children[i] = new ArrayList<>();
			}

			st = new StringTokenizer(br.readLine());
			for (int e = 0; e < E; e++) {
				int u = Integer.parseInt(st.nextToken());
				int v = Integer.parseInt(st.nextToken());

				children[u].add(v);
				parent[v] = u;
			}

			int pn = findParents();
			int subTreeSize = V;
			if (pn > 1) subTreeSize = bfs(pn);

			result.append("#" + tc + " " + pn + " " + subTreeSize + "\n");
		}
		System.out.println(result);
	}

	private static int bfs(int node) {

		Queue<Integer> q = new LinkedList<>();
		q.offer(node);

		int result = 0;
		while (!q.isEmpty()) {
			int i = q.poll();
			result++;

			for (int child : children[i]) {
				q.offer(child);
			}
		}

		return result;
	}

	private static int findParents() {
		Queue<Integer> q = new LinkedList<>();
		q.offer(n1);
		q.offer(n2);

		boolean[] visited = new boolean[V + 1];
		visited[n1] = visited[n2] = true;

		while (!q.isEmpty()) {
			int i = q.poll();

			int p = parent[i];
			if (p != 0) {
				if (visited[p]) return p;
				
				q.offer(p);
				visited[p] = true;
			}
		}

		return 0;
	}
}