#include <bits/stdc++.h>
using namespace std;

class name_grade {
	public:
		string student_name;
		int korean;
		int math;
		int english;
		int science;
		int social;
		int history;

		void set_grade(string tuple)
		{
			stringstream tuplestr(tuple);
			string tempstr;

			getline(tuplestr, student_name, ',');

			getline(tuplestr, tempstr, ',');
			korean = stoi(tempstr);
			
			getline(tuplestr, tempstr, ',');
			math = stoi(tempstr);
			
			getline(tuplestr, tempstr, ',');
			english = stoi(tempstr);
			
			getline(tuplestr, tempstr, ',');
			science = stoi(tempstr);
			
			getline(tuplestr, tempstr, ',');
			social = stoi(tempstr);
			
			getline(tuplestr, tempstr);
			history = stoi(tempstr);
		}
};

class name_number{
	public :
		string student_name;
		string student_number;

		void set_number(string tuple)
		{
			stringstream tuplestr(tuple);
			string tempstr;


			getline(tuplestr, student_name, ',');
			getline(tuplestr, student_number, ',');
		}
};

string make_tuple(string name, string number)
{
	string ret = "";

	ret += name+ "," + number +"\n";

	return ret;
}

bool over_2_sub(name_grade first, name_grade second) {
	int count = 0;
	if(first.korean < second.korean ) {
		count ++;
	}
	if(first.math < second.math ) {
	count ++;
	}
	if(first.english < second.english ) {
		count ++;
	}
	if(first.science < second.science ) {
		count ++;
	}
	if(first.social < second.social ) {
		count ++;
	}
	if(first.history < second.history ) {
		count ++;
	}

	return count>=2;
}

int make_hash(string name) {
	const char* str = name.c_str();

	int hash = 401;
	int c;
	int max_table = 11;

	while(*str != '\0') {
		hash = ((hash<<4)+(int)(*str)%max_table);
		str++;
	}

	return hash%max_table;
}

int main(){

	string buffer[2];
	name_grade temp0;
	name_grade temp1;
	name_number temp2;
	fstream block[12];
	ofstream output;

	output.open("./output3.csv");

	if(output.fail())
	{
		cout << "output file opening fail.\n";
	}

	/*********************************************************************/

	int outer_block = 0; 
	int inner_block = 0;

	for(int i=0 ; i<11 ; i++) {
		block[i].open("../buckets/test3_outer_"+to_string(i) + ".csv", ios::out);
	}

	while(outer_block<1000) {
		block[11].open("./name_grade1/"+to_string(outer_block) + ".csv");
		for(int i=0 ; i<10 ; i++) {
			getline(block[11],buffer[0],'\n');
			temp0.set_grade(buffer[0]);
			int hash_result = make_hash(temp0.student_name);

			block[hash_result] << buffer[0] << "\n";
		}
		block[11].close();
		outer_block++;
	}

	for(int i=0 ; i<11 ; i++) {
		block[i].close();
	}

	for(int i=0 ; i<11 ; i++) {
		block[i].open("../buckets/test3_inner_"+to_string(i) + ".csv", ios::out);
	}

	while(inner_block<1000) {
		block[11].open("./name_grade2/"+to_string(inner_block) + ".csv");
		for(int i=0 ; i<10 ; i++) {
			getline(block[11],buffer[1],'\n');
			temp1.set_grade(buffer[1]);
			int hash_result = make_hash(temp1.student_name);

			block[hash_result] << buffer[1] << "\n";
		}
		block[11].close();
		inner_block++;
	}
	for(int i=0 ; i<11 ; i++) {
		block[i].close();
	}

	block[3].open("../buckets/hashjoin_result.csv",ios::out);
	for(int i=0 ; i<11 ; i++) {
		block[0].open("../buckets/test3_outer_"+to_string(i) + ".csv");
		block[1].open("../buckets/test3_inner_"+to_string(i) + ".csv");

		while(getline(block[0],buffer[0],'\n')) {
			temp0.set_grade(buffer[0]);
			while(getline(block[1],buffer[1],'\n')) {
				temp1.set_grade(buffer[1]);
				if(temp0.student_name == temp1.student_name && over_2_sub(temp0,temp1)) {
					block[3] << temp0.student_name << "\n";
					break;
				}
			}
			block[1].seekg(0,ios::beg);
		}

		block[0].close();
		block[1].close();
	}
	
	block[3].close();

	outer_block = 0;
	inner_block = 0;

	block[1].open("../buckets/hashjoin_result.csv");
	
	while(outer_block<1000) {
		block[0].open("./name_number/" + to_string(outer_block) + ".csv");
		while (getline(block[0],buffer[0],'\n'))
		{
			temp2.set_number(buffer[0]);
			// cout << "??" << endl;
			while(getline(block[1],buffer[1],'\n')) {
				if(temp2.student_name == buffer[1]) {
					output << buffer[0] << "\n";
					break;
				}
			}
			block[1].seekg(0,ios::beg);
		}
		
		block[0].close();
		outer_block++;
	}

	block[1].close();

	/*********************************************************************/

	output.close();

}
