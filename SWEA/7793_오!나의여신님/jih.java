package swea.p7793;

import java.io.*;
import java.util.*;

public class Solution {
    /*
    * t초에서
    * 1. 부식이 전파
    * 2. 상하좌우 중 부식이 진행되지 않은 곳이 있으면 수연이가 이동
    *
    * 마지막에 수연이의 도달여부 확인
    * */
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        int T = Integer.parseInt(br.readLine());
        for(int test = 1; test <= T; test++){
            init();
            String answer = solve();
            System.out.println("#" + test + " " + answer);
        }
    }

    static void init() throws IOException {
        // N, M
        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        // map
        // sr, sc
        // demons
        map = new String[N];
        demons = new ArrayList<>();

        for(int r = 0; r < N; r++){
            map[r] = br.readLine();
            for(int c = 0; c < M; c++){
                if(map[r].charAt(c) == 'S'){
                    sr = r;
                    sc = c;
                }
                if(map[r].charAt(c) == '*'){
                    demons.add(new int[] { r, c });
                }
            }
        }
    }

    static int N, M;
    static String[] map;

    static int sr, sc;
    static List<int []> demons;

    static String solve(){
        final int[] dr = { -1, 0, 1, 0 };
        final int[] dc = { 0, 1, 0, -1 };

        boolean[][] visited = new boolean[N][M];
        boolean[][] rotten = new boolean[N][M];

        Queue<int[]> qu = new LinkedList<>();
        Queue<int[]> demonQu = new LinkedList<>();

        visited[sr][sc] = true;
        qu.offer(new int[] { sr, sc, 0 });

        for(int[] demon : demons){
            rotten[demon[0]][demon[1]] = true;
            demonQu.offer(new int[] { demon[0], demon[1], 0 });
        }

        while(!qu.isEmpty()){
            int[] cur = qu.poll();

            // 부식 전파
            while(!demonQu.isEmpty() && demonQu.peek()[2] == cur[2]){
                int[] dCur = demonQu.poll();

                for(int i = 0; i < 4; i++){
                    int nr = dCur[0] + dr[i];
                    int nc = dCur[1] + dc[i];

                    if(nr < 0 || nr >= N || nc < 0 || nc >= M){
                        continue;
                    }

                    if(rotten[nr][nc]){
                        continue;
                    }

                    if(map[nr].charAt(nc) == 'X' || map[nr].charAt(nc) == 'D'){
                        continue;
                    }

                    rotten[nr][nc] = true;
                    demonQu.offer(new int[] { nr, nc, dCur[2] + 1 });
                }
            }

            // 수연이 이동
            for(int i = 0; i < 4; i++){
                int nr = cur[0] + dr[i];
                int nc = cur[1] + dc[i];

                if(nr < 0 || nr >= N || nc < 0 || nc >= M){
                    continue;
                }

                if(visited[nr][nc]){
                    continue;
                }

                if(rotten[nr][nc] || map[nr].charAt(nc) == 'X'){
                    continue;
                }

                if(map[nr].charAt(nc) == 'D'){
                    return String.valueOf(cur[2] + 1);
                }

                visited[nr][nc] = true;
                qu.offer(new int[] { nr, nc, cur[2] + 1 });
            }
        }

        return "GAME OVER";
    }
}
