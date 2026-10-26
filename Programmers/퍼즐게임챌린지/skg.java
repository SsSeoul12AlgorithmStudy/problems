/*

level 요구량이 더 높으면 (diff - level) * time_prev + (diff - level + 1) * time_cur
= (diff - level) * (time_prev + time_cur) + time_cur

Math.max(diff - level, 0) * (time_prev + time_cur) + time_cur;

1 2 3 4
2가 정답이라면
1 + 4 / 2 = 2
1 2
1 + 2 / 2 = 1
2


*/


class Solution {
    int[] diffs;
    int[] times;
    long limit;
    int n;
    public int solution(int[] diffs, int[] times, long limit) {
        int answer = 0;
        this.n = diffs.length;
        this.diffs = diffs;
        this.times = times;
        this.limit = limit;
        
        int left = 1;
        int right = 100000;
        
        while (left < right) {
            int mid = (left + right) / 2;
            if (takenTime(mid) > limit) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        System.out.println(takenTime(2));
        
        return left;
    }
    
    public long takenTime(int level) {
        long sum = 0;
        long time_prev = 0;
        for (int i = 0; i < n; i++) {
            sum += Math.max(diffs[i] - level, 0) * (time_prev + times[i]) + times[i];
            time_prev = times[i];
        }
        
        return sum;
    }
}
