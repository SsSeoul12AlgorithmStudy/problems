import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class Solution {
	static int N, M;
	// 첫번째 0의 길이는 필요 없다.
	static final String[] RATIO = { "211", "221", "122", "411", "132", "231", "114", "312", "213", "112" };

	public static void main(String args[]) throws Exception {
		StringTokenizer st = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			int T;
			T = Integer.parseInt(br.readLine());

			for (int test_case = 1; test_case <= T; test_case++) {
				Set<Integer> set = new HashSet<>();
				st = new StringTokenizer(br.readLine());
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				for (int i = 0; i < N; i++) {
					String s = br.readLine();
					char[] carr = transformArr(s.toCharArray()).toCharArray();

					// 접근: 한 줄을 2진수 문자열로 만들어두고, 그 줄에 대해서
					// 1을 만나면 -> 8번 동안 1-0-1 개수 세기를 진행
					// 위 과정을 계속 반복
					int idx = 0;
					while (idx < carr.length) {
						// 의미 없는 0 넘기기
						while ((idx < carr.length) && (carr[idx] == '0'))
							idx++;
						if (idx >= carr.length)
							break;

						// 여기까지 왔다는 건 1을 만났다는 뜻
						int code = 0;
						for (int x = 0; x < 8; x++) {
							int cnt = 0;
							int[] ratios = new int[3];
							// 의미없는 0 넘기기
							while ((idx < carr.length) && (carr[idx] == '0'))
								idx++;
							// 1 개수 세기
							// while (carr[idx++] == '1') cnt++; -> 이렇게 하면 안됨. 틀릴 때도 while 문에서 비교하고 넘어감
							while (carr[idx] == '1') {
								cnt++;
								idx++;
							}
							ratios[0] =  cnt;
							cnt = 0;
							// 0 개수 세기
							while (carr[idx] == '0') {
								idx++;
								cnt++;
							}
							ratios[1] =  cnt;
							cnt = 0;
							// 1 개수 세기
							while (carr[idx] == '1') {
								idx++;
								cnt++;
							}
							ratios[2] =  cnt;
							
							// sb에 있는 녀석들 기약분수로 나눠서 비율 형태로 만들기 -> ex) 224면 112로
							final int min = Arrays.stream(ratios).min().orElse(1);
							ratios = Arrays.stream(ratios).map(v -> v / min).toArray();
							code *= 10;
							code += decode(ratios);
						}
						set.add(code);
					}

				}

				int answer = 0;

				for (int c : set) {
//					System.out.println(c);
					answer += validTest(c);
				}

				System.out.println("#" + test_case + " " + answer);

			}
		}

	}

	// 16진수 문자 -> 2진수 String 반환
	static String transformU(char hex) {
		return transformTwo(transformTen(hex));
	}

	// 16진수 문자열 -> 2진수 String 변환
	static String transformArr(char[] hex) {
		StringBuilder sb = new StringBuilder();
		for (char c : hex) {
			String s = transformTwo(transformTen(c));
			sb.append(s);
		}

		return sb.toString();
	}

	// 16진수 하나 -> 10진수 변환
	static int transformTen(char c) {
		if (c >= 'A' && c <= 'F') {
			return 10 + c - 'A';
		}
		return c - '0';
	}

	// 10 진수 -> 2진수 변환 (String 반환)
	static String transformTwo(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 3; i >= 0; i--) {
			sb.append(num % 2);
			num /= 2;
		}

		return sb.reverse().toString();
	}

	// 주어진 이진수 String의 암호코드를 반환.
	static int[] makeCode(String binaries) {
		System.out.println(binaries);
		int[] res = new int[8];
		char[] carr = binaries.toCharArray();
		int c_idx = carr.length - 1;
		int res_idx = 7;
		// 일단 맨끝에서 1이 나올 때까지 앞으로 가기
		while (carr[c_idx] == '0')
			c_idx--;
		// 8번 실행
		while (res_idx >= 0) {
			// parts에 각 숫자가 나온 횟수 저장하기
			int[] parts = new int[4];
			for (int i = 0; i < 4; i++) {
				if (c_idx < 0)
					break; // 에러 발생 -> 만약 맨 앞이 1101.. 이런 식이면 첫 번째 0 구간이 안 나온다.
				char last_c = carr[c_idx];
				while (c_idx >= 0 && last_c == carr[c_idx]) {
					parts[3 - i]++;
					c_idx--;
				}
			}
			// parts 값 중 최솟값 구하기
			int min = 100000;
			int[] parts_except_first_zero = Arrays.copyOfRange(parts, 1, 4);
			for (int p : parts_except_first_zero)
				min = Math.min(min, p);
			final int mmin = min;
			StringBuilder sb = new StringBuilder();
			Arrays.stream(parts_except_first_zero).map(v -> {
				return v / mmin;
			}).forEach(v -> sb.append(v));
			;
			res[res_idx] = decode(sb.toString());
			System.out.println(res[res_idx]);

			res_idx--;
		}

		return res;
	}

	// 주어진 String이 몇번 codes에 해당하는지 리턴
	static int decode(String str) {
		for (int i = 0; i < RATIO.length; i++) {
			if (RATIO[i].equals(str))
				return i;
		}

		return -1;
	}
	
	static int decode(int[] ratios) {
		int sum = 0;
		for (int r : ratios) {
			sum *= 10;
			sum += r;
		}
		for (int i = 0; i < RATIO.length; i++) {
			if (RATIO[i].equals(Integer.toString(sum)))
				return i;
		}

		return -1;
	}

	// 주어진 암호코드가 올바른 암호코드면 각 숫자의 합 반환, 그렇지 않다면 0 반환
	static int validTest(int code) {
		int odd_sum = 0;
		int even_sum = 0;
		for (int i = 0; i < 8; i++) {
			if (i % 2 == 0) {
				odd_sum += code % 10;
			} else {
				even_sum += code % 10;
			}
			code /= 10;
		}

		if ((even_sum * 3 + odd_sum) % 10 == 0) {
			return even_sum + odd_sum;
		}
		return 0;
	}

	// int[8] -> int 변환
	static int makeInt(int[] arr) {
		int sum = 0;
		for (int n : arr) {
			sum *= 10;
			sum += n;
		}

		return sum;
	}
}