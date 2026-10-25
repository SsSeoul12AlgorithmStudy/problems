/*

diff = diffs[i]
time_cur = times[i]
time_prev = times[i-1]

diff <= level --> time_cur
diff > level --> (diff - level) * (time_cur + time_prev) + time_cur


[단순풀이]
-> 1~limit까지 level++ 하면서 퍼즐을 해결하는 level 찾기

시간복잡도
10^21 * 3 ?? GG


알고리즘 - 이분탐색?
- 1~limit mid 만들어
- start = mid + 1
- end = mid - 1

*/

class Solution {
    
    int s, e;
    long limit;
    int[] diffs, times;
    
    void init(int[] diffs, int[] times, long limit) {
        this.diffs = diffs;
        this.times = times;
        this.limit = limit;
    }
    
    public int solution(int[] diffs, int[] times, long limit) {
        init(diffs, times, limit);
        
        s = 1;
        e = 0;
        for(int diff : diffs) {
            if(e < diff) e = diff;
        }
        
        while(s <= e) {
            int mid = (s + e) / 2;
            puzzle(mid);
        }
        
        return s;
    }
    
    private void puzzle(int level) {
        long limit2 = limit;
        
        for(int i = 0; i < diffs.length; i++) {
            if(diffs[i] <= level) {
                limit2 -= times[i];
            } else {
                int cnt = diffs[i] - level;
                limit2 -= cnt * (times[i] + times[i - 1]) + times[i];
            }
        }
        
        if(limit2 >= 0) {
            e = level - 1;
        } else {
            s = level + 1; 
        }
    }
}
