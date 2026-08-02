import java.io.*;
import java.util.*;

public class Solution {
    /*
    * 26-8-2 11:33 ~
    *
    * =====
    * 파워셋을 구해야한다
    *
    * 재귀 or 비트마스킹?
    *
    * N <= 20이므로 비트마스킹으로 파워셋을 구해도 된다고 생각했으나
    * 시간 제한이 2초라서 시간초과가 발생한다
    *
    * 재귀가 맞다
    * */

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int N, M;
    List<Set<Integer>> nonMatching;
    int count;
    Set<Integer> selected;

    public static void main(String[] args) throws IOException {
        int T = Integer.parseInt(br.readLine());

        for(int test = 1; test <= T; test++){
            int answer = 0;
            Solution s = new Solution();
            s.init();
            answer = s.solve();
            System.out.println("#" + test + " " + answer);
        }
    }

    void init() throws IOException {
        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        nonMatching = new ArrayList<>();
        for(int i = 0; i <= N; i++){
            nonMatching.add(new HashSet<>());
        }

        for(int i = 1; i <= M; i++){
            line = br.readLine();
            st = new StringTokenizer(line);

            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            if(!nonMatching.get(a).contains(b)){
                nonMatching.get(a).add(b);
                nonMatching.get(b).add(a);
            }
        }

        selected = new HashSet<>();
    }

    int solve(){
        count = 1;
        for(int depth = 1; depth <= N; depth++){
            select(0, depth, 0);
        }
        return count;
    }

    void select(int depth, int maxDepth, int prevIngred){
        if(depth >= maxDepth){
            count++;
            return;
        }

        for(int ingred = prevIngred + 1; ingred <= N; ingred++){
            if(selected.contains(ingred)){
                continue;
            }
            if(isMatching(ingred)){
                selected.add(ingred);
                select(depth + 1, maxDepth, ingred);
                selected.remove(ingred);
            }
        }
    }

    boolean isMatching(int ingred){
        for(int nm : nonMatching.get(ingred)){
            if(selected.contains(nm)){
                return false;
            }
        }
        return true;
    }
}