import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;
/*

복서간에 정보를 다음과 같이 정의 후 연결 리스트 타입의 배열로 정보 저장

n번 선수 별 
- node - 상대 선수 번호
- isChild - 0(winner), 1(loser)

1 ~ n번의 선수별로 시작부터 직접 경기한 선수들 정보를 Q에 넣음. - 방향(승패여부)을 같이 넣어줘야함.

이후부터
- 조상이였으면 그의 조상을 
- 자손이였으면 그의 자손을 

확인

*/
class Solution {
    
    ArrayList<Boxer>[] bc;
    
    public int solution(int n, int[][] results) {
        
        bc = new ArrayList[n + 1];
        for(int i = 1; i <= n; i++) {
            bc[i] = new ArrayList<>();
        }
        
        for(int[] result : results) {
            int winner = result[0];
            int loser = result[1];
            
            bc[winner].add(new Boxer(loser, 1));
            bc[loser].add(new Boxer(winner, 0));
        }
        
        int boxerNum = 1;
        int answer = 0;
        while(boxerNum <= n) {
            if(rank(boxerNum++)) answer++;
        }
        
        return answer;
    }
    
    private boolean rank(int bn) {
        
        int N = bc.length - 1;
        
        int cnt = 1;
        boolean[] visited = new boolean[N + 1];
        visited[bn] = true;
        
        Queue<Boxer> q = new ArrayDeque<>();
        for(Boxer b : bc[bn]) {
            q.offer(new Boxer(b.node, b.isChild));
            visited[b.node] = true;
        }
        
        while(!q.isEmpty()) {
            
            Boxer nb = q.poll();
            cnt++;
            
            for(Boxer b : bc[nb.node]) {
                
                if(visited[b.node]) continue;
                
                if(b.isChild != nb.isChild) continue;
                
                visited[b.node] = true;
                q.offer(b);
            }
        }
        
        if(N == cnt) return true;
        
        return false;
    }
    
    class Boxer {
        
        int node, isChild;
        
        public Boxer(int node, int isChild) {
            this.node = node;
            this.isChild = isChild;
        }
    }
}