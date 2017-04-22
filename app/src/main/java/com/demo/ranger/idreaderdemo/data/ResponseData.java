package com.demo.ranger.idreaderdemo.data;

/**
 * @project:IDCardReaderDemo-master
 * @package:com.demo.ranger.idreaderdemo.data
 * @copyright: Copyright[2017-2999] [Markor Investment Group Co. LTD]. All Rights Reserved.
 * @filename: ResponseData
 * @description:&lt;描述&gt;
 * @author: wangyunlei
 * @date: 17/4/20-上午11:00
 * @version: 1.0
 */
public class ResponseData
{
	private String   status;
	private String   message;
	private String   jobId;
	private String   isRedList;
	private String   redListLevel;
	private String   redInfo;
	private String   extendInfo;

	@Override
	public String toString()
	{
		return "ResponseData{" + "status='" + status + '\'' + ", message='" + message + '\'' + ", jobId='" + jobId + '\''
				+ ", isRedList='" + isRedList + '\'' + ", redListLevel='" + redListLevel + '\'' + ", redInfo='" + redInfo + '\''
				+ ", extendInfo='" + extendInfo + '\'' + '}';
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getJobId()
	{
		return jobId;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	public String getIsRedList()
	{
		return isRedList;
	}

	public void setIsRedList(String isRedList)
	{
		this.isRedList = isRedList;
	}

	public String getRedListLevel()
	{
		return redListLevel;
	}

	public void setRedListLevel(String redListLevel)
	{
		this.redListLevel = redListLevel;
	}

	public String getRedInfo()
	{
		return redInfo;
	}

	public void setRedInfo(String redInfo)
	{
		this.redInfo = redInfo;
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
