package study;

import java.util.*;
import java.io.*;

class Node {
	int x, y;
	Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

public class p1249 {
	
	static int N;
	// 문제에서 주어진 입력을 저장하는 이차원 배열
	static int[][] board;
	// 해당 칸에 도착하는 최소 시간을 뜻하는 이차원 배열
	static int[][] vis;
	static final int[][] dir = {{-1,0}, {1,0}, {0,-1}, {0,1}};
	
	static boolean OOB(int x, int y) {
		return (x < 0 || y < 0 || x >= N || y >= N);
	}
	
	static void BFS() {
		PriorityQueue<Node> Q = new PriorityQueue<>((a, b) -> {
			return vis[a.x][a.y] - vis[b.x][b.y]; 
		});
		
		vis[0][0] = 0;
		Q.add(new Node(0, 0));
		
		while (!Q.isEmpty()) {
			Node cur = Q.poll();
			if (cur.x == N-1 && cur.y == N-1) break;
			for (int[] d : dir) {
				int dx = cur.x + d[0];
				int dy = cur.y + d[1];
				if (OOB(dx, dy))	continue;
				if (vis[dx][dy] >= 0) continue;
				vis[dx][dy] = vis[cur.x][cur.y] + board[dx][dy]; 
				Q.add(new Node(dx, dy));
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int T = Integer.parseInt(br.readLine());
		
		for (int test_case = 1; test_case <= T; test_case++) {
			N = Integer.parseInt(br.readLine());
			board = new int[N][N];
			vis = new int[N][N];
			for (int i = 0; i < N; i++) {
				char[] row = br.readLine().toCharArray();
				for (int j = 0; j < N; j++) {
					board[i][j] = row[j] - '0';
					vis[i][j] = -1;
				}
			}
			
			BFS();
			
			System.out.printf("#%d %d\n", test_case, vis[N-1][N-1]);
			
		}
				
		br.close();
	}

}
