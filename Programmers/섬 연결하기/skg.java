/*

MSP 문제인 것 같다.
양방향 그래프.
우선순위 큐를 활용한 프림 알고리즘으로 구현해보자.



*/

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

class Solution {
    public int solution(int n, int[][] costs) {
        int answer = 0;
        
        List<int[]>[] edges = new List[n];
        
        for (int i = 0; i < edges.length; i++) {
            edges[i] = new ArrayList<>();
        }
        
        for (int[] cost : costs) {
            int aNode = cost[0];
            int bNode = cost[1];
            int dist = cost[2];
            edges[aNode].add(new int[] {bNode, dist});
            edges[bNode].add(new int[] {aNode, dist});
        }
        
        PriorityQueue<int[]> PQ = new PriorityQueue<>(
        (a, b) -> Integer.compare(a[1], b[1]));
        boolean[] visit = new boolean[n];
        
        PQ.add(new int[] {0, 0});
        
        while (!PQ.isEmpty()) {
            int[] cur = PQ.poll();
            int node = cur[0];
            int cost = cur[1];
            if (visit[node]) {
                continue;
            }
            answer += cost;
            visit[node] = true;

            for (int[] edge : edges[node]) {
                int adj = edge[0];
                int dist = edge[1];
                if (visit[adj]) continue;
                PQ.add(new int[] {adj, dist});
            }
            
            
        }
        
        
        return answer;
    }
}
