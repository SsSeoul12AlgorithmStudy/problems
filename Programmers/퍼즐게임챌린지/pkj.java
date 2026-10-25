
class Solution {
    private static int answer;
    private static long limit;
    private static int[] diffs;
    private static int[] times;

    public int solution(int[] diffs, int[] times, long limit) {
        int left = 1; //이분탐색 왼쪽 포인터
        int right = 0; //오른쪽 포인터
        answer = 1;
        this.diffs = diffs;
        this.times = times;
        this.limit = limit;

        for (int diff : diffs) right = Math.max(right, diff);
        answer = right; //일단 최대 난이도로 설정 

        while (left <= right) {
            //탐색 범위의 중간값
            int mid = left + (right - left) / 2;

            //중간 숙련도로 해결이 가능하면
            if (isValid(mid)) {
                answer = mid;
                //숙련도를 더 낮춰서 해결할 수 있는지 확인
                right = mid - 1;
            } 
            //해결 실패하면
            else {
                //left~mid는 다 실패한다는 의미니까 왼쪽포인터를 mid+1로 높힘
                left = mid + 1;
            }
        }

        return answer;
    }

    private boolean isValid(int level) {
        long totalTime = times[0]; //소요시간

        for (int i = 1; i < diffs.length; i++) {
            //숙련도가 충분하면
            if (diffs[i] <= level) {
                totalTime += times[i];
            } 
            //숙련도가 부족하면
            else {
                //문제 상 계산법 따르기 
                int mistakes = diffs[i] - level;
                totalTime += (long) (times[i] + times[i - 1]) * mistakes + times[i];
            }

            //제한시간을 넘으면 더 볼 필요없음
            if (totalTime > limit) return false;
        }

        return true;
    }
}
