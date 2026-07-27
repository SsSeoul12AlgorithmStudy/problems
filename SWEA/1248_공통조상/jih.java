package org.example.year2026.july.week5.swea1248;

import java.io.*;
import java.util.*;

public class Solution {
    /*
    * 26-7-27 20:01 ~ 20:37
    *
    * 최소 공통 조상을 구하고, 해당 조상을 루트로하는 트리의 크기를 구하자
    *
    * ---
    *
    * MCA를 구하는 알고리즘은 떠오르지 않는다
    *
    * 두 정점에서 루트 방향으로 dfs를 수행하면 최소 공통조상을 알 수 있지 않을까?
    * 트리의 크기는 dfs로 서브트리의 노드의 수를 계산하면 된다
    * */
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private int V, E, v, u;
    private List<List<Integer>> graph;
    private List<List<Integer>> reverseGraph;
    private boolean[] visitedAncestor;
    private boolean[] visited;
    int mca = -1;
    int subtreeSize = 0;

    public static void main(String[] args) throws IOException {
        int T = Integer.parseInt(br.readLine());
        for(int test = 1; test <= T; test++) {
            int answer = 0;
            Solution s = new Solution();
            s.init();
            s.solve();
            System.out.println("#" + test + " " + s.mca + " " + s.subtreeSize);
        }
    }

    private void init() throws IOException{
        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);
        V = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());
        v = Integer.parseInt(st.nextToken());
        u = Integer.parseInt(st.nextToken());

        graph = new ArrayList<>();
        reverseGraph = new ArrayList<>();
        for(int i = 0; i <= V; i++){
            graph.add(new ArrayList<>());
            reverseGraph.add(new ArrayList<>());
        }

        line = br.readLine();
        st = new StringTokenizer(line);
        for(int i = 1; i <= E; i++){
            int parent = Integer.parseInt(st.nextToken());
            int child = Integer.parseInt(st.nextToken());
            graph.get(parent).add(child);
            reverseGraph.get(child).add(parent);
        }

        visitedAncestor = new boolean[V + 1];

        visited = new boolean[V + 1];
    }

    private void solve(){
        moveToRoot(u);
        moveToRoot(v);
        dfs(mca);
    }

    private void moveToRoot(int cur){
        if(visitedAncestor[cur]){
            if(mca == -1){
                mca = cur;
            }
            return;
        }

        visitedAncestor[cur] = true;

        for(int next : reverseGraph.get(cur)){
            moveToRoot(next);
        }
    }

    private void dfs(int cur){
        if(visited[cur]){
            return;
        }

        visited[cur] = true;
        subtreeSize++;

        for(int next : graph.get(cur)){
            dfs(next);
        }
    }
}
