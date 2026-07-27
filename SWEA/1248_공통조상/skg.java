package samsung01.swea;

import java.util.*;
import java.io.*;

public class p1248 {
	
	static int[] parent;
//	얘를 루트로 하는 서브 트리의 크기
	static int[] lc;
	static int[] rc;
	static int ans_size;
	
	// node에 방문하고 자식이 있으면 자식도 func 실행
	static void func(int node) {
		ans_size++;
		if (lc[node] > 0)	func(lc[node]);
		if (rc[node] > 0)	func(rc[node]);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		StringBuilder sb = new StringBuilder();
		
		int T = Integer.parseInt(br.readLine());
		for (int test_case = 1; test_case <= T; test_case++) {
			int adj_root = 0;
			ans_size = 0;
			
			st = new StringTokenizer(br.readLine());
			int V = Integer.parseInt(st.nextToken());
			int E = Integer.parseInt(st.nextToken());
			int A = Integer.parseInt(st.nextToken());
			int B = Integer.parseInt(st.nextToken());
			
			parent = new int[V+1];
			lc = new int[V+1];
			rc = new int[V+1];
			
			st = new StringTokenizer(br.readLine());
			for (int i = 0; i < E; i++) {
				int u = Integer.parseInt(st.nextToken());
				int v = Integer.parseInt(st.nextToken());
				parent[v] = u;
				if (lc[u] != 0) rc[u] = v;
				else lc[u] = v;
			}
			
			
			Stack<Integer> Stack_a = new Stack<>();
			Stack<Integer> Stack_b = new Stack<>();
			
			while (A != 0) { // 조건을 A != 1로 하면 에러가 난다..
				Stack_a.add(A);
				A = parent[A];
			}
			while (B != 0) {
				Stack_b.add(B);
				B = parent[B];
			}
			
			while (Stack_a.peek().equals(Stack_b.peek())) { // wrapper class 동등 비교는 항상 equals 메서드로!
				adj_root = Stack_a.peek();
				Stack_a.pop();
				Stack_b.pop();
			}
			func(adj_root);
			
			sb.append("#" + test_case + " " + adj_root + " " + ans_size + "\n");
		}
		
		System.out.println(sb.toString());
		
		br.close();
	}

}
