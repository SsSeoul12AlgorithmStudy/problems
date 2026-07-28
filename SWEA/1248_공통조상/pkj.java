import java.io.*;
import java.util.*;

class Solution
{
    static ArrayList<Integer>[] graph;
    static ArrayList<Integer>[] childs;
    static int[] parents;
    
	public static void main(String args[]) throws Exception
	{BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for(int test_case = 1; test_case <= T; test_case++){
            int parentNode = 0;
            int subTree = 0;
            int V = 0;
            int E = 0;
            int nOne = 0;
            int nTwo = 0;

            StringTokenizer st = new StringTokenizer(br.readLine());
            V = Integer.parseInt(st.nextToken());
            E = Integer.parseInt(st.nextToken());
            nOne = Integer.parseInt(st.nextToken());
            nTwo = Integer.parseInt(st.nextToken());
            parents = new int[V+1]; //자식의 부모 노드 저장
            graph = new ArrayList[V+1]; //자식&손자 노드 저장
            childs = new ArrayList[V+1]; //직계자식 노드 저장

            for(int i = 1; i <= V; i++){
                graph[i] = new ArrayList<>();
                childs[i] = new ArrayList<>();
            }

            st = new StringTokenizer(br.readLine());
            while(st.hasMoreTokens()){
                int p = Integer.parseInt(st.nextToken());
                int c = Integer.parseInt(st.nextToken());
                parents[c] = p;
                childs[p].add(c);
            }

            updateParent(1); //자식&손자 노드 모두 저장
            parentNode = findParent(nOne, nTwo);
            subTree = findSubTree(parentNode);

            System.out.println("#" + test_case + " " + parentNode + " " + subTree);
        }
    }

    //서브트리 찾기 메소드
    static int findSubTree(int parentNode){
      
        //그래프의 크기 + 부모 자신 더해서 리턴
        int result = graph[parentNode].size() + 1;
        return result;
    }

    //공통부모 찾기 메소드
    static int findParent(int nOne, int nTwo){
        int result = 1;
        HashSet<Integer> hs = new HashSet<>();

        //우선 노드 하나의 부모 모두 셋에 넣기
        int current = nOne;
        while(current > 0){
            hs.add(current);
            current = parents[current];
        }

        //겹치는 부모 찾으면 탐색 종료
        current = nTwo;
        while(current > 0){
            if(hs.contains(current)) {
                return current;
            }
            current = parents[current];
        }

        return result;
    }

    //부모에 따른 모든 자식&손자 노드들 저장 메소드
    static void updateParent(int current){
        for(int next : childs[current]){
            updateParent(next);
            graph[current].add(next);
            graph[current].addAll(graph[next]);
        }
    }
}
