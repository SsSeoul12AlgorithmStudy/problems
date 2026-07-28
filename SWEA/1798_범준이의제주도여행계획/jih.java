package org.example.year2026.july.week5.swea1798;

import java.io.*;
import java.util.*;

public class Solution {
    /*
    * 26-7-28 20:10 ~
    *
    * 만족도가 최대가 될 때, 만족도와 경로를 구하자
    * */
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int N, M;
    int airport;
    List<Integer> hotels;
    List<Integer> sites;
    int[][] distances;
    int[] siteStayTimes;
    int[] siteScores;

    boolean[] visited;
    int maxScore;
    List<Integer> course;
    List<Integer> maxCourse;

    public static void main(String[] args) throws IOException{
        int T = Integer.parseInt(br.readLine());
        for(int test = 1; test <= T; test++){
            Solution s = new Solution();
            s.init();
            s.solve();
            System.out.println("#" + test + " " + s.getScore() + " " + s.getCourse());
        }
    }

    private void init() throws IOException {
        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        distances = new int[N + 1][N + 1];
        for(int i = 1; i <= N - 1; i++){
            line = br.readLine();
            st = new StringTokenizer(line);
            for(int j = i + 1; j <= N; j++){
                int time = Integer.parseInt(st.nextToken());
                distances[i][j] = time;
                distances[j][i] = time;
            }
        }

        hotels = new ArrayList<>();
        sites = new ArrayList<>();
        siteStayTimes = new int[N + 1];
        siteScores = new int[N + 1];

        for(int i = 1; i <= N; i++){
            line = br.readLine();
            st = new StringTokenizer(line);
            String command = st.nextToken();
            if("A".equals(command)){
                airport = i;
                continue;
            }
            if("H".equals(command)){
                hotels.add(i);
                continue;
            }
            if("P".equals(command)){
                sites.add(i);
                siteStayTimes[i] = Integer.parseInt(st.nextToken());
                siteScores[i] = Integer.parseInt(st.nextToken());
            }
        }

        visited = new boolean[N + 1];

        course = new ArrayList<>();

        maxCourse = new ArrayList<>();
    }

    private void solve(){
        travel(airport, 1, 9 * 60, 0);
    }

    private void travel(int cur, int day, int remainMinute, int score) {
        if (day > M) {
            if(course.get(course.size() - 1) != airport){
                return;
            }
            if (score > maxScore) {
                maxScore = score;
                maxCourse = new ArrayList<>(course);
            }
            return;
        }

        if(remainMinute <= 0){
            return;
        }

        for(int next : sites){
            if(visited[next]) {
                continue;
            }
            if(remainMinute < distances[cur][next] + siteStayTimes[next]){
                continue;
            }
            visited[next] = true;
            course.add(next);
            travel(next, day, remainMinute - distances[cur][next] - siteStayTimes[next], score + siteScores[next]);
            course.remove(course.size() - 1);
            visited[next] = false;
        }

        if(day == M){
            if(remainMinute < distances[cur][airport]){
                return;
            }
            course.add(airport);
            travel(airport, day + 1, 9 * 60, score);
            course.remove(course.size() - 1);
            return;
        }

        for(int next : hotels) {
            if (remainMinute < distances[cur][next]) {
                continue;
            }
            course.add(next);
            travel(next, day + 1, 9 * 60, score);
            course.remove(course.size() - 1);
        }
    }

    private int getScore(){
        return maxScore;
    }

    private String getCourse(){
        StringBuilder sb = new StringBuilder();
        for(int c : maxCourse){
            sb.append(c + " ");
        }
        return sb.toString();
    }
}
