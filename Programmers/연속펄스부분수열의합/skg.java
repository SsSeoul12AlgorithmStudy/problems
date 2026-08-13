// DP로 풀 수 있나?
// D[i][0] : 짝수번째 부호가 바뀌는 펄스 수열을 적용했을 때, i번째 수를 포함한 합의 최댓값
// D[i][1] : 홀수번째 부호가 `` `` `` 
// 점화식
// D[k][0] = (D[k-1][0] > 0 ? D[k-1][0] : 0) + (k%2 == 0 ? -1 : 1) * A[k];
// 시간복잡도 : O(n * 2) = O(n)
class Solution {
    long[][] D;  // 합이 최대 100,000 * 500,000 = 50,000,000,000 (50억)으로 int 범위를 벗어나는데 int로 처리해서 세 개 틀렸었음.
    int N;
    
    public long solution(int[] sequence) {
        long answer = 0;
        this.N = sequence.length;
        D = new long[N][2];
        // 초기화
        D[0][0] = (long)sequence[0] * -1L;
        D[0][1] = (long)sequence[0];
        answer = Math.max(D[0][0], D[0][1]);    // 이거 때문에 하나 틀렸었고
        for (int i = 1; i < N; i++) {
            for (int s = 0; s < 2; s++) {
                D[i][s] = (D[i-1][s] > 0 ? D[i-1][s] : 0L);
                D[i][s] += (s == 0 ? 1 : -1) * (i%2 == 0 ? -1 : 1) * (long)sequence[i];
                answer = Math.max(answer, D[i][s]);
            }
        }
        
        return answer;
    }
}