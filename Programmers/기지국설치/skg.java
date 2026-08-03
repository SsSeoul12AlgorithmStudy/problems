// 1차 접근 : 1차원 배열에 신호가 닿는지 여부를 기록 -> 효율성 시간초과
// --- 3시간 오바 ---
// 힌트 : 배열을 새로 만들지 않고 인덱스로 접근해서 O(N)에 끝내는 법
// 2차 접근 : 모든 위치에 대해 상태 저장하지 않고, 다음에 어떤 위치를 살펴볼지만 정하면 된다.
class Solution {
    public int solution(int n, int[] stations, int w) {
        int answer = 0;
        int stn_i = 0;
        for (int i = 1; i <= n; i++) {
            if (stn_i < stations.length && stations[stn_i] - w <= i && i <= stations[stn_i] + w) {
            // 다음에 원래 설치된 기지국의 범위에 포함된다면 해당 기지국의 교신이 끝나는 곳으로 이동
                i = stations[stn_i++] + w;
            } 
            else {
            // 포함되지 않는다면, 해당 위치를 왼쪽 한계로 가지는 기지국 설치 - 해당 기지국의 오른쪽 한계에서 벗어나는 위치로 이동
                answer++;
                i += 2 * w;
            }
        }

        return answer;
    }
    
}
