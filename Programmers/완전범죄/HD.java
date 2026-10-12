/*
소요시간 - 2시간 초과

단순 구현 - A가 안훔치지않거나(B가 훔침) 훔치거나.
정렬이 필요할까? -> 굳이? 큰 영향은 안끼칠 듯

가지치기 방식
- A의 흔적 < N 이여야한다.
- A의 흔적 < 현재 최솟값 보다 커야한다.
- B의 흔적이 M미만 && A의 흔적이 N미만 인 경우 result 갱신

DP로 풀어야할거라는 느낌까지는 학습된 데이터로 인해 알겠음. 근데 막상 구현은 떠오르지 않음.

애들에게 풀어오겠다고 큰소리 떵떵쳤는데 나는 백트래킹을 못하는 아이였다.
특강강사님의 말씀은 틀린게 없었다.
*/

class Solution {

    static int N, M, result;
    static Robber[] robber;

    static class Robber {

        int a, b;

        public Robber(int[] info) {
            this.a = info[0];
            this.b = info[1];
        }
    }

    public int solution(int[][] info, int n, int m) {
        N = n;
        M = m;
        result = Integer.MAX_VALUE;

        robber = new Robber[info.length];

        int totalB = 0;
        for (int i = 0; i < info.length; i++) {
            robber[i] = new Robber(info[i]);
            totalB += info[i][1];
        }

        // 모든 물건을 B가 훔친 상태에서 시작
        dfs(0, 0, totalB);

        return result == Integer.MAX_VALUE ? -1 : result;
    }

    private void dfs(int idx, int aSum, int bSum) {

        // 1. A의 흔적이 제한 이상
        if (aSum >= N) return;

        // 2. 이미 구한 최솟값 이상
        if (aSum >= result) return;

        // 3. B의 흔적이 제한 미만이면 정답 갱신
        if (bSum < M) {
            result = Math.min(result, aSum);
            return;
        }

        // 모든 물건을 확인한 경우
        if (idx == robber.length) return;

        // 현재 물건을 B가 훔치는 경우
        dfs(idx + 1, aSum, bSum);

        // 현재 물건을 A에게 넘기는 경우
        dfs(idx + 1, aSum + robber[idx].a, bSum - robber[idx].b);
    }
}
