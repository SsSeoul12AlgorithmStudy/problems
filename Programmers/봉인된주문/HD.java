import java.util.*;
/*
 소요시간 - 3시간 초과
 */
class Solution {

	public String solution(long n, String[] bans) {

		HashMap<Character, Integer> seq = new HashMap<>();
		for (int i = 0; i < 26; i++) {
			seq.put((char) ('a' + i), i + 1);
		}

		long[] rank = new long[bans.length];
		for (int i = 0; i < bans.length; i++) {
			rank[i] = getBanNum(bans[i], seq);
		}

		long targetNum = getTargetNum(n, rank);

		return getSpell(targetNum);
	}

	private static long getBanNum(String ban, HashMap<Character, Integer> seq) {

		long num = 0;
		for (int i = 0; i < ban.length(); i++) {
			num = num * 26 + seq.get(ban.charAt(i));
		}

		return num;
	}

	private static long getTargetNum(long n, long[] rank) {

		Arrays.sort(rank);
		long targetNum = n;
		for (long r : rank) {
			if (r <= targetNum) {
				targetNum++;
			} else {
				break;
			}
		}

		return targetNum;
	}

	private static String getSpell(long num) {

		StringBuilder targetSpell = new StringBuilder();
		while (num > 0) {
			num--;
			targetSpell.append((char) ('a' + (num % 26)));
			num /= 26;
		}

		return targetSpell.reverse().toString();
	}
}