import java.util.Arrays;

/*
 - 부분수열 합 -> 투포인터 생각하고 시작
 - 수열에서 연속된 부분 수열의 합 중 최댓값을 찾는 경우 -> 카데인 알고리즘 - DP 일종 O(N)
 */

class Solution {
	public long solution(int[] sequence) {

		int[][] numbers = new int[2][sequence.length];

		for (int i = 0; i < sequence.length; i++) {
			numbers[0][i] = (i % 2 == 0 ? -1 : 1) * sequence[i];
			numbers[1][i] = (i % 2 != 0 ? -1 : 1) * sequence[i];
		}

		long negativeNum = getMax(numbers[0]);
		long positiveNum = getMax(numbers[1]);

		return Math.max(negativeNum, positiveNum);
	}

	private long getMax(int[] nums) {

		long[] dp = new long[nums.length];
		dp[0] = nums[0];

		for (int i = 1; i < nums.length; i++) {
			dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
		}

		Arrays.sort(dp);

		return dp[nums.length - 1];
	}
}