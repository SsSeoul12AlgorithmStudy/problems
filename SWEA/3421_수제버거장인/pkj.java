import java.util.*;
import java.io.*;
 
public class Solution {
    static int answer;
     
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
         
        for(int test_case = 1; test_case <= T; test_case++) {
            StringTokenizer st;
            st = new StringTokenizer(br.readLine());
             
            answer = 0; //메뉴 개수
            int N = Integer.parseInt(st.nextToken()); //재료 개수
            int M = Integer.parseInt(st.nextToken()); //상극 개수
            ArrayList<Integer> selected = new ArrayList<>(); //현재 선택된 재료들
            ArrayList<ArrayList<Integer>> dislike = new ArrayList<>(); //섞이면 안되는 재료
            for(int i = 0; i <= N; i++) {
                dislike.add(new ArrayList<>());
            }
             
            for(int i = 0; i < M; i++) {
                st = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                 
                dislike.get(a).add(b);
                dislike.get(b).add(a);
            }
             
            makeMenu(1, N, M, dislike, selected); //재귀
            System.out.println("#" + test_case + " " + answer);
        }
    }
 
    static void makeMenu(int now, int N, int M, ArrayList<ArrayList<Integer>> dislike, ArrayList<Integer> selected ) {
        //모든 재료 탐색 완료 시 return 
        if(now > N) {
            answer++; //메뉴추가
            return;
        }
         
        //현재 재료 건너뛰기
        makeMenu(now+1, N, M, dislike, selected);
         
        //현재 재료 넣을지 말지 판단
        if(checkCom(now, dislike, selected)) {
            selected.add(now); //없으면 현재 재료 집어넣기
            makeMenu(now+1, N, M, dislike, selected); //다음재료 탐색
            selected.remove(selected.size()-1); //백트래킹
        }
    }
     
    //상극인 재료 있는지 판단 
    static boolean checkCom(int now, ArrayList<ArrayList<Integer>> dislike, ArrayList<Integer> selected) {
        ArrayList<Integer> arr = dislike.get(now); //현재 재료의 상극 재료들
         
        for(int d : arr) {
            if(selected.contains(d)) {
                return false;
            }
        }
         
        return true;
    }
}
