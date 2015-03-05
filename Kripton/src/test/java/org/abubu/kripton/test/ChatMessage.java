package org.abubu.kripton.test;
/**
 * 
 */


import java.util.Date;

import com.abubusoft.kripton.annotation.BindElement;
import com.abubusoft.kripton.annotation.BindRoot;


/**
 * <p>
 * Messaggio base per tutte le comunicazioni con il server.
 * </p>
 * 
 * @author Cesco
 * 
 */
@BindRoot
public class ChatMessage extends ArgonSQLEntity {

	@BindElement
	public long creationTimestamp;

	@BindElement
	public String deviceId;

	@BindElement
	public String groupUid;

	@BindElement
	public double latitude;

	@BindElement
	public double longitude;

	@BindElement
	public long mediaDuration;

	@BindElement
	public String mediaHash;

	@BindElement
	public String mediaInternalType;

	@BindElement
	public String mediaMimeType;

	@BindElement
	public String mediaName;

	@BindElement
	public int mediaSize;

	@BindElement
	public String mediaUrl;
	
	@BindElement
	public long origin;
	
	@BindElement
	public byte[] rawValue;
	
	@BindElement
	public long receivedTimestamp;
	
	@BindElement
	public String senderUid;
	
	@BindElement
	public long sentTimestamp;
	
	@BindElement
	public ChatMessageStatusType status;
	
	@BindElement
	public ChatMessageType type;

	@BindElement
	public String uid;
	@BindElement
	public String value;

	

	public ChatMessage() {
		this.creationTimestamp=new Date().getTime();		
		this.status = ChatMessageStatusType.CREATED;
	}

	public ChatMessage(ChatMessageType value) {
		this.uid=UIDHelper.generateLowPriorityUID();
		this.creationTimestamp=new Date().getTime();				
		this.status = ChatMessageStatusType.CREATED;
		type = value;
	}

	public long getCreationTimestamp() {
		return creationTimestamp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getGroupUid() {
		return groupUid;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public long getMediaDuration() {
		return mediaDuration;
	}

	public String getMediaHash() {
		return mediaHash;
	}
	

	public String getMediaName() {
		return mediaName;
	}

	public int getMediaSize() {
		return mediaSize;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public long getOrigin() {
		return origin;
	}

	public byte[] getRawValue() {
		return rawValue;
	}

	public long getReceivedTimestamp() {
		return receivedTimestamp;
	}

	public String getSenderUid() {
		return senderUid;
	}

	public long getSentTimestamp() {
		return sentTimestamp;
	}

	public ChatMessageStatusType getStatus() {
		return status;
	}

	public ChatMessageType getType() {
		return type;
	}

	public String getUid() {
		return uid;
	}

	public String getValue() {
		return value;
	}

	public boolean isFrom(String username) {
		return username.equals(senderUid);
	}

	public void setCreationTimestamp(long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setGroupUid(String groupUid) {
		this.groupUid = groupUid;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getMediaInternalType() {
		return mediaInternalType;
	}

	public void setMediaInternalType(String mediaInternalType) {
		this.mediaInternalType = mediaInternalType;
	}

	public String getMediaMimeType() {
		return mediaMimeType;
	}

	public void setMediaMimeType(String mediaMimeType) {
		this.mediaMimeType = mediaMimeType;
	}

	public void setMediaDuration(long mediaDuration) {
		this.mediaDuration = mediaDuration;
	}

	public void setMediaHash(String mediaHash) {
		this.mediaHash = mediaHash;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public void setMediaSize(int mediaSize) {
		this.mediaSize = mediaSize;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public void setOrigin(long origin) {
		this.origin = origin;
	}

	public void setRawValue(byte[] rawValue) {
		this.rawValue = rawValue;
	}

	public void setReceivedTimestamp(long receivedTimestamp) {
		this.receivedTimestamp = receivedTimestamp;
	}

	public void setSenderUid(String senderUid) {
		this.senderUid = senderUid;
	}

	public void setSentTimestamp(long sentTimestamp) {
		this.sentTimestamp = sentTimestamp;
	}

	public void setStatus(ChatMessageStatusType status) {
		this.status = status;
	}

	public void setType(ChatMessageType type) {
		this.type = type;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
