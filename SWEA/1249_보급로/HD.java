import java.io.*;
import java.util.*;
 
public class Solution {
    public static void main(String[] args) throws Exception {
 
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 
        int T = Integer.parseInt(br.readLine());
        StringBuilder result = new StringBuilder();
        for (int tc = 1; tc <= T; tc++) {
 
            int N = Integer.parseInt(br.readLine());
 
            int[][] map = new int[N][N];
            for (int r = 0; r < N; r++) {
                char[] arr = br.readLine().toCharArray();
 
                for (int c = 0; c < N; c++) {
                    map[r][c] = arr[c] - 48;
                }
            }
 
            result.append("#" + tc + " " + bfs(N, map) + "\n");
        }
        System.out.println(result);
    }
 
    private static int bfs(int N, int[][] map) {
 
        int[] dr = { 0, 1, 0, -1 }, dc = { 1, 0, -1, 0 };
        int[][] minVal = new int[N][N];
        for (int r = 0; r < N; r++) Arrays.fill(minVal[r], Integer.MAX_VALUE);
 
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[] { 0, 0 });
        minVal[0][0] = 0;
 
        while (!q.isEmpty()) {
            int[] now = q.poll();
            int r = now[0], c = now[1];
 
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];
 
                if (nr < 0 || nc < 0 || nr >= N || nc >= N) continue;
 
                if (minVal[nr][nc] > minVal[r][c] + map[nr][nc]) {
                    minVal[nr][nc] = minVal[r][c] + map[nr][nc];
                    q.offer(new int[] { nr, nc });
                }
            }
        }
 
        return minVal[N - 1][N - 1];
    }
}