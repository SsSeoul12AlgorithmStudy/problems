import java.util.*;
import java.io.*;

public class Solution {
    //암호코드를 보면, {첫번째 0그룹, 첫번째 1그룹, 두번째 0그룹, 두번째 1그룹} 으로 구성돼있음
    //그룹 지어서 판단해야하는 이유가 암호코드가 굵어질수도 있기 때문임 7-14-21-28-... 이런식으로
    public static ArrayList<int[]> arr = new ArrayList<>(
            Arrays.asList(
                new int[]{3, 2, 1, 1}, 
                new int[]{2, 2, 2, 1}, 
                new int[]{2, 1, 2, 2}, 
                new int[]{1, 4, 1, 1}, 
                new int[]{1, 1, 3, 2}, 
                new int[]{1, 2, 3, 1}, 
                new int[]{1, 1, 1, 4}, 
                new int[]{1, 3, 1, 2}, 
                new int[]{1, 2, 1, 3}, 
                new int[]{3, 1, 1, 2}
            )
        );
    static ArrayList<String> hexaCode; //16진수 문자열 저장
    static ArrayList<String> binaCode; //2진수로 변환해서 저장
    static ArrayList<String> finalCode; //해독한 코드 저장
     
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
         
         
        for(int test_case = 1; test_case <= T; test_case++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            hexaCode = new ArrayList<>();
            binaCode = new ArrayList<>();
            finalCode = new ArrayList<>();
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());
            int answer = 0;
            StringBuilder sb;
             
            for(int i = 0; i < N; i++) {
                String tmp = br.readLine().trim();
                sb = new StringBuilder();
                boolean flag = false; //연속된 문자열 판단, 0이어도 연속된 문자열 안에 있으면 포함시켜줘야 하니까

                //암호코드 사이의 0인지 16진수 내의 0인지 판단이 어려우니, 암호코드 상 0이 오지않는 뒷부분부터 탐색해서 0 거르기 
                for(int j = M-1; j>= 0; j--) {
                    char c = tmp.charAt(j);
                     
                    if(flag) {
                        sb.append(c);
                    }
                    else if(c != '0') {
                        sb = new StringBuilder();
                        flag = true;
                        sb.append(c);
                    }
                }
                 
                if(sb.length() != 0) {
                    if(sb.length() != 0) {
                        String nS = sb.reverse().toString();
                        if(!hexaCode.contains(nS)) hexaCode.add(sb.toString());
                    }
                }
            }
             
            //16진수 코드 앞에 붙은 0 삭제
            //맨앞 0그룹은 암호 판단 과정에서 안 쓰이므로 삭제돼도 됨
            for(int i = 0; i < hexaCode.size(); i++) {
                String hex = hexaCode.get(i);
                while(hex.length() > 0) {
                    if(hex.charAt(0) == '0') {
                        hex = hex.substring(1);
                        hexaCode.set(i, hex);
                    }
                    else break;
                }
            }
         
            //16진수에서 2진수로
            hexaToBina();
             
            //각 숫자별로 0과 1들의 묶음이 2개씩라는 점을 이용해서 무슨 숫자인지 판단해야함.
            binaToFinal();
             
            //암호코드 계산
            for(int i = 0; i < finalCode.size(); i++) {
                answer += calCode(finalCode.get(i));
            }
             
