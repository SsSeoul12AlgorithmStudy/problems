/*
숫자를 이진수 문자열로 변환 
포화 이진트리의 길이는 무조건 1,3,7,15,31, ...
포화 이진트리 형태가 되도록 부족한 길이만큼 0으로 채움

문자열의 가운데 문자를 루트 노드로 생각하고 서브트리 분리하기

루트 노드와 좌우 서브트리를 쪼개가면서 재귀적으로 검사
부모 노드가 0인데 자식 노드에 1이 존재하는 경우 표현 불가능
*/

class Solution {
    public int[] solution(long[] numbers) {
        int[] answer = new int[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            //2진수로 바꾸기
            String binary = Long.toBinaryString(numbers[i]);

            //이진트리 길이 구하기
            //완전이진트리의 길이는 무조건 1,3,7,15,31, ...
            int length = 1;
            while (length < binary.length()) {
                length = length * 2 + 1;
            }

            //문자열 왼쪽에 0을 추가하기 
            //루트노드의 좌우를 같은 크기의 서브트리로 나누기 위함
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < length - binary.length(); j++)             {
                sb.append('0');
            }

            sb.append(binary);
            binary = sb.toString();

            //이진수, 시작인덱스, 마지막인덱스, 부모노드가 0인지 아닌지 
            answer[i] = isValid(binary, 0, binary.length() - 1, false) ? 1 : 0;
        }

        return answer;
    }

    private boolean isValid(String binary, int start, int end, boolean parentIsZero) {
        if (start > end) return true;

        int mid = (start + end) / 2;
        //가운데 노드 
        char root = binary.charAt(mid);

        //부모노드가 0이라면 자손들도 무조건 0이어야 함 
        if (parentIsZero && root == '1') return false;
        //리프노드이면
        if (start == end) return true;

        //다음 부모 노드가 0인지 아닌지 
        boolean currentIsZero = false;
        if(root == '0') currentIsZero = true;

        //현재 루트노드의 좌우 트리 탐색 
        return isValid(binary, start, mid - 1, currentIsZero)
                && isValid(binary, mid + 1, end, currentIsZero);
    }
}
