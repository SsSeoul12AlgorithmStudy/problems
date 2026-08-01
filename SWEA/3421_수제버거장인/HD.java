import java.util.*;
import java.io.*;
 
public class Solution {
 
    static int N, M, cnt;
    static boolean[][] graph;
    static boolean[] visited;
 
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 
        int T = Integer.parseInt(br.readLine());
 
        StringBuilder result = new StringBuilder();
 
        StringTokenizer st;
        for (int tc = 1; tc <= T; tc++) {
            st = new StringTokenizer(br.readLine());
 
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
 
            graph = new boolean[N + 1][N + 1];
 
            for (int m = 0; m < M; m++) {
                st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
 
                graph[u][v] = graph[v][u] = true;
            }
 
            visited = new boolean[N + 1];
            cnt = 0;
            makeComb(0);
 
            result.append("#" + tc + " " + cnt + "\n");
        }
 
        System.out.println(result);
    }
 
    private static void makeComb(int node) {
        cnt++;
 
        for (int i = node + 1; i <= N; i++) {
 
            boolean canGo = true;
 
            for (int j = 1; j < i; j++) {
                if (visited[j] && graph[j][i]) {
                    canGo = false;
                    break;
                }
            }
 
            if (!canGo) continue;
 
            visited[i] = true;
            makeComb(i);
            visited[i] = false;
        }
    }
}