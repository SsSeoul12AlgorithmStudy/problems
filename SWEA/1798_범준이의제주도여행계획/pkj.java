import java.io.*;
import java.util.*;
 
public class Solution {
    static int N;
    static int M;
    static int airport;
    static int[][] distance;
    static Node[] nodes;
    static boolean[] visited;
    static int[][] memoRate;
    static int[][] memoTime;
    static int maxRate;
    static ArrayList<Integer> bestRoute; //최적 경로
    static ArrayList<Integer> currentRoute;
 
    //노드 정보
    static class Node {
        int id; //노드 번호
        char type; //A: 공항, P: 핫플, H: 호텔
        int playTime; //관람 시간
        int rate; //만족도
 
        public Node(int id, char type, int playTime, int rate){
            this.id = id;
            this.type = type;
            this.playTime = playTime;
            this.rate = rate;
        }
    }
 
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
 
        for(int test_case = 1; test_case <= T; test_case++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken()); //노드 수
            M = Integer.parseInt(st.nextToken()); //여행 일수
            airport = 0; //공항 노드
            distance = new int[N+1][N+1]; //노드 간 소요 시간
            nodes = new Node[N+1]; //노드 정보
            visited = new boolean[N+1]; //노드 방문 여부
            memoRate = new int[M+1][N+1]; //일자별 최고 만족도
            memoTime = new int[M+1][N+1]; //노드별 최저 시간
 
            maxRate = 0; //최대 만족도
            bestRoute = new ArrayList<>(); //최적 경로
            currentRoute = new ArrayList<>(); //현재 경로
 
            for(int i = 1; i < N; i++){
                st = new StringTokenizer(br.readLine());
                for(int j = i + 1; j <= N; j++){
                    int time = Integer.parseInt(st.nextToken());
                    distance[i][j] = time;
                    distance[j][i] = time;
                }
            }
 
            for(int i = 1; i <= N; i++){
                st = new StringTokenizer(br.readLine());
                char type = st.nextToken().charAt(0);
 
                //핫플이라면
                if(type == 'P'){
                    int playTime = Integer.parseInt(st.nextToken());
                    int rate = Integer.parseInt(st.nextToken());
                    nodes[i] = new Node(i, type, playTime, rate);
                }
                else {
                    if(type == 'A') airport = i;
                    nodes[i] = new Node(i, type, 0, 0);
                }
            }
 
            makePlan(1, airport, 540, 0); //날짜, 최근 노드, 시간, 만족도
            if(maxRate == 0) System.out.println("#" + test_case + " " + 0);
            else System.out.println("#" + test_case + " " + maxRate + makeAnswer());
        }
    }
 
    static String makeAnswer(){
        StringBuilder sb = new StringBuilder(maxRate);
        for(int i = 0; i < bestRoute.size(); i++){
            sb.append(" " + bestRoute.get(i));
        }
 
        return sb.toString();
    }
 
    static void makePlan(int day, int current, int remainTime, int rate){
        //만족도가 낮거나 남은시간이 적다면 탐색 종료
        if(memoRate[day][current] >= rate && memoTime[day][current] >= remainTime){
            return;
        }
 
        memoRate[day][current] = rate; //만족도 갱신
        memoTime[day][current] = remainTime; //시간 갱신
 
        //관광지 탐색
        for(int i = 1; i <= N; i++){
            if(nodes[i].type == 'P' && !visited[i]){
                int nT = distance[current][i] + nodes[i].playTime; //소요시간
 
                //현재 루트에 저장
                if(remainTime >= nT){
                    visited[i] = true;
                    currentRoute.add(i);
 
                    //다음 노드 탐색
                    makePlan(day, i, remainTime - nT, rate + nodes[i].rate);
                    currentRoute.remove(currentRoute.size()-1); //백트래킹
                    visited[i] = false;
                }
            }
        }
 
        //마지막날이 아니면 호텔로 이동
        if(day < M) {
            for(int i = 1; i <= N; i++){
                if(nodes[i].type == 'H'){
                    int tT = distance[current][i];
                    if(remainTime >= tT){
                        currentRoute.add(i);
 
                        //내일로 넘어가기
                        makePlan(day+1, i, 540, rate);
                        currentRoute.remove(currentRoute.size()-1);
                    }
                }
            }
        }
        else {
            int tT = distance[current][airport];
 
            if(remainTime >= tT){
                if(rate > maxRate){
                    maxRate = rate;
                    currentRoute.add(airport);
                    bestRoute = new ArrayList<>(currentRoute);
                    currentRoute.remove(currentRoute.size() - 1);
                }
            }
        }
    }
}
