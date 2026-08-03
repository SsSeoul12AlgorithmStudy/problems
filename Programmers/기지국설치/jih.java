import java.util.*;

class Solution {
    /*
    26-8-3 10:15
    
    ===
    
    추가해야할 기지국의 최솟값을 구하자
    
    ===
    
    그리디로 풀 수 있을까?
    
    전파가 닿지 않는 영역을 2 * w + 1만큼 덮어나간다
    */
    public int solution(int n, int[] stations, int w) {
        if(stations.length == 0){
            return (n + 2 * w) / (2 * w + 1);
        }
        
        int answer = 0;
        
        int left = 1;
        for(int i = 0; i < stations.length; i++){
            int curStart = stations[i] - w;
            if(left < curStart){
                answer += (curStart - left + 2 * w) / (2 * w + 1); 
            }
            left = stations[i] + w + 1;
        }
        if(left <= n){
            answer += (n - left + 1 + 2 * w) / (2 * w + 1);
        }
        
        return answer;
    }
}