/*

아효
감도 안 온다.
7 = 111
42 = 101010
5 = 101

이진수 길이는 1 혹은 1+2 혹은 1+2+4... 이런 식으로 맞춰야 함. (앞에 0을 추가해서)
재귀?
(서브트리1)루트(서브트리2)
010 - 1 - 010
서브트리가 0이 아닐 때까지 또 같은 방식으로 나눌 수 있다.
0 - 1 - 0
바람직한지?

재귀 대신 dp로 풀어볼까?
dp[0101010] = dp[010] & root == 1 * dp[010];

문자열 쪼개는 방법을 몰랐어.. substring(int start, int end);
start ~ end-1 인덱스 까지 쪼개기

*/

class Solution {
    public int[] solution(long[] numbers) {
        int[] answer = new int[numbers.length];
        
        for (int i = 0; i < numbers.length; i++) {
            long number = numbers[i];
            
            int l = 0;
            long temp = number;
            while (temp > 0L) {
                temp /= 2L;
                l++;
            }

            // 0이 앞에 붙은 진짜 l 길이를 찾기.
            int real_l = 1;
            while (real_l < l) {
                real_l = real_l * 2 + 1;
            }
            // System.out.println(makeBinary(number));
            answer[i] = (validNumber(number, real_l) ? 1 : 0);
        }
        
        return answer;
    }
    
    public boolean validNumber(long number, int len) {
        if (len == 1 && number == 1 || number == 0)    return true;
        
        // System.out.println("real_l:" + real_l + ", l:" + l);
        long root = (number >> (len / 2)) % 2L;
        // System.out.println("root:" + root);
        
        if (root == 0)  return false;
        
        long leftSubtree = number >> (len / 2 + 1);
        long rightSubtree = ((1L << (long)(len / 2)) - 1L) & number;
        // System.out.println("left:" + leftSubtree + ", right:" + rightSubtree);
        
        return validNumber(leftSubtree, len/2) && validNumber(rightSubtree, len/2);
    }
    
    // 숫자를 이진수 문자열로 변환 (1 + 2 + 4... ) 길이에 맞춰서 0 추가.
    // 길이는 1, 3, 7, 15 중 하나. -> 가 아니다.
    public String makeBinary(long number) {
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            sb.append(number % 2);
            number /= 2;
        }
        while (sb.length() != 1 && sb.length() != 3 && sb.length() != 7 && sb.length() != 15) {
            sb.append("0");
        }
        
        return sb.reverse().toString();
    } 
}
