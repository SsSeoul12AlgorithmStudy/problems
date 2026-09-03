import java.util.*;

class Solution {
    /*
    26-9-3 21:33 ~ 21:56
    
    경기결과를 통해서 순위를 정확하게 매길 수 있는 사람의 수를 구하자
    
    ===
    
    제한
    선수의 수 <= 100
    경기 결과의 수 <= 4500
    
    ===
    예제 분석
    
    4 > 3, 2
    3 > 2
    1 > 2
    2 > 5
    
    4 > 3 > 2 > 5 는 확실. 1은 모름
    2는 3사람에게 지고 1사람에게 이기므로 4등
    5는 4사람에게 지므로 5등
    
    ===
    
    순위가 확실하다는 것은 어떻게 알 수 있나?
    - 순위를 결정할 수 있는 사람인 경우는 자기보다 잘하는 사람과 못하는 사람의 합이 n - 1인 경우로 보임.
    - 나를 이기는 사람들과, 내가 이긴 사람들을 알아내는 방법
        -> 내가 패배하는 경우에 대한 그래프를 dfs해서 이기는 사람들을 구하고
        내가 승리하는 경우에 대한 그래프를 dfs해서 지는 사람들을 구한 후에
        그 사람들의 합이 서로 다른 n - 1명인지 확인하면 될 것 같다.
    
    */
    public int solution(int n, int[][] results) { 
        // 그래프 구성
        List<List<Integer>> losers = new ArrayList<>();
        List<List<Integer>> winners = new ArrayList<>();
        
        for(int i = 0; i <= n; i++){
            losers.add(new ArrayList<>());
            winners.add(new ArrayList<>());
        }
        
        for(int[] r : results){
            losers.get(r[0]).add(r[1]);
            winners.get(r[1]).add(r[0]);
        }
        
        // 각 선수에 대해서 dfs로 확정 가능 여부 판정
        int answer = 0;
        
        for(int player = 1; player <= n; player++){
            List<Integer> losings = bfs(player, losers, n);
            List<Integer> winnings = bfs(player, winners, n);
            
            Set<Integer> set = new HashSet<>();
            set.addAll(losings);
            set.addAll(winnings);
            
            if(set.size() == n - 1){
                answer++;
            }
        }
        
        return answer;
    }
    
    List<Integer> bfs(int start, List<List<Integer>> graph, int n){
        List<Integer> ret = new ArrayList<>();
        Queue<Integer> qu = new LinkedList<>();
        boolean[] visited = new boolean[n + 1];
        
        qu.offer(start);
        visited[start] = true;
        
        while(!qu.isEmpty()){
            int cur = qu.poll();
            
            for(int next : graph.get(cur)){
                if(visited[next]) continue;
                
                qu.offer(next);
                visited[next] = true;
                
                ret.add(next);
            }
        }
        
        return ret;
    }
}