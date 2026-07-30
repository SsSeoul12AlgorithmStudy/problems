import java.util.*;

class Solution {
    /*
    26-7-30 17:34 ~ 
    
    ===
    
    한글자씩 차이나는 단어를 정점으로 보고 엣지로 연결하는 그래프를 만들고 BFS를 수행하면 된다
    */
    public int solution(String begin, String target, String[] words) {        
        List<String> nodes = new ArrayList<>();
        
        nodes.add(begin);
        for(int i = 0; i < words.length; i++){
            nodes.add(words[i]);
        }
        
        // words에 target이 없으면 0 리턴
        if(!nodes.contains(target)){
            return 0;
        }
        
        // begin과 words를 포함해서 그래프를 생성
        List<List<Integer>> graph = new ArrayList<>();
        for(int i = 0; i <= nodes.size(); i++){
            graph.add(new ArrayList<>());
        }
        
        int end = -1;
        
        for(int i = 0; i < nodes.size(); i++){
            if(target.equals(nodes.get(i))){
                end = i;
            }
            for(int j = i + 1; j < nodes.size(); j++){
                if(canConvert(nodes.get(i), nodes.get(j))){
                    graph.get(i).add(j);
                    graph.get(j).add(i);
                }
            }
        }
        
        // bfs로 최단거리 구하기
        int minDist = Integer.MAX_VALUE;
        
        Queue<int []> qu = new LinkedList<>();
        boolean[] visited = new boolean[nodes.size()];
        
        qu.offer(new int[] { 0, 0 });
        visited[0] = true;
        
        while(!qu.isEmpty()){
            int[] cur = qu.poll();
            
            for(int next : graph.get(cur[0])){
                if(visited[next]){
                    continue;
                }
                
                if(next == end){
                    minDist = cur[1] + 1;
                    break;
                }
                
                qu.offer(new int[] { next, cur[1] + 1 });
                visited[next] = true;
            }
        }
        
        return minDist;
    }
    
    private boolean canConvert(String a, String b){
        int diffCount = 0;
        for(int i = 0; i < a.length(); i++){
            if(a.charAt(i) != b.charAt(i)){
                diffCount++;
            }
        }
        return diffCount == 1;
    }
}