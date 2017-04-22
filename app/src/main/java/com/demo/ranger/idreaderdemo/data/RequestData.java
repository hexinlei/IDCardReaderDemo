package com.demo.ranger.idreaderdemo.data;

/**
 * @project:IDCardReaderDemo-master
 * @package:com.demo.ranger.idreaderdemo.data
 * @copyright: Copyright[2017-2999] [Markor Investment Group Co. LTD]. All Rights Reserved.
 * @filename: RequestData
 * @description:&lt;描述&gt;
 * @author: wangyunlei
 * @date: 17/4/20-上午11:00
 * @version: 1.0
 */
public class RequestData
{
	private String deviceCode;
	private String cardNo;
	private String extendInfo;

	@Override
	public String toString()
	{
		return "RequestData{" + "deviceCode='" + deviceCode + '\'' + ", cardNo='" + cardNo + '\'' + ", extendInfo='" + extendInfo
				+ '\'' + '}';
	}

	public String getDeviceCode()
	{
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode)
	{
		this.deviceCode = deviceCode;
	}

	public String getCardNo()
	{
		return cardNo;
	}

	public void setCardNo(String cardNo)
	{
		this.cardNo = cardNo;
	}

	public String getExtendInfo()
	{
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo)
	{
		this.extendInfo = extendInfo;
	}
}
