package com.engc.smartedu.bean;


public class RecentItem implements Comparable<RecentItem> {
	private String userId;
	private String headImg;// 头像
	private String name;// 消息来自
	private String message;// 消息内容
	private int newNum;// 新消息数目
	private long time;// 消息日期


	public RecentItem() {
	}

	public RecentItem(String userId, String headImg, String name, String message,
			int newNum, long time) {
		super();
		this.userId = userId;
		this.headImg = headImg;
		this.name = name;
		this.message = message;
		this.newNum = newNum;
		this.time = time;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNewNum() {
		return newNum;
	}

	public void setNewNum(int newNum) {
		this.newNum = newNum;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}



	@Override
	public int hashCode() {
		int code = 0;
		code = (31 * (this.userId.hashCode())) >> 2;
		return code;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o instanceof RecentItem) {
			RecentItem item = (RecentItem) o;
			if (item.userId.equals(this.userId))
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(RecentItem another) {
		// TODO Auto-generated method stub
		return (int) (another.time - this.time);
	}

}
