
#include <iostream>
#include <string>
#include <vector>
#include <queue>

using namespace std;

//data 내 구조: [code, date, maximum, remain]
//어떤 정보를 기준으로 데이터를 뽑아낼지 의미하는 문자열 ext
//뽑아낼 정보의 기준값 val_ext
//정보를 정렬할 기준이 되는 문자열 sort_by
//해당하는 값을 기준으로 ** 오름차순 **

struct cmp {
	int idx;
	
	cmp(int val) {
		idx = val;
	}

	bool operator()(const vector<int>& a, vector<int>& b) {
		return a[idx] > b[idx];
	}
};

int change(string s) {
	int val = 0;

	if (s == "code") val = 0;
	else if (s == "date") val = 1;
	else if (s == "maximum") val = 2;
	else if (s == "remain") val = 3;

	return val;
}

vector<vector<int>> solution(vector<vector<int>> data, string ext, int val_ext, string sort_by) {
    vector<vector<int>> answer;
    //pq 2개 만들기
    //첫 pq에서는 ext 기준 val_ext 정렬

	int fIdx = 0;

	fIdx = change(ext);

	// 첫 번째 pq: ext 기준으로 데이터 관리
	priority_queue<vector<int>, vector<vector<int>>, cmp> pq{ cmp(fIdx) };
	for (vector<int> v : data) {
		pq.push(v);
	}

	// 두 번째 pq: sort_by 기준 오름차순 정렬
	int sIdx = 0;
	sIdx = change(sort_by);

	priority_queue<vector<int>, vector<vector<int>>, cmp> ans{ cmp(sIdx) };

    //두번째 pq로 sort_by기준으로 옮기며 val_ext 아래는 옮기지 말기
	while (!pq.empty()) {
		vector<int> v = pq.top();
		pq.pop();

		//val_ext까지 도달하면 break
		if (v[fIdx] >= val_ext) break;

		ans.push(v);
	}
    
    //정렬된 요소 answer vector에 넣기
	while(!ans.empty()) {
		vector<int> v = ans.top();
		answer.push_back(v);
		ans.pop();
	}

	return answer;
}
