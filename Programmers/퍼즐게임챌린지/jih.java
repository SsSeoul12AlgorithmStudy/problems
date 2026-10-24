class Solution {
    /*
    26-10-24 19:28 ~ 19:51
    
    모든 퍼즐을 제한 시간 내에 풀기 위해서 필요한 숙련도의 최솟값을 구하자
    
    ---
    
    n = 퍼즐의 수 <= 30만
    첫번째 퍼즐은 무조건 난이도가 1
    
    m = 난이도 = 숙련도 : 1 ~ 10만
    
    limit = 제한 시간 <= 10^15
    
    ---
    
    완탐이 가능할까?
    - 가능한 숙련도에 대해서 총 소요 시간 <= limit인지 확인해야
    - 시간복잡도 = n * m <= 30만 * 10만
    
    최솟값을 찾아야 한다
    - 이분탐색이 가능할까?
        - 숙련도가 작아질 수록 소요시간은 커지고, 커지면 작아진다 -> 단조성
        - 판정은 선형시간 n이 소요
        - 총 시간복잡도는 n log m < 30만 * 17
        - 가능해보인다
        
    */
    
    public int solution(int[] diffs, int[] times, long limit) {
        // 가능한 숙련도에 대해서 이분탐색
        int lo = 0;
        int hi = 300_001;
        
        while(lo + 1 < hi){
            int mid = (lo + hi) / 2;
            if(calcTime(mid, diffs, times) <= limit){
                hi = mid;
            }
            else{
                lo = mid;
            }
        }
        
        return hi;
    }
    
    private long calcTime(int level, int[] diffs, int[] times){
        long ret = times[0];
        
        for(int i = 1; i < diffs.length; i++){
            if(diffs[i] <= level){
                ret += times[i];
            }
            else{
                ret += (times[i - 1] + times[i]) * (diffs[i] - level) + times[i];
            }
        }
        
        return ret;
    }   
}