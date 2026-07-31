package samsung01.swea;

// 핵심 아이디어: 백트래킹
import java.util.*;
import java.io.*;

public class p3421 {
	
	static int cnt = 0;
	static boolean[][] worst;
	static int N;
	static List<Integer> arr;

	public static void main(String[] args) {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			int T = Integer.parseInt(br.readLine());
			StringTokenizer st = null;
			for (int t = 1; t <= T; t++) {
				cnt = 0;
				
				st = new StringTokenizer(br.readLine());
				N = Integer.parseInt(st.nextToken());
				int M = Integer.parseInt(st.nextToken());
				worst = new boolean[N+1][N+1];
				for (int i = 0; i < M; i++) {
					st = new StringTokenizer(br.readLine());
					int a = Integer.parseInt(st.nextToken());
					int b = Integer.parseInt(st.nextToken());
					worst[a][b] = true;
					worst[b][a] = true;
				}
				
				arr = new ArrayList<>();
				MakeBurgers(0);
				
				
				System.out.printf("#%d %d\n", t, cnt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

  // 백트래킹 함수
	static void MakeBurgers(int last_ing) {
		cnt++;
		for (int i = last_ing + 1; i <= N; i++) {
			if (isWorst(i))	continue;
			arr.add(i);
			MakeBurgers(i);
			arr.remove(arr.size() - 1);
		}
	}

  // 주어진 재료가 현재 재료 배열과 잘 어울리는지 여부
	static boolean isWorst(int ing) {
		for (int a : arr) {
			if (worst[a][ing])	return true;
		}
		return false;
	}

}
