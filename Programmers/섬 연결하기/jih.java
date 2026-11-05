import java.util.*;

class Solution {
    /*
    
    21:27 ~ 21:53
    
    n개의 섬을 이을 때 드는 최소 비용을 구하자
    
    간선은 많아봐야 5000개
    전형적인 MST 문제다

    크루스칼과 프림 둘다 시도해보자
    */

    static class Edge{
        int u, v, weight;

        public Edge(int u, int v, int weight){
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }

    private int[] parent;
    private int index = 0;
    private Map<Integer, Integer> vertexToIndex; // 문제에 섬 번호에 대한 조건이 없기 때문에, 정말 심보가 꼬인 테케가 있다면 섬 번호가 1부터 차례로 부여되지 않을 수도 있음

    public int solution(int n, int[][] costs) {
        int answer = 0;

        vertexToIndex = new HashMap<>();
        parent = new int[n];

        for(int i = 0; i < n; i++){
            parent[i] = i;
        }

        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> a.weight - b.weight);
        for(int i = 0; i < costs.length; i++){
            int u = getIndex(costs[i][0]);
            int v = getIndex(costs[i][1]);

            pq.offer(new Edge(u, v, costs[i][2]));
        }

        int count = 0;
        while(!pq.isEmpty() && count < n - 1){
            Edge edge = pq.poll();

            if(find(edge.u) != find(edge.v)){
                union(edge.u, edge.v);
                answer += edge.weight;
                count++;
            }
        }

        return answer;
    }

    private int getIndex(int ori){
        if(vertexToIndex.containsKey(ori)){
            return vertexToIndex.get(ori);
        }
        vertexToIndex.put(ori, index);
        return index++;
    }

    private int find(int u){
        if(parent[u] != u){
            return parent[u] = find(parent[u]);
        }
        return u;
    }

    private void union(int u, int v){
        int up = find(u);
        int vp = find(v);
        parent[up] = vp;
    }
}