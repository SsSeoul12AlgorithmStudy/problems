/*
메인 아이디어는 재귀
시작 역을 기준으로 그 역에 정차하는 열차를 확인하고 그 열차의 정착역을 돌면서 겹치는 열차를 확인
-> 겹치는 열차가 있다면 그 열차가 정차하는 곳에 종착역이 있는지 체크하는 방식
*/

import java.util.*;
 
class UserSolution {
    static int N, K;
    static ArrayList<Integer>[] stations;
    static HashMap<Integer, ArrayList<Integer>> trains;
    static int[] minCnt;
     
    public void init(int N, int K, int mId[], int sId[], int eId[], int mInterval[]) {
        this.N = N; //열차 역의 개수
        this.K = K; //열차의 개수
        this.stations = (ArrayList<Integer>[]) new ArrayList[N + 1]; //인덱스는 역사 번호, 원소값은 역에 정차하는 열차번호 (역 번호는 1번부터)
        for (int i = 0; i <= N; i++) {
            stations[i] = new ArrayList<>();
        }
        this.trains = new HashMap<Integer, ArrayList<Integer>>(); //키는 열차 아이디, 밸류는 열차가 정차하는 역번호 리스트
         
        for(int i = 0; i < K; i++) {
            int tN = mId[i]; //열차 번호
            int s = sId[i]; //시작역
            int e = eId[i]; //종착역
            int g = mInterval[i]; //간격
             
            ArrayList<Integer> tmp = new ArrayList<>();
            for(int j = s; j <= e; j+=g) {
                //열차 정차역 저장
                tmp.add(j);
                stations[j].add(tN);
            }
            trains.put(tN, tmp);
        }
         
         
        for(int i = 1; i <= N; i++) {
            Collections.sort(stations[i]);
        }
         
        return;
    }
 
    public void add(int mId, int sId, int eId, int mInterval) {
        ArrayList<Integer> tmp = new ArrayList<>();
        for(int j = sId; j <= eId; j+=mInterval) {
            //열차 정차역 저장
            tmp.add(j);
            stations[j].add(mId);
        }                  
        trains.put(mId, tmp);
         
        for(int i = 1; i <= N; i++) {
            Collections.sort(stations[i]);
        }
         
        return;
    }
 
    public void remove(int mId) {
        trains.remove(mId);
         
        for(int i = 1; i <= N; i++) {
            ArrayList<Integer> tmp = stations[i];
            for(int j = 0; j < tmp.size(); j++) {
                if(tmp.get(j) == mId) {
                    tmp.remove(j);
                    break;
                }
            }
        }
         
        return;
    }
     
    private int recur(int eId, int currentTrain, int cnt, ArrayList<Integer> visited) {
 
        ArrayList<Integer> trainStations = trains.get(currentTrain);
 
        // 현재 열차로 목적지에 갈 수 있는 경우
        if (trainStations.contains(eId)) return cnt;
 
        int result = Integer.MAX_VALUE;
 
        // 현재 열차가 정차하는 역들을 확인
        for (int stationId : trainStations) {
 
            // 해당 역에서 갈아탈 수 있는 열차
            for (int nextTrain : stations[stationId]) {
                if (nextTrain == currentTrain) continue;
 
                // 현재 경로에서 이미 탄 열차인지 확인
                if (visited.contains(nextTrain)) continue;
 
                visited.add(nextTrain);
 
                int tmp = recur(eId, nextTrain,  cnt + 1, visited);
                result = Math.min(result, tmp);
 
                // 다른 경로를 탐색하기 위해 제거
                visited.remove(visited.size() - 1);
            }
        }
 
        return result;
    }
 
    public int calculate(int sId, int eId) {
        int result = Integer.MAX_VALUE;
 
        for (int trainId : stations[sId]) {
            ArrayList<Integer> visited = new ArrayList<>();
            visited.add(trainId);
 
            int tmp = recur( eId, trainId, 0, visited );
            result = Math.min(result, tmp);
        }
 
        return result == Integer.MAX_VALUE ? -1 : result;
    }
}
