/*
백트래킹으로 모든 경우의 수? -> 각 경우를 모두 구하는 방법 : 3 * 2^9 = 1500정도
각 경우에 대해서 시뮬레이션 진행 : BFS? 

아니면 도달가능한 모든 노드에 대해서 접근하는 방법을 구하고 
2: A, 3: A, 4: C, 5: B, 6: BA, 7: BA, 8: AC, 9:AB, 10: ABA
각 경우에 대해서 ABA 뭐 이렇게 있으면 이걸로 도달 가능한 노드의 개수 세기.

접근 방법 구하는 건 flood-fill로 한번에 구할 수 있을 듯.

실수:
AACC 이런 경우에 A하나로 AA를 뚫을 수 있게 처리해줘야함.
*/

import java.util.*;

class Info {
    int node;
    char pipe;
    Info(int node, char pipe) {
        this.node = node;
        this.pipe = pipe;
    }
}

class Solution {
    int n, k;
    int answer;
    String[] flow;
    public int solution(int n, int infection, int[][] edges, int k) {
        this.n = n;
        this.k = k;
        // 0. 각 노드 별로 edge 정보 추가
        List<Info>[] e = new List[n+1];
        for (int i = 1; i <= n; i++)    e[i] = new ArrayList<>();
        
        for (int[] edge : edges) {
            int a = edge[0];
            int b = edge[1];
            char p = (edge[2] == 1 ? 'A' : (edge[2] == 2 ? 'B' : 'C'));
            e[a].add(new Info(b, p));
            e[b].add(new Info(a, p));
        }
        
        // 1. 근원지로부터 flood-fill해서 각 노드별로 도달 공식 구하기.
        flow = new String[n+1];
        
        Queue<Integer> Q = new ArrayDeque<>();
        boolean[] vis = new boolean[n+1];
        
        vis[infection] = true;
        Q.add(infection);
        flow[infection] = "";
        
        while (!Q.isEmpty()) {
            int cur = Q.poll();
            for (Info i : e[cur]) {
                if (vis[i.node])    continue;
                flow[i.node] = flow[cur] + i.pipe;
                Q.add(i.node);
                vis[i.node] = true;
            }
        }
        
        // 2. 백트래킹 : answer 업데이트
        arr = new char[k];
        DFS(0);
        
        return answer;
    }
    
    char[] arr;
    final char[] cand = {'A', 'B', 'C'};
    void DFS(int p) {
        if (p == k) {
            int result = 0;
            for (int i = 1; i <= n; i++) {
               if (canInfect(i))    result++;
            }
            
            if (result > answer) answer = Math.max(answer, result);
            
            return;
        }
        
        for (char c : cand) {
            if (p != 0 && arr[p-1] == c)    continue;
            arr[p] = c;
            DFS(p+1);
        }
    }
    
    // 주어진 arr로 flow[i]에 도달 가능한지 검사.
    boolean canInfect(int p) {
        char[] f = flow[p].toCharArray();
        
        int l = 0;
        
        for (int i = 0; i < k; i++) {
            if (l >= f.length)  break;
            while (l < f.length && arr[i] == f[l]) {
                l++;
            }
        }
        if (l >= f.length)  return true;
        return false;
    }
}
