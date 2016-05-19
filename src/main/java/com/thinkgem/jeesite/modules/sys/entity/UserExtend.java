package com.thinkgem.jeesite.modules.sys.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * Created with IntelliJ IDEA.
 * User: Xiaoke Zhang
 * Date: 5/14/2016
 * Time: 8:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserExtend extends DataEntity<UserExtend> {
//	"name":name,
//			"class_no":class_no,
//			"university_name":university_name,
//			"major_name":major_name,
//			"email":email,
//			"mobile":mobile

	private String classNo;             //班级
	private String universityName;      //学校名称
	private String majorName;           //专业名称
	private String qq;                  //qq号
	private String wechat;              //微信号
	private String classTeacherName;    //班主任姓名

	public String getClassNo() {
		return classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getClassTeacherName() {
		return classTeacherName;
	}

	public void setClassTeacherName(String classTeacherName) {
		this.classTeacherName = classTeacherName;
	}

	@Override
	public String toString() {
		return "UserExtend{" +
				"classNo='" + classNo + '\'' +
				", universityName='" + universityName + '\'' +
				", majorName='" + majorName + '\'' +
				", qq='" + qq + '\'' +
				", wechat='" + wechat + '\'' +
				", classTeacherName='" + classTeacherName + '\'' +
				'}';
	}
}
