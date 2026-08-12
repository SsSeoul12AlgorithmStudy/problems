import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

// 아이디어 : 악마의 손아귀 -> 먼저 몇 초에 잠식되는지 기록해놓는다.
// 그 다음에 수연이가 BFS로 진행 -> 진행 조건에 잠식 시간이 현재 초보다 빠르면 불가 조건 추가.
class Node {
	int x, y;
	Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			Node node = (Node) obj;
			return node.x == this.x && node.y == this.y;
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}

class Solution {
	static int N, M;
	// 악마의 손아귀 잠식이 이뤄지는 시간 저장 + 수연이 도착 시간 저장 (-1로 초기화)
	static int[][] vis;
	static char[][] board;
	static final int[][] dir = {{1,0}, {-1,0}, {0,1}, {0,-1}};
	
	public static void main(String args[]) throws Exception {
		StringTokenizer st = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			int T;
			T = Integer.parseInt(br.readLine());
			
			for (int test_case = 1; test_case <= T; test_case++) {
				st = new StringTokenizer(br.readLine());
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				
				Queue<Node> Q = new ArrayDeque<Node>();
				vis = new int[N][M];
				for (int[] row : vis) {
					Arrays.fill(row, -1);
				}
				board = new char[N][M];
				Node start = null;
				Node end = null;
				
				for (int i = 0; i < N; i++) {
					char[] carr = br.readLine().toCharArray();
					for (int j = 0; j < M; j++) {
						board[i][j] = carr[j];
						if (carr[j] == 'S')	start = new Node(i, j);
						else if (carr[j] == 'D') end = new Node(i, j);
						else if (carr[j] == '*') {
							vis[i][j] = 0;
							Q.add(new Node(i, j));
						}
					}
				}
				
				// 악마들 먼저 잠식 진행
				while (!Q.isEmpty()) {
					Node cur = Q.poll();
					for (int[] d : dir) {
						int dx = d[0] + cur.x;
						int dy = d[1] + cur.y;
						if (OOB(dx, dy))	continue;
						if (board[dx][dy] == 'D' || board[dx][dy] == 'X' || vis[dx][dy] >= 0)	continue;
						vis[dx][dy] = vis[cur.x][cur.y] + 1;
						Q.add(new Node(dx, dy));
					}
				}
				
				// printVis();
				
				// 수연이 진행
				Q.add(start);
				vis[start.x][start.y] = 0;
				while (!Q.isEmpty()) {
					Node cur = Q.poll();
					// System.out.println("cur is " + cur);
					if (cur.equals(end))	break;
					for (int[] d : dir) {
						int dx = d[0] + cur.x;
						int dy = d[1] + cur.y;
						if (OOB(dx, dy))	continue;
						if (board[dx][dy] == 'X' || (vis[dx][dy] >= 0 && vis[dx][dy] <= vis[cur.x][cur.y] + 1))	continue;	// 실수 : != '.'로 하면 도착 못함 && vis[dx][dy] >= 0일 때만 (기본값 -1이므로)
						vis[dx][dy] = vis[cur.x][cur.y]+ 1;
						Q.add(new Node(dx, dy));
					}
				}
				
				String result = vis[end.x][end.y] == -1 ? "GAME OVER" : Integer.toString(vis[end.x][end.y]);
				System.out.printf("#%d %s\n", test_case, result);
			}
		}
	}
	
	public static void printVis() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				System.out.print(vis[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static boolean OOB(int x, int y) {
		return (x < 0 || y < 0 || x >= N || y >= M);
	}
}