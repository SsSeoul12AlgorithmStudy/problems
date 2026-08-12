import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
 
public class Solution {
    static int[] dx = {1, -1, 0, 0};
    static int[] dy = {0, 0, 1, -1};
    static boolean[][] visited;
    static HashMap<Integer, char[][]> devils; //시간에 따른 맵의 변화 
    static int N, M;
     
    public static class Pair {
        int x, y, cnt;
         
        public Pair(int x, int y, int cnt) {
            this.x = x;
            this.y = y;
            this.cnt = cnt;
        }
    }
     
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
         
        for(int test_case = 1; test_case <= T; test_case++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            char[][] map = new char[N][M];
            visited = new boolean[N][M];
            devils = new HashMap<>();
            int answer = 0;
            int sX = 0, sY = 0;
                 
            for(int i = 0; i < N; i++) {
                String tmp = br.readLine();
                for(int j = 0; j < M; j++) {
                    map[i][j] = tmp.charAt(j);
                    if(map[i][j] == 'X' || map[i][j] == '*') 
                        visited[i][j] = true;
                    if(map[i][j] == 'S') {
                        sX = i; 
                        sY = j;
                    }
                }
            }
            devils.put(0, map);
             
            answer = findGoddess(sX, sY);
             
            // 도달 불가능하면 GAME OVER, 가능하면 최단 시간 출력
            if (answer == -1) {
                System.out.println("#" + test_case + " GAME OVER");
            } else {
                System.out.println("#" + test_case + " " + answer);
            }
        }
    }
    static char[][] spreadDevil(int c) {
        // 이미 c + 1초 시점의 맵이 계산되어 있다면 꺼내 반환
        if (devils.containsKey(c + 1)) {
            return devils.get(c + 1);
        }
         
        char[][] prevMap = devils.get(c);
        char[][] nextMap = new char[N][M];
         
        // 깊은 복사
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                nextMap[i][j] = prevMap[i][j];
            }
        }
         
        // 악마 위치를 c + 1초 시점 확산 적용
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (prevMap[i][j] == '*') {
                    for (int d = 0; d < 4; d++) {
                        int nx = i + dx[d];
                        int ny = j + dy[d];
                         
                        if (nx >= 0 && nx < N && ny >= 0 && ny < M) {
                            // 악마 확장
                            if (prevMap[nx][ny] == '.' || prevMap[nx][ny] == 'S') {
                                nextMap[nx][ny] = '*';
                            }
                        }
                    }
                }
            }
        }
         
        devils.put(c + 1, nextMap);
        return nextMap;
    }
     
    static int findGoddess(int sX, int sY) {
        Queue<Pair> q = new LinkedList<>();
        q.add(new Pair(sX,sY,0));
        visited[sX][sY] = true;
         
        while(!q.isEmpty()) {
            Pair now = q.poll();
            int nowX = now.x;
            int nowY = now.y;
            int nowCnt = now.cnt;
             
            // (현재 + 1)초 시점에 악마가 퍼진 맵 구하기
            char[][] nextMap = spreadDevil(nowCnt);
             
            for (int i = 0; i < 4; i++) {
                int nextX = nowX + dx[i];
                int nextY = nowY + dy[i];
                 
                if (nextX >= 0 && nextX < N && nextY >= 0 && nextY < M) {
                    // 여신님에 도달한 경우
                    if (nextMap[nextX][nextY] == 'D') {
                        return nowCnt + 1;
                    }
                     
                    // 방문하지 않았고 다음 초에 악마가 퍼지지 않은 빈 공간('.')인 경우 이동
                    if (!visited[nextX][nextY] && nextMap[nextX][nextY] == '.') {
                        visited[nextX][nextY] = true;
                        q.add(new Pair(nextX, nextY, nowCnt + 1));
                    }
                }
            }
        }
         
        return -1; // 여신님에게 도달할 수 없는 경우
    }
}
