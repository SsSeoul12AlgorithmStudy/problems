import java.util.*;

class Solution {
    /*
    26/10/24 14:53 ~ 
    
    파이프를 최대 k번 열고 닫을 때, 감염시킬 수 있는 배양체 개수의 최댓값을 구하자
    
    ---
    
    감염된 배양체와 파이프로 이동할 수 있다면 감염된다
    
    k <= 10
    
    ---

    완전탐색이 가능할까?
    - k <= 10이고, 매번 열 수 있는 파이프는 3가지
    - 3^k의 파이프를 열고 닫는 경우의 수를 구하고, 각 경우마다 배양체가 몇개나 감염되는지 구해서 최댓값을 구할 수 있다
    - k번의 bfs를 수행해서 감염체가 도달할 수 있는 배양체를 감염시키면, 최종 감염체의 수를 구할 수 있다.
    - 시간복잡도는 3^k * k * (n + n - 1) <= 3^10 * 10 * 200 < 100만 * 10 * 200 
    - 아슬아슬하지만 가능할 것 같다

    */
    
    static class Node{
        int vertex, pipe;
        
        public Node(int vertex, int pipe){
            this.vertex = vertex;
            this.pipe = pipe;
        }
    }
    
    static int answer;
    static int n, infection, k;
    static List<List<Node>> graph;
    static int[] selected;
    
    public int solution(int n, int infection, int[][] edges, int k) {
        answer = 0;
        
        this.n = n;
        this.infection = infection;
        this.k = k;
        
        selected = new int[k];
        
        graph = new ArrayList<>();
        for(int i = 0; i <= n; i++){
            graph.add(new ArrayList<>());
        }
        
        for(int i = 0; i < n - 1; i++){
            int s = edges[i][0];
            int e = edges[i][1];
            int p = edges[i][2];
            
            graph.get(s).add(new Node(e, p));
            graph.get(e).add(new Node(s, p));
        }
        
        findMaxInfection(0);
        
        return answer;
    }
    
    private void findMaxInfection(int depth){
        if(depth >= k){
            int infected = infect();
            answer = Math.max(answer, infected);
            return;
        }   
        
        for(int pipe = 1; pipe <= 3; pipe++){
            selected[depth] = pipe;
            findMaxInfection(depth + 1);
            selected[depth] = 0;
        }
    }
    
    private int infect(){
        // k번 파이프를 열고 닫으면서 감염시킨다
        int[] infected = new int[n + 1];
        
        infected[infection] = 1;
        
        // System.out.println("infect() : " + Arrays.toString(selected));
        
        for(int i = 0; i < k; i++){
            boolean[] visited = new boolean[n + 1];
            
            for(int v = 1; v <= n; v++){
                if(infected[v] == 1 && !visited[v]){
                    dfs(v, selected[i], visited);  
                } 
            }
            
            for(int v = 1; v <= n; v++){
                if(visited[v]){
                    infected[v] = 1;
                }
            }
        }
        
        // System.out.println("infected = " + Arrays.toString(infected));
        
        // 감염된 총 개수를 구한다
        int ret = 0;
        for(int i = 1; i <= n; i++){
            if(infected[i] == 1){
                ret++;
            }
        }
        
        // System.out.println("ret = " + ret);
        
        return ret;
    }
    
    private void dfs(int cur, int openPipe, boolean[] visited){
        if(visited[cur]){
            return;   
        }
        
        visited[cur] = true;
        
        for(Node next : graph.get(cur)){
            if(visited[next.vertex]) continue;
            if(next.pipe != openPipe) continue;
      
            dfs(next.vertex, openPipe, visited);
        }
    }
}