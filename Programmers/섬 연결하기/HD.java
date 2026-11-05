/*

모든 섬이 서로 통행 가능하도록 만들 수 있는 다리건설 최소비용

n <= 100 자연수
costs.length = 설치 가능한 건설비용

최소간선트리 그거 같은데 풀이법이 기억안나네.

int[] p = new int[n];
for(int i = 0; i < n; i++) p[i] = i;

건설 비용이 작은순으로 뽑아야하는거같음.

PrioriyQueue 사용해서 일단 구현
costs는 최대 100 * 99 / 2 = 50 * 100 - 50 => 4,950
costs 한번 도는건 시간초과 안날듯

costs 한번 돌면서 p 갱신
a,b가 서로 같은 부모를 가지면 무시
다른 부모를 가지면 연결

++ 유니온파인드는 까먹어서 다시 한번 찾아봄

*/

import java.util.PriorityQueue;

class Solution {
    
    int result, P[];
    PriorityQueue<Bridge> bridges;
    
    public int solution(int n, int[][] costs) {
        
        init(n, costs);
        
        while(!bridges.isEmpty()) {
            Bridge now = bridges.poll();
            
            // 이미 연결된 섬
            if(isSame(now.a, now.b)) continue;
            
            // 연결되지 않은 섬
            union(now.a, now.b);
            result += now.cost;
        }
        
        return result;
    }
    
    private void init(int n, int[][] costs) {
        
        // 각 섬의 루트 초기화
        P = new int[n];
        for(int i = 0; i < n; i++) P[i] = i;
        
        // 적은 비용부터 건설하기
        bridges = new PriorityQueue<>();
        for(int[] cost : costs) bridges.offer(new Bridge(cost));
        
        // 최소 비용
        result = 0;
    }
    
    private int find(int x) {
        
        if(P[x] == x) return x;
        
        return P[x] = find(P[x]);
    }
    
    private void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) return;

        P[rootB] = rootA;
    }
    
    private boolean isSame(int a, int b) {
        return find(a) == find(b);
    }
    
    class Bridge implements Comparable<Bridge> {
        
        int a, b, cost;
        
        public Bridge(int[] cost) {
            a = cost[0];
            b = cost[1];
            this.cost = cost[2];
        }
        
        @Override
        public int compareTo(Bridge other) {
            return Integer.compare(this.cost, other.cost);
        }
    }
}
