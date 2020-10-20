package com.jff.ims.test;

public class TestJava {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String test = "D:\\MyWork\\tools\\new-genealogy\\apache-tomcat-8.0.47-forBDP\\webapps\\bdp\\uploadClanPictures\\null\\null20181128\\IMG_5328.JPG";
		int temp = test.indexOf("\\bdp");
		int name = test.lastIndexOf("\\");
		System.out.println(test.substring(temp));
		System.out.println(test.substring(name+1));
	}

}
