#include <string>
#include <vector>
#include <bitset>
#include <iostream>

using namespace std;

//이진탐색과 같이 진행 (중위순회는 부모노드가 중앙이기 때문)
bool checkTree(const string& binary, int left, int right, bool parentIsZero) {
    // 더 이상 확인할 노드가 없음
    if (left > right) {
        return true;
    }

    int mid = (left + right) / 2;

    // 부모가 0인데 현재 노드가 1이면 불가능
    if (parentIsZero && binary[mid] == '1') {
        return false;
    }

    // 현재 노드가 0인지 자식에게 전달
    bool currentIsZero = (binary[mid] == '0');

    return checkTree(binary, left, mid - 1, currentIsZero) &&
           checkTree(binary, mid + 1, right, currentIsZero);
}

vector<int> solution(vector<long long> numbers) {
    vector<int> answer;

    for (long long num : numbers) {
        string binary = bitset<64>(num).to_string();
        // 2진수로 바꾸면서 포화 이진트리 형식으로 바꿔두기
        // 처음으로 1이 나오는 위치 찾기
        int firstOne = binary.find('1');

        // 1이 없으면 0, 있으면 그 위치부터 끝까지 자르기
        binary = (firstOne == string::npos) ? "0" : binary.substr(firstOne);

        // 포화 이진트리의 노드 개수 찾기
        int treeSize = 1;

        while (treeSize < binary.size()) {
            treeSize = treeSize * 2 + 1;
        }

        // 부족한 만큼 앞에 0 채우기
        binary = string(treeSize - binary.size(), '0') + binary;
        
        bool possible = checkTree(binary, 0, binary.size() - 1, false);
        answer.push_back(possible ? 1 : 0);
    }

    return answer;
}
