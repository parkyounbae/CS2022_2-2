#include <bits/stdc++.h>
using namespace std;

class name_age {
	public:
		string name;
		string age;
		
		void set_name_age(string tuple)
		{
			stringstream tuplestr(tuple);
			string agestr;

			getline(tuplestr, name, ',');
			getline(tuplestr, age);
		}
};

class name_salary {
	public:
		string name;
		string salary;
		
		void set_name_salary(string tuple)
		{
			stringstream tuplestr(tuple);
			string salarystr;

			getline(tuplestr, name, ',');
			getline(tuplestr, salary);
		}
};

string make_tuple(string name, string age, string salary)
{
	return name+ ',' + age + ',' + salary + '\n';
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
	name_age temp0;
	name_salary temp1;
	fstream block[12];
	ofstream output;

	output.open("./output2.csv");

	if(output.fail())
	{
		cout << "output file opening fail.\n";
	}


	/******************************************************************/
	
	int outer_block = 0; 
	int inner_block = 0;

	for(int i=0 ; i<11 ; i++) {
		block[i].open("../buckets/test2_outer_"+to_string(i) + ".csv", ios::out);
	}

	while(outer_block<1000) {
		block[11].open("./name_age/"+to_string(outer_block) + ".csv");
		for(int i=0 ; i<10 ; i++) {
			getline(block[11],buffer[0],'\n');
			temp0.set_name_age(buffer[0]);
			int hash_result = make_hash(temp0.name);

			block[hash_result] << temp0.name << "," << temp0.age << "\n";
		}
		block[11].close();
		outer_block++;
	}

	for(int i=0 ; i<11 ; i++) {
		block[i].close();
	}

	for(int i=0 ; i<11 ; i++) {
		block[i].open("../buckets/test2_inner_"+to_string(i) + ".csv", ios::out);
	}

	while(inner_block<1000) {
		block[11].open("./name_salary/"+to_string(inner_block) + ".csv");
		for(int i=0 ; i<10 ; i++) {
			getline(block[11],buffer[1],'\n');
			temp1.set_name_salary(buffer[1]);
			int hash_result = make_hash(temp1.name);

			block[hash_result] << temp1.name << "," << temp1.salary << "\n";
		}
		block[11].close();
		inner_block++;
	}
	for(int i=0 ; i<11 ; i++) {
		block[i].close();
	}

	for(int i=0 ; i<11 ; i++) {
		block[0].open("../buckets/test2_outer_"+to_string(i) + ".csv");
		block[1].open("../buckets/test2_inner_"+to_string(i) + ".csv");

		while(getline(block[0],buffer[0],'\n')) {
			temp0.set_name_age(buffer[0]);
			while(getline(block[1],buffer[1],'\n')) {
				temp1.set_name_salary(buffer[1]);
				if(temp0.name == temp1.name) {
					output << make_tuple(temp0.name,temp0.age,temp1.salary);
					break;
				}
			}
			block[1].seekg(0,ios::beg);
		}


		block[0].close();
		block[1].close();
	}


	/******************************************************************/

	output.close();

	
}
