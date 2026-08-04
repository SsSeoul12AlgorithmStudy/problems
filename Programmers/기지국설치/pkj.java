import java.util.*;

class Solution {
    
    public int solution(int n, int[] stations, int w) {
        int answer = applyStation(n, stations, w);

        return answer;
    }
    
    static int applyStation(int n, int[] stations, int w) {
        int result = 0; 
        int position = 1; // 1번 아파트부터 탐색 시작
        int stationIndex = 0; //존재하는 기지국 인덱스
        int networkRange = 2 * w + 1; // 기지국 하나가 커버하는 아파트 수
        
        while (position <= n) {
            // 전파 범위 안에 존재하는 아파트의 경우
            if (stationIndex < stations.length && stations[stationIndex] - w <= position) {
                //전파 범위가 벗어나는 인덱스로 이동
                position = stations[stationIndex] + w + 1;
                //기지국 전파 범위 확인했으니 다음 기지국 범위 탐색
                stationIndex++;
            } 
              
            // 전파가 닿지 않는 빈 구간인 경우
            else {
                // 다음 기지국의 전파 시작 위치, 만약 남은 기지국이 없다면, 아파트 전체 끝
                int nextStationStart = (stationIndex < stations.length) ? stations[stationIndex] - w : n + 1;
                // 전파가 닿지 않는 빈 구간의 길이
                int emptyLength = nextStationStart - position;
              
                // 빈 구간에 필요한 최소 기지국 개수를 계산
                result += (int) Math.ceil((double) emptyLength / networkRange);
              
                // 탐색 위치 이동
                position = nextStationStart;
            }
        }
        
        return result;
    }
}
