package com.divya.homework;

import java.util.ArrayList;
import java.util.List;

public class InputParams {
List<String> list1 = new ArrayList<String>();
List<String> list2 = new ArrayList<String>();
String db;
public List<String> getList1() {
	return list1;
}
public void setList1(List<String> list1) {
	this.list1 = list1;
}
public List<String> getList2() {
	return list2;
}
public void setList2(List<String> list2) {
	this.list2 = list2;
}
public String getDb() {
	return db;
}
public void setDb(String db) {
	this.db = db;
}

}
