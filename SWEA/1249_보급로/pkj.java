import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Solution
{
  static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static class Pair{
		int x, y, time;
		
		Pair(int x, int y, int time){
			this.x = x;
			this.y = y;
			this.time = time; //해당위치까지 소요시간
		}
	}
    
	public static void main(String args[]) throws Exception
	{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
        
		for(int test_case = 1; test_case <= T; test_case++)
		{
      int answer = 0;
			int N = Integer.parseInt(br.readLine());
			int[][] map = new int[N][N];
			int[][] vT = new int[N][N]; //해당 위치 최소 시간 저장
			boolean[][] visited = new boolean[N][N]; //방문 확인
			
			for(int i = 0; i < N; i++) {
				String s = br.readLine();
				
				for(int j = 0; j < N; j++) {
					char c = s.charAt(j);
					map[i][j] = Character.getNumericValue(c);
				}
			}
			
			answer = repairRoad(map, vT, visited);
			System.out.println("#" + test_case + " " + answer);
		}
	}

  //최소 소요 시간 찾는 메소드
    static int repairRoad(int[][] map, int[][] vT, boolean[][] visited) {
		int result = 0;
		Queue<Pair> q = new LinkedList<>();
		q.add(new Pair(0,0,map[0][0]));
		visited[0][0] = true;
		
		while(!q.isEmpty()) {
			Pair p = q.poll();
			int nowX = p.x;
			int nowY = p.y;
			int nowT = p.time;
			
			for(int d = 0; d < 4; d++) {
				int nextX = nowX + dx[d];
				int nextY = nowY + dy[d];
				int nextT = 0;
				
				if(nextX >= 0 && nextX < map.length && nextY >= 0 && nextY < map.length) {
					nextT = nowT + map[nextX][nextY];

          //방문을 했더라도 현재 시도가 최소 시간이면 갱신
					if(!visited[nextX][nextY] || (visited[nextX][nextY] && (vT[nextX][nextY] > nextT))) {
						vT[nextX][nextY] = nextT;
						q.add(new Pair(nextX, nextY, nextT));
						visited[nextX][nextY] = true;
					}
				}
			}
		}
		
		result = vT[map.length-1][map.length-1];
		return result;
	}
}
