/*
끄적여놨던 거 저장 안해서 날아감.. 근데 중요한 건 없었음..

못 풀겠는디;

1등 아님 꼴등이 안 나오고 순위를 확정할 수 있냐? 없을 것 같은디
내 앞에 몇 명이 있고, 내 뒤에 몇 명이 있는지 확인 => Set에 무작정 담기?
노드 별로 앞으로 한 번, 뒤로 한 번 진행 O(100 * (100))

뒤에 있는 녀석들은 root에서부터 진행
자식의 뒤의 있는 녀석들 Set.add(내 뒤의 있는 녀석들 + 나)
Queue에 root부터 넣으면서 진행

앞에 있는 녀석들은 leaf에서부터 진행
부모의 앞에 있는 녀석들 Set.add(내 앞에 있는 녀석들 + 나)
마찬가지로 Queue에 leaf를 넣으면서 진행
(방문 표시로 중복 방지)

[a, b]에서 a에 한번도 나타난 적 없다면 => leaf
          b에 한번도 나타난 적 없다면 => root

구현하는데 너무 더러워서 이건 아닌 것 같네요 ㅎ;

AI 코드 피드백 궁금해서 써봤다가 스포당함;;
각 노드 별로 앞(이긴사람) + 뒤(진사람) 수가 n-1이면 순위 확정
수 탐색은 BFS로
*/
import java.util.*;

class Solution {
    public int solution(int n, int[][] results) {
        int answer = 0;
        
        List<Integer>[] parent = new List[n+1];
        List<Integer>[] child = new List[n+1];

        for (int i = 1; i <= n; i++) {
            parent[i] = new ArrayList<>();
            child[i] = new ArrayList<>();
        }
        
        for (int[] r : results) {
            int a = r[0];
            int b = r[1];
            parent[b].add(a);
            child[a].add(b);
        }
        
        for (int i = 1; i <= n; i++) {
            // 앞 노드 구하기(날 이긴 사람)
            Queue<Integer> Q = new ArrayDeque<>();
            boolean[] vis = new boolean[n+1];
            int cnt = 0;
            
            Q.add(i);
            vis[i] = true;
            
            while (!Q.isEmpty()) {
                int cur = Q.poll();
                for (int p : parent[cur]) {
                    if (vis[p]) continue;
                    Q.add(p);
                    vis[p] = true;
                    cnt++;
                }
            }
            
            Arrays.fill(vis, false);
            
            Q.add(i);
            vis[i] = true;
            
            while (!Q.isEmpty()) {
                int cur = Q.poll();
                for (int c : child[cur]) {
                    if (vis[c]) continue;
                    Q.add(c);
                    vis[c] = true;
                    cnt++;
                }
            }
            
            if (cnt == n-1) {
                answer++;
            }
        }
         
        return answer;
    }
}