            System.out.println("#" + test_case + " " + answer);
        }
    }
     
    //유효한 코드인지 확인 
    static int calCode(String s) {
        int check = 0;
        int result = 0;
         
        for(int i = 1; i <= s.length(); i++) {
            int x = Character.getNumericValue(s.charAt(i-1));
             
            if(i == s.length()) check += x;
            else if(i % 2 == 0) check += x;
            else check += (x*3);
            result += x;
        }
         
        if(check % 10 != 0) result = 0;
        return result;
    }
     
    //복호화 코드
    static int decrypt(int c2, int c3, int c4) {
        //그룹들 중 가장 작은 값은 1이어야 함.
        //1이 아닌 경우, 암호코드가 굵어졌다고 판단 -> 암호코드를 7자리로 축소하기
        int ratio = Math.min(Math.min(c2, c3), c4);
        int n2 = c2 / ratio;
        int n3 = c3 / ratio;
        int n4 = c4 / ratio;
        int n1 = 7 - (n2 + n3 + n4); // 전체 길이는 항상 7
         
        int[] temp = {n1, n2, n3, n4};
        for (int i = 0; i < 10; i++) {
            if (Arrays.equals(arr.get(i), temp)) {
                return i;
            }
        }
        return -1; // 일치하는 암호가 없는 경우
    }

    //2진수를 암호코드 단위로 쪼개기, 여기가 제일 오래 걸렸음..ㅠ
    static void binaToFinal() {
        for(int i = 0; i < binaCode.size(); i++) {
            String bina = binaCode.get(i);
            int idx = bina.length() - 1; //뒤부터 탐색
             
            while (idx >= 0) {
                if (bina.charAt(idx) == '1') { 
                    int[] codes = new int[8]; //암호코드는 7자리 + 검증코드의 구성으로 8자리
                    boolean isValid = true; //유효한 암호인지
                     
                    for (int d = 7; d >= 0; d--) {
                        int c2 = 0, c3 = 0, c4 = 0; //첫번째 1그룹, 두번째 0그룹, 두번째 1그룹
                         
                        while (idx >= 0 && bina.charAt(idx) == '1') { c4++; idx--; }
                        while (idx >= 0 && bina.charAt(idx) == '0') { c3++; idx--; }
                        while (idx >= 0 && bina.charAt(idx) == '1') { c2++; idx--; }
                         
                        if(c2 == 0 || c3 == 0 || c4 == 0) {
                            isValid = false;
                            break;
                        }
                         
                        int digit = decrypt(c2, c3, c4); //그룹을 기준으로 암호 해독
                        if (digit == -1) {
                            isValid = false;
                            break;
                        }
                         
                        codes[d] = digit; //해독한 코드 뒷자리부터 저장
                         
                        // 첫번째 0그룹의 인덱스값을 찾아서 그 앞으로 탐색 위치 이동
                        // 암호코드를 살펴보면 그룹에서 가장 작은 숫자는 무조건 1이다
                        int ratio = Math.min(Math.min(c2, c3), c4);
                        // 이 점을 활용해서 쓸데없는 0 건너뛰고 앞으로 이동
                        int n1 = 7 - (c2 / ratio + c3 / ratio + c4 / ratio);
                        idx -= (n1 * ratio); 
                    }
                     
                    // 8자리가 정상적으로 해독된 경우
                    if (isValid) {
                        StringBuilder sb = new StringBuilder();
                        for (int k = 0; k < 8; k++) sb.append(codes[k]);
                        String decoded = sb.toString();

                        // 중복된 암호코드가 존재할수도
                        // 예를 들어, 
                        // 1) 3803F1C7FF8FC01C01C7E3F00703F038E00FC01F8E00007FF8E00FC71C01F8E00FC7FC7E00FC71C7FF8E3FF
                        // 2) 7FF8E00FC71C01F8E00FC7FC7E00FC71C7FF8E3FFC
                        // 3) 3803F1C7FF8FC01C01C7E3F00703F038E00FC01F8E 
                        // 1-3번이 각각 hexaCode에 저장돼있는데, 1번을 해독하면서 2번과 3번으로 나눠서 저장했는데
                        // 2번과 3번을 또 해독해서 저장할수도 있기 때문임
                        // 이 로직 없으면 합이 커지면서 테케 10번 초반대부터 틀림
                        if (!finalCode.contains(decoded)) {
                            finalCode.add(decoded);
                        }
                    }
                } else {
                    idx--; // 1을 찾을 때까지 왼쪽으로 이동
                }
            }
        }
    }

    //16진수를 2진수로 변환
    static void hexaToBina() {
        for(int i = 0; i < hexaCode.size(); i++) {
            String hexa = hexaCode.get(i);
            StringBuilder sb = new StringBuilder();
             
            for(int j = 0; j < hexa.length(); j++) {
                //16 -> 10
                int dec = Character.digit(hexa.charAt(j), 16);
                //10 -> 2
                String bin = Integer.toBinaryString(dec);
                //2진수 4자리 채우기
                String paddedBin = String.format("%4s", bin).replace(' ', '0');
                sb.append(paddedBin);
            }
             
            binaCode.add(sb.toString());
        }
    }
     
}
